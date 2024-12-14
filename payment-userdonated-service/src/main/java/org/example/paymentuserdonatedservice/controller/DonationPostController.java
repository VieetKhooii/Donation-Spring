package org.example.paymentuserdonatedservice.controller;

import com.gabriel.donation.dto.DonationPostDTO;
import com.gabriel.donation.dto.ImageOfDonationDTO;
import com.gabriel.donation.dto.UserDTO;
import com.gabriel.donation.service.DonationPostService;
import com.gabriel.donation.service.ImageOfDonationService;
import com.gabriel.donation.service.UserService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/donation_post")
@Mapper
public class DonationPostController {
    //admin
    @Autowired
    DonationPostService donationPostService;
    @Autowired
    ImageOfDonationService imageOfDonationService;
    @Autowired
    private UserService userService;

    @GetMapping("/admin/get")
    @Cacheable("donationPostAdmin")
    public String getAllDonationPost(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(
                page, limit
        );
        Page<DonationPostDTO> list = getDonationPostDTOPage(pageRequest);
        int totalPages = list.getTotalPages();
        List<DonationPostDTO> donations = list.getContent();
        model.addAttribute("donationPost", donations);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        return "admin/DonationPost/donationPost";
    }
    @GetMapping("/admin/get/{id}")
    public String showDonationPostById(
            @PathVariable("id") int id,
            Model model
    ){
        DonationPostDTO donationPostDTO = donationPostService.getDonationPostById(id);
        model.addAttribute("donationpost", donationPostDTO);
        return "admin/DonationPost/DetailDonationPost";
    }

    @GetMapping("/admin/add")
    public String showAddDonationPostForm(Model model)
    {
        List<UserDTO> userDTOList = userService.getUsers();
        model.addAttribute("users", userDTOList);
        model.addAttribute("donationpost", new DonationPostDTO());
        model.addAttribute("imagePost", new ImageOfDonationDTO());
        return "admin/DonationPost/addDonationPost";
    }
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//        dateFormat.setLenient(false);
//        binder.registerCustomEditor(Date.class, "startDate", new CustomDateEditor(dateFormat, true));
//        binder.registerCustomEditor(Date.class, "endDate", new CustomDateEditor(dateFormat, true));
//    }
    @PostMapping("/saveDonationPost")
    public String saveDonationPost(
            @ModelAttribute("donationpost") DonationPostDTO donationPostDTO,
            @ModelAttribute("imagePost") ImageOfDonationDTO imageOfDonationDTO,
            Model model
    ) {

        donationPostService.addDonationPost(donationPostDTO, imageOfDonationDTO);
        return "redirect:/api/admin/user/get";
    }

    @GetMapping("/admin/hide/{id}")
    @CacheEvict(value = "donationPostAdmin", allEntries = true)
    public String deleteDonationPost(@PathVariable("id") int id, Model model) {

        donationPostService.deleteDonationPost(id);
        return "redirect:/api/admin/user/get";
    }

    @GetMapping("/admin/edit/{id}")
    public String showUpdateForm(
            @PathVariable("id") int id,
            Model model
    ) {
        DonationPostDTO donationPostDTO = donationPostService.getDonationPostById(id);
        ImageOfDonationDTO imageOfDonationDTO = imageOfDonationService.getImageOfDonationById(id);
        model.addAttribute("donationpost", donationPostDTO);
        model.addAttribute("imageofdonation", imageOfDonationDTO);
        return "admin/DonationPost/updateDonationPost";
    }

    @PostMapping("/updateDonationPost")
    public String updateDonationPost(
            @ModelAttribute("donationpost") DonationPostDTO donationPostDTO,
            @ModelAttribute("imageofdonation") ImageOfDonationDTO imageOfDonationDTO,
            Model model
    ) {
        System.out.println(donationPostDTO.getStartDate());

        donationPostService.updateDonationPostAndImage(donationPostDTO,imageOfDonationDTO ,donationPostDTO.getDonationPostId());
        return "redirect:/api/admin/user/get";
    }

    //<<<<==================>>>>
    // User

