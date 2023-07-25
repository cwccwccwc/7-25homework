package zy.tomcatV2.javax.http;

import zy.tomcatV2.javax.YcServlet;
import zy.tomcatV2.javax.YcServletRequest;
import zy.tomcatV2.javax.YcServletResponse;

public abstract class YcHttpServlet implements YcServlet {
    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }
    protected void doGet(YcHttpServletRequest req, YcHttpServletResponse resp) {
    }
    protected void doPost(YcHttpServletRequest req, YcHttpServletResponse resp) {
    }
    protected void doHead(YcHttpServletRequest req, YcHttpServletResponse resp) {
    }
    protected void doDelete(YcHttpServletRequest req, YcHttpServletResponse resp)  {
    }
    protected void doTrace(YcHttpServletRequest req, YcHttpServletResponse resp) {
    }
    protected void doOption(YcHttpServletRequest req, YcHttpServletResponse resp)  {
    }

    /*
    * 模板设计模式 规范httpServlet中各方法的调用顺序
    * */

    public void service(YcServletRequest request, YcServletResponse response) {
        String method = ((YcHttpServletRequest) request).getMethod();
        if ("get".equalsIgnoreCase(method)){
            doGet((YcHttpServletRequest)request,(YcHttpServletResponse)response);
        }else if ("post".equalsIgnoreCase(method)){
            doPost((YcHttpServletRequest)request,(YcHttpServletResponse)response);
        }else if ("head".equalsIgnoreCase(method)){
            doHead((YcHttpServletRequest)request,(YcHttpServletResponse)response);
        }else if ("trace".equalsIgnoreCase(method)){
            doTrace((YcHttpServletRequest)request,(YcHttpServletResponse)response);
        }else if ("delete".equalsIgnoreCase(method)){
            doDelete((YcHttpServletRequest)request,(YcHttpServletResponse)response);
        }else if ("option".equalsIgnoreCase(method)){
            doOption((YcHttpServletRequest)request,(YcHttpServletResponse)response);
        }else{
            //TODO:错误的响应协议
        }
    }
    public void service(YcHttpServletRequest request, YcHttpServletResponse response) {
        service(request,response);
    }
}
