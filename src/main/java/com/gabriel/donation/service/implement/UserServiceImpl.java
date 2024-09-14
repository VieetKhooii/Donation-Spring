package com.gabriel.donation.service.implement;

import com.gabriel.donation.mapper.UserMapper;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.repository.UserRepo;
import com.gabriel.donation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Override
    public Page<UserDTO> getUsersForAdmin(PageRequest pageRequest) {
        List<User> users = userRepo.findAll(pageRequest).getContent();
        List<UserDTO> userDTOS = users
                .stream()
                .map(UserMapper.INSTANCE::toDto)
                .toList();
        return new PageImpl<UserDTO>(
                userDTOS,
                userRepo.findAll(pageRequest).getPageable(),
                userRepo.findAll(pageRequest).getTotalElements()
        );
    }
}
