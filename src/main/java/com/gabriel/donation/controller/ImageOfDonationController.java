package com.gabriel.donation.controller;

import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/image_of_donation")
@Mapper
public class ImageOfDonationController {
    /*
     * Các api cần làm:
     *   - Admin:
     *       + Lấy toàn bộ image theo donation post id [/admin/get?donation_post_id={id}] (có phân trang)
     *       + Thêm [/admin/add]
     *       + Sửa [/admin/edit]
     *       + Xóa [/admin/hide]
     * */
}
