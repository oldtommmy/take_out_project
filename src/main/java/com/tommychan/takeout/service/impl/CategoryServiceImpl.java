package com.tommychan.takeout.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tommychan.takeout.common.CustomException;
import com.tommychan.takeout.entity.Category;
import com.tommychan.takeout.entity.Dish;
import com.tommychan.takeout.entity.Setmeal;
import com.tommychan.takeout.mapper.CategoryMapper;
import com.tommychan.takeout.service.CategoryService;
import com.tommychan.takeout.service.DishService;
import com.tommychan.takeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据 id 删除分类 删除前需检查是否关联
     * @param id id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(dishLambdaQueryWrapper);
        if (dishCount > 0) {
            //抛异常
            throw new CustomException("当前分类关联了菜品，无法删除");
        }

        //是否关联套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int setMealCount = setmealService.count(setmealLambdaQueryWrapper);
        if (setMealCount > 0) {
            //抛异常
            throw new CustomException("当前分类关联了套餐，无法删除");
        }
        super.removeById(id);
    }
}
