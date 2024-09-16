package com.gabriel.donation.service;

import com.gabriel.donation.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    List<RoleDTO> getAll();

    RoleDTO addRole(RoleDTO roleDTO);

    RoleDTO updateRole(RoleDTO roleDTO, int id);

    void deleteRole(int id);
}
