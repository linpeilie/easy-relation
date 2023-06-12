package cn.easii.relation;

import cn.easii.relation.core.InjectRelation;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class AutoRelationAspect {

    private final InjectRelation injectRelation;

    @Around("@annotation(cn.easii.relation.annotation.AutoRelation)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        if (result instanceof Collection) {
            injectRelation.injectRelation((Collection) result);
        } else {
            injectRelation.injectRelation(result);
        }
        return result;
    }

}
