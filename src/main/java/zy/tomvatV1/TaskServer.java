package zy.tomvatV1;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/*
*   Connection  close
*               keep-alive
* */
public class TaskServer implements Runnable{
    private static Logger logger=Logger.getLogger(TaskServer.class);
    private Socket s;
    private InputStream iss;
    private OutputStream oos;
    private boolean flag=true;
    public TaskServer(Socket s) {
        this.s=s;
        try {
            this.iss=s.getInputStream();
            this.oos=s.getOutputStream();
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error("Socket获取流异常");
            flag=false;
        }

    }

    @Override
    public void run() {
        if (this.flag){
            //HttpServletRequest中解析出所有的请求信息()
            //method，资源地址uri  http版本 头域（referre，user-agent） 参数parameter
            //存在 HttpServletRequest对象中
            CwcHttpServletRequest request = new CwcHttpServletRequest(this.s,this.iss);

            CwcHttpServletResponse response = new CwcHttpServletResponse(request, this.oos);
            response.send();
        }
        try {
            this.iss.close();
            this.oos.close();
            this.s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
