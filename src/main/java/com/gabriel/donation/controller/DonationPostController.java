package com.gabriel.donation.controller;

import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}
