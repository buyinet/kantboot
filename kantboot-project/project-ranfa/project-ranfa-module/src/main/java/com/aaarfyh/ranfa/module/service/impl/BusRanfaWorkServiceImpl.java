package com.aaarfyh.ranfa.module.service.impl;

import com.aaarfyh.ranfa.module.entity.BusRanfaBrand;
import com.aaarfyh.ranfa.module.entity.BusRanfaTechnique;
import com.aaarfyh.ranfa.module.entity.BusRanfaWork;
import com.aaarfyh.ranfa.module.entity.BusRanfaWorkVideo;
import com.aaarfyh.ranfa.module.repository.BusRanfaBrandRepository;
import com.aaarfyh.ranfa.module.repository.BusRanfaTechniqueRepository;
import com.aaarfyh.ranfa.module.repository.BusRanfaWorkRepository;
import com.aaarfyh.ranfa.module.repository.BusRanfaWorkVideoRepository;
import com.aaarfyh.ranfa.module.service.IBusRanfaWorkService;
import com.aaarfyh.ranfa.module.util.RanfaWorkChangeUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.kantboot.pay.util.common.service.impl.BaseGoodsServiceImpl;
import com.kantboot.pay.util.common.util.GoodsPayParam;
import com.kantboot.pay.util.common.util.PayParam;
import com.kantboot.pay.util.common.util.PayResult;
import com.kantboot.system.user.module.entity.SysUser;
import com.kantboot.system.user.module.security.TokenManage;
import com.kantboot.system.user.module.service.ISysUserService;
import com.kantboot.util.common.exception.BaseException;
import com.kantboot.util.common.util.RedisUtil;
import com.kantboot.util.core.entity.CommonEntity;
import com.kantboot.util.core.entity.CommonEntityPageParam;
import com.kantboot.util.core.entity.OperatorEntity;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class BusRanfaWorkServiceImpl extends BaseGoodsServiceImpl<BusRanfaWork, Long> implements IBusRanfaWorkService {

    @Resource
    RedisUtil redisUtil;
    @Resource
    ISysUserService userService;

    @Resource
    BusRanfaWorkRepository repository;

    @Resource
    BusRanfaWorkVideoRepository ranfaWorkVideoRepository;


    @Resource
    BusRanfaTechniqueRepository techniqueRepository;

    @Resource
    TokenManage tokenManage;

    @Resource
    HttpServletRequest request;

    Interner<String> pool = Interners.newWeakInterner();

    @Override
    public void submit(BusRanfaWork entity) {
        entity.setId(null);
        String timeStr = (new Date().getTime() - 999999999) + "";
        synchronized (pool.intern(timeStr)) {
            entity.setIden(timeStr);
        }

        List<BusRanfaWorkVideo> ranfaWorkVideos =
                JSON.parseArray(
                        JSON.toJSONString(entity.getRanfaWorkVideos()),BusRanfaWorkVideo.class);
        entity.setGmtCreate(new Date());
        entity.setRanfaWorkVideos(null);
        entity.setAuditStatus(0);
        entity.setUserIdByUpload(userService.getUserInfo().getId());
        BusRanfaWork save = super.save(entity);

        for (BusRanfaWorkVideo ranfaWorkVideo : ranfaWorkVideos) {
            ranfaWorkVideo.setId(null);
            ranfaWorkVideo.setRanfaWorkId(save.getId());
            ranfaWorkVideoRepository.save(ranfaWorkVideo);
        }
    }

    @SneakyThrows
    @Override
    public BusRanfaWork change() {
        synchronized (pool.intern(request.getHeader("token"))) {
        }

        SysUser userInfo = userService.getUserInfo();
        Long userId = userInfo.getId();

        //redis中保存的key
        String redisKey = "user_id:" + userId + ":ranfa_work_change_util";
        String redisData = redisUtil.get(redisKey);


        long count = repository.count();
        if (redisData == null) {
            RanfaWorkChangeUtil ranfaWorkChangeUtil = new RanfaWorkChangeUtil();

            CommonEntityPageParam<BusRanfaWork> pageParam = new CommonEntityPageParam<BusRanfaWork>();
//            pageParam.setData(new CommonEntity<BusRanfaWork>().setEntity(new BusRanfaWork()));
            pageParam.setSortField("gmtCreate");
            pageParam.setSortType("DESC");
            pageParam.setPageNumber(1);

            //start: 只查询审核状态为已审核
            CommonEntity<BusRanfaWork> busRanfaWorkCommonEntity = new CommonEntity<>();
            OperatorEntity<BusRanfaWork> busRanfaWorkOperatorEntity = new OperatorEntity<>();
            ArrayList<BusRanfaWork> busRanfaWorks = new ArrayList<>();
            busRanfaWorks.add(new BusRanfaWork().setAuditStatus(1));
            busRanfaWorkOperatorEntity.setEq(busRanfaWorks);
            busRanfaWorkCommonEntity.setAnd(busRanfaWorkOperatorEntity);
            pageParam.setData(busRanfaWorkCommonEntity.setEntity(new BusRanfaWork()));
            //end: 只查询审核状态为已审核


            pageParam.setPageSize(20);
            Page<BusRanfaWork> commonByPage = findCommonByPage(pageParam);
            List<BusRanfaWork> common = commonByPage.getContent();
            List<Long> blockIds = new ArrayList<Long>();
            for (BusRanfaWork cesRanfaWork : common) {
                blockIds.add(cesRanfaWork.getId());
            }
            Collections.shuffle(blockIds);
            ranfaWorkChangeUtil.setBlockIds(blockIds);
            Integer index = 0;
            ranfaWorkChangeUtil.setPage(1);
            ranfaWorkChangeUtil.setIndex(index);
            ranfaWorkChangeUtil.setId(blockIds.get(index));

            /**
             * 用户切换的区块视频id数组
             */
            redisUtil.setEx("user_id:" + userId + ":ranfa_work_change_util", JSON.toJSONString(ranfaWorkChangeUtil), 7, TimeUnit.DAYS);

//            return repository.findById(ranfaWorkChangeUtil.getId()).get();
            BusRanfaWork busRanfaWork = new BusRanfaWork().setId(ranfaWorkChangeUtil.getId());
            return super.findById(busRanfaWork);
        }

        if (redisData != null) {
            RanfaWorkChangeUtil ranfaWorkChangeUtil = JSON.parseObject(redisData, RanfaWorkChangeUtil.class);
            Integer index = ranfaWorkChangeUtil.getIndex() + 1;
            if (index > ranfaWorkChangeUtil.getBlockIds().size() - 1) {

                //start:用来做比较的查询
                CommonEntityPageParam<BusRanfaWork> pageParam1 = new CommonEntityPageParam<BusRanfaWork>();
                pageParam1.setData(new CommonEntity<BusRanfaWork>().setEntity(new BusRanfaWork()));
                pageParam1.setSortField("gmtCreate");
                pageParam1.setSortType("DESC");
                //start: 只查询审核状态为已审核
                CommonEntity<BusRanfaWork> busRanfaWorkCommonEntity1 = new CommonEntity<>();
                OperatorEntity<BusRanfaWork> busRanfaWorkOperatorEntity1 = new OperatorEntity<>();
                ArrayList<BusRanfaWork> busRanfaWorks1 = new ArrayList<>();
                busRanfaWorks1.add(new BusRanfaWork().setAuditStatus(1));
                busRanfaWorkOperatorEntity1.setEq(busRanfaWorks1);
                busRanfaWorkCommonEntity1.setAnd(busRanfaWorkOperatorEntity1);
                pageParam1.setData(busRanfaWorkCommonEntity1.setEntity(new BusRanfaWork()));
                //end: 只查询审核状态为已审核

                Integer page1 = ranfaWorkChangeUtil.getPage() + 1;
                pageParam1.setPageNumber(1);
                pageParam1.setPageSize(20);
                //如果redis中存储的ranfaBrandId（品牌id）不为空，则根据ranfaBrandId（品牌id）查询
                if (ranfaWorkChangeUtil.getRanfaBrandId() != null && ranfaWorkChangeUtil.getRanfaBrandId() != 0) {
                    List<BusRanfaWork> eq = pageParam1.getData().getAnd().getEq();
                    eq.add(new BusRanfaWork().setRanfaBrandId(ranfaWorkChangeUtil.getRanfaBrandId()));
                }
                if (ranfaWorkChangeUtil.getRanfaTechniqueId() != null && ranfaWorkChangeUtil.getRanfaTechniqueId() != 0) {
                    List<BusRanfaWork> eq = pageParam1.getData().getAnd().getEq();
                    HashSet<BusRanfaTechnique> busRanfaTechniques = new HashSet<>();
                    busRanfaTechniques.add(new BusRanfaTechnique().setId(ranfaWorkChangeUtil.getRanfaTechniqueId()));
                    eq.add(new BusRanfaWork().setRanfaTechniques(busRanfaTechniques));
                }

                Page<BusRanfaWork> commonByPage1 = findCommonByPage(pageParam1);
                //end:用来做比较的查询

                CommonEntityPageParam<BusRanfaWork> pageParam = new CommonEntityPageParam<BusRanfaWork>();
//                pageParam.setData(new CommonEntity<BusRanfaWork>().setEntity(new BusRanfaWork()));
                pageParam.setSortField("gmtCreate");
                pageParam.setSortType("DESC");
                //start: 只查询审核状态为已审核
                CommonEntity<BusRanfaWork> busRanfaWorkCommonEntity = new CommonEntity<>();
                OperatorEntity<BusRanfaWork> busRanfaWorkOperatorEntity = new OperatorEntity<>();
                ArrayList<BusRanfaWork> busRanfaWorks = new ArrayList<>();
                busRanfaWorks.add(new BusRanfaWork().setAuditStatus(1));
                busRanfaWorkOperatorEntity.setEq(busRanfaWorks);
                busRanfaWorkCommonEntity.setAnd(busRanfaWorkOperatorEntity);
                pageParam.setData(busRanfaWorkCommonEntity.setEntity(new BusRanfaWork()));
                //end: 只查询审核状态为已审核

                Integer page = ranfaWorkChangeUtil.getPage() + 1;
                if (page > commonByPage1.getTotalPages()) {
                    page = 1;
                }
                //如果redis中存储的ranfaBrandId（品牌id）不为空，则根据ranfaBrandId（品牌id）查询
                if (ranfaWorkChangeUtil.getRanfaBrandId() != null && ranfaWorkChangeUtil.getRanfaBrandId() != 0) {
                    List<BusRanfaWork> eq = pageParam.getData().getAnd().getEq();
                    eq.add(new BusRanfaWork().setRanfaBrandId(ranfaWorkChangeUtil.getRanfaBrandId()));
                }
                if (ranfaWorkChangeUtil.getRanfaTechniqueId() != null && ranfaWorkChangeUtil.getRanfaTechniqueId() != 0) {
                    List<BusRanfaWork> eq = pageParam.getData().getAnd().getEq();
                    HashSet<BusRanfaTechnique> busRanfaTechniques = new HashSet<>();
                    busRanfaTechniques.add(new BusRanfaTechnique().setId(ranfaWorkChangeUtil.getRanfaTechniqueId()));
                    eq.add(new BusRanfaWork().setRanfaTechniques(busRanfaTechniques));
                }
                pageParam.setPageNumber(1 * page);
                pageParam.setPageSize(20);
                Page<BusRanfaWork> commonByPage = findCommonByPage(pageParam);
                List<BusRanfaWork> common = commonByPage.getContent();
                List<Long> blockIds = new ArrayList<Long>();
                for (BusRanfaWork cesRanfaWork : common) {
                    blockIds.add(cesRanfaWork.getId());
                }
                Collections.shuffle(blockIds);
                ranfaWorkChangeUtil.setPage(page);
                ranfaWorkChangeUtil.setBlockIds(blockIds);
                index = 0;
            }
            ranfaWorkChangeUtil.setIndex(index);
            ranfaWorkChangeUtil.setId(ranfaWorkChangeUtil.getBlockIds().get(index));
            /**
             * 用户切换的区块视频id数组
             */
            redisUtil.setEx("user_id:" + userId + ":ranfa_work_change_util", JSON.toJSONString(ranfaWorkChangeUtil), 7, TimeUnit.DAYS);

//            return repository.findById(ranfaWorkChangeUtil.getId()).get();
            BusRanfaWork busRanfaWork = new BusRanfaWork().setId(ranfaWorkChangeUtil.getId());
            return super.findById(busRanfaWork);
        }

        return null;
    }
    

    /**
     * 查看用户自己上传的作品
     * @return
     */
    @Override
    public Page<BusRanfaWork> findByUploadSelf(
            CommonEntityPageParam<BusRanfaWork> pageParam) {
        Long userId = userService.getUserInfo().getId();
        if(pageParam.getData().getAnd()==null){
            ArrayList<BusRanfaWork> busRanfaWorks = new ArrayList<>();
            busRanfaWorks.add(new BusRanfaWork().setUserIdByUpload(userId));
            pageParam.getData().setAnd(new OperatorEntity<BusRanfaWork>().setEq(busRanfaWorks));
            
        }else if(pageParam.getData().getAnd().getEq()==null){
            ArrayList<BusRanfaWork> busRanfaWorks = new ArrayList<>();
            busRanfaWorks.add(new BusRanfaWork().setUserIdByUpload(userId));
            pageParam.getData().getAnd().setEq(busRanfaWorks);
        }else{
            ArrayList<BusRanfaWork> busRanfaWorks = new ArrayList<>();
            busRanfaWorks.add(new BusRanfaWork().setUserIdByUpload(userId));
            pageParam.getData().getAnd().getEq().addAll(busRanfaWorks);
        }
        System.out.println(JSON.toJSONString(pageParam));

        return super.findCommonByPage(pageParam);
    }

    @Override
    public BusRanfaWork brandToChange(Long ranfaBrandId) {
        Long userId = userService.getUserInfo().getId();
        String redisKey = "user_id:" + userId + ":ranfa_work_change_util";
        redisUtil.delete(redisKey);
        CommonEntityPageParam<BusRanfaWork> pageParam = new CommonEntityPageParam<BusRanfaWork>();
        pageParam.setData(new CommonEntity<BusRanfaWork>().setEntity(new BusRanfaWork()));
        pageParam.setSortField("gmtCreate");
        pageParam.setSortType("DESC");
        pageParam.setPageNumber(1);
        pageParam.setPageSize(20);
        List<BusRanfaWork> eq = pageParam.getData().getAnd().getEq();
        eq.add(new BusRanfaWork().setRanfaBrandId(ranfaBrandId));
        Page<BusRanfaWork> commonByPage = findCommonByPage(pageParam);
        List<BusRanfaWork> common = commonByPage.getContent();
        List<Long> blockIds = new ArrayList<Long>();
        for (BusRanfaWork cesRanfaWork : common) {
            blockIds.add(cesRanfaWork.getId());
        }
        Collections.shuffle(blockIds);
        String redisData = redisUtil.get(redisKey);
        RanfaWorkChangeUtil ranfaWorkChangeUtil = new RanfaWorkChangeUtil();
        ranfaWorkChangeUtil.setRanfaBrandId(ranfaBrandId);
        ranfaWorkChangeUtil.setPage(1);
        ranfaWorkChangeUtil.setBlockIds(blockIds);
        ranfaWorkChangeUtil.setIndex(0);
        Long aLong = ranfaWorkChangeUtil.getBlockIds().get(0);
        redisUtil.setEx(redisKey, JSON.toJSONString(ranfaWorkChangeUtil), 7, TimeUnit.DAYS);

        try {
            BusRanfaWork busRanfaWork = commonByPage.getContent().get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new BaseException(3000, "该品牌下暂没有课程视频");
        }
        return commonByPage.getContent().get(0);
    }

    @Override
    public BusRanfaWork techniqueToChange(Long ranfaTechniqueId) {
        Long userId = userService.getUserInfo().getId();
        String redisKey = "user_id:" + userId + ":ranfa_work_change_util";
        redisUtil.delete(redisKey);
        CommonEntityPageParam<BusRanfaWork> pageParam = new CommonEntityPageParam<BusRanfaWork>();
        pageParam.setData(new CommonEntity<BusRanfaWork>().setEntity(new BusRanfaWork()));
        pageParam.setSortField("gmtCreate");
        pageParam.setSortType("DESC");
        pageParam.setPageNumber(1);
        pageParam.setPageSize(20);
        List<BusRanfaWork> eq = pageParam.getData().getAnd().getEq();
        HashSet<BusRanfaTechnique> busRanfaTechniques = new HashSet<>();
        busRanfaTechniques.add(new BusRanfaTechnique().setId(ranfaTechniqueId));
        eq.add(new BusRanfaWork().setRanfaTechniques(busRanfaTechniques));
        Page<BusRanfaWork> commonByPage = findCommonByPage(pageParam);
        List<BusRanfaWork> common = commonByPage.getContent();
        List<Long> blockIds = new ArrayList<Long>();
        for (BusRanfaWork cesRanfaWork : common) {
            blockIds.add(cesRanfaWork.getId());
        }
        Collections.shuffle(blockIds);
        String redisData = redisUtil.get(redisKey);
        RanfaWorkChangeUtil ranfaWorkChangeUtil = new RanfaWorkChangeUtil();
        ranfaWorkChangeUtil.setRanfaTechniqueId(ranfaTechniqueId);
        ranfaWorkChangeUtil.setPage(1);
        ranfaWorkChangeUtil.setBlockIds(blockIds);
        ranfaWorkChangeUtil.setIndex(0);
        Long aLong = ranfaWorkChangeUtil.getBlockIds().get(0);
        System.out.println("ranfaWorkChangeUtil.getRanfaTechniqueId() = " + ranfaWorkChangeUtil.getRanfaTechniqueId());
        System.out.println("JSON.toJSONString(ranfaWorkChangeUtil) = " + JSON.toJSONString(ranfaWorkChangeUtil));
        redisUtil.setEx(redisKey, JSON.toJSONString(ranfaWorkChangeUtil), 7, TimeUnit.DAYS);

        try {
            BusRanfaWork busRanfaWork = commonByPage.getContent().get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new BaseException(3000, "该分类下暂没有课程视频");
        }
        return commonByPage.getContent().get(0);
    }

    @Override
    public BusRanfaTechnique techniqueByChange() {
        Long userId = userService.getUserInfo().getId();
        String redisKey = "user_id:" + userId + ":ranfa_work_change_util";
        String utilStr = redisUtil.get(redisKey);
        if (utilStr == null) {
            return null;
        }
        RanfaWorkChangeUtil ranfaWorkChangeUtil = JSON.parseObject(utilStr, RanfaWorkChangeUtil.class);
        Long ranfaTechniqueId = ranfaWorkChangeUtil.getRanfaTechniqueId();
        if (ranfaTechniqueId == null || ranfaTechniqueId == 0) {
            return new BusRanfaTechnique().setId(null);
        }
        BusRanfaTechnique busRanfaTechnique = techniqueRepository.findById(ranfaTechniqueId).get();
        return busRanfaTechnique;
    }

    @Resource
    BusRanfaBrandRepository ranfaBrandRepository;

    @Override
    public BusRanfaBrand brandByChange() {
        Long userId = userService.getUserInfo().getId();
        String redisKey = "user_id:" + userId + ":ranfa_work_change_util";
        String utilStr = redisUtil.get(redisKey);
        if (utilStr == null) {
            return null;
        }
        RanfaWorkChangeUtil ranfaWorkChangeUtil = JSON.parseObject(utilStr, RanfaWorkChangeUtil.class);
        Long ranfaBrandId = ranfaWorkChangeUtil.getRanfaBrandId();
        if (ranfaBrandId == null || ranfaBrandId == 0) {
            return new BusRanfaBrand().setId(null);
        }
        BusRanfaBrand busRanfaBrand = ranfaBrandRepository.findById(ranfaBrandId).get();
        return busRanfaBrand;
    }

    @Override
    public PayResult payBefore(GoodsPayParam param) {
        //此商品只能一个个单独购买，所以只会有一个下标
        List<PayParam> payParams = param.getPayParams();
        PayParam payPramItem = payParams.get(0);
        payPramItem.setTitle("染发一号-教学视频");
//        param.setPayParams(payParams);
        BusRanfaWork cesRanfaWork = repository.findById(Long.valueOf(payPramItem.getGoodsId())).get();
        String body = "染发一号-教学视频";

        if (cesRanfaWork.getTitle() != null) {
            body += "-" + cesRanfaWork.getTitle();
        }

        PayResult result = new PayResult();
        result.setState(2000);
        result.setTotalFee(cesRanfaWork.getPrice());
        result.setPayParams(payParams);
        result.setBody(body);
        return result;
    }

}
