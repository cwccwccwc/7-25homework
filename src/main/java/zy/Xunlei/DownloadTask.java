package zy.Xunlei;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask implements Runnable{
   private int i;
   private long fileSize;
   private int threads;
   private long sizePerThread;
   private String url;
   private String newFilePath;

    Xunlei.LengthNotify lengthNotify;
    public DownloadTask(int i, long fileSize, int threads, long sizePerThread, String url, String newFilePath, Xunlei.LengthNotify lengthNotify) {
        this.i = i;
        this.fileSize = fileSize;
        this.threads = threads;
        this.sizePerThread = sizePerThread;
        this.url = url;
        this.newFilePath = newFilePath;
        this.lengthNotify=lengthNotify;
    }

    @Override
    public void run() {
        //计算 下载的起 止
        long start=i*sizePerThread;
        long end=(i+1)*sizePerThread-1;
        RandomAccessFile raf=null;
        InputStream iis=null;

        try{
            //利用RandomAccessFile在  newFilePath中寻找保存位置
            raf = new RandomAccessFile(newFilePath, "rw");
            raf.seek(start);

            //利用http的请求头域 Range 到服务区下载指定位置的内容
            URL urlobj = new URL(url);

            HttpURLConnection con =(HttpURLConnection) urlobj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Range","bytes="+start+"-"+end);
            con.setConnectTimeout(10*1000);

            //再开始下载
            iis=new BufferedInputStream(con.getInputStream());
            byte[] bs = new byte[100 * 1024];
            int length=0;
            while ((length=iis.read(bs,0,bs.length))!=-1){
                raf.write(bs,0,length);
                long total = lengthNotify.notifyResult(length);
                System.out.println("到线程"+Thread.currentThread().getName()+"下载了到"+total*100/fileSize+"%");
//                System.out.println("线程"+Thread.currentThread().getName()+"下载了"+length);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (iis!=null){
                try {
                    iis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (raf!=null){
                try {
                    raf.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("下载完毕");
    }
}
