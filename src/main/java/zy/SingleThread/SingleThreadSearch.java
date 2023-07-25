package zy.SingleThread;

import java.io.File;

public class SingleThreadSearch {
    public static void singleThreadSearch(File file,String regex,Result result){
        File[] files = file.listFiles();//该目录下的文件列表
        /*if (files!= null) {
            for (File f : files) {
                //循环遍历是否有目标路径
//                System.out.println(file + "下的文件有..." + files);
                if (f.getName().equals(regex)) {
                    String absolutePath = f.getAbsolutePath();
                    result.setPath(absolutePath);
                    result.setFound(true);
                    System.out.println("查找到了  路径为"+f.getAbsolutePath());
                    break;
                } else if (f.isDirectory()) {
                    //如果这个不是目标文件 先判断是否为目录 为目录就递归
//                    findFiles(f, regex);
                    singleThreadSearch(f, regex,result); // 递归调用，保存结果在变量中
                }
                if (result.isFound()){
                    return;
                }
            }
        }*/

        if (files==null||files.length==0){
            return;
        }
        for (File f:files){
            if (f.isDirectory()){
                singleThreadSearch(f,regex,result);
            }else {
                if (f.getName().equals(regex)){
                    result.setPath(f.getAbsolutePath());
                    result.setFound(true);
                    System.out.println("查找到了  路径为"+f.getAbsolutePath());
                    return;
                }
            }
            if (result.isFound()){
                return;
            }
        }
    }
}
