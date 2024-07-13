package com.hamza.spring.myblog.service.servicesImplementation;

import com.hamza.spring.myblog.entity.Role;
import com.hamza.spring.myblog.payload.RoleDto;
import com.hamza.spring.myblog.repository.RoleRepository;
import com.hamza.spring.myblog.service.services.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImplementation implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RoleDto createRole(RoleDto roleDto) {
        Role role = modelMapper.map(roleDto, Role.class);
        role.setName("ROLE_" + role.getName().toUpperCase()); // Assuming roles are prefixed with "ROLE_"
        role = roleRepository.save(role);
        return modelMapper.map(role, RoleDto.class);
    }

    @Override
    @Transactional
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // Update the role properties
        existingRole.setName(roleDto.getName().toUpperCase());

        Role updatedRole = roleRepository.save(existingRole);
        return modelMapper.map(updatedRole, RoleDto.class);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        roleRepository.delete(role);
    }

    @Override
    @Transactional
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        return modelMapper.map(role, RoleDto.class);
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream().map(role -> modelMapper.map(role, RoleDto.class)).toList();
    }
}
