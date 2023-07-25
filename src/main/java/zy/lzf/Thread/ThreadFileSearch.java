package zy.lzf.Thread;

import java.io.File;
import java.util.Date;

/**
 * @program: Thread
 * @description:
 * @author: Joker
 * @create: 2023-07-17 14:10
 */
public class ThreadFileSearch {
    public static void main(String[] args) {
        Result result = new Result();
        Date start, end;
        start = new Date();
//        SingleThreadSearch.searchFile(new File("D://"), "jdk api 1.8.", result);
//        end = new Date();
//        System.out.println(result.isFound() ? result.getPath() + "花费" + (end.getTime() - Start.getTime()) : "查无此文件");
        MultiThreadSearch.searchFile(new File("C:"), "hosts", result);
        end = new Date();
        System.out.println(result.isFound() ? result.getPath() + ",花费" + (end.getTime() - start.getTime()) + "ms" : "查无此文件");
    }
}
