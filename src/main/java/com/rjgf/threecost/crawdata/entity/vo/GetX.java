package com.rjgf.threecost.crawdata.entity.vo;

import lombok.Data;

/**
 * @author linch
 * @create 2022/2/24 17:49
 */
@Data
public class GetX {
    double yc;
    double xc;
    public GetX(double lon, double lat){
        this.yc = lon;
        this.xc = lat;
    }
}
