package com.tommychan.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tommychan.takeout.common.CustomException;
import com.tommychan.takeout.dto.DishDto;
import com.tommychan.takeout.entity.Dish;
import com.tommychan.takeout.entity.DishFlavor;
import com.tommychan.takeout.entity.Setmeal;
import com.tommychan.takeout.entity.SetmealDish;
import com.tommychan.takeout.mapper.DishMapper;
import com.tommychan.takeout.service.DishFlavourService;
import com.tommychan.takeout.service.DishService;
import com.tommychan.takeout.service.SetmealDishService;
import com.tommychan.takeout.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavourService dishFlavourService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

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

    @Transactional
    @Override
    public void updateStatus(int statusCode, List<Long> ids) {
        //停售菜品前应先确定是否存在含有该菜品的套餐
        //若含有 应先停售该套餐
        //查询涉及到哪些套餐
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getDishId, ids);
        List<SetmealDish> list = setmealDishService.list(wrapper);
        log.info("setmeal dish:{}", list);
        HashSet<Long> setmealId = new HashSet<>();
        for (SetmealDish setmealDish : list) {
            setmealId.add(setmealDish.getSetmealId());
        }
        if (setmealId.size() != 0) {
            List<Setmeal> setmeals = setmealService.listByIds(setmealId);
            for (Setmeal setmeal : setmeals) {
                if (setmeal.getStatus() == 0) {
                    throw new CustomException("请先将菜品所属的套餐停售");
                }
            }
        }


        List<Dish> newDishList = dishService.listByIds(ids).stream().map((item) -> {
            item.setStatus(statusCode);
            return item;
        }).collect(Collectors.toList());
        dishService.updateBatchById(newDishList);

    }
}
