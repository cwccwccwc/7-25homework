package zy.tomcatV2.javax;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
* 注解
* */
@Target({ElementType.TYPE})         //Type表示这个注解只能放在 类 接口
@Retention(RetentionPolicy.RUNTIME)     //Runtime表示这个注解在运行时才有
public @interface YcWebServlet {

    String value() default "";
}

//      使用一： @YcWebServlet(value="/hello")
//      使用二:  @YcWebServlet("/hello")
//              public class HelloServlet extends YcHttpServlet{}
