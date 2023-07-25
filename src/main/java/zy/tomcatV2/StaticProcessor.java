package zy.tomcatV2;

import zy.tomcatV2.javax.YcServletRequest;
import zy.tomcatV2.javax.YcServletResponse;
import zy.tomcatV2.javax.http.YcHttpServletResponse;

public class StaticProcessor implements Processor {
    @Override
    public void process(YcServletRequest request, YcServletResponse response) {
        ((YcHttpServletResponse)response).send();
    }


}
