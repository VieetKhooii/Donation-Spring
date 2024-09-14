package com.gabriel.donation.controller;

import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}
