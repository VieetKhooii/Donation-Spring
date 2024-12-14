package org.example.paymentuserdonatedservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.donation.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;


public interface UserService {
    Page<UserDTO> getUsersForAdmin(PageRequest pageRequest);

    String addUser(UserDTO userDTO);

    UserDTO updateUserAndCookie(UserDTO userDTO, int id, HttpServletResponse response) throws JsonProcessingException;

    UserDTO updateUser(UserDTO userDTO, int id);

    void deleteUser(int id);

    boolean exitsByPhone(String phone);

    List<UserDTO> getUsers();

    Set<String> checkCache();

    UserDTO findById(int id);

    UserDTO findByPhone(String phone);

    UserDTO findByEmail(String email);

    String register(UserDTO userDTO);

    void updatePassword(String email, String password);

    boolean signOut(HttpServletRequest request, HttpServletResponse response);
    UserDTO getUserById(int id);

    UserDTO registerNewGoogleUser(String email, String name);
}
