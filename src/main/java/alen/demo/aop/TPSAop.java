package alen.demo.aop;

import alen.demo.annotation.TPSLimit;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Alen
 * @Date: 2022/2/9
 */
@Aspect
@Component
public class TPSAop {

    // 保证线程安全
    private static ConcurrentHashMap<String, ExpiringMap<String, Integer>> map = new ConcurrentHashMap<>();

    // 定义切点
    @Pointcut("@annotation(tpsLimit)")
    public void excudeService(TPSLimit tpsLimit) {
    }

    @Around("excudeService(tpsLimit)")
    public Object doAround(ProceedingJoinPoint pjp, TPSLimit tpsLimit) throws Throwable {

        // 获得request对象
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        // 获取Map对象， 如果没有则返回默认值
        // 第一个参数是key， 第二个参数是默认值
        ExpiringMap<String, Integer> uc = map.getOrDefault(request.getRequestURI(), ExpiringMap.builder().variableExpiration().build());
        Integer uCount = uc.getOrDefault(request.getRemoteAddr(), 0);

        // 超过次数，不执行目标方法
        if (uCount >= tpsLimit.count()) {
            return "接口请求超过次数";
        } else if (uCount == 0){
            // 第一次请求时，设置有效时间
            uc.put(request.getRemoteAddr(), uCount + 1, ExpirationPolicy.CREATED, tpsLimit.time(), TimeUnit.MILLISECONDS);
        } else {
            // 未超过次数， 记录加一
            uc.put(request.getRemoteAddr(), uCount + 1);
        }
        map.put(request.getRequestURI(), uc);

        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();

        return result;
    }
 }
