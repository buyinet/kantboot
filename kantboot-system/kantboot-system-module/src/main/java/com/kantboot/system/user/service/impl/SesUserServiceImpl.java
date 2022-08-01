package com.kantboot.system.user.service.impl;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.kantboot.project.util.common.exception.BaseException;
import com.kantboot.system.user.entity.SysDept;
import com.kantboot.system.user.entity.SysPermission;
import com.kantboot.system.user.entity.SysRole;
import com.kantboot.system.user.entity.SysUser;
import com.kantboot.system.user.ex.PasswordFaildException;
import com.kantboot.system.user.repository.SysPermissionRepository;
import com.kantboot.system.user.repository.SysUserRepository;
import com.kantboot.system.user.security.DefaultPasswordEncoder;
import com.kantboot.system.user.security.TokenManage;
import com.kantboot.system.user.service.ISysRoleService;
import com.kantboot.system.user.service.ISysUserService;
import com.kantboot.system.user.vo.LoginVO;
import com.kantboot.util.core.util.PageParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Slf4j
public class SesUserServiceImpl implements ISysUserService {


    /**
     * 管理员的角色名称，
     * 如果用户的角色名称与此对应，便可访问所有路径
     */
    public final static String ROLE_AMIN_KEY = "ROLE_admin";

    @Resource
    SysUserRepository sysUserRepository;

    @Resource
    SysPermissionRepository sysPermissionMapper;

    @Resource
    ISysRoleService roleService;
    @Resource
    HttpServletRequest request;
    @Resource
    TokenManage tokenManage;
    @Resource
    DefaultPasswordEncoder defaultPasswordEncoder;

    /**
     * @param requestUri 访问的uri
     * @param userHasUri 用户的uri
     * @return
     */
    public Boolean isCanTo(String requestUri, String userHasUri) {

        if (userHasUri.endsWith("/**")) {

            String substring =
                    userHasUri.substring(0, userHasUri.length() - 3);
            if (requestUri.startsWith(substring)) {
                return true;
            }
        }

        if (requestUri.equals(userHasUri)) {
            return true;
        }

        return false;

    }

    @Override
    public Boolean isCanToUri() {
        //获取当前的 URI
        String requestURI = request.getRequestURI();
        //如果是swagger-ui则直接进
        if (request.getRequestURI().indexOf("swagger-ui") != -1) {
            return true;
        }

        //查询所有白名单,无须登录
//        SysPermission byUri = sysPermissionMapper.findByUri(requestURI);
        List<SysPermission> byMatcherEqualsTrue
                = sysPermissionMapper.findByMatcherIsTrue();


        for (SysPermission permission : byMatcherEqualsTrue) {
            if (isCanTo(requestURI, permission.getUri())) {
                return true;
            }
        }

//        if(byUri!=null&&byUri.getMatcher()){
//            //如果访问的路径，直接在白名单中，则直接访问
//            return true;
//        }


        // 获取 用户名
        String userName = tokenManage.getUserName();
        List<SysPermission> byAllPersonCurrentIsTrue
                = sysPermissionMapper.findByAllPersonCurrentIsTrue();

//         获取全员都可访问的uri
        for (SysPermission permission : byAllPersonCurrentIsTrue) {
            if (isCanTo(requestURI, permission.getUri())) {
                return true;
            }
        }


        // 获取全员都可访问的uri
//        if(byUri!=null&&byUri.getIsAllPersonCurrent()){
//            return true;
//        }

        //根据 用户名 得到用户
        SysUser sysUser = sysUserRepository.findByUsername(userName);

        List<SysRole> roles = sysUser.getRoles();

        // 验证用户是否为管理员，如果是的话，便可以访问所有路径
        for (SysRole role : roles) {
            if (ROLE_AMIN_KEY.equals(role.getName())) {
                return true;
            }
        }

        //所有用户信息
        Set<SysPermission> permissions = new HashSet<>();
        for (SysRole role : roles) {
            List<SysPermission> permissionsInRole = role.getPermissions();
            for (SysPermission permission : permissionsInRole) {
                permissions.add(permission);
            }
        }

        // 判断用户拥有的权限中是否含有对应的路径，如果有的话，便可进行访问
        for (SysPermission permission : permissions) {
            if (isCanTo(requestURI, permission.getUri())) {
                return true;
            }
        }


        return false;
    }

