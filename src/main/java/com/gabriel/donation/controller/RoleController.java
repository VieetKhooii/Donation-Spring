package com.gabriel.donation.controller;

import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
@Mapper
public class RoleController {
    /*
     * Các api cần làm:
     *   - Admin:
     *       + Lấy toàn bộ role [/admin/get] (có phân trang)
     *       + Sửa [/admin/edit]
     *       + Xóa [/admin/hide]
     */
}
