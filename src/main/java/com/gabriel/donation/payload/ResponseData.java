package com.gabriel.donation.payload;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ResponseData {
    private int statusCode;
    private String message;
    private Object data;
    private int totalPages; // chỉ dùng cho phân trang
}
