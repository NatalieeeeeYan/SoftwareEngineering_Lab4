package com.team.ShopSystem.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.team.ShopSystem.sys.entity.DeliveryAddress;

import java.util.List;

/**
 * @Author: Zhong Siqi
 * @Description: TODO
 * @DateTime: 2023/5/9 11:03
 **/
public interface DeliveryAddressMapper extends BaseMapper<DeliveryAddress> {
    List<DeliveryAddress> getByUserId(Integer userId);

    void deleteById(Integer id);

    void setDefault(Integer id);

    void clearDefault(Integer userId);

    Integer getId();
}
