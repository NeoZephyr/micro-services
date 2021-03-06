package com.pain.yellow.security.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Slf4j
@RequiredArgsConstructor
@Aspect
public class RoleHierarchyReloadAspect {
    private final RoleHierarchyImpl roleHierarchy;
//    private final RoleHierarchyService roleHierarchyService;

    @Pointcut("execution(* com.pain.yellow.security.service.admin.*.*(..))")
    public void applicationPackagePointcut() {
    }

    @AfterReturning("applicationPackagePointcut() && @annotation(com.pain.yellow.security.annotation.ReloadRoleHierarchy)")
    public void reloadRoleHierarchy() {
//        val roleMap = roleHierarchyService.getRoleHierarchyExpr();
//        roleHierarchy.setHierarchy(roleMap);
        log.debug("RoleHierarchy Reloaded");
    }
}
