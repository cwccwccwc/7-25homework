package zy.MultiThread;

import zy.SingleThread.Result;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultiThreadSearch {
    public static void searchFile(File dir, String fileName, Result result){
        /*
        * 1.循环dir下所有的文件和目录，分别处理，如果是文件，则 equals（fileName） 如成功，则结束
        *   将目录保存到一个queue（这个队列中存所有将查找的目录，则多个线程来操作
        *   所以queue必须为线程安全 如此queue不是线程安全 则需synchroinazed）
        * */
        File[] sonFiles = dir.listFiles();
        ConcurrentLinkedQueue<File> files = new ConcurrentLinkedQueue<>();
        for (File f:sonFiles){
            if (f.isDirectory()){
                files.add(f);
            }else {
                if (f.getName().equals(fileName)){
                    result.setFound(true);
                    result.setPath(f.getAbsolutePath());
                    return;
                }
            }
        }
        //2.启动 合适的线程数  Runtime类
        int numThreads = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numThreads];
        SearchTask[] tasks = new SearchTask[numThreads];
        //绑定任务类
        for (int i = 0; i < numThreads; i++) {
            tasks[i]=new SearchTask( fileName,files,result);
            threads[i]=new Thread(tasks[i]);
            threads[i].start();
            System.out.println(threads[i].getName()+"启动");
        }

        //TODO:  3.发出终止线程问题
        //只要有情况就发送中止信号
        boolean finish=false;//判断是否完成
        int numFinished=0;//完成的线程
        while (!finish){
            //循环所有的子线程，查看状态
            numFinished=0;
            for (int i = 0; i < threads.length; i++) {
                if (threads[i].getState()==Thread.State.TERMINATED){
                    numFinished++;
                    //如果thread[i]的线程找到文件，则它的found属性为true
                    if (tasks[i].isFound()){
                        finish=true;
                    }
                }
            }
            //全部的线程都递归，都没有文件
            if (numFinished==threads.length){
                finish=true;
            }
        }
        //判断当前while循环是因为  查找到文件而中止、
        System.out.println("numFinished"+numFinished);
        System.out.println("threads.length"+threads.length);
        if (numFinished!=threads.length){
            for (Thread t:threads){
                System.out.println("向"+t.getName()+"发中断信号");
                t.interrupt();//发中断
            }
        }
    }
}
