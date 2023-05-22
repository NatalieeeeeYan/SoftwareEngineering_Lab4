package com.team.ShopSystem.sys.mapper;

import com.team.ShopSystem.sys.entity.Event;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xby
 * @since 2023-05-09
 */
public interface EventMapper extends BaseMapper<Event> {
    Integer getLastId();
    List<Event> show();
    Integer queryRep(@Param("amount") Float amount, @Param("discount")Float discount, @Param("startDate")LocalDate startDate);
}
