package com.itheima.service;

import java.sql.SQLException;
import java.util.List;

import com.itheima.dao.ProductDao;
import com.itheima.domain.Category;
//import com.itheima.domain.PageBean;
import com.itheima.domain.PageBean;
import com.itheima.domain.Product;

public class ProductService {

	//获得热门商品
	public List<Product> findHotProductList() {

		ProductDao dao = new ProductDao();
		List<Product> hotProductList = null;
		try {
			hotProductList = dao.findHotProductList();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询热门商品失败"+e);
		}
		return hotProductList;

	}

	//获得最新商品
	public List<Product> findNewProductList() {
		ProductDao dao = new ProductDao();
		List<Product> newProductList = null;
		try {
			newProductList = dao.findNewProductList();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询最新商品失败"+e);
		}
		return newProductList;
	}

	public List<Category> findAllCategory() {
		ProductDao dao = new ProductDao();
		List<Category> categoryList = null;
		try {
			categoryList = dao.findAllCategory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categoryList;
	}

//	根据pid查询商品信息
    public Product findProductById(String pid) {
		ProductDao productDao = new ProductDao();
		try {
			return productDao.findProductById(pid);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("根据pid查询商品信息失败"+e);
		}

	}

//
	public PageBean findProductListByCid(String cid, int currentPage, int currentCount) {
		ProductDao productDao = new ProductDao();
		PageBean<Product> pageBean = new PageBean();
//		获取当前页,封装到pagebean
		pageBean.setCurrentPage(currentPage);

//		获取每页记录条数,pagesize,封装到pagebean
		pageBean.setCurrentCount(currentCount);

//		获取总记录数,封装到pagebean
		int cout = 0;
		try {
			cout = productDao.getCount(cid);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("根据cid查询商品总记录数失败"+e);
		}
		pageBean.setTotalCount(cout);

//		获取总页数,封装到pagebean
		int totalpage = (int) Math.ceil(1.0*cout/currentCount);
		pageBean.setTotalPage(totalpage);

//		计算startIndex
		//当前页显示的数据
		// select * from product where cid=? limit ?,?
		// 当前页与起始索引index的关系
		int startIndex = (currentPage-1)*currentCount;
//		list<product> 返回查询的商品列表,封装到pagebean
		List<Product> list= null;
		try {
			list = productDao.findProductListByCid(cid,startIndex,currentCount);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("分类查询商品分页失败"+e);
		}

		pageBean.setList(list);
		return pageBean;


	}

//	public PageBean findProductListByCid(String cid,int currentPage,int currentCount) {
//
//		ProductDao dao = new ProductDao();
//
//		//封装一个PageBean 返回web层
//		PageBean<Product> pageBean = new PageBean<Product>();
//
//		//1、封装当前页
//		pageBean.setCurrentPage(currentPage);
//		//2、封装每页显示的条数
//		pageBean.setCurrentCount(currentCount);
//		//3、封装总条数
//		int totalCount = 0;
//		try {
//			totalCount = dao.getCount(cid);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		pageBean.setTotalCount(totalCount);
//		//4、封装总页数
//		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
//		pageBean.setTotalPage(totalPage);
//
//		//5、当前页显示的数据
//		// select * from product where cid=? limit ?,?
//		// 当前页与起始索引index的关系
//		int index = (currentPage-1)*currentCount;
//		List<Product> list = null;
//		try {
//			list = dao.findProductByPage(cid,index,currentCount);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		pageBean.setList(list);
//
//
//		return pageBean;
//	}
//
//	public Product findProductByPid(String pid) {
//		ProductDao dao = new ProductDao();
//		Product product = null;
//		try {
//			product = dao.findProductByPid(pid);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return product;
//	}

}
