package com.tommychan.takeout.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tommychan.takeout.common.R;
import com.tommychan.takeout.dto.SetmealDto;
import com.tommychan.takeout.entity.Category;
import com.tommychan.takeout.entity.Setmeal;
import com.tommychan.takeout.service.CategoryService;
import com.tommychan.takeout.service.SetmealDishService;
import com.tommychan.takeout.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        Page<SetmealDto> pageInfoResult = new Page(page, pageSize);
        Page<Setmeal> pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name != null, Setmeal::getName, name);
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, wrapper);

        BeanUtils.copyProperties(pageInfo, pageInfoResult);

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> resultRecords = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            setmealDto.setCategoryName(category.getName());

            setmealDto.setSetmealDishes(setmealDishService.getBySetmealId(item.getId()));
            return setmealDto;
        }).collect(Collectors.toList());

        pageInfoResult.setRecords(resultRecords);
        return R.success(pageInfoResult);
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(@RequestParam("categoryId") Long id, @RequestParam("status") Integer status) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        log.info("id:{}, status:{}", id, status);
        wrapper.eq(Setmeal::getCategoryId, id);
        wrapper.eq(Setmeal::getStatus, status);
        List<Setmeal> list = setmealService.list(wrapper);
        return R.success(list);
    }


    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }


}
