package Utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionFactoryUtils {
    private static volatile SqlSessionFactory sqlSessionFactory;

    /*static {
        try {
            String resource="mybatis-config.xml";
            InputStream inputStream=null;
            inputStream= Resources.getResourceAsStream(resource);
            sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/
    public static SqlSessionFactory getSqlSessionFactory(){

        if (sqlSessionFactory==null){
            synchronized (SqlSessionFactory.class){
                if (sqlSessionFactory==null){
                    try {
                        String resource="mybatis-config.xml";
                        InputStream inputStream=null;
                        inputStream= Resources.getResourceAsStream(resource);
                        sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return sqlSessionFactory;
    }
}
