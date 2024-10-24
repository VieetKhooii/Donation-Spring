package com.gabriel.donation.service;

import com.gabriel.donation.dto.RoleDTO;
import com.gabriel.donation.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RoleService {
    Page<RoleDTO> getAll(PageRequest pageRequest);

    RoleDTO addRole(RoleDTO roleDTO);

    RoleDTO updateRole(RoleDTO roleDTO, int id);

    void deleteRole(int id);

    RoleDTO getRoleById(int id);

    RoleDTO findByName(String name);
}
