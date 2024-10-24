package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.RoleDTO;
import com.gabriel.donation.entity.Role;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.mapper.RoleMapper;
import com.gabriel.donation.mapper.UserMapper;
import com.gabriel.donation.repository.RoleRepo;
import com.gabriel.donation.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepo roleRepo;

    @Override
    public Page<RoleDTO> getAll(PageRequest pageRequest)
    {
        List<Role> roles=roleRepo.findAll(pageRequest).getContent();
        List<RoleDTO> roleDTOS = roles
                .stream()
                .map(RoleMapper.INSTANCE::toDto)
                .toList();
        return new PageImpl<RoleDTO>(
                roleDTOS,
                roleRepo.findAll(pageRequest).getPageable(),
                roleRepo.findAll(pageRequest).getTotalElements()
        );
    }

    @Override
    public RoleDTO addRole(RoleDTO roleDTO)
    {
        Role role=RoleMapper.INSTANCE.toEntity(roleDTO);
        Role savedRole=roleRepo.save(role);
        return RoleMapper.INSTANCE.toDto(savedRole);
    }

    @Override
    public RoleDTO updateRole(RoleDTO roleDTO, int id)
    {
        Role role=roleRepo.findById(id).get();
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        role.setDeleted(roleDTO.isDeleted());
        Role updatedRole=roleRepo.save(role);
        return RoleMapper.INSTANCE.toDto(updatedRole);
    }

    @Override
    public void deleteRole(int id)
    {
        if(roleRepo.existsById(id))
            roleRepo.deleteById(id);
    }

    @Override
    public RoleDTO getRoleById(int id)
    {
        return RoleMapper.INSTANCE.toDto(roleRepo.findById(id).get());
    }

    @Override
    public RoleDTO findByName(String name) {
        Optional<Role> roleOptional = roleRepo.findByName(name);
        Role role = roleOptional.get();
        return RoleMapper.INSTANCE.toDto(role);

    @Override
    public String findRoleNameById(int id) {
        return roleRepo.findById(id).get().getName();
    }


}