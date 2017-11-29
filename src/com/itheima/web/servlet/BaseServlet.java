package com.itheima.web.servlet;

import com.sun.deploy.net.HttpRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@WebServlet(name = "BaseServlet")
public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        * 封装servlet基类*/
        req.setCharacterEncoding("UTF-8");
//        1.获取method
        String method = req.getParameter("method");
//        2.根据方法名这个字符串,调用方法----->想到用反射
        Class clazz = this.getClass();
        try {
            Method getMethod = clazz.getMethod(method,HttpServletRequest.class,HttpServletResponse.class);
            getMethod.invoke(this,req,resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("反射调用servlet方法失败"+e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("反射调用servlet方法失败"+e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("反射调用servlet方法失败"+e);
        }
    }
}
