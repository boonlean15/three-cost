package com.rjgf.threecost.crawdata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjgf.threecost.crawdata.entity.SbData;
import com.rjgf.threecost.crawdata.mapper.SbDataMapper;
import com.rjgf.threecost.crawdata.service.SbDataService;
import org.springframework.stereotype.Service;

@Service
public class SbDataServiceImpl extends ServiceImpl<SbDataMapper, SbData> implements SbDataService {
}
