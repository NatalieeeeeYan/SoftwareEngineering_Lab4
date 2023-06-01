package com.team.ShopSystem.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author xby
 * @since 2023-05-07
 */
@TableName("ss_goods_category")
@Data
@ApiModel(value = "商品类别对象", description = "")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GoodsCategory {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String categoryName;

    private Integer goodsId;

}
