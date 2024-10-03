package com.gabriel.donation.controller;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.PaymentDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.service.DonationPostService;
import com.gabriel.donation.service.PaymentService;
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
@RequestMapping("/api/payment")
@Mapper
public class PaymentController {
    /*
     * Các api cần làm:
     *   - Admin:
     *       + Lấy toàn bộ payment [/admin/get] (có phân trang)
     *       + Lấy toàn bộ payment của 1 user [/admin/get?user_id={id}] (có phân trang)
     *       + Lấy toàn bộ payment của 1 user theo ngày [/admin/get?user_id={id}&transaction_date={}] (có phân trang)
     *       + Sửa [/admin/edit]
     *       + Xóa [/admin/hide]
     *   - User:
     *       + Xem toàn bộ payment (xem lịch sử nạp tiền) [/get]
     *       + Xem toàn bộ payment theo ngày
     *       + Lấy toàn bộ payment theo donation post id của bản thân [/get?donation_post_id={id}]
     * */

    @Autowired
    PaymentService paymentService;
    @Autowired
    UserService userService;

    @GetMapping("/admin/get")
    public String getAllPayment(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<PaymentDTO> list = paymentService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<PaymentDTO> payments = list.getContent();
        model.addAttribute("Payment", payments);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/Payment";

    }

    //lọc theo user
    @GetMapping("/admin/getByUserID")
    public String getAllPaymentByUserId(
            @RequestParam("user_id") int id,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<PaymentDTO> list = paymentService.getAll(pageRequest);

        List<PaymentDTO> payments = list.getContent()
                .stream()
                .filter(paymentDTO -> paymentDTO.getUserId() ==id )
                .toList();
        int totalPages = list.getTotalPages();
        model.addAttribute("donationPost", payments);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "";
    }

    //lọc theo user và transaction date
    @GetMapping("/admin/getByUserIDAndDate")
    public String getAllPaymentByUserAndDate(
            @RequestParam("user_id") int id,
            @RequestParam("transaction_date") LocalDate transaction_date,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<PaymentDTO> list = paymentService.getAll(pageRequest);

        List<PaymentDTO> payments = list.getContent()
                .stream()
                .filter(paymentDTO -> paymentDTO.getUserId() ==id )
                .filter(paymentDTO -> paymentDTO.getTransactionDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().isEqual(transaction_date))
                .toList();
        int totalPages = list.getTotalPages();
        model.addAttribute("donationPost", payments);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "";
    }

    @GetMapping("/admin/hide/{id}")
    public String deletePayment(@PathVariable("id") int id, Model model) {

        paymentService.deletePayment(id);
        return "redirect:/admin/get";
    }

    @PostMapping("/admin/edit/{id}")
    public String showUpdateForm(
            @PathVariable("id") int id,
            Model model
    ) {
        PaymentDTO paymentDTO = paymentService.getPaymentById(id);
        model.addAttribute("payment", paymentDTO);
        return "admin/updatePayment";
    }

    @PostMapping("/updatePayment")
    public String updateDonationPost(
            @ModelAttribute("payment") PaymentDTO paymentDTO,
            Model model
    ) {
        paymentService.updatePayment(paymentDTO, paymentDTO.getPaymentId());
        return "redirect:/admin/get";
    }

    //User
    @GetMapping("/get")
    public String getAllPaymentForUser(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<PaymentDTO> list = paymentService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<PaymentDTO> payments = list.getContent();
        model.addAttribute("Payment", payments);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/Payment";

    }

    //lấy toàn bộ payment theo ngày
    @GetMapping("/getByDate")
    public String getAllPaymentByDate(
            @RequestParam("transaction_date") LocalDate transaction_date,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<PaymentDTO> list = paymentService.getAll(pageRequest);

        List<PaymentDTO> payments = list.getContent()
                .stream()
                .filter(paymentDTO -> paymentDTO.getTransactionDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().isEqual(transaction_date))
                .toList();
        int totalPages = list.getTotalPages();
        model.addAttribute("donationPost", payments);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "";
    }

    @PostMapping("/deposit")
    public String deposit(
            @RequestBody PaymentDTO paymentDTO, HttpSession session
    ){
        int userId = (int) session.getAttribute("userId");
        paymentDTO.setUserId(userId);
        paymentService.addPayment(paymentDTO);

        UserDTO userDTO = userService.findById(userId);
        userDTO.setBalance(userDTO.getBalance()+paymentDTO.getAmount());
        userService.updateUser(userDTO, userId);
        return "";
    }
}
