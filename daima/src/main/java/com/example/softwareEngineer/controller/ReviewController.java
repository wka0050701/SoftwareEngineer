package com.example.softwareEngineer.controller;


import com.example.softwareEngineer.DTO.Result;
import com.example.softwareEngineer.DTO.Review;
import com.example.softwareEngineer.service.ReviewService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.JwtUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviews")
    public Result addReview(
            @RequestHeader("Authorization")String authorization,
            @RequestBody Review review) {

        Integer userId = review.getUserId();

        if (userId == null) {
            return Result.error("用户ID不能为空");
        }

        try {
            Claims claims = JwtUtils.parseJWT(authorization);
            Integer userIdFromJwt = (Integer) claims.get("user_id");

            // 校验 JWT 中的用户 ID 与请求参数中的 id 是否一致（可选安全校验）
            if (!userIdFromJwt.equals(userId)) {
                return Result.error("权限验证失败");
            }
        } catch (Exception e) {
            log.error("JWT 验证失败：{}", e.getMessage());
            return Result.error("无效的 JWT 令牌");
        }

        Review review1 = reviewService.addReview(review,userId);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("reviewId", review1.getReviewId());
        data.put("orderId", review1.getOrderId());
        data.put("rating", review1.getRating());
        data.put("content", review1.getContent());
        data.put("images", review1.getImages());

        return Result.success(data);
    }


    @PostMapping("/reviews/{reviewId}/reply")
    public Result addReply(@PathVariable("reviewId") Integer reviewId,
                           @RequestBody Review review,
                            @RequestHeader("Authorization")String authorization) {
        Integer userId = 13;
        try {
            Claims claims = JwtUtils.parseJWT(authorization);
            Integer userIdFromJwt = (Integer) claims.get("user_id");

            if (!userIdFromJwt.equals(userId)) {
                return Result.error("权限验证失败");
            }
        } catch (Exception e) {
            log.error("JWT 验证失败：{}", e.getMessage());
            return Result.error("无效的 JWT 令牌");
        }

        Review review1 = reviewService.addReply(reviewId, review);

        // 构建返回结果
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("reviewId", review1.getReviewId());
        data.put("reply", review1.getReply());

        return Result.success(data);
    }
}
