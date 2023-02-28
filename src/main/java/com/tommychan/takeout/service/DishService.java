package com.tommychan.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tommychan.takeout.dto.DishDto;
import com.tommychan.takeout.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    void saveDishWithFlavor(DishDto dishDto);

    void updateDishWithFlavor(DishDto dishDto);

    void updateStatus(int statusCode, List<Long> ids);
}
