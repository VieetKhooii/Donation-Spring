package com.gabriel.donation.controller;

import com.gabriel.donation.dto.CategoryDTO;
import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.service.DonationPostService;
import com.gabriel.donation.service.UserService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/donation_post")
@Mapper
public class DonationPostController {
    /*
     * Các api cần làm:
     *   - Admin:
     *       + Lấy toàn bộ donation post [/admin/get] (có phân trang)
     *       + Lấy toàn bộ donation post theo sponsor (có phân trang)
     *       + Thêm [/admin/add]
     *       + Sửa [/admin/edit]
     *       + Xóa [/admin/hide]
     *   - User:
     *       + Lấy toàn bộ donation post [/get]
     *       + Lấy toàn bộ donation post theo category [/get?category_id={id}]
     * */

    //admin
    @Autowired
    DonationPostService donationPostService;

    @GetMapping("/admin/get")
    public String getAllDonationPost(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<DonationPostDTO> list = donationPostService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<DonationPostDTO> donations = list.getContent();
        model.addAttribute("donationPost", donations);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/DonationPost";

    }

    //lọc theo sponsor
    @GetMapping("/admin/getBySponsorID")
    public String getAllDonationPostBySponsor(
            @RequestParam("sponsor_id") int id,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<DonationPostDTO> list = donationPostService.getAll(pageRequest);

        List<DonationPostDTO> donations = list.getContent()
                .stream()
                .filter(donationPostDTO -> donationPostDTO.getSponsorId() ==id )
                .toList();
        int totalPages = list.getTotalPages();
        model.addAttribute("donationPost", donations);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "";
    }

    @GetMapping("/admin/add")
    public String showAddDonationPostForm(Model model)
    {
        model.addAttribute("donationpost", new DonationPostDTO());
        return "admin/addDonationPost";
    }

    @PostMapping("/saveDonationPost")
    public String saveDonationPost(
            @ModelAttribute("donationpost") DonationPostDTO donationPostDTO,
            Model model
    )
    {
        donationPostService.addDonationPost(donationPostDTO);
        return "redirect:/admin/get";
    }

    @GetMapping("/admin/hide/{id}")
    public String deleteDonationPost(@PathVariable("id") int id, Model model) {

        donationPostService.deleteDonationPost(id);
        return "redirect:/admin/get";
    }

    @PostMapping("/admin/edit/{id}")
    public String showUpdateForm(
            @PathVariable("id") int id,
            Model model
    ) {
        DonationPostDTO donationPostDTO = donationPostService.getDonationPostById(id);
        model.addAttribute("donationpost", donationPostDTO);
        return "admin/updateDonationPost";
    }

    @PostMapping("/updateDonationPost")
    public String updateDonationPost(
            @ModelAttribute("donationpost") DonationPostDTO donationPostDTO,
            Model model
    ) {
        donationPostService.updateDonationPost(donationPostDTO, donationPostDTO.getDonationPostId());
        return "redirect:/admin/get";
    }

    //<<<<==================>>>>
    // User

    @GetMapping("/get")
    public String getAllDonationPostForUser(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<DonationPostDTO> list = donationPostService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<DonationPostDTO> donations = list.getContent();
        model.addAttribute("donationPost", donations);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "";
    }

    //lọc theo category id
    @GetMapping("/getByCategoryID")
    public String getAllDonationPostForUser(
            @RequestParam("category_id") int id,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<DonationPostDTO> list = donationPostService.getAll(pageRequest);

        List<DonationPostDTO> donations = list.getContent()
                .stream()
                .filter(donationPostDTO -> donationPostDTO.getCategoryId() ==id )
                .toList();
        int totalPages = list.getTotalPages();
        model.addAttribute("donationPost", donations);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "";
    }
}
