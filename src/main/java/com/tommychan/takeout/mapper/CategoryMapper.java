package com.tommychan.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tommychan.takeout.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
