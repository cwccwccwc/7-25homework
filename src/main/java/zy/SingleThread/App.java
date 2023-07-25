package zy.SingleThread;

import zy.MultiThread.MultiThreadSearch;

import java.io.File;
import java.util.Date;

public class App {
    public static void main(String[] args) {
        Result result = new Result();
        Date start = new Date();
//        SingleThreadSearch.singleThreadSearch(new File("C:\\Windows\\"),"hosts",result);
        Date end = new Date();
        /*System.out.println("找到的文件路径为"+result.getPath());
        System.out.println("耗时为"+(end.getTime()-start.getTime()));*/

        start = new Date();
        MultiThreadSearch.searchFile(new File("C:\\Windows\\"),"hosts",result);
        end = new Date();
        System.out.println("找到的文件路径为"+result.getPath());
        System.out.println("耗时为"+(end.getTime()-start.getTime()));
    }
}
