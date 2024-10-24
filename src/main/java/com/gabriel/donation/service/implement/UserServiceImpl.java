package com.gabriel.donation.service.implement;

import com.gabriel.donation.entity.Role;
import com.gabriel.donation.mapper.UserMapper;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.repository.RoleRepo;
import com.gabriel.donation.repository.UserRepo;
import com.gabriel.donation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public Page<UserDTO> getUsersForAdmin(PageRequest pageRequest) {
        List<User> users = userRepo.findAll(pageRequest).getContent();
        List<UserDTO> userDTOS = users
                .stream()
                .map(UserMapper.INSTANCE::toDto)
                .toList();
        return new PageImpl<UserDTO>(
                userDTOS,
                pageRequest,
                userRepo.findAll(pageRequest).getTotalElements()
        );
    }

    @Override
    public UserDTO addUser(UserDTO userDTO)
    {
        User user= UserMapper.INSTANCE.toEntity(userDTO);
        User savedUser=userRepo.save(user);
        return UserMapper.INSTANCE.toDto(savedUser);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, int id)
    {
        User use1=userRepo.findById(id);
        use1.setPhone(userDTO.getPhone());
        use1.setBalance(userDTO.getBalance());

        Role role1=roleRepo.findById(userDTO.getRoleId()).get();
        use1.setRole(role1);
        use1.setDeleted(userDTO.isDeleted());
        if (userDTO.getPassword()!= null && !userDTO.getPassword().isEmpty())
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

    @Override
    public boolean exitsByPhone(String phone) {
        return userRepo.existsByPhone(phone);
    }

    @Cacheable(value = "usersCache", key = "'userList'", sync = true)
    @Override
    public List<UserDTO> getUsers() {
        System.out.println("hello - fetching users from DB");
        List<User> user = userRepo.findAll();
        return user
                .stream()
                .map(UserMapper.INSTANCE::toDto)
                .toList();
    }

    public Set<String> checkCache() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            keys.forEach(System.out::println);
            return keys;
        } else {
            System.out.println("No keys found in Redis cache.");
            return null;
        }
    }

    @Override
    public UserDTO findById(int id) {
        return UserMapper.INSTANCE.toDto(userRepo.findById(id));
    }

    @Override
    public UserDTO findByPhone(String phone){
        Optional<User> userOptional = userRepo.findByPhone(phone);
        User user = userOptional.get();
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    public String register(UserDTO userDTO) {
        if (userRepo.findByPhone(userDTO.getPhone()).isPresent()) {
            return "Số điện thoại đã tồn tại!";
        }
        try {
            User user = new User();
            user.setName(userDTO.getName());
            user.setPhone(userDTO.getPhone());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            Role role = roleRepo.findByName("USER").get();
            user.setRole(role);
            userRepo.save(user);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "Đăng ký thành công!";
    }

    @Override
    public void updatePassword(String email, String password){
        userRepo.updatePassword(email, password);
    }


}
