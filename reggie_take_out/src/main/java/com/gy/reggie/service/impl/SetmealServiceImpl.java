package com.gy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gy.reggie.Dto.SetmealDto;
import com.gy.reggie.common.CustomException;
import com.gy.reggie.entity.Setmeal;
import com.gy.reggie.entity.SetmealDish;
import com.gy.reggie.mapper.SetmealMapper;
import com.gy.reggie.service.SetmealDishService;
import com.gy.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    private SetmealService setmealService;

    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
//       保存菜单的基本信息，操作的是Setmeal表
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Transactional
    public void removewithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids).eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if(count>0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(LambdaQueryWrapper);
    }

}
