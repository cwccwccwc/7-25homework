package zy.tomcatV2;
/*
* 资源处理接口
* */


import zy.tomcatV2.javax.YcServletRequest;
import zy.tomcatV2.javax.YcServletResponse;

public interface Processor {
    public void process(YcServletRequest request, YcServletResponse response);
}
