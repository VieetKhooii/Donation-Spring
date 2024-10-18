package com.gabriel.donation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.PaymentDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.dto.UserDonatedDTO;
import com.gabriel.donation.entity.User;
import com.gabriel.donation.payload.CookieName;
import com.gabriel.donation.payload.PaymentMethod;
import com.gabriel.donation.service.DonationPostService;
import com.gabriel.donation.service.UserDonatedService;
import com.gabriel.donation.service.UserService;
import com.gabriel.donation.utils.CookieUtil;
import com.gabriel.donation.utils.DateTimeFormatter;
import com.gabriel.donation.utils.UserDonatedUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/user_donated")
@Mapper
public class UserDonatedController {

    @Autowired
    UserDonatedService userDonatedService;
    @Autowired
    UserService userService;
    @Autowired
    DonationPostService donationPostService;
    @Autowired
    CookieUtil cookieUtil;
    @Autowired
    UserDonatedUtil userDonatedUtil;
    @Autowired
    DateTimeFormatter dateTimeFormatter;

    @GetMapping("/admin/get")
    public String getAllUserDonated(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<UserDonatedDTO> list = userDonatedService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<UserDonatedDTO> userdonated = list.getContent();
        model.addAttribute("userdonated", userdonated);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/UserDonated";

    }

    //lọc theo user
    @GetMapping("/admin/getByUserId")
    public String getAllUserDonatedByUserId(
            @RequestParam("user_id") int id,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<UserDonatedDTO> list = userDonatedService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<UserDonatedDTO> userDonated = list.getContent()
                .stream()
                .filter(userDonatedDTO -> userDonatedDTO.getUserId()==id)
                .toList();
        model.addAttribute("userdonated", userDonated);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/UserDonated";

    }

    //lọc theo user và ngày donate
    @GetMapping("/admin/getByUserIdAndDate")
    public String getAllUserDonatedByUserIdAndDate(
            @RequestParam("user_id") int id,
            @RequestParam("donate_date") LocalDate donate_date,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<UserDonatedDTO> list = userDonatedService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<UserDonatedDTO> userdonated = list.getContent()
                .stream()
                .filter(userDonatedDTO -> userDonatedDTO.getUserId()==id)
                .filter(userDonatedDTO -> userDonatedDTO.getDonateDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().isEqual(donate_date))
                .toList();
        model.addAttribute("userdonated", userdonated);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/UserDonated";

    }

    @GetMapping("/admin/hide/{id}")
    public String deleteUserDonated(@PathVariable("id") int id, Model model) {

        userDonatedService.deleteUserDonated(id);
        return "redirect:/admin/get";
    }

    @PostMapping("/admin/edit/{id}")
    public String showUpdateForm(
            @PathVariable("id") int id,
            Model model
    ) {
        UserDonatedDTO userDonatedDTO = userDonatedService.getUserDonatedById(id);
        model.addAttribute("userdonated", userDonatedDTO);
        return "admin/updateUserdonated";
    }

    @PostMapping("/updateUserdonated")
    public String updateUserdonated(
            @ModelAttribute("userdonated") UserDonatedDTO userDonatedDTO,
            Model model
    ) {
        userDonatedService.updateUserDonated(userDonatedDTO, userDonatedDTO.getUserDonatedId());
        return "redirect:/admin/get";
    }

    //user
    @GetMapping("/get")
    public String getAllUserDonatedForUser(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<UserDonatedDTO> list = userDonatedService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<UserDonatedDTO> userdonated = list.getContent();
        model.addAttribute("userdonated", userdonated);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/UserDonated";

    }

    //xem toàn bộ user theo ngày
    @GetMapping("/admin/getByDate")
    public String getAllUserDonatedByDate(
            @RequestParam("donate_date") LocalDate donate_date,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<UserDonatedDTO> list = userDonatedService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<UserDonatedDTO> userdonated = list.getContent()
                .stream()
                .filter(userDonatedDTO -> userDonatedDTO.getDonateDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().isEqual(donate_date))
                .toList();
        model.addAttribute("userdonated", userdonated);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/UserDonated";

    }

    //lọc theo donation post id
    @GetMapping("/getByDonationPostId")
    public String getAllUserDonatedByDonationPostId(
            @RequestParam("donationpost_id") int id,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<UserDonatedDTO> list = userDonatedService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<UserDonatedDTO> userdonated = list.getContent()
                .stream()
                .filter(userDonatedDTO -> userDonatedDTO.getDonationPostId()==id)
                .toList();
        model.addAttribute("userdonated", userdonated);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "";

    }

    @PostMapping("donate")
    public String donate(
            @RequestParam("amount") int amount,
            @RequestParam("orderInfo") String orderInfo,
            @RequestParam("receiver") String receiver,
            @RequestParam("donationPostId") int donationPostId,
            @RequestParam("anonymous") boolean anonymous,
            HttpServletRequest request,
            Model model
    ) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        String userInfoJson = cookieUtil.getCookieValue(cookies, String.valueOf(CookieName.userInfo));
        UserDTO userDTO = cookieUtil.decodeUserDTOInCookie(userInfoJson);
        userDTO.setBalance(userDTO.getBalance() - amount);
        int userId = userDTO.getUserId();
        userService.updateUser(userDTO, userId);

        UserDonatedDTO userDonatedDTO = userDonatedUtil.setupUserDonatedForTransactions(amount, donationPostId, anonymous, PaymentMethod.GABRIEL_PAY, request);

        userDonatedService.processDonation(userDonatedDTO, userId);

        String paymentTime = new Date().toString();

        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("totalPrice", userDonatedDTO.getAmount());
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", userDonatedDTO.getUserDonatedId());
        return "/transaction/order-success";
    }

    @GetMapping("enter-donation-information")
    public String enterDonationInformation(
            @RequestParam("donationPostId") String donationPostId,
            @RequestParam("receiverId") int receiverId,
            Model model
    ){
        UserDTO userDTO = userService.findById(receiverId);
        String phone = userDTO.getPhone();
        model.addAttribute("donationPostId", donationPostId);
        model.addAttribute("receiverPhone", phone);
        return "transaction/transaction-information";
    }
}
