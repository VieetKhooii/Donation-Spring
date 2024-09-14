package com.gabriel.donation.controller;

import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}