    @GetMapping("/get")
    @Cacheable("donationPostUsers")
    public String getAllDonationPostForUser(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "3") int limit,
            Model model) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<DonationPostDTO> list = getDonationPostDTOPage(pageRequest);

        donationPostService.updateCurrentAmountForDonationPosts(list.getContent());

        List<Long> dateDifferences = getDateDifferences(list);

        model.addAttribute("lstDonationPost", list.getContent());
        model.addAttribute("totalPages", list.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("dateDifferences", dateDifferences);
        return "home";
    }

    @GetMapping("/getDonationPostByID")
    public String getDonationPostByID(@RequestParam("donationPostId") int id,  Model model)
    {
        DonationPostDTO donationPostDTO = donationPostService.getDonationPostById(id);
        model.addAttribute("donationPosts", donationPostDTO);
        return "donationPostDetail";
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
        Page<DonationPostDTO> list = getDonationPostDTOPage(pageRequest);

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

    // Extracted Methods
    public List<Long> getDateDifferences(Page<DonationPostDTO> list) {
        List<Long> dateDifferences = new ArrayList<>();

        for (DonationPostDTO donationPostDTO : list.getContent())
        {
            Date startDate = donationPostDTO.getStartDate();
            Date endDate = donationPostDTO.getEndDate();


            LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), endLocalDate);
            dateDifferences.add(daysBetween);
        }
        return dateDifferences;
    }

    public Page<DonationPostDTO> getDonationPostDTOPage(PageRequest pageRequest) {
        return donationPostService.getAll(pageRequest);
    }

    @GetMapping("/search")
    public String searchDonationPost(
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            Model model
    ){
        try
        {
            PageRequest pageRequest = PageRequest.of(page, limit);

            Page<DonationPostDTO> list = donationPostService.searchDonationPosts(pageRequest, title);
            List<Long> dateDifferences = getDateDifferences(list);
            model.addAttribute("lstDonationPost", list.getContent());
            model.addAttribute("totalPages", list.getTotalPages());
            model.addAttribute("currentPage", page);
            model.addAttribute("dateDifferences", dateDifferences);

            return "home";
        } catch (Exception e) {
            model.addAttribute("", "Có lỗi xảy ra trong quá trình tìm kiếm. Vui lòng thử lại sau.");

            return "home";
        }

    }

    @GetMapping("/getSorted")
    @Cacheable("donationPostUsers")
    public String getAllDonationPostForUserSorted(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "sortBy",defaultValue = "none")String sortBy,
            Model model) {
        PageRequest pageRequest = PageRequest.of(page, limit);

        Page<DonationPostDTO> list;
        if("endDateAsc".equals(sortBy))
        {
            list=donationPostService.getAllSortedByEndDate(pageRequest,true);
        }
        else if ("endDateDesc".equals(sortBy))
            list=donationPostService.getAllSortedByEndDate(pageRequest,false);
        else if ("PercentAsc".equals(sortBy))
            list=donationPostService.getAllSortedByPercent(pageRequest,true);
        else if ("PercentDesc".equals(sortBy))
            list=donationPostService.getAllSortedByPercent(pageRequest,false);
        else list=getDonationPostDTOPage(pageRequest);
        donationPostService.updateCurrentAmountForDonationPosts(list.getContent());

        List<Long> dateDifferences = getDateDifferences(list);

        model.addAttribute("lstDonationPost", list.getContent());
        model.addAttribute("totalPages", list.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("dateDifferences", dateDifferences);
        model.addAttribute("sortBy",sortBy);
        return "home";
    }

    @GetMapping("/admin/searchByDate")
    public String searchDonationPostsByDateRange(
            @RequestParam(value = "startDate") LocalDate startDate,
            @RequestParam(value = "endDate") LocalDate endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            Model model
    ) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        try
        {
            PageRequest pageRequest = PageRequest.of(page, limit);

            Page<DonationPostDTO> donationPostsPage = donationPostService.searchByDateRange(pageRequest, startDateTime, endDateTime);
            List<Long> dateDifferences = getDateDifferences(donationPostsPage);

            model.addAttribute("donationPost", donationPostsPage.getContent());
            model.addAttribute("totalPages", donationPostsPage.getTotalPages());
            model.addAttribute("currentPage", page);
            model.addAttribute("dateDifferences", dateDifferences);
            return "admin/DonationPost/donationPost";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra trong quá trình tìm kiếm. Vui lòng thử lại sau.");

            return "admin/DonationPost/donationPost";
        }

    }
    @GetMapping("/admin/searchByTitle")
    public String searchByTitle(
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            Model model
    ){
        try
        {
            PageRequest pageRequest = PageRequest.of(page, limit);

            Page<DonationPostDTO> list = donationPostService.searchDonationPosts(pageRequest, title);
            List<Long> dateDifferences = getDateDifferences(list);

            model.addAttribute("donationPost", list.getContent());
            model.addAttribute("totalPages", list.getTotalPages());
            model.addAttribute("currentPage", page);
            model.addAttribute("dateDifferences", dateDifferences);

            return "admin/DonationPost/donationPost";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra trong quá trình tìm kiếm. Vui lòng thử lại sau.");

            return "admin/DonationPost/donationPost";
        }

    }

}
