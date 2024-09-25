package com.gabriel.donation.controller;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.RoleDTO;
import com.gabriel.donation.dto.SponsorDTO;
import com.gabriel.donation.service.DonationPostService;
import com.gabriel.donation.service.SponsorService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/sponsor")
@Mapper
public class SponsorController {
    /*
     * Các api cần làm:
     *   - Admin:
     *       + Lấy toàn bộ sponsor [/admin/get] (có phân trang)
     *       + Sửa [/admin/edit]
     *       + Xóa [/admin/hide]
     * */

    @Autowired
    SponsorService sponsorService;

    @GetMapping("/admin/get")
    public String getAllSponsor(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<SponsorDTO> list = sponsorService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<SponsorDTO> sponsors = list.getContent();
        model.addAttribute("Sponsor", sponsors);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/Sponsor";

    }

    @GetMapping("/admin/hide/{id}")
    public String deleteSponsor(@PathVariable("id") int id, Model model) {

        sponsorService.deleteSponsor(id);
        return "redirect:/admin/get";
    }

    @PostMapping("/admin/edit/{id}")
    public String showUpdateForm(
            @PathVariable("id") int id,
            Model model
    ) {
        SponsorDTO sponsorDTO = sponsorService.getSponsorById(id);
        model.addAttribute("sponsor", sponsorDTO);
        return "admin/updateSponsor";
    }

    @PostMapping("/updateSponsor")
    public String updateSponsor(
            @ModelAttribute("sponsor") SponsorDTO sponsorDTO,
            Model model
    ) {
        sponsorService.updateSponsor(sponsorDTO, sponsorDTO.getSponsorId());
        return "redirect:/admin/get";
    }
}
