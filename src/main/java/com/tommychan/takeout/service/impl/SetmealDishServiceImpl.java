package com.tommychan.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tommychan.takeout.entity.SetmealDish;
import com.tommychan.takeout.mapper.SetmealDishMapper;
import com.tommychan.takeout.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public List<SetmealDish> getBySetmealId(Long id) {
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SetmealDish::getSetmealId, id);
        return setmealDishService.list(wrapper);
    }
}
