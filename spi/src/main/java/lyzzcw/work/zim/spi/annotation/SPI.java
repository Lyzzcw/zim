package lyzzcw.work.zim.spi.annotation;

import java.lang.annotation.*;

/**
 * @author lzy
 * @version 1.0.0
 * @description @SPI
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    /**
     * 默认的实现方式
     */
    String value() default "";
}
