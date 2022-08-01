package com.rjgf.threecost.crawdata.listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.StringUtils;
import com.rjgf.threecost.crawdata.entity.vo.CostOtherData;
import com.rjgf.threecost.crawdata.entity.vo.CostOtherDataOut;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linch
 * @create 2022/3/9 14:19
 */
@Slf4j
public class CostOtherDataListener extends AnalysisEventListener<CostOtherData> {

    /**
     * 输出临时文件夹
     */
    private final String OUT_PATH = "D:\\润建\\节电\\new demand\\";

    private String excelFileName = OUT_PATH + "节电建表语句.sql";

    private List<CostOtherData> dataList = new ArrayList<>();

    private String tableName;

    private List<CostOtherDataOut> costOtherDataOuts = new ArrayList<>();

    @Override
    public void invoke(CostOtherData costOtherData, AnalysisContext analysisContext) {
        String dataTableName = costOtherData.getEnTableName();
        if(StringUtils.isBlank(this.tableName)){
            this.tableName = dataTableName;
        }
        if(tableName.equals(dataTableName)){
            dataList.add(costOtherData);
        }else {
            //生成dataList字段集合的
            otherSaveData();
            tableName = dataTableName;
            dataList.clear();
            dataList.add(costOtherData);
        }
    }

    private void otherSaveData(){
        CostOtherDataOut costOtherDataOut = new CostOtherDataOut();
        costOtherDataOut.setTableName(tableName);
        StringBuilder sb = new StringBuilder();
        String id = dataList.get(0).getPropName();
        sb.append("-- ----------------------------\n" +
                "-- Table structure for ").append(tableName).append("\n" +
                "-- ----------------------------\n");
        sb.append("DROP TABLE IF EXISTS \"public\".\"").append(tableName).append("\";\n\n");
        sb.append("CREATE TABLE ").append("\"public\".").append("\""+tableName+"\"").append(" (\n");
        dataList.forEach(item -> {
            String propName = item.getPropName();
            String prop = item.getProp();
            sb.append("  \"").append(propName).append("\" ");
            sb.append(prop);
            sb.append(" COLLATE \"pg_catalog\".\"default\" NOT NULL,\n");
        });
        String sbStr = sb.substring(0,sb.length()-2);
        sb.delete(0,sb.length());
        sb.append(sbStr);
        sb.append("\n)").append(";\n\n");
        dataList.forEach(item -> {
            String content = item.getContent();
            String propName = item.getPropName();
            sb.append("COMMENT ON COLUMN \"public\".").append("\"").append(tableName).append("\".")
                    .append("\"").append(propName).append("\"").append(" IS ").append("'").append(content).append("';\n");
        });
        sb.append("ALTER TABLE \"public\".\"").append(tableName).append("\" ")
                .append("ADD CONSTRAINT \"pk_qj_complaint2\" PRIMARY KEY (\"").append(id).append("\");");
        String sql = sb.toString();
        String text = "DROP TABLE IF EXISTS \"public\".\"qj_complaint\";" +
                "CREATE TABLE \"public\".\"qj_complaint\" (\n" +
                "  \"id\" varchar(32) COLLATE \"pg_catalog\".\"default\" NOT NULL,\n" +
                "  \"order_no\" varchar(50) COLLATE \"pg_catalog\".\"default\",\n" +
                "  \"emos_order_no\" varchar(50) COLLATE \"pg_catalog\".\"default\",\n" +
                "  \"crm_order_no\" varchar(50) COLLATE \"pg_catalog\".\"default\",\n" +
                "  \"city\" varchar(15) COLLATE \"pg_catalog\".\"default\",\n" +
                "  \"county\" varchar(50) COLLATE \"pg_catalog\".\"default\",\n" +
                "  \"fault_area\" text COLLATE \"pg_catalog\".\"default\",\n" +
                "  \"order_subject\" text COLLATE \"pg_catalog\".\"default\",\n" +
                "  \"send_time\" varchar(32) COLLATE \"pg_catalog\".\"default\",\n" +
                "  \"poi_scene\" varchar(15) COLLATE \"pg_catalog\".\"default\",\n" +
                "  \"city_county\" varchar(15) COLLATE \"pg_catalog\".\"default\",\n" +
                "  \"problem_location_cgi\" text COLLATE \"pg_catalog\".\"default\",\n" +
                "  \"project_id\" varchar(32) COLLATE \"pg_catalog\".\"default\" NOT NULL\n" +
                ")\n" +
                ";\n" +
                "COMMENT ON COLUMN \"public\".\"qj_complaint\".\"id\" IS 'id';\n" +
                "COMMENT ON COLUMN \"public\".\"qj_complaint\".\"order_no\" IS '工单编号';\n" +
                "COMMENT ON COLUMN \"public\".\"qj_complaint\".\"emos_order_no\" IS 'emos工单编号';\n" +
                "COMMENT ON COLUMN \"public\".\"qj_complaint\".\"crm_order_no\" IS 'crm工单编号';\n" +
                "COMMENT ON COLUMN \"public\".\"qj_complaint\".\"city\" IS '城市';\n" +
                "COMMENT ON COLUMN \"public\".\"qj_complaint\".\"county\" IS '区县';\n" +
                "COMMENT ON COLUMN \"public\".\"qj_complaint\".\"fault_area\" IS '故障地点';\n" +
                "COMMENT ON COLUMN \"public\".\"qj_complaint\".\"order_subject\" IS '工单主题';\n" +
                "COMMENT ON COLUMN \"public\".\"qj_complaint\".\"send_time\" IS '派单时间';\n" +
                "COMMENT ON COLUMN \"public\".\"qj_complaint\".\"poi_scene\" IS 'poi场景';\n" +
                "COMMENT ON COLUMN \"public\".\"qj_complaint\".\"city_county\" IS '城市农村';\n" +
                "COMMENT ON COLUMN \"public\".\"qj_complaint\".\"problem_location_cgi\" IS '问题解决最终定位CGI';\n" +
                "COMMENT ON COLUMN \"public\".\"qj_complaint\".\"project_id\" IS '工程id';\n" +
                "COMMENT ON TABLE \"public\".\"qj_complaint\" IS '基础数据表-投诉通报-关键环节';" + "" +
                "ALTER TABLE \"public\".\"qj_complaint\" ADD CONSTRAINT \"pk_qj_complaint2\" PRIMARY KEY (\"id\");";
        costOtherDataOut.setCreateContent(sql);
        costOtherDataOuts.add(costOtherDataOut);
    }

