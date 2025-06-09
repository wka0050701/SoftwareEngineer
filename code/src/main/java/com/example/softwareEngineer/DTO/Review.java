package com.example.softwareEngineer.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Review {
    private Integer reviewId;
    private Integer orderId;
    private Integer userId;
    private Integer rating;
    private String content;
    private String images;
    private String reply;
    private LocalDateTime replyTime;
    private LocalDateTime createdAt;
}
