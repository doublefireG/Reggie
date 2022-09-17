package com.gy.reggie.Dto;

import com.gy.reggie.entity.Setmeal;
import com.gy.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
