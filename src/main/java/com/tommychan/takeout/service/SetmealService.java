package com.tommychan.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tommychan.takeout.dto.SetmealDto;
import com.tommychan.takeout.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void deleteWithDish(List<Long> ids);
}
