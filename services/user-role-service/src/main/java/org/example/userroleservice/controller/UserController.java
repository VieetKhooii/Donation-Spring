package org.example.userroleservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.userroleservice.config.RestPage;
import org.example.userroleservice.dto.RoleDTO;
import org.example.userroleservice.dto.UserDTO;
import org.example.userroleservice.payload.CookieName;
import org.example.userroleservice.service.RoleService;
import org.example.userroleservice.service.UserService;
import org.example.userroleservice.utils.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final CookieUtil cookieUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    public UserController(
            final UserService userService,
            final RoleService roleService,
            final CookieUtil cookieUtil,
            final RedisTemplate<String, Object> redisTemplate) {
        this.userService = userService;
        this.roleService = roleService;
        this.cookieUtil = cookieUtil;
        this.redisTemplate = redisTemplate;
    }
//    @Autowired
//    PaymentService paymentService;

    @GetMapping("/admin/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            Model model
    ) throws JsonProcessingException {

        String redisKey = "usersAdmin::" + page + "," + limit;

        String cachedData = (String) redisTemplate.opsForValue().get(redisKey);
        ObjectMapper mapper = new ObjectMapper();
        if (cachedData != null) {
            RestPage<UserDTO> cachedUsers = mapper.readValue(cachedData, new TypeReference<RestPage<UserDTO>>() {});
            model.addAttribute("users", cachedUsers.getContent());
            model.addAttribute("totalPages", cachedUsers.getTotalPages());
            model.addAttribute("currentPage", page);
            return ResponseEntity.ok().body(cachedUsers);
        }
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        RestPage<UserDTO> list = new RestPage<>(userService.getUsersForAdmin(pageRequest));
        String jsonInString = mapper.writeValueAsString(list);
        redisTemplate.opsForValue().set(redisKey, jsonInString, 1, TimeUnit.MINUTES);

        System.out.println(redisKey );

        model.addAttribute("users", list.getContent());
        model.addAttribute("totalPages", list.getTotalPages());
        model.addAttribute("currentPage", page);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/admin/add")
    public String showAddUserForm(
            Model model
    ) {
        List <RoleDTO> roleDTOS= roleService.getRoles();
        model.addAttribute("listRole",roleDTOS);
        model.addAttribute("user", new UserDTO());
        return "admin/User/addUser";
    }

    @PostMapping("/admin/saveUser")
    public ResponseEntity<?> saveUser(
            @RequestBody UserDTO userDTO,
            Model model
    )
    {
        if (userService.findByPhone(userDTO.getPhone()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số điện thoại đã tồn tại");
        }
        String message = userService.addUser(userDTO);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/admin/edit/{id}")
    public String showUpdateForm(
            @PathVariable("id") int id,
            Model model
    ) {
        UserDTO userDTO = userService.getUserById(id);
        model.addAttribute("user", userDTO);
        return "admin/User/updateUser";
    }
    @PostMapping("/admin/edit")
    public String editUser(
            @ModelAttribute UserDTO userDTO,
            HttpServletResponse response,
            Model model
    ){
        try {
            userService.updateUserAndCookie(userDTO, userDTO.getUserId(), response);
            model.addAttribute("message", "User updated successfully");
        } catch (Exception e) {
            model.addAttribute("message", "An error occurred while adding the user");
        }
        return "redirect:/api/admin/user/get";
    }

    @GetMapping("/admin/hide/{userId}")
    public String hideUser(
            @PathVariable int userId,
            Model model
    ){
        try {
            userService.deleteUser(userId);
            model.addAttribute("message", "User hidden successfully");
        } catch (Exception e) {
            model.addAttribute("message", "An error occurred while adding the user");
        }
        return "redirect:/api/admin/user/get";
    }

    // Test only, not use for Production
    @GetMapping("/getcache")
    public ResponseEntity<?> inspectCache(
            @RequestParam("key") String key
    ) {
        Object cachedData = redisTemplate.opsForValue().get(key);
        return cachedData != null ? ResponseEntity.ok(cachedData) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data found for key: " + key);
    }

    @GetMapping("/user_info")
    public String user_info(HttpSession session,
                            Model model,
                            HttpServletRequest request
    ) throws JsonProcessingException {
        Cookie[] cookie = request.getCookies();
        CookieUtil cookieUtil = new CookieUtil();
        String info = cookieUtil.getCookieValue(cookie, String.valueOf(CookieName.userInfo));
        if (!info.isEmpty() && info != null) {
            UserDTO userDTO = cookieUtil.decodeUserDTOInCookie(info);
            if (userDTO.getPhone().equalsIgnoreCase("phone")){
                userDTO.setPhone("Hãy nhớ cung cấp số điện thoại nha!");
            }
            model.addAttribute("userLogin", userDTO);
        }
        return "user_information/user_info";
    }

    @GetMapping("/to_deposit")
    public String to_deposit(HttpSession session,
                            Model model){
        return "user_information/deposit";
    }

//    @PostMapping("/deposit")
//    public String deposit(
//            @RequestParam("deposit_amount") long depositAmount,
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws JsonProcessingException {
//        Cookie[] cookie = request.getCookies();
//        String userInfo = cookieUtil.getCookieValue(cookie, String.valueOf(CookieName.userInfo));
//        int userId = cookieUtil.decodeUserDTOInCookie(userInfo).getUserId();
//
//        PaymentDTO paymentDTO=new PaymentDTO();
//        paymentDTO.setAmount(depositAmount);
//        paymentDTO.setUserId(userId);
//        paymentDTO.setDeleted(false);
//        paymentDTO.setTransactionDate(Date.valueOf(LocalDate.now()));
//        paymentService.addPayment(paymentDTO);
//
//        UserDTO userDTO = userService.findById(userId);
//        userDTO.setBalance(userDTO.getBalance()+paymentDTO.getAmount());
//        userService.updateUserAndCookie(userDTO, userId, response);
//        return  "redirect:/api/user/user_info";
//    }

    @GetMapping("/to_changeInfo")
    public String to_changeInfo(HttpServletRequest request,
                             Model model) throws JsonProcessingException {
        Cookie[] cookie = request.getCookies();
        CookieUtil cookieUtil = new CookieUtil();
        String info = cookieUtil.getCookieValue(cookie, String.valueOf(CookieName.userInfo));
        UserDTO userDTO = cookieUtil.decodeUserDTOInCookie(info);
        model.addAttribute("userLogin", userDTO);
        return "user_information/change_info";
    }

    @PostMapping("changeInfo")
    public  ResponseEntity<?> changeInfo(@RequestBody UserDTO userDTOInput,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws JsonProcessingException {
        if (userDTOInput.getPhone() == null || userDTOInput.getPhone().isEmpty() ||
                userDTOInput.getName() == null || userDTOInput.getName().isEmpty()||
                userDTOInput.getEmail() == null || userDTOInput.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thông tin đăng nhập không được để trống!");
        }

        // Kiểm tra định dạng số điện thoại Việt Nam
        if (!userDTOInput.getPhone().matches("^0[0-9]{9}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số điện thoại không đúng định dạng Việt Nam!");
        }

        // Kiểm tra định dạng email
        if (!userDTOInput.getEmail().matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email không đúng định dạng!");
        }

        // Kiểm tra tên người dùng ít nhất 3 ký tự
        if (userDTOInput.getName().length() < 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tên người dùng phải có ít nhất 3 ký tự!");
        }

        Cookie[] cookie = request.getCookies();
        String info = cookieUtil.getCookieValue(cookie, String.valueOf(CookieName.userInfo));
        UserDTO userDTO = cookieUtil.decodeUserDTOInCookie(info);

        // nhập số chưa tồn tại trong db thif bị null, cập nhật k được
        UserDTO existingUser = userService.findByPhone(userDTOInput.getPhone());

        if (existingUser != null) {
            if (existingUser.getUserId() != userDTO.getUserId())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số điện thoại đã được dùng");
        }

        UserDTO unChangeInfo = userService.findById(userDTO.getUserId());
        userDTOInput.setBalance(unChangeInfo.getBalance());
        userDTOInput.setRoleId(unChangeInfo.getRoleId());
        userDTOInput.setDeleted(unChangeInfo.isDeleted());
        userService.updateUserAndCookie(userDTOInput, userDTO.getUserId(), response);

//        session.setAttribute("username", userDTOInput.getName());
        return ResponseEntity.ok("update_success");
    }
}
