package com.choicemanager.service;

import com.choicemanager.domain.Role;
import com.choicemanager.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("RoleService")
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Set<Role> addRole(Set<Role> roles) {
        for (Role role : roles) {
            if (roleRepository.findByName(role.getName()) == null) {
                roleRepository.save(role);
            }
        }
        return roles;
    }
}
