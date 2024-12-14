package org.example.postimageservice.controller;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.ImageOfDonationDTO;
import com.gabriel.donation.service.DonationPostService;
import com.gabriel.donation.service.ImageOfDonationService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/api/image_of_donation")
@Mapper
public class ImageOfDonationController {

    @Autowired
    ImageOfDonationService imageOfDonationService;

    @GetMapping("/admin/get/{id}")
    @Cacheable("imageOfDonationPostAdmin")
    public String getAllImageOfDonationPost(
            @PathVariable("id") int id,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<ImageOfDonationDTO> list = imageOfDonationService.getAll(pageRequest);
        int totalPages = list.getTotalPages();
        List<ImageOfDonationDTO> imageOfDonations = list.getContent()
                .stream()
                .filter(imageOfDonationDTO -> imageOfDonationDTO.getDonationPostId()==id)
                .toList();
        model.addAttribute("donationPost", imageOfDonations);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/ImageOfDonationPost";

    }

    @GetMapping("/admin/add")
    public String showAddForm(Model model)
    {
        model.addAttribute("IMGdonationpost", new ImageOfDonationDTO());
        return "admin/addImageOfDonationPost";
    }

    @PostMapping("/saveDonationPost")
    public String saveDonationPost(
            @ModelAttribute("IMGdonationpost") ImageOfDonationDTO imageOfDonationDTO,
            Model model
    )
    {
        imageOfDonationService.addImageOfDonation(imageOfDonationDTO);
        return "redirect:/admin/get";
    }

    @GetMapping("/admin/hide/{id}")
    public String deleteImageOfDonationPost(@PathVariable("id") int id, Model model) {

        imageOfDonationService.deleteImageOfDonation(id);
        return "redirect:/admin/get";
    }

    @PostMapping("/admin/edit/{id}")
    public String showUpdateForm(
            @PathVariable("id") int id,
            Model model
    ) {
        ImageOfDonationDTO imageOfDonationDTO = imageOfDonationService.getImageOfDonationById(id);
        model.addAttribute("IMGdonationpost", imageOfDonationDTO);
        return "admin/updateImageOfDonationPost";
    }

    @PostMapping("/updateImageOfDonationPost")
    public String updateDonationPost(
            @ModelAttribute("IMGdonationpost") ImageOfDonationDTO imageOfDonationDTO,
            Model model
    ) {
        imageOfDonationService.updateImageOfDonation(imageOfDonationDTO, imageOfDonationDTO.getImageOfDonationId());
        return "redirect:/admin/get";
    }
}
