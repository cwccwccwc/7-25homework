import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestMyMethod {
    private MyClassTest mt;

    @Before
    public void beforeMt(){mt=new MyClassTest();}

    @Test
    public void testAdd(){
        Assert.assertEquals(3,mt.add(1,2));
    }
}
