package com.abcd.config.interceptor;

import com.abcd.common.annotation.RateLimiter;
import com.abcd.common.constant.CommonConstant;
import com.abcd.common.exception.ApiException;
import com.abcd.common.limit.RedisRaterLimiter;
import com.abcd.common.utils.IpInfoUtil;
import com.abcd.config.properties.IpLimitProperties;
import com.abcd.config.properties.LimitProperties;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 限流拦截器
 * @author 
 */
@Slf4j
@Component
public class LimitRaterInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private LimitProperties limitProperties;

    @Autowired
    private IpLimitProperties ipLimitProperties;

    @Autowired
    private RedisRaterLimiter redisRaterLimiter;

    @Autowired
    private IpInfoUtil ipInfoUtil;

    /**
     * 预处理回调方法，实现处理器的预处理（如登录检查）
     * 第三个参数为响应的处理器，即controller
     * 返回true，表示继续流程，调用下一个拦截器或者处理器
     * 返回false，表示流程中断，通过response产生响应
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        String ip = ipInfoUtil.getIpAddr(request);

        if(ipLimitProperties.getEnable()) {
            String token1 = redisRaterLimiter.acquireToken(ip,
                    ipLimitProperties.getLimit(), ipLimitProperties.getTimeout());
            if (StrUtil.isBlank(token1)) {
                throw new ApiException("你手速怎么这么快，请点慢一点");
            }
        }

        if(limitProperties.getEnable()){
            String token2 = redisRaterLimiter.acquireToken(CommonConstant.LIMIT_ALL,
                    limitProperties.getLimit(), limitProperties.getTimeout());
            if (StrUtil.isBlank(token2)) {
                throw new ApiException("当前访问总人数太多啦，请稍后再试");
            }
        }

        try {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
            if (rateLimiter != null) {
                int limit = rateLimiter.limit();
                int timeout = rateLimiter.timeout();
                String token3 = redisRaterLimiter.acquireToken(method.getName(), limit, timeout);
                if (StrUtil.isBlank(token3)) {
                    throw new ApiException("当前访问人数太多啦，请稍后再试");
                }
            }
        }catch (ApiException e){
            throw new ApiException(e.getMsg());
        }catch (Exception e){

        }

        return true;
    }

    /**
     * 当前请求进行处理之后，也就是Controller方法调用之后执行，
     * 但是它会在DispatcherServlet 进行视图返回渲染之前被调用。
     * 此时我们可以通过modelAndView对模型数据进行处理或对视图进行处理。
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 方法将在整个请求结束之后，也就是在DispatcherServlet渲染了对应的视图之后执行。
     * 这个方法的主要作用是用于进行资源清理工作的。
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
    }

}
