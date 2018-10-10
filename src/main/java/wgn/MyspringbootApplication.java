package wgn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Company        :   mamahao.com
 * author         :   wangguannan
 * Date           :   2018/10/10
 * Time           :   下午5:43
 * Description    :
 */
@SpringBootApplication
public class MyspringbootApplication {
    public static void main(String[] args) {

        try {
            SpringApplication.run(MyspringbootApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
