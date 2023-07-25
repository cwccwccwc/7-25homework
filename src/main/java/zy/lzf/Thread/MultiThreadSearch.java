package zy.lzf.Thread;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @program: Thread
 * @description: 多线程文件搜索
 * @author: Joker
 * @create: 2023-07-17 14:44
 */
public class MultiThreadSearch {
    public static void searchFile(File dir, String fileName, Result result) {
        //1.循环dir下所有的文件和目录 分别处理 如是文件 则 equals(fileName) 如成功 则结束
//                                            将目录保存到一个 queue(这个队列存所有的待查找的目录，则多个线程来操作
//                                            所以queue必须为线程安全 如果queue不是线程安全的 则要加入synchronized)
        File[] sonFiles = dir.listFiles();
        ConcurrentLinkedDeque<File> files = new ConcurrentLinkedDeque<>(); //ConcurrentLinkedDeque是Java中的一个并发安全的双向链表，它支持高效的并发访问和修改操作。
        for (File f : sonFiles) {
            if (f.isDirectory()) {
                files.add(f);
            } else {
                if (f.getName().equals(fileName)) {
                    result.setFound(true);
                    result.setPath(f.getAbsolutePath());
                    return;
                }
            }
        }
        //2.启动 合适的线程数 Runtime类
        int numThreads = Runtime.getRuntime().availableProcessors();
//        int numThreads = 3;
        Thread[] threads = new Thread[numThreads];
        SearchTask[] tasks = new SearchTask[numThreads];
        //绑定任务类
        for (int i = 0; i < threads.length; i++) {
            tasks[i] = new SearchTask(fileName, files, result); //创建第i个任务
            threads[i] = new Thread(tasks[i]); // 将第i个任务与第i个线程绑定
            threads[i].start();
            System.out.println(threads[i].getName() + "启动了");
        }
        boolean finish = false;
        int numFinished = 0;
        while (!finish) {
            numFinished = 0;
            for (int i = 0; i < threads.length; i++) {
                if (threads[i].getState() == Thread.State.TERMINATED) {
                    numFinished++;
                    if (tasks[i].isFound()) {
                        finish = true;
                    }
                }
            }
            if (numFinished == threads.length) {
                finish = true;
            }
        }
        if (numFinished != threads.length) {
            for (Thread t : threads) {
                System.out.println("向" + t.getName() + " 发中断信号");
                t.interrupt(); //发中断
            }
        }
    }
}
