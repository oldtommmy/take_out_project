package com.tommychan.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tommychan.takeout.common.BaseContext;
import com.tommychan.takeout.common.R;
import com.tommychan.takeout.entity.Category;
import com.tommychan.takeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){

        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByAsc(Category::getSort);
        service.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Category category){
        log.info("save category:{}", category);
        service.save(category);
        return R.success("添加成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("update category:{}", category);
        log.info("current operator:{}",BaseContext.getCurrentId());
        service.updateById(category);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("id:{}",ids);
        service.remove(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        List<Category> list = service.list(lambdaQueryWrapper);
        return R.success(list);
    }

}
