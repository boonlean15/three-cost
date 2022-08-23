package com.rjgf.threecost.crawdata.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

@Slf4j
public class MyAsync {


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
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);//消费者线程为4，留两个出来给重启处理未完成的中断单
        taskExecutor.setMaxPoolSize(6);
        taskExecutor.setQueueCapacity(50); //处理未完成的中断单--超过50说明问题很严重啊
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setThreadNamePrefix("requestExecutor--");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(600);
        taskExecutor.initialize();
        for (int i = 0; i < 5; i++) { //不要超了线程池的设置
            taskExecutor.execute(() -> {
                while (true) {
                    log.info("循环方法 while 循环 内层 n ========== 线程名：" + Thread.currentThread().getName());
                    try {
                        //消费事件
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
                }
            });
        }
        while(true){
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            int noThreads = threadGroup.activeCount();
            Thread[] lstThreads = new Thread[noThreads];
            threadGroup.enumerate(lstThreads);
            for (int i = 0; i < noThreads; i++)
                System.out.println("线程号：" + i + " = " + lstThreads[i].getName());
        }
    }
}
