package com.rjgf.threecost.crawdata.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rjgf.threecost.crawdata.entity.vo.BaiduReCoordinate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 百度API请求转换获取经纬度的城市信息
 * @author linch
 * @create 2022/2/28 15:29
 */
@Component
@Slf4j
public class BaiduCoordinateConvertUtils {

    @Resource
    private RestOperations restOperations;

    private static final String RE_COORDINATE = "https://api.map.baidu.com/reverse_geocoding/v3/?ak={ak}&coordtype={coordtype}&location={location}&output={output}";

    public BaiduReCoordinate getReCoordinate(String coordType, double longitude, double latitude){
        Map<String, Object> uriVariables = new HashMap<>(4);
        uriVariables.put("coordtype", coordType);
        uriVariables.put("location", latitude+","+longitude);
        uriVariables.put("ak","yQGltnVGt7h3BDGGROmoZ4nR7HGTq7ri");
        uriVariables.put("output", "json");
        String forObject = null;
        try{
            forObject = restOperations.getForObject(RE_COORDINATE, String.class, uriVariables);
            if(forObject.startsWith("{") && forObject.endsWith("}")){
                log.info("返回结果-----" + forObject);
                JSONObject jsonObject = JSON.parseObject(forObject);
                if((int)jsonObject.get("status") == 0){
                    Object result = jsonObject.get("result");
                    JSONObject jsonObject1 = JSON.parseObject(JSON.toJSONString(result));
                    Object addressComponent = jsonObject1.get("addressComponent");
                    BaiduReCoordinate baiduReCoordinate = JSON.parseObject(JSON.toJSONString(addressComponent), BaiduReCoordinate.class);
                    return baiduReCoordinate;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
