package com.rjgf.threecost.crawdata.util;

import com.rjgf.threecost.crawdata.entity.vo.GetX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author linch
 * @create 2022/2/24 17:47
 */
public class CoordinateConvertUtils {
    static double x_PI = 3.14159265358979324 * 3000.0 / 180.0;
    static double PI = 3.1415926535897932384626;
    static double a = 6378245.0;
    static double ee = 0.00669342162296594323;
    static double R = 6378137; // 赤道半径 6378137
    static double R1 = 6356752;

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     * @param bd_lon
     * @param bd_lat
     * @returns {*[]}
     */
    public String  bd09togcj02(double bd_lon, double bd_lat){
        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_PI);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        // Point point=new Point(gg_lng, gg_lat);
        // return point;
        return gg_lng+","+gg_lat;
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * 即谷歌、高德 转 百度
     * @param lng
     * @param lat
     * @returns {*[]}
     */
    public static String gcj02tobd09(double lng, double lat){
        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_PI);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_PI);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        //Point point=new Point(bd_lng, bd_lat);
        // return point;
        return bd_lng+","+bd_lat;
    };

    /**
     * WGS84转GCj02
     * @param lng
     * @param lat
     * @returns {*[]}
     */
    public static String wgs84togcj02(double lng, double lat){
        double dlat = transformlat(lng - 105.0, lat - 35.0);
        double dlng = transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        //Point point=new Point(mglng, mglat);
        // return point;
        return mglng+","+mglat;
    };

    /**
     * GCJ02 转换为 WGS84
     * @param lng
     * @param lat
     * @returns {*[]}
     */
    public String gcj02towgs84(double lng, double lat){
        double dlat = transformlat(lng - 105.0, lat - 35.0);
        double dlng = transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
        double mglat = lat + dlat;
        double mglng = lng + dlng;
        // Point point=new Point(mglng, mglat);
        // return point;
        return mglat+","+mglng;
    };

    /**
     * WGS84 转换为 BD-09
     * @param lng
     * @param lat
     * @returns {*[]}
     *
     */
    public static String  wgs84tobd09(double lng, double lat){
        //第一次转换
        double dlat = transformlat(lng - 105.0, lat - 35.0);
        double dlng = transformlng(lng - 105.0, lat - 35.0);
        double radlat = lat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
        double mglat = lat + dlat;
        double mglng = lng + dlng;

        //第二次转换
        double z = Math.sqrt(mglng * mglng + mglat * mglat) + 0.00002 * Math.sin(mglat * x_PI);
        double theta = Math.atan2(mglat, mglng) + 0.000003 * Math.cos(mglng * x_PI);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return bd_lng+","+bd_lat;
    }

    /**
     * 84转web墨卡托
     * @param boundaries
     * @return
     */
    public static List<GetX> wgs84ToWebMercator(String boundaries){
        List<GetX> result = new ArrayList<>();
        String[] split = boundaries.split(";");
        List<String> strings = Arrays.asList(split);
        strings.stream().forEach(item -> {
            String[] split1 = item.split(",");
            GetX getX = wgs84ToWebMercator(Double.parseDouble(split1[0]), Double.parseDouble(split1[1]));
            result.add(getX);
        });
        return result;
    }

    private static GetX wgs84ToWebMercator(double lon,double lat){
        double y = R * lon * Math.cos(lat/180 *Math.PI)/180 *Math.PI;
        double x = R * lat/180 * Math.PI;
        GetX xy = new GetX(y,x);
        return xy ;
    }

    /**
     * 多个坐标的wgs84坐标转osmbuildings.js的web墨卡托的xyz
     * @param boundaries
     * @return
     */
    public static List<GetX> wgs84ToOsmXy(String boundaries){
        List<GetX> result = new ArrayList<>();
        String[] split = boundaries.split(";");
        List<String> strings = Arrays.asList(split);
        strings.stream().forEach(item -> {
            String[] split1 = item.split(",");
            GetX getX = wgs84ToOsmXy(Double.parseDouble(split1[0]), Double.parseDouble(split1[1]),15);
            result.add(getX);
        });
        return result;
    }

    /**
     * wgs84坐标转osmbuildings.js的web墨卡托的xyz
     * @param lon
     * @param lat
     * @param t
     * @return
     */
    public static GetX wgs84ToOsmXy(double lon, double lat, int t){
        double x = (lon / 360 + .5) * (1 << t);
        double y = (1 - Math.log(Math.tan(lat * Math.PI / 180) + 1 / Math.cos(lat * Math.PI / 180)) / Math.PI) / 2 * (1 << t);
        GetX xy = new GetX(y,x);
        System.out.println(x+ "," + y);
        return xy ;
    }

    private static double transformlat(double lng,double lat){
        double ret= -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformlng(double lng,double lat){
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    public static void main(String[] args) {
        // 两次谷歌转换为百度坐标
        // 第一次  WGS84转GCj02
        String lnglat=wgs84togcj02(117.20296517261839,31.841652709281103);
        double lng=Double.parseDouble(lnglat.split(",")[0]);
        double lat=Double.parseDouble(lnglat.split(",")[1]);
        System.out.println("第一次转换的结果:"+lng+","+lat);
        // 第二次 gcj02tobd09
        System.out.println("第二次转换的结果:"+gcj02tobd09(lng,lat));

        // 谷歌转百度一次转换
        System.out.println("谷歌转换为百度一次转换的结果:"+wgs84tobd09(117.20296517261839,31.841652709281103));
    }


    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     * @param lng1 经度1
     * @param lat1 纬度1
     * @param lng2 经度2
     * @param lat2 纬度2
     * @return
     */
    public static double getDistance(double lng1, double lat1, double lng2,
                                     double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s*1000;
        return s;
    }

}
