package com.gy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gy.reggie.entity.Orders;

public interface OrdersService extends IService<Orders> {
    void submit(Orders order);

}
