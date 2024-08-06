package com.example.userservice.bootstrap;

import com.example.userservice.entity.Role;
import com.example.userservice.enums.RoleEnum;
import com.example.userservice.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RoleRepository roleRepository;
    private static Logger logger = LoggerFactory.getLogger(RoleSeeder.class);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadRoles();
    }

    private void loadRoles() {
        RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.USER, RoleEnum.ADMIN, RoleEnum.SUPER_ADMIN };
        Map<RoleEnum, String> roleDescriptionMap = Map.of(
                RoleEnum.ADMIN, "Administrator role",
                RoleEnum.SUPER_ADMIN, "System administrator role",
                RoleEnum.USER, "Default user role"
        );

        Arrays.stream(roleNames).forEach(roleName -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);

            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role newRole = new Role();
                newRole.setName(roleName);
                newRole.setDescription(roleDescriptionMap.get(roleName));
                roleRepository.save(newRole);
            });
        });
    }
}
