package com.gabriel.donation.service.implement;

import com.gabriel.donation.entity.Role;
import com.gabriel.donation.mapper.UserMapper;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.repository.RoleRepo;
import com.gabriel.donation.repository.UserRepo;
import com.gabriel.donation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;


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

    @Override
    public UserDTO addUser(UserDTO userDTO)
    {
        User user= UserMapper.INSTANCE.toEntity(userDTO);
        User savedUser=userRepo.save(user);
        return  UserMapper.INSTANCE.toDto(savedUser);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, int id)
    {
        User use1=userRepo.findById(id).get();
        use1.setPhone(userDTO.getPhone());
        use1.setBalance(userDTO.getBalance());

        Role role1=roleRepo.findById(userDTO.getRoleId()).get();
        use1.setRole(role1);
        use1.setDeleted(userDTO.isDeleted());
        use1.setPassword(userDTO.getPassword());

        User updatedUser=userRepo.save(use1);
        return  UserMapper.INSTANCE.toDto(updatedUser);
    }

    @Override
    public void deleteUser(int id)
    {
        if(userRepo.existsById(id))
            userRepo.deleteById(id);
    }
}
