import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rjgf.threecost.crawdata.entity.vo.BaiduReCoordinate;
import com.rjgf.threecost.crawdata.entity.vo.CostOtherData;
import com.rjgf.threecost.crawdata.listener.CostOneDataListener;
import com.rjgf.threecost.crawdata.listener.CostOtherDataListener;
import com.rjgf.threecost.crawdata.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author linch
 * @create 2022/2/28 17:38
 */
@RunWith(SpringRunner.class)
@Slf4j
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
        String fileName = "H:\\润建\\三费\\仓库已有的表结构\\仓库已有的表结构\\三费库表结构（物联网）.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        // 这里每次会读取3000条数据 然后返回过来 直接调用使用数据就行
        //输出设计大表 和pmis表
        EasyExcel.read(fileName, CostOtherData.class, new CostOtherDataListener()).sheet("取消合并单元格").doRead();
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


}
