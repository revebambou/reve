package test;

import cn.reve.utils.BCrypt;
import org.junit.Test;

public class TestBCrypt {

    @Test
    public void TestBCrypt(){
        String gensalt = BCrypt.gensalt();
        System.out.println(gensalt);
        String reve = BCrypt.hashpw("reve", gensalt);
        System.out.println(reve);
        boolean checkpw = BCrypt.checkpw("reve", reve);
        System.out.println(checkpw);
    }

}
