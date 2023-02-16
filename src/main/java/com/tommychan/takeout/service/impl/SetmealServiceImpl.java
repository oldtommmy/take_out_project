package com.tommychan.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tommychan.takeout.common.CustomException;
import com.tommychan.takeout.dto.SetmealDto;
import com.tommychan.takeout.entity.Setmeal;
import com.tommychan.takeout.entity.SetmealDish;
import com.tommychan.takeout.mapper.SetmealMapper;
import com.tommychan.takeout.service.SetmealDishService;
import com.tommychan.takeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {

        //保存 setmeal
        this.save(setmealDto);

        Long id = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(id);
            return item;
        }).collect(Collectors.toList());

        //保存 dishes
        setmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    @Override
    public void deleteWithDish(List<Long> ids) {

        //查询套餐是否已禁售
        // select count(*) from setmeal where id in ids and status=1
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId, ids);
        wrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(wrapper);

        if (count > 0) {
            throw new CustomException("存在在售套餐，不可删除, 若想删除请先停售该商品");
        }
        //删除 dish
        LambdaQueryWrapper<SetmealDish> dishWrapper = new LambdaQueryWrapper<>();
        dishWrapper.eq(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(dishWrapper);

        //删除 setmeal
        this.removeByIds(ids);

    }
}
