package com.gabriel.donation.service;

import com.gabriel.donation.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface UserService {
    Page<UserDTO> getUsersForAdmin(PageRequest pageRequest);
}
