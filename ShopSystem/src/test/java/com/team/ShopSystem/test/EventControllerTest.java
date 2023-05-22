package com.team.ShopSystem.test;

import com.team.ShopSystem.common.vo.MsgEnum;
import com.team.ShopSystem.common.vo.Result;
import com.team.ShopSystem.config.ConstantsProperties;
import com.team.ShopSystem.sys.controller.EventController;
import com.team.ShopSystem.sys.controller.UserOrderController;
import com.team.ShopSystem.sys.entity.*;
import com.team.ShopSystem.sys.mapper.*;
import com.team.ShopSystem.sys.service.IMerchantOrderService;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventControllerTest {

    @InjectMocks
    private UserOrderController userOrderController;

    @InjectMocks
    private EventController eventController;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private CartGoodsMapper cartGoodsMapper;

    @Mock
    private UserOrderMapper userOrderMapper;

    @Mock
    private GoodsMapper goodsMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private EventFundsMapper eventFundsMapper;

    @Mock
    private TransferRecordsMapper transferRecordsMapper;

    @Mock
    private AdminMapper adminMapper;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventCategoryMapper eventCategoryMapper;

    @Mock
    private EventApplyMapper eventApplyMapper;

    @Mock
    private IMerchantOrderService merchantOrderService;

    @Mock
    private ConstantsProperties constants;

    @Mock
    private ShopMapper shopMapper;

    @Mock
    private MerchantOrderMapper merchantOrderMapper;

    @Test
    public void testOpenEvent() {
        // 生成mock数据
        Event event = new Event();
        event.setFunds(100.0f);
        event.setCategory(Arrays.asList("test"));

        Admin admin = new Admin();
        admin.setId(1);
        admin.setProfitAccount(200.0f);
        admin.setIntermediateAccount(200.0f);

        // mock方法的行为和返回值
        Mockito.when(adminMapper.get()).thenReturn(admin);
        Mockito.when(eventMapper.getLastId()).thenReturn(1);
        // 接下来为新的mock对象eventCategoryMapper分配一个新的值，以在测试运行期间正确初始化。
        Mockito.when(eventCategoryMapper.insert(Mockito.any(EventCategory.class))).thenReturn(1);


        // 执行要测试的方法
        LocalDate time = LocalDate.of(2023, 5, 18); //设置当前日期
        String stime = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Result<?> result = eventController.openEvent(event, stime);

        // 断言测试结果是否符合预期
        assertTrue(result.getCode() == MsgEnum.SUCCESS.getCode());
        assertTrue(result.getMessage().equals("成功增加活动"));

        //断言转账记录和管理员帐户更新已完成
        Mockito.verify(transferRecordsMapper, Mockito.times(1)).insert(Mockito.any(TransferRecords.class));
        assertTrue(admin.getIntermediateAccount() == 300.0f);
        assertTrue(admin.getProfitAccount() == 100.0f);
    }

}