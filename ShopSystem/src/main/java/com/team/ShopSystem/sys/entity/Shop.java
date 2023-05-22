package com.team.ShopSystem.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author xby
 * @since 2023-03-13
 */
@TableName("ss_shop")
@Getter
@Setter
@NoArgsConstructor
public class Shop {
    public Shop(Integer userId, String shopName, List<String> category, String idNumber, String introduce, String address, Float fund, LocalDate registrationTime) {
        this.userId = userId;
        this.shopName = shopName;
        this.category = category;
        this.idNumber = idNumber;
        this.introduce = introduce;
        this.address = address;
        this.fund = fund;
        this.registrationTime = registrationTime;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String shopName;
    @TableField(exist = false)
    private List<String> category;
    private String idNumber;
    private String introduce;
    private String address;
    private Float fund;
    private LocalDate registrationTime;
    private Integer status;
    private Float account;

}
