package zy.Xunlei;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;


public class Xunlei {
//    private static volatile int total;

        private static AtomicLong total=new AtomicLong(0L);//原子类
        static class LengthNotify implements Notify{
        @Override
        public long notifyResult(long length) {
//            total+=length;
            long l = total.addAndGet(length);
            System.out.println("总共下载了:"+total);
            return l;
        }
    }
    interface Notify{
        public long notifyResult(long length);
    }
    public static void main(String[] args) {

        //1.待下载文件大小
        String url="https://uu.gdl.netease.com/4096/UU-4.58.3.exe";
        long fileSize = getFileSize(url);
        System.out.println("文件长度为"+fileSize);//文件长度为53494272
        System.out.println("新文件名"+getNewFileName(url));
        String newFilePath = getNewFilePath(getNewFileName(url));
        System.out.println("新文件位置"+newFilePath);
        //创建空文件 占位置
        createNewFile(fileSize,getNewFilePath(getNewFileName(url)));
        //线程数
        int threads = Runtime.getRuntime().availableProcessors();
        //每个线程的最大下载量
        long sizePerThread = getSizePerThread(threads, fileSize);
        System.out.println("每个线程的最大下载量为"+sizePerThread);

        //分配线程任务
        for (int i = 0; i < threads; i++) {
            DownloadTask task = new DownloadTask(i,fileSize,threads,sizePerThread,url,newFilePath,new LengthNotify());
            Thread t = new Thread(task);
            t.start();
        }
    }

    /*
     * 待下载文件大小
     * 在下载开始前要先获取到待下载的文件的大小，在本地创建一个空文件，占好空间
     *           a.先发一个请求 method:HEAD
     *           b.Socket开发 Socket(文件地址，端口)
     *                       获取输出流，拼接协议
     *           ***URL->URLConneciton->HttpURLConneciont
     *               contentLength
     * */
    public static long getFileSize(String url){
        long fileSize=0;
        URL u = null;
        try {
            u = new URL(url);
            HttpURLConnection huc = (HttpURLConnection)u.openConnection();
            huc.setRequestMethod("HEAD");//请求行: HEAD/xxx HTTP/1.1
            huc.connect();
            fileSize=huc.getContentLength();

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileSize;
    }
    /*
    * 获取一个新文件名
    *
    * */
    public static String getNewFileName(String url){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String prefix = sdf.format(date);
        int i = url.lastIndexOf(".");
        String substring = url.substring(i);
        String newName = prefix+substring;
        return newName;
    }

    /*
    * 获取新文件 的用户路径位置
    * */
    public static String getNewFilePath(String newFileName){
        String userhome = System.getProperty("user.home");
        return userhome+ File.separator+newFileName;
    }

    /*
    * 建立一个空文件 占位置
    *
    * */
    public static void createNewFile(long fileSize,String newFilePath){
        try (RandomAccessFile raf = new RandomAccessFile(newFilePath, "rw");){
            raf.setLength(fileSize);
            raf.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /*
    * 计算每个线程要下载的大小
    *
    * */
    public static long getSizePerThread(int threads,long fileSize){
        return fileSize%threads==0?fileSize/threads:fileSize/threads+1;
    }
}
