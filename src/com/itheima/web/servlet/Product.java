package com.itheima.web.servlet;

import com.google.gson.Gson;
import com.itheima.domain.Category;
import com.itheima.domain.PageBean;
import com.itheima.service.ProductService;
import com.itheima.utils.JedisPoolUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "Product")
public class Product extends BaseServlet {

//* 根据id查询商品*/
    public void ProductFindById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService  = new ProductService();
        //获得当前页
        String currentPage = req.getParameter("currentPage");
        //获得商品类别
        String cid = req.getParameter("cid");
//        获取pid
        String pid = req.getParameter("pid");
        com.itheima.domain.Product product = productService.findProductById(pid);


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
                    List<String > list = Arrays.asList(arrays);
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


    /*获取导航栏标签
    * */
    public void CategoryList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从缓存中读取分类信息
        Jedis jedis =null;
        String value =null;
        try{
            jedis = JedisPoolUtils.getJedis();
            value = jedis.get("categoryList");
            if (value == null){
                System.out.println("缓存中没有分类信息");
//		从数据库中读取分类信息，返回分类信息 json串
                ProductService service = new ProductService();
//		查数据库
                List<Category> categoryList = service.findAllCategory();
//		转成Json
                Gson gson = new Gson();
                value = gson.toJson(categoryList);
//       存到jedis中
                jedis.set("categoryList",value);
            }
            resp.setContentType("text/html;charset=UTF-8");
//		回写json
            resp.getWriter().write(value);

        }finally {
            jedis.close();
        }
    }

    public void ProductListByCid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductService productService = new ProductService();
//    获取cid,获取currentpage
        String cid = req.getParameter("cid");
        String currentPageStr = req.getParameter("currentPage");

//        如果currentPageStr==null,表示第一次访问,currentPageStr="1"
        if(currentPageStr==null) currentPageStr="1";
        int currentPage = Integer.parseInt(currentPageStr);

//        一页放多少条记录,pagesize
        int currentCount = 12;

//    创建pagebean实例,接收ProductService传递的pagebean
        PageBean<com.itheima.domain.Product> pageBean = new PageBean();
        pageBean = productService.findProductListByCid(cid,currentPage,currentCount);

//    把pagebean存放到request作用域
        req.setAttribute("pageBean",pageBean);
        req.setAttribute("cid",cid);



/*
* 浏览历史*/
//        获取客户端的cookie
        Cookie[] cookies = req.getCookies();
//        创建浏览历史 List
        List<com.itheima.domain.Product> list = new ArrayList<>();
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
        req.setAttribute("historyList",list);
        //       请求转发
        req.getRequestDispatcher(req.getContextPath()+"/product_list.jsp").forward(req,resp);
    }

//    首页,最热商品和最新商品显示
    public void Index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProductService service = new ProductService();

        //准备热门商品---List<Product>
        List<com.itheima.domain.Product> hotProductList = service.findHotProductList();

        //准备最新商品---List<Product>
        List<com.itheima.domain.Product> newProductList = service.findNewProductList();

        //准备分类数据
        //List<Category> categoryList = service.findAllCategory();

        //request.setAttribute("categoryList", categoryList);
        req.setAttribute("hotProductList", hotProductList);
        req.setAttribute("newProductList", newProductList);

        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    }
