package com.tommychan.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tommychan.takeout.common.BaseContext;
import com.tommychan.takeout.common.R;
import com.tommychan.takeout.dto.OrdersDto;
import com.tommychan.takeout.entity.OrderDetail;
import com.tommychan.takeout.entity.Orders;
import com.tommychan.takeout.service.OrderDetailService;
import com.tommychan.takeout.service.OrdersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> userPage(int page, int pageSize) {

        Page<OrdersDto> dtoPage = new Page<>();
        Page<Orders> ordersPage = new Page<>();

        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        wrapper.orderByDesc(Orders::getOrderTime);

        ordersService.page(ordersPage, wrapper);
        BeanUtils.copyProperties(ordersPage, dtoPage, "records");

        List<Orders> ordersPageRecords = ordersPage.getRecords();
        List<OrdersDto> dtoRecords = ordersPageRecords.stream().map((item) -> {
            OrdersDto dto = new OrdersDto();
            BeanUtils.copyProperties(item, dto);

            dto.setUserName(item.getUserName());
            dto.setPhone(item.getPhone());
            dto.setAddress(item.getAddress());
            dto.setConsignee(item.getConsignee());

            LambdaQueryWrapper<OrderDetail> orderDetailWrapper = new LambdaQueryWrapper<>();
            orderDetailWrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> detailList = orderDetailService.list(orderDetailWrapper);
            dto.setOrderDetails(detailList);

            return dto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(dtoRecords);

        return R.success(dtoPage);
    }
}
