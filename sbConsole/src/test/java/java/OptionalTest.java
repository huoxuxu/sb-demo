package java;

import com.hxx.sbConsole.SbConsoleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class OptionalTest {

    @Test
    public void test() {
        System.out.println("==============test==============");

    }

    @Test
    public void createOptionalTest() {
        // Optional 构造方式1 - of 传入的值不能为 null
        Optional<String> helloOption = Optional.of("hello");

        // Optional 构造方式2 - empty 一个空 optional
        Optional<String> emptyOptional = Optional.empty();

        // Optional 构造方式3 - ofNullable 支持传入 null 值的 optional
        Optional<String> nullOptional = Optional.ofNullable(null);
    }

    @Test
    public void isPresent() {
        Optional<String> helloOptional = Optional.of("Hello");
        System.out.println(helloOptional.isPresent());// true

        Optional<Object> emptyOptional = Optional.empty();
        System.out.println(emptyOptional.isPresent());// false
    }

    /**
     * 如果有值，输出长度
     */
    @Test
    public void ifPresent2() {
        // 如果没有值，获取默认值
        Optional<String> helloOptional = Optional.of("Hello");
        Optional<String> emptyOptional = Optional.empty();
        helloOptional.ifPresent(s -> System.out.println(s.length()));
        emptyOptional.ifPresent(s -> System.out.println(s.length()));
    }

    /**
     * 如果没有值，会抛异常
     */
    @Test
    public void getVal() {
        Optional<String> stringOptional = Optional.of("hello");
        System.out.println(stringOptional.get());
        // 如果没有值，会抛异常
        Optional<String> emptyOptional = Optional.empty();
        System.out.println(emptyOptional.get());
    }

    /**
     * 如果没有值，获取默认值
     */
    @Test
    public void orElse() {
        // orElseGet 传入的方法在有值的情况下并不会运行。而 orElse却都会运行
        // 如果没有值，获取默认值
        Optional<String> emptyOptional = Optional.empty();
        String orElse = emptyOptional.orElse("orElse default");
        String orElseGet = emptyOptional.orElseGet(() -> "orElseGet default");
        System.out.println(orElse);
        System.out.println(orElseGet);
    }

}
