package zy.tomcatV2.javax;

import java.io.OutputStream;
import java.io.PrintWriter;

public interface YcServletResponse {
    public void send();

    public OutputStream getOutputStream();

    public PrintWriter getPrintWriter();
}
