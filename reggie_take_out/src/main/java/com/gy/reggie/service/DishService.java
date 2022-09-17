package com.gy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gy.reggie.Dto.DishDto;
import com.gy.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    public void savewithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(long id);
}
