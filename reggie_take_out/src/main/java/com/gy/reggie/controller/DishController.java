package com.gy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gy.reggie.Dto.DishDto;
import com.gy.reggie.common.R;
import com.gy.reggie.entity.Category;
import com.gy.reggie.entity.Dish;
import com.gy.reggie.entity.DishFlavor;
import com.gy.reggie.service.CategoryService;
import com.gy.reggie.service.DishFlavorService;
import com.gy.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController  {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    public R<String> save(@RequestBody DishDto dishdto){
        log.info(dishdto.toString());
        dishService.savewithFlavor(dishdto);
        return R.success("恭喜添加成功");
    }
    @GetMapping("/page")
    public  R<Page> page(int page,int pageSize,String name){
        Page<Dish> pageinfo = new Page<>();
        Page<DishDto> Dtopage = new Page<>(page,pageSize);
//条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        dishService.page(pageinfo,queryWrapper);
        BeanUtils.copyProperties(pageinfo,Dtopage,"records");

        List<Dish> records = pageinfo.getRecords();


        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        Dtopage.setRecords(list);

        return R.success(Dtopage);

    }

    //根据id查询对应口味信息
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        DishDto dishdto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) ->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishDtoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishDtoLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> flavorListlist = dishFlavorService.list(dishDtoLambdaQueryWrapper);
            dishDto.setFlavors(flavorListlist);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);

    }
    @PostMapping("/status/{statu}")
    public R<String> stop(@PathVariable int statu,long ids){
        log.info(statu+"-------------------"+ids);
        Dish dish = dishService.getById(ids);
        dish.setStatus(statu);
        dishService.updateById(dish);
        return R.success("停售成功");

    }

}

