package com.example.impl;

import com.example.service.DrinkService;
import com.wentry.wrpc.annotation.WRpcService;

/**
 * @Description:
 * @Author: tangwc
 */
@WRpcService
public class DrinkServiceImpl implements DrinkService {
    @Override
    public String drink() {
        return " Drink Coffee... ";
    }
}
