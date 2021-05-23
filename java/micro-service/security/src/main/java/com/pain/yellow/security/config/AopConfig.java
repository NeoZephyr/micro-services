package com.pain.yellow.security.config;

import com.pain.yellow.security.aspect.RoleHierarchyReloadAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@RequiredArgsConstructor
@EnableAspectJAutoProxy
@Configuration
public class AopConfig {
    private final RoleHierarchyImpl roleHierarchy;
//    private final RoleHierarchyService roleHierarchyService;

    @Bean
    public RoleHierarchyReloadAspect roleHierarchyReloadAspect() {
//        return new RoleHierarchyReloadAspect(roleHierarchy, roleHierarchyService);
        return null;
    }
}
