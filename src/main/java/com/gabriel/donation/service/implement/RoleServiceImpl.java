package com.gabriel.donation.service.implement;

import com.gabriel.donation.dto.RoleDTO;
import com.gabriel.donation.entity.Role;
import com.gabriel.donation.mapper.RoleMapper;
import com.gabriel.donation.repository.RoleRepo;
import com.gabriel.donation.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepo roleRepo;

    @Override
    public List<RoleDTO> getAll()
    {
        List<Role> roles=roleRepo.findAll();
        return  roles.stream()
                .map(RoleMapper.INSTANCE::toDto)
                .toList();
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

}
