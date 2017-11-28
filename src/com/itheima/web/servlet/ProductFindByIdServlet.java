package com.itheima.web.servlet;

import com.itheima.domain.Product;
import com.itheima.service.ProductService;
import com.mchange.v1.util.ArrayUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
* 根据id查询商品*/
public class ProductFindByIdServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService  = new ProductService();
        //获得当前页
        String currentPage = req.getParameter("currentPage");
        //获得商品类别
        String cid = req.getParameter("cid");
//        获取pid
        String pid = req.getParameter("pid");
        Product product = productService.findProductById(pid);


//        把查询到的商品信息，放到request作用域
        req.setAttribute("product",product);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("cid", cid);


        /*记录浏览的商品,使用cookie存储pid
* */
//        获取客户端的cookie[]
        String pids = pid;
        Cookie[] cookies = req.getCookies();
        if (cookies!=null){
            for (Cookie cookie:cookies){
                if (cookie.getName().equals("pids")){
                    String value = cookie.getValue();
                    String[] arrays = value.split("-");
                    List<String > list =Arrays.asList(arrays);
                    LinkedList<String > linkedList = new LinkedList<>(list);
//                    判断是否包含该pid
                    if (linkedList.contains(pid)){
                        linkedList.remove(pid);
                        linkedList.addFirst(pid);
                    }
//                    不包含,直接放到头部
                    else {
                        linkedList.addFirst(pid);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i=0;i<linkedList.size()&&i<7;i++){
                        stringBuilder.append(linkedList.get(i));
                            stringBuilder.append("-");
                    }
                    pids = stringBuilder.toString();
                    pids = pids.substring(0,stringBuilder.length()-1);
                }
            }
        }
        Cookie cookie  = new Cookie("pids",pids);
        resp.addCookie(cookie);
//        System.out.println("cookie"+pids);
        /*  cookie34-32-31-3
            cookie1-34-32-31-3
            cookie11-1-34-32-31-3
            cookie10-11-1-34-32-31-3
            cookie14-10-11-1-34-32-31*/
        req.getRequestDispatcher("/product_info.jsp").forward(req,resp);



    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
