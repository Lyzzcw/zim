package lyzzcw.work.zim.router.pagehelper;

import com.github.pagehelper.PageHelper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/3/7
 * Time: 9:15
 * Description: PageHelper 使用 ThreadLocal 的线程复用问题
 */
public class PageLocalWebInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        // PageHelper.clearPage() 内部调用 LOCAL_PAGE.remove()
        //1.保证startPage()后紧跟sql命令
        //2.使用clearPage()来打补丁
        PageHelper.clearPage();

    }
}
