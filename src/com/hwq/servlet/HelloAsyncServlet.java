package com.hwq.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//asyncSupported = true 表示该servlet支持异步
@WebServlet(value = "/async",asyncSupported = true)
public class HelloAsyncServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("main thread start..."+Thread.currentThread()+"===>"+System.currentTimeMillis());
        //1、支持异步处理 @WebServlet(value = "/async",asyncSupported = true)
        //2、开启异步模式
        AsyncContext asyncContext = req.startAsync();
        //开始异步处理业务逻辑
        asyncContext.start(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("sub thread start..."+Thread.currentThread()+"===>"+System.currentTimeMillis());
                    sayHello();
                    //异步处理完
                    asyncContext.complete();
                    //获取到异步的上下文，和req.startAsync();是一样的
                    AsyncContext asyncContext1 = req.getAsyncContext();
                    //获取响应
                    ServletResponse response = asyncContext1.getResponse();
                    System.out.println("sub thread end..."+Thread.currentThread()+"===>"+System.currentTimeMillis());
                    response.getWriter().write("async result...");

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        System.out.println("main thread end..."+Thread.currentThread()+"===>"+System.currentTimeMillis());
    }

    public void sayHello() throws InterruptedException {
        System.out.println(Thread.currentThread()+" processing...");
        Thread.sleep(3000);
    }
}
