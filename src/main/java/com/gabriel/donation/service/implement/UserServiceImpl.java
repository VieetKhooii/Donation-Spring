package com.gabriel.donation.service.implement;

import com.gabriel.donation.entity.BlacklistedToken;
import com.gabriel.donation.entity.Role;
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
import jakarta.servlet.http.HttpSession;
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

import java.sql.Timestamp;
import java.util.Date;
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
        use1.setName(userDTO.getName());
        Role role1=roleRepo.findById(userDTO.getRoleId()).get();
        use1.setRole(role1);
        use1.setDeleted(userDTO.isDeleted());
        if (userDTO.getPassword()!= null && !userDTO.getPassword().isEmpty())
            use1.setPassword(userDTO.getPassword());

        User updatedUser=userRepo.save(use1);
        return  UserMapper.INSTANCE.toDto(updatedUser);
    }
//$2a$10$g2sXLiee22iaWNNUJCSgeu4qNvFO5X8ucUILKgQ49P86x3N2NvNC6
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
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return UserMapper.INSTANCE.toDto(user);
        } else {
            return null;
        }
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
            System.out.println("qiwfbiqwubfwiu");
            System.out.println(userDTO.getEmail());
            System.out.println(userDTO.getPhone());
            return "Số điện thoại đã tồn tại!";
        }
        try {
            User user = new User();
            user.setName(userDTO.getName());
            user.setPhone(userDTO.getPhone());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setEmail(userDTO.getEmail());
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
            e.printStackTrace();
            return false;
        }

    }


}
