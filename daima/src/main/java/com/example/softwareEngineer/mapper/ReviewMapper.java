package com.example.softwareEngineer.mapper;

import com.example.softwareEngineer.DTO.Review;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface ReviewMapper {

    // 提交评价
    @Insert("INSERT INTO reviews " +
            "(user_id,order_id, rating, content, created_at,images) " +
            "VALUES (#{userId},#{orderId}, #{rating}, #{content}, NOW(),#{images})")
    @Options(useGeneratedKeys = true, keyProperty = "reviewId")
    void addReview(Review review);

    // 商家回复评价
    @Update("UPDATE reviews SET reply = #{reply} WHERE review_id = #{reviewId}")
    int replyReview(@Param("reviewId") Integer reviewId, @Param("reply") String reply);

    // 查询评价
    @Select("SELECT * FROM reviews WHERE review_id = #{reviewId}")
    Review getReviewById(Integer reviewId);

    // 验证订单是否存在且未评价
    @Select("SELECT COUNT(*) FROM orders WHERE order_id = #{orderId} AND status = 'COMPLETED'")
    int countCompletedOrder(Integer orderId);

    @Select("SELECT COUNT(*) FROM reviews WHERE order_id = #{orderId}")
    int countReviewsByOrderId(Integer orderId);

    // 添加商家回复
    @Update("UPDATE reviews " +
            "SET reply = #{reply}, reply_time = #{replyTime} " +
            "WHERE review_id = #{reviewId}")
    void addReply(@Param("reviewId") Integer reviewId,
                 @Param("reply") String reply,
                 @Param("replyTime") LocalDateTime replyTime);
}
