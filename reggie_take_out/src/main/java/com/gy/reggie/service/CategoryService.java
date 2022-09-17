package com.gy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gy.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
        void removeById(long ids);
}


