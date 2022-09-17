package com.gy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gy.reggie.common.R;
import com.gy.reggie.entity.Category;
import com.gy.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category){
        if(category == null)
            return R.error("未知错误");
        categoryService.save(category);
        return R.success("添加成功");
    }
    @GetMapping("/page")
    public R<Page> list(Integer page,Integer pageSize){
        Page<Category> pageinfo = new Page(page,pageSize);
        LambdaQueryWrapper<Category> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageinfo,LambdaQueryWrapper);

        return R.success(pageinfo);
    }
    @DeleteMapping
    public  R<String> delete(long ids){
        categoryService.removeById(ids);
        return R.success("删除成功");
    }
    @PutMapping
    public R<String> update(@RequestBody  Category category){
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        List<Category> list = categoryService.list(categoryLambdaQueryWrapper);
        return R.success(list);

    }


}
