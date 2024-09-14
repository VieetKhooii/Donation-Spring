package com.gabriel.donation.controller;

import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@Mapper
public class CategoryController {
    /*
    * Các api cần làm:
    *   - Admin:
    *       + Lấy toàn bộ category [/admin/get] (có phân trang)
    *       + Thêm [/admin/add]
    *       + Sửa [/admin/edit]
    *       + Xóa [/admin/hide]
    *   - User:
    *       + Lấy toàn bộ category [/get]
    * */
}
