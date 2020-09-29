package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-09-26
 */
public interface OrderService extends IService<Order> {

    //创建订单，返回订单号
    String createOrders(String courseId, String memberIdByJwtToken);
}
