package com.gabriel.donation.controller;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.PaymentDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.dto.UserDonatedDTO;
import com.gabriel.donation.service.DonationPostService;
import com.gabriel.donation.service.UserDonatedService;
import com.gabriel.donation.service.UserService;
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
import java.util.List;

@Controller
@RequestMapping("/api/user_donated")
@Mapper
public class UserDonatedController {
    /*
     * Các api cần làm:
     *   - Admin:
     *       + Lấy toàn bộ user donated [/admin/get] (có phân trang)
     *       + Lấy toàn bộ user donated của 1 user [/admin/get?user_id={id}] (có phân trang)
     *       + Lấy toàn bộ user donated của 1 user theo ngày (có phân trang)
     *       + Sửa [/admin/edit]
     *       + Xóa [/admin/hide]
     *   - User:
     *       + Xem toàn bộ user donated (xem lịch sử quyên góp) [/get]
     *       + Xem toàn bộ user donated theo ngày
     *       + Lấy toàn bộ user donated theo donation post id của bản thân
     * */

    @Autowired
    UserDonatedService userDonatedService;

    @Autowired
    UserService userService;

    @Autowired
    DonationPostService donationPostService;

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
            @RequestBody UserDonatedDTO userDonatedDTO, HttpSession session
    ){
        int userId = (int) session.getAttribute("userId");
        userDonatedDTO.setUserId(userId);
        userDonatedService.addUserDonated(userDonatedDTO);

        UserDTO userDTO = userService.findById(userId);
        userDTO.setBalance(userDTO.getBalance() - userDonatedDTO.getAmount());
        userService.updateUser(userDTO, userId);

        DonationPostDTO donationPostDTO = donationPostService.findById(userDonatedDTO.getDonationPostId());
        donationPostDTO.setCurrentAmount(donationPostDTO.getCurrentAmount() + userDonatedDTO.getAmount());
        donationPostDTO.setNumberOfDonation(donationPostDTO.getNumberOfDonation() + 1);
        donationPostService.updateDonationPost(donationPostDTO, userDonatedDTO.getDonationPostId());
        return "";
    }
}
