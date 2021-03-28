package com.pain.yellow.api.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface ApiMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
