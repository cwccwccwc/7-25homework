package zy.tomcatV2.javax.http;

import zy.tomcatV2.javax.YcServletResponse;

import java.io.*;

/*
* 根据request 处理响应
*                  1xx
*                  2xx 有这个资源 正常响应
*                   3xx 重定向
*                   4xx 找不到这个资源
*                   5xx 服务器内部错误
* */
public class YcHttpServletResponse implements YcServletResponse {
   private YcHttpServletRequest request;
   private OutputStream oos;
    
    public YcHttpServletResponse(YcHttpServletRequest request, OutputStream oos) {
        this.request=request;
        this.oos=oos;
    }
    @Override
    public void send() {
        String uri = request.getRequestURI();
        String realPath = request.getRealPath();
        File f = new File(realPath, uri);
        byte[] fileContent=null;
        String responseProtocol=null;
        if (!f.exists()){
            //文件不存在，4xx响应
            fileContent=readFile(new File(realPath+"/404.html"));
            responseProtocol=gen404(fileContent);
        }else {
            //文件存在 2xx响应
            fileContent=readFile(new File(realPath+uri));
            responseProtocol=gen200(fileContent);
        }
        try{
            oos.write(responseProtocol.getBytes());
            oos.flush();
            oos.write(fileContent);
            oos.flush();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                oos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public OutputStream getOutputStream() {
        return oos;
    }

    @Override
    public PrintWriter getPrintWriter() {
        return new  PrintWriter(this.oos);
    }


    /*
    * 200协议
    *
        HTTP/1.1 200 OK
        Bdpagetype: 2
        Bdqid: 0x9a827bbc000be251
        Connection: keep-alive
        Content-Encoding: gzip
        Content-Security-Policy: frame-ancestors 'self' https://chat.baidu.com http://mirror-chat.baidu.com https://fj-chat.baidu.com https://hba-chat.baidu.com https://hbe-chat.baidu.com https://njjs-chat.baidu.com https://nj-chat.baidu.com https://hna-chat.baidu.com https://hnb-chat.baidu.com http://debug.baidu-int.com;
        Content-Type: text/html; charset=utf-8
    * */
    private String gen200(byte[] fileContent) {
        String protocol200="";
        //先取出请求资源的类型
        String uri = request.getRequestURI();
        int index = uri.lastIndexOf(".");
        if (index>=0){
            index=index+1;
        }
        String fileExtension = uri.substring(index);
        if ("JPG".equalsIgnoreCase(fileExtension)){
            protocol200= "HTTP/1.1 200 OK\r\nContent-Type: image/jpeg; charset=utf-8\r\nContent-length:"+fileContent.length+"\r\n\r\n";
        } else if ("css".equalsIgnoreCase(fileExtension)){
            protocol200= "HTTP/1.1 200 OK\r\nContent-Type: text/css; charset=utf-8\r\nContent-length:"+fileContent.length+"\r\n\r\n";
        } else if ("js".equalsIgnoreCase(fileExtension)){
            protocol200= "HTTP/1.1 200 OK\r\nContent-Type: text/javascript; charset=utf-8\r\nContent-length:"+fileContent.length+"\r\n\r\n";
        } else if ("gif".equalsIgnoreCase(fileExtension)){
            protocol200= "HTTP/1.1 200 OK\r\nContent-Type: image/gif; charset=utf-8\r\nContent-length:"+fileContent.length+"\r\n\r\n";
        } else if ("png".equalsIgnoreCase(fileExtension)){
            protocol200= "HTTP/1.1 200 OK\r\nContent-Type: image/png; charset=utf-8\r\nContent-length:"+fileContent.length+"\r\n\r\n";
        } else  {
            protocol200= "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\nContent-length:"+fileContent.length+"\r\n\r\n";
        }
        return protocol200;
    }


    /*
    * 404协议
    *
        HTTP/1.1 404 Not Found
        Content-Length: 208
        Content-Type: text/html; charset=iso-8859-1
        Date: Thu, 20 Jul 2023 13:12:23 GMT
        Server: Apache
    * */
    private String gen404(byte[] fileContent){
        String protocol404="HTTP/1.1 404 Not Found\r\nContent-Length: "+fileContent.length+"\r\n";
        protocol404+="Content-Type: text/html; charset=utf-8\r\nServer: CWC Server\r\n\r\n";
        return protocol404;
    }

    /*
    * 读取本地文件
    * */

    private byte[] readFile(File file){
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        FileInputStream fis=null;
        try {
            fis=new FileInputStream(file);
            byte[] bs = new byte[100 * 1024];
            int length=-1;
            while ((length=fis.read(bs,0,bs.length))!=-1){
                boas.write(bs,0,length);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return boas.toByteArray();
    }
}
