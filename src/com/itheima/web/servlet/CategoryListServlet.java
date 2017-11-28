package com.itheima.web.servlet;
import com.google.gson.Gson;
import com.itheima.domain.Category;
import com.itheima.service.ProductService;
import com.itheima.utils.JedisPoolUtils;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CategoryListServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
