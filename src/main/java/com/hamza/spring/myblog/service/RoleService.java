package com.hamza.spring.myblog.service;

import com.hamza.spring.myblog.payload.RoleDto;

import java.util.List;

public interface RoleService {
    RoleDto createRole(RoleDto roleDto);

    RoleDto updateRole(Long id, RoleDto roleDto);

    void deleteRole(Long id);

    RoleDto getRoleById(Long id);

    List<RoleDto> getAllRoles();
}
