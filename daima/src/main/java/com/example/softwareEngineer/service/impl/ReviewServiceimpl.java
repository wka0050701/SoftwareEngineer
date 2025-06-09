package com.example.softwareEngineer.service.impl;

import com.example.softwareEngineer.DTO.Order;
import com.example.softwareEngineer.DTO.Review;
import com.example.softwareEngineer.Exception.BusinessException;
import com.example.softwareEngineer.mapper.OrderMapper;
import com.example.softwareEngineer.mapper.ReviewMapper;
import com.example.softwareEngineer.service.ReviewService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class ReviewServiceimpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Review addReview(Review review,Integer userId) throws BusinessException {

        Order order = orderMapper.getOrderById(review.getOrderId());

        // 验证订单是否已评价
        int reviewCount = reviewMapper.countReviewsByOrderId(review.getOrderId());
        if (reviewCount > 0) {
            throw new BusinessException("该订单已评价，不能重复评价");
        }

        // 构建评价对象
        Review review1 = new Review();
        BeanUtils.copyProperties(review, review1);
        review1.setCreatedAt(LocalDateTime.now());
        review1.setUserId(userId);

        // 验证订单是否存在且已完成
        if(order == null|| order.getStatus() != 5) {
            throw new BusinessException("订单不存在或未完成，无法评价");
        }else{
            reviewMapper.addReview(review1);
        }

        return review1;
    }

    @Override
    public Review addReply(Integer reviewId, Review review) {
        // 验证评价是否存在
        Review review1 = reviewMapper.getReviewById(reviewId);
        if (review1 == null) {
            throw new BusinessException("评价不存在");
        }

        // 验证是否已回复
        if (review1.getReply() != null) {
            throw new BusinessException("该评价已回复，不能重复回复");
        }

        // 添加回复
        reviewMapper.addReply(
                reviewId,
                review.getReply(),
                LocalDateTime.now()
        );

        // 更新并返回
        review1.setReply(review.getReply());
        review1.setReplyTime(LocalDateTime.now());
        return review1;
    }

}
