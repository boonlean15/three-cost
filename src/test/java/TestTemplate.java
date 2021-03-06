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
            log.info("θΏεη»ζ-----" + forObject);
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
        String fileName = "H:\\ζΆ¦ε»Ί\\δΈθ΄Ή\\δ»εΊε·²ζηθ‘¨η»ζ\\δ»εΊε·²ζηθ‘¨η»ζ\\δΈθ΄ΉεΊθ‘¨η»ζοΌη©θη½οΌ.xlsx";
        // θΏι ιθ¦ζε?θ―»η¨εͺδΈͺclassε»θ―»οΌηΆεθ―»εη¬¬δΈδΈͺsheet ζδ»Άζ΅δΌθͺε¨ε³ι­
        // θΏιζ―ζ¬‘δΌθ―»ε3000ζ‘ζ°ζ? ηΆεθΏεθΏζ₯ η΄ζ₯θ°η¨δ½Ώη¨ζ°ζ?ε°±θ‘
        //θΎεΊθ?Ύθ?‘ε€§θ‘¨ εpmisθ‘¨
        EasyExcel.read(fileName, CostOtherData.class, new CostOtherDataListener()).sheet("εζΆεεΉΆεεζ Ό").doRead();
    }


    @Test
    public void testShortUUid(){
        String sys = UUIDUtils.getShortUUID("sys");
        log.info("sys -------------- " + sys);
        double x = 45.0;
//ε°θ§εΊ¦θ½¬ζ’ζεΌ§εΊ¦εΌ
        x = Math.toRadians(x);
        log.info("45εΊ¦ε―ΉεΊηεΌ§εΊ¦--- " + x);
        System.out.println(Math.cos(x));//0.7071067811865476
        double acos = Math.acos(0.7071067811865476);
        log.info("acos ---- " + acos);
        double degrees = Math.toDegrees(acos);
        log.info("degrees ---- " + degrees);
    }

    @Test
    public void generateOneData(){
        String fileName = "H:\\ζΆ¦ε»Ί\\δΈθ΄Ή\\δ»εΊε·²ζηθ‘¨η»ζ\\δ»εΊε·²ζηθ‘¨η»ζ\\δΈθ΄ΉεΊθ‘¨η»ζοΌη©θη½οΌεθ‘¨.xlsx";
        String tableName = "twr_towerbillbalance";
        EasyExcel.read(fileName, CostOtherData.class, new CostOneDataListener(tableName)).sheet("sheet1").doRead();
    }

    @Test
    public void generateSql() throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File("H:\\ζΆ¦ε»Ί\\εζ°ι‘Ήη?\\εζ°ι‘Ήη?δΊ€ζ₯\\ζ°ε’ε­ζ?΅.txt")));
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
