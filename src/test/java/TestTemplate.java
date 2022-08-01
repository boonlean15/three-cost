import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rjgf.threecost.ThreeCostApplication;
import com.rjgf.threecost.crawdata.dao.SbDataDao;
import com.rjgf.threecost.crawdata.entity.vo.BaiduReCoordinate;
import com.rjgf.threecost.crawdata.entity.vo.CostOtherData;
import com.rjgf.threecost.crawdata.listener.CostOneDataListener;
import com.rjgf.threecost.crawdata.listener.CostOtherDataListener;
import com.rjgf.threecost.crawdata.mapper.SbDataMapper;
import com.rjgf.threecost.crawdata.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

/**
 * @author linch
 * @create 2022/2/28 17:38
 */
@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest(classes = ThreeCostApplication.class)
public class TestTemplate {

    private static final String RE_COORDINATE = "https://api.map.baidu.com/reverse_geocoding/v3/?ak={ak}&coordtype={coordtype}&location={location}&output={output}";


    @Test
    public void test(){
        Map<String, Object> uriVariables = new HashMap<>(4);
        uriVariables.put("coordtype", "wgs84ll");
        uriVariables.put("location", 31.225696563611 + "," + 121.49884033194);
        uriVariables.put("ak","yQGltnVGt7h3BDGGROmoZ4nR7HGTq7ri");
        uriVariables.put("output", "json");
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(RE_COORDINATE, String.class, uriVariables);
        log.info(forObject);
        if(forObject.startsWith("{") && forObject.endsWith("}")){
            log.info("返回结果-----" + forObject);
            JSONObject jsonObject = JSON.parseObject(forObject);
            if((int)jsonObject.get("status") == 0){
                Object result = jsonObject.get("result");
                JSONObject jsonObject1 = JSON.parseObject(JSON.toJSONString(result));
                Object addressComponent = jsonObject1.get("addressComponent");
                String s = JSON.toJSONString(addressComponent);
                log.info("----------"+s);
                BaiduReCoordinate baiduReCoordinate = JSON.parseObject(s, BaiduReCoordinate.class);
                log.info("bai---------" + baiduReCoordinate);

            }
        }
    }

    @Test
    public void testData(){
        String fileName = "D:\\润建\\节电\\new demand\\数据字典.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取3000条数据 然后返回过来 直接调用使用数据就行
        //输出设计大表 和pmis表
        EasyExcel.read(fileName, CostOtherData.class, new CostOtherDataListener()).sheet("Sheet1").doRead();
    }


    @Test
    public void testShortUUid(){
        String sys = UUIDUtils.getShortUUID("sys");
        log.info("sys -------------- " + sys);
        double x = 45.0;
//将角度转换成弧度值
        x = Math.toRadians(x);
        log.info("45度对应的弧度--- " + x);
        System.out.println(Math.cos(x));//0.7071067811865476
        double acos = Math.acos(0.7071067811865476);
        log.info("acos ---- " + acos);
        double degrees = Math.toDegrees(acos);
        log.info("degrees ---- " + degrees);
    }

    @Test
    public void generateOneData(){
        String fileName = "H:\\润建\\三费\\仓库已有的表结构\\仓库已有的表结构\\三费库表结构（物联网）单表.xlsx";
        String tableName = "twr_towerbillbalance";
        EasyExcel.read(fileName, CostOtherData.class, new CostOneDataListener(tableName)).sheet("sheet1").doRead();
    }

