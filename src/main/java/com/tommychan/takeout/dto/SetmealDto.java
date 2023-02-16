package com.tommychan.takeout.dto;


import com.tommychan.takeout.entity.Setmeal;
import com.tommychan.takeout.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