    @Override
    public SysUser getUserInfo() {
        //获取 用户名
        String userName = tokenManage.getUserName();
        return sysUserRepository.findByUsername(userName);
    }

    private Interner<String> interner= Interners.<String>newStrongInterner();

    /**
     * 生成用户名
     *
     * @return
     */
    @Override
    public String createUsername() {
        // 获取当前时间戳
        String currentTime = System.currentTimeMillis() + "";

        // 获取现在的用户总数
        String countAll = sysUserRepository.count()+"";

        String username = "";

        // 当用户总数一致时锁住
        synchronized (interner.intern(countAll)) {
            countAll = sysUserRepository.count()+"";
            username = "86"+currentTime.substring(8) + countAll + 1;
        }

        return username;
    }


    @Override
    public LoginVO join(SysUser sysUser) {

        // 因为在JPA的save方法中
        // 如果传入id，便会根据id在数据库中进行修改，固将用户传入的id设为null
        sysUser.setId(null);

        SysUser byUsername = sysUserRepository.findByUsername(sysUser.getUsername());
        if (byUsername != null) {
            throw new BaseException(3000, "账号已被注册");
        }


        String username = sysUser.getUsername();

        if (sysUser.getUsername() == null) {
            username = createUsername();
        }

        username = sysUser.getUsername();

        // start:获取配置的角色id
        List<SysRole> sysRoleList=roleService.roleByUserJoin();
        // end:获取配置的角色id

        sysUser.setUsername(username)
                .setRoles(sysRoleList)
                .setPassword(defaultPasswordEncoder.encode(sysUser.getPassword()))
                .setCreateIp(request.getRemoteAddr())
                .setCreateDevice(request.getHeader("User-Agent"));

        SysUser save = sysUserRepository.save(sysUser);

        // 创建token，并将token传入redis
        String token = tokenManage.createToken(username);

        log.info("新用户注册成功:{}", "username:" + username);

        return new LoginVO()
                .setUsername(save.getUsername())
                .setToken(token)
                .setUser(save);
    }

    @Override
    public LoginVO login(SysUser sysUser) {
        String username = sysUser.getUsername();

        SysUser byUsername = sysUserRepository.findByUsername(username);

        // 如果用户名在数据库中查询不到时，提示客户端 账号或者密码错误
        if (byUsername == null) {
            throw new PasswordFaildException();
        }

        // 当客户端传来的密码和数据库中加密的密码进行验证失败时，提示客户端 账号或者密码错误
        if (!defaultPasswordEncoder.matches(sysUser.getPassword(), byUsername.getPassword())) {
            throw new PasswordFaildException();
        }

        String token = tokenManage.createToken(username);
        log.info("有用户登录成功" , request.getRemoteAddr() ,sysUser.getUsername(), token );
        return new LoginVO().setToken(token).setUsername(username).setUser(byUsername);
    }

    @Override
    public void loginOut() {
        tokenManage.removeToken();
    }


