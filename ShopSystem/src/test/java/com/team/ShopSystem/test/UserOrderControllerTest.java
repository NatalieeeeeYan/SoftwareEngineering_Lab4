package com.team.ShopSystem.test;

import com.team.ShopSystem.common.vo.MsgEnum;
import com.team.ShopSystem.common.vo.Result;
import com.team.ShopSystem.config.ConstantsProperties;
import com.team.ShopSystem.sys.controller.UserOrderController;
import com.team.ShopSystem.sys.entity.*;
import com.team.ShopSystem.sys.mapper.*;
import com.team.ShopSystem.sys.service.IMerchantOrderService;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserOrderControllerTest {

    @InjectMocks
    private UserOrderController userOrderController;

    @InjectMocks
    private  UserOrderController orderController;

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
    public void testOrderFromCart() {
        // 创建一个用户购物车
        Integer userId = 10001;
        Integer cartId = 20001;
        when(cartMapper.getCartByUserId(userId)).thenReturn(cartId);

        // 创建要下单的商品列表
        List<UserOrder> userOrders = new ArrayList<>();
        UserOrder userOrder1 = new UserOrder(null, 30001, 10001,
                BigDecimal.valueOf(20).floatValue(), 2,
                BigDecimal.valueOf(40).floatValue(), null, null, null);
        UserOrder userOrder2 = new UserOrder(null, 30002, 10001,
                BigDecimal.valueOf(50).floatValue(),4,
                BigDecimal.valueOf(20).floatValue(), null, null, null);
        userOrders.add(userOrder1);
        userOrders.add(userOrder2);

        // 删除购物车中的商品，并创建订单
        Integer goodsId1 = userOrder1.getGoodsId();
        Integer goodsId2 = userOrder2.getGoodsId();
        doNothing().when(cartGoodsMapper).deleteBatch(eq(cartId), eq(goodsId1));
        doNothing().when(cartGoodsMapper).deleteBatch(eq(cartId), eq(goodsId2));
        when(userOrderMapper.insert(userOrder1)).thenReturn(1);
        when(userOrderMapper.insert(userOrder2)).thenReturn(1);

        // 调用下单方法，比较实际输出和预期输出是否一致
        Result<?> result = userOrderController.orderFromCart(userOrders);
        assertEquals(Result.success("下单成功"), result);
    }

    @Test
    public void testOrderDirect() {
        // 创建一个要下单的UserOrder对象
        UserOrder userOrder = new UserOrder(null, 30001, 10001,
                BigDecimal.valueOf(20).floatValue(), 2,
                BigDecimal.valueOf(40).floatValue(), null, null, null);

        // 使用Mockito框架模拟UserOrderMapper的insert()方法
        UserOrderMapper userOrderMapper = Mockito.mock(UserOrderMapper.class);
        doReturn(1).when(userOrderMapper).insert(userOrder);

        // 创建被测Controller对象，并设置依赖的Mock对象
        UserOrderController controller = new UserOrderController();
        controller.setUserOrderMapper(userOrderMapper);

        // 调用被测方法并检查结果是否正确
        Result<?> result = controller.orderDirect(userOrder);
        assertEquals(Result.success("下单成功"), result);
    }

    @Before
    public void setUp() {
        //初始化mock对象
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPay() throws Exception {
        // Mock数据
        Integer userId = 1;
        Float account = 1000f;

        Goods goods = new Goods();
        goods.setId(1);
        goods.setPrice(10f);

        List<UserOrder> userOrders = new ArrayList<>();
        UserOrder order1 = new UserOrder();
        order1.setId(1);
        order1.setUserId(userId);
        order1.setGoodsId(goods.getId());
        order1.setQuantity(2);
        order1.setTotalPrice(goods.getPrice() * order1.getQuantity());
        userOrders.add(order1);

        User user = new User();
        user.setId(userId);
        user.setAccount(account);

        Event event = new Event();
        event.setId(1);

        EventFunds eventFunds = new EventFunds();
        eventFunds.setId(1);
        eventFunds.setEventId(event.getId());
        eventFunds.setFunds(20f);

        //设置Mock行为和返回值
        Mockito.when(goodsMapper.selectById(order1.getGoodsId())).thenReturn(goods);
        Mockito.when(userMapper.selectById(userId)).thenReturn(user);
        Mockito.when(eventMapper.selectById(event.getId())).thenReturn(event);
        Mockito.when(eventFundsMapper.selectByEventId(event.getId())).thenReturn(eventFunds);

        //执行测试请求
        Result<?> result = userOrderController.pay(userOrders);

        //断言测试结果
        Assert.assertEquals(MsgEnum.SUCCESS.getCode(), result.getCode());
        Assert.assertEquals("支付成功", result.getMessage());
    }

    @Test
    public void testReceive() {
        // Create mock data
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1);
        userOrder.setShopId(2);
        userOrder.setEventId(3);
        userOrder.setGoodsId(4);
        userOrder.setQuantity(5);
        userOrder.setTotalPrice(50f);
        userOrder.setStatus(constants.getShipped());

        String stime = "2023-05-18";



        Shop shop = new Shop();
        shop.setId(userOrder.getShopId());
        shop.setAccount(100f);

        MerchantOrder merchantOrder = new MerchantOrder();
        merchantOrder.setId(1);
        merchantOrder.setUserOrderId(userOrder.getId());
        merchantOrder.setStatus(constants.getShipped());

        Event event = new Event();
        event.setId(userOrder.getEventId());

        Admin admin = new Admin();
        admin.setId(1);
        admin.setIntermediateAccount(500f);
        admin.setProfitAccount(100f);

        // Mock behaviors and return values
        Mockito.when(userOrderMapper.selectById(userOrder.getId())).thenReturn(userOrder);

        Goods goods = new Goods();
        goods.setId(userOrder.getGoodsId());
        goods.setPrice(10f);
        goods.setSales(10);
        Mockito.when(goodsMapper.getById(userOrder.getGoodsId())).thenReturn(goods);
        Mockito.when(goodsMapper.selectById(userOrder.getGoodsId())).thenReturn(goods);

        Mockito.when(shopMapper.getById(userOrder.getShopId())).thenReturn(shop);
        Mockito.when(merchantOrderMapper.selectByUserOrderId(userOrder.getId())).thenReturn(merchantOrder);
        Mockito.when(eventMapper.selectById(userOrder.getEventId())).thenReturn(event);
        Mockito.when(adminMapper.get()).thenReturn(admin);

        // Execute the testing method
        Result<?> result = orderController.receive(userOrder, stime);

        // Assert the test result
        assertTrue(result.getCode() == MsgEnum.SUCCESS.getCode());
        assertTrue(result.getMessage().equals("收货成功"));
    }

    @Test
    public void testRefunding() {
        // create mock data
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1);
        userOrder.setStatus(constants.getShipped());

        MerchantOrder merchantOrder = new MerchantOrder();
        merchantOrder.setId(1);
        merchantOrder.setUserOrderId(userOrder.getId());
        merchantOrder.setStatus(constants.getShipped());

        // setup mock behavior and return values
        Mockito.when(userOrderMapper.selectById(userOrder.getId())).thenReturn(userOrder);
        Mockito.when(merchantOrderMapper.selectByUserOrderId(userOrder.getId())).thenReturn(merchantOrder);

        // execute the testing method
        Result<?> result = orderController.refunding(userOrder.getId());

        // assert the test result
        assertTrue(result.getCode() == MsgEnum.SUCCESS.getCode());
        assertTrue(result.getMessage().equals("申请退货退款成功"));

        // assert that the user order and merchant order are updated with correct status
        assertTrue(userOrder.getStatus() == constants.getRefunding());
        assertTrue(merchantOrder.getStatus() == constants.getRefunding());
    }

    @Test
    public void testRefunded() {
        // create mock data
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1);
        userOrder.setUserId(2);
        userOrder.setTotalPrice(100f);
        userOrder.setStatus(constants.getRefunding());

        MerchantOrder merchantOrder = new MerchantOrder();
        merchantOrder.setId(1);
        merchantOrder.setUserOrderId(userOrder.getId());
        merchantOrder.setStatus(constants.getRefunding());

        Admin admin = new Admin();
        admin.setId(1);
        admin.setIntermediateAccount(200f);

        User user = new User();
        user.setId(userOrder.getUserId());
        user.setAccount(0f);

        // setup mock behavior and return values
        Mockito.when(userOrderMapper.updateById(userOrder)).thenReturn(1);
        Mockito.when(userOrderMapper.selectById(userOrder.getId())).thenReturn(userOrder);
        Mockito.when(merchantOrderMapper.selectByUserOrderId(userOrder.getId())).thenReturn(merchantOrder);
        Mockito.when(adminMapper.get()).thenReturn(admin);
        Mockito.when(userMapper.selectById(userOrder.getUserId())).thenReturn(user);
        Mockito.when(userMapper.updateById(user)).thenReturn(1);
        Mockito.when(adminMapper.updateById(admin)).thenReturn(1);

        // execute the testing method
        LocalDate time = LocalDate.of(2023, 5, 18); // set the 'stime' parameter to the current date
        String stime = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Result<?> result = orderController.refunded(userOrder, stime);

        // assert the test result
        assertTrue(result.getCode() == MsgEnum.SUCCESS.getCode());
        assertTrue(result.getMessage().equals("退货退款成功"));

        // assert that the user order and merchant order are updated with correct status
        assertTrue(userOrder.getStatus() == constants.getRefunded());
        assertTrue(merchantOrder.getStatus() == constants.getRefunded());

        // assert that the transfer record and account updates have been made
        Mockito.verify(transferRecordsMapper, Mockito.times(1)).insert(Mockito.any(TransferRecords.class));
        assertTrue(admin.getIntermediateAccount() == 100f);
        assertTrue(user.getAccount() == 100f);
    }
}