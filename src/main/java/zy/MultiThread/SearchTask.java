package zy.MultiThread;

import zy.SingleThread.Result;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SearchTask implements Runnable{
    private final String fileName;
    private final ConcurrentLinkedQueue<File> files;
    private final Result result;

    private boolean found;

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public SearchTask(String fileName, ConcurrentLinkedQueue<File> files, Result result) {
        this.fileName = fileName;
        this.files = files;
        this.result = result;
        this.found=false;
    }

    @Override
    public void run() {
        //循环 files 取队头的每个目录
        while (files.size()>0){
            File f = files.poll();
            try {
                //递归搜索
                processDirectory(f, fileName, result);
                if (found) {
                    System.out.printf("%s 查到文件了 %s\n", Thread.currentThread().getName(), fileName);
                    System.out.printf("位置在%s\n", result.getPath());
                    return;
                }
            }catch (InterruptedException e){
                System.out.printf("%s 被中断\n",Thread.currentThread().getName());
                return;
            }
        }
        System.out.println();
        System.out.printf("线程%s对应的任务没有文件了，即将 terminated\n",Thread.currentThread().getName());
    }

    //TODO:中断信号的处理
    private void processDirectory(File f,String fileName,Result result) throws InterruptedException {
        File[] contents = f.listFiles();
        if (contents==null||contents.length<=0){
            return;
        }
        //如果不为空 则循环contents 判断是目录 则递归
        for (File content:contents){
            if (content.isDirectory()){
                processDirectory(content,fileName,result);
                if (Thread.currentThread().isInterrupted()){
                    throw new InterruptedException();
                }
                //判断是否在子目录content中找到 如果找到 则返回
                if (found){
                    return;
                }
            }else {
                if (content.getName().equals(fileName)){
                    result.setPath(content.getAbsolutePath());
                    result.setFound(true);
                    this.found=true;
                    return;
                }
                if (Thread.currentThread().isInterrupted()){
                    throw new InterruptedException();
                }
                if (found){
                    return;
                }
            }

        }
    }
}
