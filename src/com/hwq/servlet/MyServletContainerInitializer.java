package com.hwq.servlet;

import com.hwq.service.HelloService;

import javax.servlet.*;
import javax.servlet.annotation.HandlesTypes;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 容器启动的时候 会将 @HandlesTypes 指定的感兴趣的类型的子类型传递过来
 */
@HandlesTypes(value={HelloService.class})
public class MyServletContainerInitializer implements ServletContainerInitializer {

    /**
     * servlet容器启动时会运行这个方法
     * @param set  感兴趣的类型集合， @HandlesTypes 注解指定的
     * @param servletContext 代表web应用
     *
     * 使用servletContext 注册web组件 (Servlet,Filter,Listener)
     * 使用编码的方式，在项目启动时候给ServletContext里面添加组件
     * 必须在项目启动时候添加
     *   1、ServletContainerInitializer 实现类得到 ServletContext 进行添加
     *   2、ServletContextListener 的实现类得到 ServletContext 进行添加
     *
     */
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        System.out.println("MyServletContainerInitializer.onStartup");
        System.out.println("感兴趣的类型:");
        for (Class clazz :set){
            System.out.println(clazz.getName());
        }
        // 注册组件
        ServletRegistration.Dynamic userServlet = servletContext.addServlet("userServlet", new UserServlet());
        userServlet.addMapping("/user");
        // 注册Listener
        servletContext.addListener(UserListener.class);
        // 注册Filter
        FilterRegistration.Dynamic userFilter = servletContext.addFilter("userFilter", UserFilter.class);
        userFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),true,"/*");


    }

}
