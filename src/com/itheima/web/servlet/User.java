package com.itheima.web.servlet;

import com.itheima.service.UserService;
import com.itheima.utils.CommonsUtils;
import com.itheima.utils.MailUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class User extends BaseServlet {
    public void Active(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获得激活码
        String activeCode = request.getParameter("activeCode");

        UserService service = new UserService();
        service.active(activeCode);

        //跳转到登录页面
        response.sendRedirect(request.getContextPath()+"/login.jsp");
    }
    public void Register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        //获得表单数据
        Map<String, String[]> properties = request.getParameterMap();
        com.itheima.domain.User user = new com.itheima.domain.User();
        try {
            //自己指定一个类型转换器（将String转成Date）
            ConvertUtils.register(new Converter() {
                @Override
                public Object convert(Class clazz, Object value) {
                    //将string转成date
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date parse = null;
                    try {
                        parse = format.parse(value.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return parse;
                }
            }, Date.class);
            //映射封装
            BeanUtils.populate(user, properties);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //private String uid;
        user.setUid(CommonsUtils.getUUID());
        //private String telephone;
        user.setTelephone(null);
        //private int state;//是否激活
        user.setState(0);
        //private String code;//激活码
        String activeCode = CommonsUtils.getUUID();
        user.setCode(activeCode);


        //将user传递给service层
        UserService service = new UserService();
        boolean isRegisterSuccess = service.regist(user);

        //是否注册成功
        if(isRegisterSuccess){
            //发送激活邮件
            String emailMsg = "恭喜您注册成功，请点击下面的连接进行激活账户"
                    + "<a href='http://localhost:8080/HeimaShop/active?activeCode="+activeCode+"'>"
                    + "http://localhost:8080/HeimaShop/active?activeCode="+activeCode+"</a>";
            try {
                MailUtils.sendMail(user.getEmail(), emailMsg);
            } catch (MessagingException e) {
                e.printStackTrace();
            }


            //跳转到注册成功页面
            response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
        }else{
            //跳转到失败的提示页面
            response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
        }
    }
    public void CheckUsername(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获得用户名
        String username = request.getParameter("username");

        UserService service = new UserService();
        boolean isExist = service.checkUsername(username);

        String json = "{\"isExist\":"+isExist+"}";

        response.getWriter().write(json);
    }
    }
