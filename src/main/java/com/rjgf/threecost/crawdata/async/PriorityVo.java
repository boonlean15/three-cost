package com.rjgf.threecost.crawdata.async;

import lombok.Data;

import java.util.Date;

@Data
public class PriorityVo implements Comparable<PriorityVo>{

    private String name;

    private Date date;

    @Override
    public int compareTo(PriorityVo other) {
        return this.date.compareTo(other.date);
    }
}
