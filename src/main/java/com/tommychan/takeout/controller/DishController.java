package com.tommychan.takeout.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tommychan.takeout.common.R;
import com.tommychan.takeout.dto.DishDto;
import com.tommychan.takeout.entity.Category;
import com.tommychan.takeout.entity.Dish;
import com.tommychan.takeout.entity.DishFlavor;
import com.tommychan.takeout.service.CategoryService;
import com.tommychan.takeout.service.DishFlavourService;
import com.tommychan.takeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavourService dishFlavourService;

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("DishDto:{}", dishDto);
        dishService.saveDishWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }


    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        Page<Dish> pageInfo = new Page(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        //设置records
        List<Dish> dishList = pageInfo.getRecords();

        List<DishDto> dishDtoList = dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            String categoryName = categoryService.getById(categoryId).getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getDishById(@PathVariable Long id) {


        DishDto dishDto = new DishDto();
        Dish dish = dishService.getById(id);

        //对象拷贝
        BeanUtils.copyProperties(dish, dishDto);

        //根据 dish 的 id 拿到 category
        Category category = categoryService.getById(dish.getCategoryId());

        //根据 dish 的 id 拿到 flavour
        List<DishFlavor> dishFlavors = dishFlavourService.getByDishId(dish.getId());


        dishDto.setFlavors(dishFlavors);
        dishDto.setCategoryName(category.getName());

        return R.success(dishDto);
    }

    @Transactional
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        //更新
        dishService.updateDishWithFlavor(dishDto);
        return R.success("更新成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(@RequestParam("categoryId") Long id) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getCategoryId, id);
        List<Dish> dishes = dishService.list(wrapper);
        List<DishDto> dishDtoList = dishes.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            //设置flavour
            LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flavorLambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> flavorList = dishFlavourService.list(flavorLambdaQueryWrapper);
            dishDto.setFlavors(flavorList);

            //设置 category
            Category category = categoryService.getById(item.getCategoryId());
            dishDto.setCategoryName(category.getName());

            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }
}