package org.example.paymentuserdonatedservice.service;

import com.gabriel.donation.dto.RoleDTO;
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

    String findRoleNameById(int id);

    List<RoleDTO> getRoles();
}
