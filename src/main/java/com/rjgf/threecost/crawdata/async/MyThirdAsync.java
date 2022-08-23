package com.rjgf.threecost.crawdata.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MyThirdAsync {

    private static PriorityBlockingQueue<PriorityVo> msgs = new PriorityBlockingQueue<>(50);

    static {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        for(int i = 1; i <= 50; i++){
            PriorityVo priorityVo = new PriorityVo();
            priorityVo.setName("priorityVo --" + i);
            priorityVo.setDate(calendar.getTime());
            msgs.add(priorityVo);
            calendar.add(Calendar.MINUTE,10);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final ThreadPoolExecutor EXECUTOR_SERVICE = new ThreadPoolExecutor(5, 10,
                60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(50),
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").build()
                ,new ThreadPoolExecutor.CallerRunsPolicy());
        /**
         * take 获取queue中的数据 如果队列为空 notEmpty条件await 当有人往里加offer数据时，single一个醒来
         */
        while (true) {
            EXECUTOR_SERVICE.execute(() -> {
                try {
                    log.error("msgs.take()前");
                    PriorityVo dto = msgs.take();
                    log.error("take到的对象是，{}", dto);
                    log.error("msgs.take()后，个数：{}, msgs：{}", msgs.size(), msgs);
                    String name = Thread.currentThread().getName();
                    log.info("currentThread ----- " + name);
                    long time = dto.getDate().getTime();
                    String name1 = dto.getName();
                    log.info("time ------------- " + time + "------------ name1=========" + name1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        }




    }

}