    @Test
    public void generateSql() throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File("H:\\润建\\参数项目\\参数项目交接\\新增字段.txt")));
        String sql1 = "ALTER TABLE \"pacs\".\"nr_hua_nrducellrsvdext02\" ADD COLUMN";
        for(int i = 0; i < 200; i++){
            String rsvdParam = sb.append(sql1).append(" \"").append("rsvdparam").append(i + "").append("\" ").append("varchar(255);").append("\n").toString();
            printWriter.write(rsvdParam);
            printWriter.flush();
            sb.delete(0, sb.length());
        }

    }

    @Test
    public void testSplit(){
        String moc = "tst123";
        moc = Optional.ofNullable(moc).map(v -> v.contains("-") ? v.split("-")[0] : v).orElse(moc);

        log.info("first----" + moc);

    }

    @Test
    public void strReplace(){
        String warnInfo = "\tDN: PLMN-PLMN/MRBTS-826802\tType: Error\t5G/LTE/SRAN plan provision failed.\tDetails: java.net.ConnectException: Connection timed out";
        String s = warnInfo.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "");
        log.info(s);
    }


    @Test
    public void testStream(){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        List<String> collect = list.stream().filter(item -> "c".equals(item)).collect(Collectors.toList());
        log.info("list ----- " + Arrays.toString(list.toArray()));
        log.info("collect ----- " + Arrays.toString(collect.toArray()));
    }

    @Test
    public void  getCenterPoint() {
        String str = "118.822488,30.974598;118.837723,30.974041;118.819326,30.968777;118.830824,30.967352";
        String[] arr = str.split(";");
        int total = arr.length;
        double X = 0, Y = 0, Z = 0;
        for(int i=0; i<arr.length; i++){
            double lat, lon, x, y, z;
            //将Lat1和Lon1从角度转换为弧度
            lon = Double.parseDouble(arr[i].split(",")[0]) * Math.PI / 180;
            lat = Double.parseDouble(arr[i].split(",")[1]) * Math.PI / 180;
            //将纬度/经度转换为第一个位置的笛卡尔坐标。
            x = Math.cos(lat) * Math.cos(lon);
            y = Math.cos(lat) * Math.sin(lon);
            z = Math.sin(lat);
            //计算所有位置的综合总权重
            X += x;
            Y += y;
            Z += z;
        }
        //计算x、y、z坐标的加权平均值
        X = X / total;
        Y = Y / total;
        Z = Z / total;
        //将平均x, y, z坐标转换为纬度和经度。注意，在Excel和其他一些应用程序中，参数需要在atan2函数中颠倒，例如，使用atan2(X,Y)而不是atan2(Y,X)。
        double Lon = Math.atan2(Y, X);
        double Hyp = Math.sqrt(X * X + Y * Y);
        double Lat = Math.atan2(Z, Hyp);
        //将纬度和经度转换为度。
        Map<String,Double> map = new HashMap<String,Double>();
        map.put("lon", Lon * 180 / Math.PI);
        map.put("lat", Lat * 180 / Math.PI);
        log.info("中心坐标-----------" + map);
    }


    @Resource
    SbDataMapper sbDataMapper;

    @Test
    public void testReplace() throws FileNotFoundException, UnsupportedEncodingException {
        String abc = "a-b-c-d";
        String replace = abc.replace("-", "");
        log.info("abc ------------- " + abc);
        log.info("replace -----------"+ replace);

        PrintWriter printWriter = new PrintWriter("D:\\test\\test.txt", "UTF-8");
        printWriter.println("测试输出");
        printWriter.close();

        //4166911607
        String username = "username";
        CRC32 crc32 = new CRC32();
        crc32.update(username.getBytes(StandardCharsets.UTF_8));
        log.info("username ---------String.valueOf(crc32.getValue()) ----------  " + String.valueOf(crc32.getValue()));

        log.info("username ========= " + username);
        log.info("username.getBytes ------------- " + username.getBytes(StandardCharsets.UTF_8));
        log.info("crc32.update(username.getBytes(StandardCharsets.UTF_8)); ------------ " + crc32.getValue());

        Map<String, String> sbData = new HashMap<>(4);
        sbData.put("5","v1");
        sbData.put("6","v2");
        sbDataMapper.insertMapData(sbData);


    }


//    @Autowired
//    DataSourceProperties dataSourceProperties;
//
//    @Autowired
//    protected ApplicationContext applicationContext;
////
////    @Before
////    public void init(){
////    }
//

    @Resource
    JdbcTemplate jdbcTemplate;

    @Test
    public void query() {
        // 获取配置的数据源
//        DataSource dataSource = applicationContext.getBean(DataSource.class);
//        // 查看配置数据源信息
//        System.out.println(dataSource);
//        System.out.println(dataSource.getClass().getName());
//        System.out.println(dataSourceProperties);
        //执行SQL,输出查到的数据
        String queryForObject = jdbcTemplate.queryForObject("select area from tc_craw_data where province = (select name from sb_data where id = ?)", new Object[]{"3"},  String.class);
        log.info("queryForObject -------- " + queryForObject);
    }





    @Test
    public void testIfOrNot(){
        String one = "one";
        String two = "two";
        String three = "three";

        String type1 = "类型1";
        String type2 = "类型2";

        String myType = "类型1";
        String myNumber = "two";
        String hisNumber = "nan";

        if("类型1".equals(myType) && (myNumber.equals(two) && hisNumber.equals(one))){
            log.info("与判断加或判断");
        }
        URLConnection connection;
        connection.connect();

    }


}
