package com.gy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gy.reggie.Dto.SetmealDto;
import com.gy.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
    public void removewithDish(List<Long> ids);
}
