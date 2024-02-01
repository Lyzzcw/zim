package lyzzcw.work.zim.router.pagehelper;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/3/7
 * Time: 9:16
 * Description: No Description
 */
@Configuration
public class PageHelperAutoConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PageLocalWebInterceptor());
    }
}
