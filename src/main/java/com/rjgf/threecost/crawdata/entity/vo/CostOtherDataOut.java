package com.rjgf.threecost.crawdata.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author linch
 * @create 2022/3/9 15:07
 */
@Data
public class CostOtherDataOut {

    @ExcelProperty("表名")
    private String tableName;

    @ExcelProperty("建表语句")
    private String createContent;

}
