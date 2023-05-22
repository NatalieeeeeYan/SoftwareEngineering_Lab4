package com.team.ShopSystem.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author xby
 * @since 2023-05-09
 */
@TableName("ss_event")
@ApiModel(value = "Event对象", description = "")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Float funds;

    private Float amount;

    private Float discount;

    private Float shopFund;

    private Integer shopSales;

    private Integer shopProfit;

    public Event(LocalDate startDate, LocalDate endDate, Float funds, Float amount, Float discount, Float shopFund, Integer shopSales, Integer shopProfit, List<String> category) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.funds = funds;
        this.amount = amount;
        this.discount = discount;
        this.shopFund = shopFund;
        this.shopSales = shopSales;
        this.shopProfit = shopProfit;
        this.category = category;
    }

    private Integer status;

    @TableField(exist = false)
    private List<String> category;
}
