package com.rjgf.threecost.crawdata.entity.vo;

import lombok.Data;

/**
 * @author linch
 * @create 2022/2/28 16:20
 */
@Data
public class BaiduReCoordinate {

    //国家
    private String country;
    //国家编码
    private int country_code;
    //国家英文缩写（三位）
    private String country_code_iso;
    //国家英文缩写（两位）
    private String country_code_iso2;
    //省名
    private String province;
    //城市名
    private String city;
    //城市所在级别（仅国外有参考意义。国外行政区划与中国有差异，城市对应的层级不一定为『city』。country、province、city、district、town分别对应0-4级，若city_level=3，则district层级为该国家的city层级）
    private int city_level;
    //区县名
    private String district;
    //乡镇名，需设置extensions_town=true时才会返回
    private String town;
    //乡镇id
    private String town_code;
    //街道名（行政区划中的街道层级）
    private String street;
    //街道门牌号
    private String street_number;
    //行政区划代码
    private String adcode;
    //相对当前坐标点的方向，当有门牌号的时候返回数据
    private String direction;
    //相对当前坐标点的距离，当有门牌号的时候返回数据
    private String distance;

}
