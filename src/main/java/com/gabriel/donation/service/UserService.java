package com.gabriel.donation.service;

import com.gabriel.donation.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;


public interface UserService {
    Page<UserDTO> getUsersForAdmin(PageRequest pageRequest);

    UserDTO addUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO, int id);

    void deleteUser(int id);

    List<UserDTO> getUsers();

    Set<String> checkCache();

    UserDTO findById(int id);

    UserDTO findByPhone(String phone);

    String register(UserDTO userDTO);
}
