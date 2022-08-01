package com.kantboot.system.user.filter;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.kantboot.project.util.common.util.ResponseUtil;
import com.kantboot.project.util.common.util.RestResult;
import com.kantboot.system.user.security.TokenManage;
import com.kantboot.system.user.service.ISysUserService;
import com.kantboot.util.core.exception.NotLoginException;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 访问过滤器
 * 此过滤器为全局过滤器，用来对用户的访问进行过滤
 */
@Slf4j
@Component
@Data
@WebFilter(filterName="UilFilter",urlPatterns="/*")
public class UriFilter implements Filter {

    @Resource
    private HandlerExceptionResolver handlerExceptionResolver;

    @Resource
    private TokenManage tokenManage;

    @Resource
    private ISysUserService sysUserService;


    public UriFilter(){}

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    private Interner<String> interner= Interners.<String>newStrongInterner();

    @SneakyThrows
    @Override
    public void doFilter
            (ServletRequest servletRequest,
             ServletResponse servletResponse,
             FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        String uri=request.getRequestURI();
        String token = request.getHeader("token");

        String ip = request.getRemoteAddr();

        String uriIp=uri+":"+ip;
        String uriIpToken = uri +":" + ip + ":" + token;
        log.info("ip["+ip+"]正在访问"+request.getRequestURL());

        Boolean isCanTo=false;
            try {
//            log.info("正在过滤："+uriIpToken);
                isCanTo = sysUserService.isCanToUri();
            } catch (NotLoginException e) {
                // 判断没有登录
                handlerExceptionResolver.resolveException(request, response, null, new NotLoginException());
                return;
            }
        if(isCanTo){
//            log.info("过滤成功："+uriIpToken);
            // 有权限时直接进入对应的请求
            chain.doFilter(request,response);
            return;
        }else{
//            log.info("没有对应权限："+uriIpToken);
            // 没有权限时，给相对于的权限
            ResponseUtil.out(response, RestResult.error("没有权限").setState(7776));
            return;
        }

    }

    @Override
    public void destroy() {
        log.info("页面过滤器 遭到了摧毁");
    }

}
