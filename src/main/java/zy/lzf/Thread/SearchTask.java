package zy.lzf.Thread;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @program: Thread
 * @description:
 * @author: Joker
 * @create: 2023-07-17 14:56
 */
public class SearchTask implements Runnable {
    private final String fileName;
    private final ConcurrentLinkedDeque<File> files;
    private final Result result;

    private boolean found; //此任务是否找到文件

    /**
     * @param fileName 搜索的文件名
     * @param result   搜索到的结果
     * @param files    待搜索的子目录
     */
    public SearchTask(String fileName, ConcurrentLinkedDeque<File> files, Result result) {
        this.fileName = fileName;
        this.files = files;
        this.result = result;
        this.found = false;
    }


    @Override
    public void run() {
//        try {
        //循环 files ，取队头的每个目录
        while (files.size() > 0) {
            File f = files.poll();
            //递归搜索
            try {
            procseeDirectory(f, fileName, result);
            if (found) {
                System.out.printf("%s查找到了文件%s\n", Thread.currentThread().getName(), fileName);
                System.out.printf("位置在%s\n", result.getPath());
                return; //结束线程 已经找到文件了 那线程结束 则线程进入 TERMINATED状态
            }
//        }
            } catch (InterruptedException e) {
                System.out.printf("%s被中断......\n", Thread.currentThread().getName());
                return;
            }
        }
        System.out.println(Thread.currentThread().getName()+"线程对应的任务没有文件了，即将terminated ");
    }

    private void procseeDirectory(File f, String fileName, Result result) throws InterruptedException {

        File[] contents = f.listFiles();
        if (contents == null || contents.length <= 0) {
            return;
        }
        //如果不为空 则循环 contents 判断是目录 则递归
        //                        是文件 则判断是否为fileName
        for (File content : contents) {
            if (content.isDirectory()) {
                procseeDirectory(content, fileName, result);
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("******************");
                    throw new InterruptedException();
                }
                //判断是否在子目录content中找到 如果找到 则返回
                if (found) {
                    return;
                }
            } else {
                if (content.getName().equals(fileName)) {
                    result.setPath(content.getAbsolutePath());
                    result.setFound(true);
                    this.found = true;
                }
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + "抛出异常_____________");
                    throw new InterruptedException();
                }
                if (found) {
                    return;
                }
            }
        }
    }

    public boolean isFound() {
        return found;
    }
}
