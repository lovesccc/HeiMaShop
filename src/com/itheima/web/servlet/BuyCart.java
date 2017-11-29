package com.itheima.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

import com.itheima.domain.Cart;
import com.itheima.domain.CartItem;
import com.itheima.domain.Product;
import com.itheima.service.ProductService;

//购物车servlet
public class BuyCart extends BaseServlet{

//    添加购物车
public void addToCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        获取要添加的商品pid
    String pid = req.getParameter("pid");
//    获取商品的数量
    int count = Integer.parseInt(req.getParameter("count"));
//    调用业务方法查商品信息
    ProductService  productService = new ProductService();
    Product product = productService.findProductById(pid);
//    计算小计
    double subtotal = product.getShop_price()*count;
//    创建cartItem对象
    CartItem cartItem = new CartItem();
//    封装小计
    cartItem.setSubtotal(subtotal);
//    封装product
    cartItem.setProduct(product);
    //    封装商品数量到购物项
    cartItem.setCount(count);



//        获取Session
    HttpSession httpSession = req.getSession();
//    获得购物车session
    Cart cart = (Cart) httpSession.getAttribute("cart");
    if (cart==null){
        cart = new Cart();
    }

    //将购物项放到车中---key是pid
    //先判断购物车中是否已将包含此购物项了 ----- 判断key是否已经存在
    //如果购物车中已经存在该商品----将现在买的数量与原有的数量进行相加操作

//  获得购物车中的购物项
    Map<String ,CartItem> cartItemMap = cart.getCartItem();
    double newsubTotal = 0;
//    判断当前购物项是否已经在购物车
    if (cartItemMap.containsKey(pid)){
//        购物项在购物车中
//        取出已存在的购物项
        CartItem oldCartItem = cartItemMap.get(pid);
//        取出已存在的购物项的商品数量
        int oldNum = oldCartItem.getCount();
//        取出当前购物项的商品的数量
        int currentNum = cartItem.getCount();
//        修改后的购物项的商品数量
        int newNum = oldNum + currentNum;
        //        把新的购物项数量封装到购物项
        oldCartItem.setCount(newNum);
//        修改购物项的小计
        newsubTotal = currentNum*cartItem.getProduct().getShop_price();
        oldCartItem.setSubtotal(newNum*cartItem.getProduct().getShop_price());
    }
    else {
//        如果购物车中不包含该购物项，添加到购物车中
        cartItemMap.put(pid,cartItem);
        newsubTotal = cartItem.getSubtotal();
    }
//    购物车中总计
    double total = newsubTotal + cart.getTotalPrice();
    cart.setTotalPrice(total);
//    把购物车重新存到session中
    httpSession.setAttribute("cart",cart);
//    跳转到购物车页面cart.jsp
    resp.sendRedirect(req.getContextPath()+"/cart.jsp");
    }

//    清除购物车
    public void delProFromCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession httpSession = req.getSession();
//    清除session
//    httpSession.setAttribute("cart",null);
    httpSession.removeAttribute("cart");
    resp.sendRedirect(req.getContextPath()+"/cart.jsp");

    }
    public void delProInCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//       获取要删除的pid
        String pid = req.getParameter("pid");
        System.out.println(pid);
//        获取session购物车
        HttpSession httpSession = req.getSession();
//        获取购物车对象
        Cart cart = (Cart) httpSession.getAttribute("cart");
        if (cart!=null){
            //        获取购物项
            Map<String,CartItem> cartItem = cart.getCartItem();
//        删除为pid的购物项,并修改购物车的总金额
            double newTotalprice = cart.getTotalPrice() - cartItem.get(pid).getSubtotal();
//      把修改后的商品总金额封装到购物车
            cart.setTotalPrice(newTotalprice);
            cartItem.remove(pid);
        }
        httpSession.setAttribute("cart",cart);

        resp.sendRedirect(req.getContextPath()+"/cart.jsp");
    }
}
