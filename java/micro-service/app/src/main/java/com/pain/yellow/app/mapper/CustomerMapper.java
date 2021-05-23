package com.pain.yellow.app.mapper;

import com.pain.yellow.app.domain.pojo.Customer;
import com.pain.yellow.app.util.mapper.ApiMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMapper extends ApiMapper<Customer> {
}