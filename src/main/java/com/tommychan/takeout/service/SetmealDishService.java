package com.tommychan.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tommychan.takeout.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {
    List<SetmealDish> getBySetmealId(Long id);
}