    private void saveData() {
        CostOtherDataOut costOtherDataOut = new CostOtherDataOut();
        costOtherDataOut.setTableName(tableName);
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append("`"+tableName+"` ").append("(\n");
        sb.append("  `my_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n");
        dataList.forEach(item -> {
            String content = item.getContent();
            String propName = item.getPropName();
            sb.append("  `");
            if(StringUtils.isNotBlank(content)){
                sb.append(content);
            }else {
                sb.append(propName);
            }
            sb.append("` ");
            sb.append("varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ");
            sb.append("'");
            if(StringUtils.isNotBlank(content)){
                sb.append(content);
            }else {
                sb.append(propName);
            }
            sb.append("',\n");
        });
        sb.append("  PRIMARY KEY (`my_id`) USING BTREE\n");
        sb.append(") ENGINE = InnoDB AUTO_INCREMENT = 161 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '");
        sb.append(tableName);
        sb.append("' ROW_FORMAT = Compact;");
        String sql = sb.toString();
        String text = "CREATE TABLE `drawings_information_database`  (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
                "  `drawing_classes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图纸类别',\n" +
                "  `drawing_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图纸名称',\n" +
                "  `identify_state` int(32) NULL DEFAULT NULL COMMENT '识别状态',\n" +
                "  `upload_time` timestamp NULL DEFAULT NULL COMMENT '上传时间',\n" +
                "  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始识别时间',\n" +
                "  `end_time` timestamp NULL DEFAULT NULL COMMENT '识别完成时间',\n" +
                "  `uuid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'uuid',\n" +
                "  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传路径',\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE = InnoDB AUTO_INCREMENT = 161 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '图纸信息库' ROW_FORMAT = Compact;";
        costOtherDataOut.setCreateContent(sql);
        costOtherDataOuts.add(costOtherDataOut);
    }

    @SneakyThrows
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        otherSaveData();
//        EasyExcel.write(excelFileName).sheet("建表语句").doWrite(costOtherDataOuts);

        File file = new File(excelFileName);
        if(!file.exists()){
            file.createNewFile();
        }else {
            file.delete();
            file.createNewFile();
        }
        PrintWriter printWriter = new PrintWriter(file);
        costOtherDataOuts.forEach(item -> {
            printWriter.write(item.getCreateContent());
            printWriter.write("\n\n\n");
        });
       printWriter.flush();
       printWriter.close();
    }


}
