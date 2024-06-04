package com.atguigu.cloud.service;

import com.atguigu.cloud.entities.Pay;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PayService {
    int add(Pay pay);
    int delete(Integer id);
    int update(Pay pay);

    Pay getById(Integer id);

    List<Pay> getAll();
}
