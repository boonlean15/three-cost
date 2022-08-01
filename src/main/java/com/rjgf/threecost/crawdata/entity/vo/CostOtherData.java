package com.rjgf.threecost.crawdata.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author linch
 * @create 2022/3/9 14:11
 */
@Data
public class CostOtherData {

    @ExcelProperty("表名")
    private String tableName;

    @ExcelProperty("英文表名")
    private String enTableName;

    @ExcelProperty("列名")
    private String propName;

    @ExcelProperty("注释")
    private String content;

    @ExcelProperty("类型及长度")
    private String prop;

}
