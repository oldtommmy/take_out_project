package com.tommychan.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tommychan.takeout.dto.DishDto;
import com.tommychan.takeout.entity.Dish;
import com.tommychan.takeout.entity.DishFlavor;
import com.tommychan.takeout.mapper.DishMapper;
import com.tommychan.takeout.service.DishFlavourService;
import com.tommychan.takeout.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavourService dishFlavourService;

    @Autowired
    private DishService dishService;

    @Transactional
    @Override
    public void saveDishWithFlavor(DishDto dishDto) {
        //保存菜品基本信息到 dish 表
        this.save(dishDto);

        Long dishId = dishDto.getId();

        //保存口味信息到 flavor
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavourService.saveBatch(flavors);
    }

    @Override
    public void updateDishWithFlavor(DishDto dishDto) {

        //更新 dish
        dishService.updateById(dishDto);

        //删除 DishFlavour 原有的 flavour
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavourService.remove(wrapper);

        //新增 flavour
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavourService.saveBatch(flavors);
    }
}
