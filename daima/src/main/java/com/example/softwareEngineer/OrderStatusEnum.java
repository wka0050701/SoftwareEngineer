package com.example.softwareEngineer;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum OrderStatusEnum {
    UNPAID(1, "待支付"),
    PAID(2, "已支付"),
    MAKING(3, "制作中"),
    DELIVERING(4, "配送中"),
    COMPLETED(5, "已完成"),
    CANCELED(6,"已取消");

    private final Integer code;
    private final String desc;

    OrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 根据 code 获取枚举
    public static OrderStatusEnum fromCode(Integer code) {
        for (OrderStatusEnum status : OrderStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的订单状态码：" + code);
    }
}
