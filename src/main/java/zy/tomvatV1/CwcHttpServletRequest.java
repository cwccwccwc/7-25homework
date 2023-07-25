package zy.tomvatV1;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从输入流中取出http请求，解析出相应的信息，存好
 */
public class CwcHttpServletRequest {
    private Logger logger = Logger.getLogger(CwcHttpServletRequest.class);

    private Socket s;
    private InputStream iis;
    //GET,post,put,delete,head,trace,option
    private String method;
    /*
        定位符 http://localhost:81/188109res/doUpload.action?uname=z&pwd=b
    */
    private String requestURL;
    //标识符  /188109res/doUpload.action
    private String requestURI;
    //上下文  /188109res
    private String contextPath;
    //请求字符串，请求的地址栏参数  age=20&sex=female
    private String queryString;
    //参数：地址栏参数  uname=z&pwd=b       表单中的参数-请求实体：sex=女&ins=玩
    private Map<String,String[]> parameterMap = new ConcurrentHashMap<>();
    //协议参数  http://
    private String scheme;
    //协议版本
    private String protocol;
    //项目的真实路径
    private String realPath;

    public CwcHttpServletRequest(Socket s, InputStream iis) {
        this.s = s;
        this.iis = iis;
        this.parseRequest();
    }

    //TODO:解析方法
    private void parseRequest(){
        String requestInfoString = readFromInputStream(); // 从输入流中读取http请求信息（文字）
        if(requestInfoString == null || "".equals(requestInfoString.trim())){
            throw new RuntimeException("读取输入流异常");
        }
        //2.解析http请求头（存各种信息）
        parseRequestInfoString(requestInfoString);
    }

    /**
     * 解析http请求头（存各种信息）
     * @param requestInfoString     http请求协议
     *                              method 资源地址  协议版本
     *                              请求头域键  值*
     *                              空行
     *                              请求实体
     *                           资源地址：/188109res/doUpload.action?uname=z&pwd=b
     */
    private void parseRequestInfoString(String requestInfoString){
        StringTokenizer st = new StringTokenizer(requestInfoString); // 按空格切割
        this.method = st.nextToken();
        this.requestURI = st.nextToken();
        //requestURI要考虑地址栏参数
        int questionIndex = this.requestURI.lastIndexOf("?");
        if(questionIndex >= 0){
            //有?，即有地址栏参数 -> 参数存queryString属性
            this.queryString = this.requestURI.substring(questionIndex + 1);
            this.requestURI = this.requestURI.substring(0, questionIndex);
        }
        //第三部分：协议版本  HTTP/1.1
        this.protocol = st.nextToken();
        // HTTP
        this.scheme = this.protocol.substring(0, this.protocol.indexOf("/"));

        //requestURI: /188109res/index.html
        //         ww.baidu.com -> GET /
        //contextPath:/188109res
        //              /
        int slash2Index = this.requestURI.indexOf("/", 1);
        if(slash2Index >= 0){
            this.contextPath = this.requestURI.substring(0, slash2Index);
        }else{
            this.contextPath = this.requestURI;
        }

        //requestURL:统一资源定位符  http://ip:端口/requestURI
        this.requestURL = this.scheme + "://" + this.s.getLocalSocketAddress() + this.requestURI;

        //参数的处理：/188109res/index.html?uname=z&pwd=b
        //从queryString中取出参数
        if(this.queryString != null && this.queryString.length() > 0){
            String[] ps = this.queryString.split("&");
            for(String s:ps){
                String[] params = s.split("=");
                // 情况一：uname=1
                // 情况二：ins=a,b,c
                this.parameterMap.put(params[0], params[1].split(","));
            }
            //TODO:还有post实体中也有可能有参数....
        }
        //realPath
        this.realPath = System.getProperty("user.dir") + File.separator + "webapps";
    }

    /**
     * 从输入流中读取http请求信息（文字）
     * @return
     */
    private String readFromInputStream(){
        int length = -1;
        StringBuffer sb = null;
        byte[] bs = new byte[300 * 1024]; // TODO:300k足够存除文件上传的请求
        try{
            length = this.iis.read(bs, 0, bs.length);
            sb = new StringBuffer();
            for(int i = 0; i < length; i ++){
                sb.append((char)bs[i]);
            }
        }catch (Exception e){
            logger.error("读取请求信息失败...");
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String[] getParameterValues(String name){
        if(parameterMap == null || parameterMap.size() <= 0){
            return null;
        }
        String[] values = this.parameterMap.get(name);
        return values;
    }
    public String getParameter(String name){
        String[] values = getParameterValues(name);
        if(values == null || values.length <= 0){
            return null;
        }
        return values[0];
    }

    public String getMethod() {
        return this.method;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getQueryString() {
        return queryString;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    public String getScheme() {
        return scheme;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getRealPath() {
        return realPath;
    }
}
