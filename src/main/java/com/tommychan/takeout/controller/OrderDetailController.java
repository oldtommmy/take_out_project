package com.tommychan.takeout.controller;


import com.tommychan.takeout.mapper.OrderDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class OrderDetailController {

    @Autowired
    private OrderDetailMapper orderDetailMapper;


}
