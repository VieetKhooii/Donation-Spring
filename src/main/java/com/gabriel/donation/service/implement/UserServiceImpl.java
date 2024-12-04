package com.gabriel.donation.service.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.donation.entity.BlacklistedToken;
import com.gabriel.donation.entity.Role;
import com.gabriel.donation.mapper.RoleMapper;
import com.gabriel.donation.mapper.UserMapper;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.payload.CookieName;
import com.gabriel.donation.repository.BlacklistedTokenRepo;
import com.gabriel.donation.repository.RoleRepo;
import com.gabriel.donation.repository.UserRepo;
import com.gabriel.donation.security.JwtGenerator;
import com.gabriel.donation.service.UserService;
import com.gabriel.donation.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    UserRepo userRepo;

    @Autowired
    CookieUtil cookieUtil;

    @Autowired
    BlacklistedTokenRepo blacklistedTokenRepo;

    @Autowired
    JwtGenerator jwtGenerator;

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
    public String addUser(UserDTO userDTO)
    {
        if (userRepo.findByPhone(userDTO.getPhone()).isPresent() &&
            !userDTO.getPhone().equalsIgnoreCase("phone")) {
            return "Số điện thoại đã tồn tại!";
        }
        try {
            User user = new User();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            roleRepo.findById(userDTO.getRoleId()).ifPresent(user::setRole);
            userRepo.save(user);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "Thêm tài khoản thành công!";
    }

    @Override
    public UserDTO updateUserAndCookie(UserDTO userDTO, int id, HttpServletResponse response) throws JsonProcessingException {
        User updatedUser = updateUserInformation(userDTO, id);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                updatedUser.getPhone(),
                updatedUser.getPassword(),
                updatedUser.getAuthorities()
        );

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String token = jwtGenerator.generateToken(authenticationToken);
        cookieUtil.createNewCookie(response, token, CookieName.jwt);

        UserDTO userDTOReturn = UserMapper.INSTANCE.toDto(updatedUser);
        cookieUtil.createNewCookie(response, userDTOReturn);

        return userDTOReturn;
    }

    private User updateUserInformation(UserDTO userDTO, int id) {
        User user = userRepo.findById(id);
        user.setPhone(userDTO.getPhone());
        user.setBalance(userDTO.getBalance());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        Role role1=roleRepo.findById(userDTO.getRoleId()).get();
        user.setRole(role1);
        user.setDeleted(userDTO.isDeleted());

        User updatedUser=userRepo.save(user);
        return updatedUser;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, int id) {
        User user = updateUserInformation(userDTO, id);
        return UserMapper.INSTANCE.toDto(user);
    }

    @Override
    public void deleteUser(int id)
    {
        User user = userRepo.findById(id);
        if(user != null){
            user.setDeleted(!user.isDeleted());
            userRepo.save(user);
        }
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
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return UserMapper.INSTANCE.toDto(user);
        } else {
            return null;
        }
    }

    @Override
    public UserDTO findByEmail(String email) {
        System.out.println("Searching for email "+email);
        Optional<User> user = userRepo.findByEmail(email);
        return user.map(UserMapper.INSTANCE::toDto).orElse(null);
//                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
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
            user.setEmail(userDTO.getEmail());
            Role role = roleRepo.findByName("ROLE_USER").get();
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

    @Override
    public boolean signOut(HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie[] cookies = request.getCookies();

            String tokenString = cookieUtil.getCookieValue(cookies, String.valueOf(CookieName.jwt));
            long expirationTime = jwtGenerator.getExpirationDateFromToken(tokenString);
            cookieUtil.setCookieToExpire(cookies, String.valueOf(CookieName.jwt), response);
            cookieUtil.setCookieToExpire(cookies, String.valueOf(CookieName.userInfo), response);

            BlacklistedToken blacklistedToken = new BlacklistedToken();
            blacklistedToken.setToken(tokenString);
            blacklistedToken.setExpirationTime(new Timestamp(expirationTime));
            blacklistedTokenRepo.save(blacklistedToken);


            return true;
        }
        catch (Exception e) {
//            e.printStackTrace();
            return false;
        }

    }

    @Override
    public UserDTO registerNewGoogleUser(String email, String name) {
        UserDTO user = new UserDTO();
        user.setEmail(email);
        user.setName(name);
//        user.setRoleId(roleService.getDefaultUserRoleId());

//        userRepo.save(user);
        return null;
    }



    @Override
    public UserDTO getUserById(int id) {
        return UserMapper.INSTANCE.toDto(userRepo.findById(id));
    }

}
