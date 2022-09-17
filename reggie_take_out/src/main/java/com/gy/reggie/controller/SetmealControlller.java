package com.gy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gy.reggie.Dto.SetmealDto;
import com.gy.reggie.common.R;
import com.gy.reggie.entity.Category;
import com.gy.reggie.entity.Setmeal;
import com.gy.reggie.service.CategoryService;
import com.gy.reggie.service.SetmealDishService;
import com.gy.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//套餐管理
@RequestMapping("/setmeal")
@RestController
@Slf4j
public class SetmealControlller {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page>  page(String name, int page, int pageSize){
        log.info("name"+name + "\npage" + page + "\npageSize" + pageSize);

        /**
         * Description:构建分页构造器Page对象
         * @author: guo
         * date: 2022/9/7 19:56
         * @since JDK 1.8
         */
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> DtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name != null,Setmeal::getName,name)
                .orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,setmealLambdaQueryWrapper);

        BeanUtils.copyProperties(pageInfo,DtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String categoryname = category.getName();
                setmealDto.setCategoryName(categoryname);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        DtoPage.setRecords(list);
        return R.success(DtoPage);
    }
    @GetMapping("/{ids}")
    public R<SetmealDto> list(@PathVariable long ids){
        return null;
    }
    @DeleteMapping()
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:"+ids);
        setmealService.removewithDish(ids);
        return R.success("删除成功");
    }
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,setmeal.getStatus());
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);
        return R.success(list);

    }

}
