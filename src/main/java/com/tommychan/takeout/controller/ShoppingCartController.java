package com.tommychan.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tommychan.takeout.common.BaseContext;
import com.tommychan.takeout.common.R;
import com.tommychan.takeout.entity.ShoppingCart;
import com.tommychan.takeout.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartService.list(wrapper);
        return R.success(list);
    }

    @PostMapping("/add")
    public R<String> add(@RequestBody ShoppingCart shoppingCart) {

        log.info("shoppingCart:{}", shoppingCart);
        Long userId = BaseContext.getCurrentId();

        //设置用户 id
        shoppingCart.setUserId(userId);

        //查询当前添加的是否已存在购物车中
        LambdaQueryWrapper<ShoppingCart> shoppingWrapper = new LambdaQueryWrapper<>();
        shoppingWrapper.eq(ShoppingCart::getUserId, userId);
        shoppingWrapper.eq(ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());

        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        //判断是菜品还是套餐
        if (dishId != null) {
            shoppingWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            shoppingWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }

        //如果已经存在相同的商品 则数目加一
        ShoppingCart cart = shoppingCartService.getOne(shoppingWrapper);
        if (cart != null) {
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartService.updateById(cart);
        } else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
        }
        return R.success("添加成功");
    }

    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart) {

        log.info("shoppingCart:{}", shoppingCart);
        Long userId = BaseContext.getCurrentId();

        //设置用户 id
        shoppingCart.setUserId(userId);

        //查询
        LambdaQueryWrapper<ShoppingCart> shoppingWrapper = new LambdaQueryWrapper<>();
        shoppingWrapper.eq(ShoppingCart::getUserId, userId);
        shoppingWrapper.eq(ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());

        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        //判断是菜品还是套餐
        if (dishId != null) {
            shoppingWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            shoppingWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }


        ShoppingCart cart = shoppingCartService.getOne(shoppingWrapper);
        if (cart.getNumber() > 1) {
            cart.setNumber(cart.getNumber() - 1);
            shoppingCartService.updateById(cart);
        } else {
            shoppingCartService.removeById(cart);
        }
        return R.success("删除成功");
    }
}
