package com.itheima.web.servlet;

import com.itheima.domain.PageBean;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProductListByCidServlet")
public class ProductListByCidServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductService productService = new ProductService();
//    获取cid,获取currentpage
        String cid = request.getParameter("cid");
        String currentPageStr = request.getParameter("currentPage");

//        如果currentPageStr==null,表示第一次访问,currentPageStr="1"
        if(currentPageStr==null) currentPageStr="1";
        int currentPage = Integer.parseInt(currentPageStr);

//        一页放多少条记录,pagesize
        int currentCount = 12;

//    创建pagebean实例,接收ProductService传递的pagebean
        PageBean<Product> pageBean = new PageBean();
        pageBean = productService.findProductListByCid(cid,currentPage,currentCount);

//    把pagebean存放到request作用域
        request.setAttribute("pageBean",pageBean);
        request.setAttribute("cid",cid);



/*
* 浏览历史*/
//        获取客户端的cookie
        Cookie[] cookies = request.getCookies();
//        创建浏览历史 List
        List<Product> list = new ArrayList<>();
        for (Cookie cookie: cookies){
            if (cookie.getName().equals("pids")){
                String value = cookie.getValue();
                String [] arrays = value.split("-");
                for (String pid :arrays){
//                    System.out.println(pid);
                    list.add(productService.findProductById(pid));
                }
            }
        }
//        for (Product product:list){
//            System.out.println(product.getPimage());
//        }
//        把历史商品放到request作用域
        request.setAttribute("historyList",list);
        //       请求转发
        request.getRequestDispatcher(request.getContextPath()+"/product_list.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request,response);
    }
}
