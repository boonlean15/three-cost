package com.rjgf.threecost.crawdata.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.StringUtils;
import com.rjgf.threecost.crawdata.entity.vo.CostOtherData;
import com.rjgf.threecost.crawdata.entity.vo.CostOtherDataOut;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author linch
 * @create 2022/3/15 14:12
 */
@Slf4j
public class CostOneDataListener extends AnalysisEventListener<CostOtherData> {

    /**
     * 输出临时文件夹
     */
    private final String OUT_PATH = "H:\\润建\\三费\\仓库已有的表结构\\仓库已有的表结构\\单表\\";

    private List<CostOtherData> dataList = new ArrayList<>();

    private String tableName;

    public CostOneDataListener(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void invoke(CostOtherData otherData, AnalysisContext analysisContext) {
        dataList.add(otherData);
    }

    @SneakyThrows
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
    }

    private void saveData() throws IOException {
        StringBuilder sb = new StringBuilder();
        dataList.stream().filter(v -> StringUtils.isNotBlank(v.getContent())).forEach(v -> {
            String content = v.getContent();
            Optional<String> s = Optional.ofNullable(content).map(c -> c.contains("：") ? c.substring(0, c.indexOf("：")) : c);
            Optional<String> s1 = s.map(c -> c.contains(":") ? c.substring(0, c.indexOf(":")) : c);
            log.info("optional ----------- s" + s);
            log.info("optional ----------- s1" + s1);
            String propName = v.getPropName();
            sb.append("COMMENT ON COLUMN \"ncms_gd\".").append("\"").append(tableName).append("\".")
                    .append("\"").append(propName).append("\"").append(" IS ").append("'").append(s1.orElse("null")).append("';\n");
        });
        String sql = sb.toString();
        String excelFileName = OUT_PATH + tableName + ".sql";
        File file = new File(excelFileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        PrintWriter printWriter = new PrintWriter(file);
        printWriter.write(sql);
        printWriter.flush();
        printWriter.close();
    }
}
