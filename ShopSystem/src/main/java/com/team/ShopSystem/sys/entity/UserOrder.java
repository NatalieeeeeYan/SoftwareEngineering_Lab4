package com.team.ShopSystem.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author xby
 * @since 2023-05-03
 */
@TableName("ss_user_order")
@ApiModel(value = "UserOrder对象", description = "")
@Data
@Getter
@Setter
@NoArgsConstructor
public class UserOrder {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer shopId;

    private Integer goodsId;

    private Integer userId;

    private Float unitPrice;

    private Integer quantity;

    private Float totalPrice;

    private Integer addressId;

    private Integer status;
    private Integer eventId;
    private Integer deleted;
    private LocalDate date;

    public UserOrder(Integer shopId, Integer goodsId, Integer userId, Float unitPrice, Integer quantity, Float totalPrice, Integer addressId, Integer eventId, LocalDate date) {
        this.shopId = shopId;
        this.goodsId = goodsId;
        this.userId = userId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.addressId = addressId;
        this.eventId = eventId;
        this.date = date;
    }
}
