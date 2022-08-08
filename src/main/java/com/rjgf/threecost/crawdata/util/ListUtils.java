package com.rjgf.threecost.crawdata.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linch
 * @create 2022/3/1 11:56
 */
@Slf4j
public class ListUtils {

    public static <T> List<List<T>> split(List<T> list, int pageSize) {
        List<List<T>> listArray = new ArrayList<List<T>>();
        List<T> subList = null;
        for (int i = 0; i < list.size(); i++) {
            // 每次到达页大小的边界就重新申请一个subList
            if (i % pageSize == 0) {
                subList = new ArrayList<T>();
                listArray.add(subList);
            }
            subList.add(list.get(i));
            log.info("");
        }
        return listArray;
    }
}
