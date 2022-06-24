package com.rjgf.threecost.crawdata.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * @author linch
 * @create 2022/2/28 14:31
 */
@Data
@TableName(value = "tc_craw_data")
public class CrawDataResultExcel extends Model<CrawDataResultExcel> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ExcelProperty("序号")
    private int num;

    @ExcelProperty("省份")
    private String province;

    @ExcelProperty("城市")
    private String city;

    @ExcelProperty("区县")
    private String county;

    @ExcelProperty("区域")
    private String area;

    @ExcelProperty("小区")
    private String plot;

    @ExcelProperty("经度")
    private String longitude;

    @ExcelProperty("纬度")
    private String latitude;

    @ExcelProperty("每平米平均价格")
    private String aver_price;

    @ExcelProperty("平均租金")
    private String aver_rent;


}
