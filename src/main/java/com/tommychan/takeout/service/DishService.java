package com.tommychan.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tommychan.takeout.dto.DishDto;
import com.tommychan.takeout.entity.Dish;

public interface DishService extends IService<Dish> {

    void saveDishWithFlavor(DishDto dishDto);

    void updateDishWithFlavor(DishDto dishDto);
}
