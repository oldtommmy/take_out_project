package com.tommychan.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tommychan.takeout.entity.DishFlavor;

import java.util.List;

public interface DishFlavourService extends IService<DishFlavor> {

    List<DishFlavor> getByDishId(Long id);
}
