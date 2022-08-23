package com.rjgf.threecost.crawdata.rpc.server;

import com.rjgf.threecost.crawdata.rpc.CalculateService;

public class CalculateServiceImpl implements CalculateService {
    @Override
    public String add(int a, int b) {
        int result = a + b;
        return "结果是：" + result;
    }
}
