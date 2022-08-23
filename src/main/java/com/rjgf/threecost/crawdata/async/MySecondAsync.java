package com.rjgf.threecost.crawdata.async;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MySecondAsync {


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
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setQueueCapacity(50); //处理未完成的中断单--超过50说明问题很严重啊
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setThreadNamePrefix("requestExecutor--");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(600);
        taskExecutor.initialize();


        /**
         * take 获取queue中的数据 如果队列为空 notEmpty条件await 当有人往里加offer数据时，single一个醒来
         */

            while (true) {
                int activeCount = taskExecutor.getActiveCount();
                log.info("循环打印日志---- " +  activeCount);
                ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                int noThreads = threadGroup.activeCount();
                Thread[] lstThreads = new Thread[noThreads];
                threadGroup.enumerate(lstThreads);
                for (int i = 0; i < noThreads; i++)
                    System.out.println("线程号：" + i + " = " + lstThreads[i].getName());
                taskExecutor.execute(() -> {
                    try {
                        log.error("msgs.take()前");
                        msgs.poll(2000, TimeUnit.MILLISECONDS);
                        PriorityVo dto = msgs.poll(2000,TimeUnit.MILLISECONDS);
                        log.error("take到的对象是，{}", dto);
                        log.error("msgs.take()后，个数：{}, msgs：{}", msgs.size(), msgs);
                        String name = Thread.currentThread().getName();
                        log.info("currentThread ----- " + name);
//                        long time = dto.getDate().getTime();
//                        String name1 = dto.getName();
//                        log.info("time ------------- " + time + "------------ name1=========" + name1);



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
            }
    }




}