    @Override
    public List<SysUser> findAll(PageParam<SysUser> pageParam) {
        SysUser sysUser = pageParam.getData();

        Specification<SysUser> specification = new Specification<SysUser>() {

            @Override
            public Predicate toPredicate(Root<SysUser> root,
                                         CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                // 如果 用户名 不为空，则根据 用户名 进行查询
                if (!StringUtils.isEmpty(sysUser.getUsername())) {
                    predicates.add(
                            criteriaBuilder
                                    .equal(root.get("username"),
                                            sysUser.getUsername()));
                }


                // 如果 昵称 不为空，则根据 昵称 进行模糊查询
                if (!StringUtils.isEmpty(sysUser.getNickname())) {
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("nickname"),
                                            "%" + sysUser.getNickname() + "%"));
                }

                // 如果 真实姓名 不为空，则根据 真实姓名 进行模糊查询
                if (!StringUtils.isEmpty(sysUser.getRealname())) {
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("realname"),
                                            "%" + sysUser.getRealname() + "%"));
                }

                // 如果 身份证 不为空，则根据 身份证 进行模糊查询
                if (!StringUtils.isEmpty(sysUser.getIdCard())) {
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("idCard"),
                                            "%" + sysUser.getIdCard() + "%"));
                }

                // 如果 性别 不为空，则根据 性别 进行查询
                if (!StringUtils.isEmpty(sysUser.getGender())) {
                    predicates.add(
                            criteriaBuilder
                                    .equal(root.get("gender"), sysUser.getGender()));
                }

                // 如果 性别 不为空，则根据 性别 进行查询
                if (!StringUtils.isEmpty(sysUser.getPhoneNumber())) {
                    predicates.add(
                            criteriaBuilder
                                    .like(root.get("phoneNumber"), "%" + sysUser.getPhoneNumber() + "%"));
                }

                //如果 角色 不为空，则根据 角色 进行查询
                if (!(StringUtils.isEmpty(sysUser.getRoles()))) {
                    List<SysRole> roles = sysUser.getRoles();
                    for (int i = 0; i < roles.size(); i++) {
                        SysRole role = roles.get(i);
                        if (role != null && role.getId() != null) {
                            predicates.add(
                                    criteriaBuilder
                                            .equal(root.join("roles").get("id"), role.getId())
                            );
                        }

                        if (role != null && role.getName() != null) {
                            predicates.add(
                                    criteriaBuilder
                                            .like(root.join("roles").get("name"), "%" + role.getName() + "%")
                            );
                        }
                    }
                }

                //如果 组织 不为空，则根据 组织 进行查询
                if (!(StringUtils.isEmpty(sysUser.getDepts()))) {
                    Set<SysDept> depts = sysUser.getDepts();
                    for (SysDept dept:depts) {
                        if (dept != null && dept.getId() != null) {
                            predicates.add(
                                    criteriaBuilder
                                            .equal(root.join("depts").get("id"), dept.getId())
                            );
                        }

                        if (dept != null && dept.getName() != null) {
                            predicates.add(
                                    criteriaBuilder
                                            .like(root.join("depts").get("name"), "%" + dept.getName() + "%")
                            );
                        }
                    }
                }

                Predicate[] predicateArr = new Predicate[predicates.size()];
                return criteriaBuilder.and(predicates.toArray(predicateArr));
            }
        };

        return sysUserRepository.findAll(specification);
    }

    /**
     * 添加 余额，以 分 为单位
     *
     * @param money
     */
    @Override
    public void addBalance(Long money) {
        SysUser userInfo = getUserInfo();
        String username = userInfo.getUsername();

        synchronized(username.intern()) {
            userInfo.setBalance(userInfo.getBalance() + money);
            sysUserRepository.save(userInfo);
        }

    }
//
//    /**
//     * 添加 余额，以 元 为单位
//     *
//     * @param moneyYuan
//     */
//    @Override
//    public void addBalanceYuan(Double moneyYuan) {
//
//        SysUser userInfo = getUserInfo();
//        String username = userInfo.getUsername();
//
//        synchronized (username.intern()){
//            Long balance = userInfo.getBalance();
//            // 将余额转换成单位元
//            BigDecimal balanceYuan = new BigDecimal
//                    (Double.toString(balance)).divide(new BigDecimal(100));
//            BigDecimal multiply = balanceYuan.add
//                    (new BigDecimal(moneyYuan)).multiply(new BigDecimal(100));
//            userInfo.setBalance(multiply.longValue());
//            sysUserRepository.save(userInfo);
//        }
//
//    }

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        SysUser userInfo = getUserInfo();
        String encodePassword = userInfo.getPassword();
        String username = userInfo.getUsername();

        synchronized (username.intern()) {

            // 加密后与数据库里得密码对比
            // 当客户端传来的密码和数据库中加密的密码进行验证失败时，提示客户端 账号或者密码错误
            if (!defaultPasswordEncoder.matches(oldPassword, encodePassword)) {
                throw new BaseException(3000, "原密码错误");
            }

            sysUserRepository.save(userInfo
                    .setPassword(defaultPasswordEncoder.encode(newPassword))
                    .setGmtModified(new Date()));
        }

    }

    @Override
    public SysUser updateUserInfo(SysUser sysUser) {
        SysUser userInfo = getUserInfo();

        sysUser
                .setBalance(userInfo.getBalance())
                .setUsername(userInfo.getUsername())
                .setPassword(userInfo.getPassword())
                .setGmtCreate(userInfo.getGmtCreate());

        SysUser save = sysUserRepository.save(sysUser);
        return save;
    }
}
