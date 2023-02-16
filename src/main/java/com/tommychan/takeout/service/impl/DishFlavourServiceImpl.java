package com.tommychan.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tommychan.takeout.entity.DishFlavor;
import com.tommychan.takeout.mapper.DishFlavourMapper;
import com.tommychan.takeout.service.DishFlavourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishFlavourServiceImpl extends ServiceImpl<DishFlavourMapper, DishFlavor> implements DishFlavourService {

    @Autowired
    DishFlavourService dishFlavourService;

    @Override
    public List<DishFlavor> getByDishId(Long id) {
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, id);
        return dishFlavourService.list(wrapper);
    }
}
