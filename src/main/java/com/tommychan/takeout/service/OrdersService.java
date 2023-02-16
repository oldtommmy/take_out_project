package com.tommychan.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tommychan.takeout.entity.Orders;

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
