package com.itheima.dao;

import java.sql.SQLException;
import java.util.List;

import com.itheima.domain.Product;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.itheima.domain.Category;
import com.itheima.utils.DataSourceUtils;

public class ProductDao {



	public List<Category> findAllCategory() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from category";
		List<Category> categoList = queryRunner.query(sql, new BeanListHandler<>(Category.class));
		return categoList;
	}


//	查询热门商品
    public List<Product> findHotProductList() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product where is_hot=? limit ?,?";
		Object[] params = {1,0,9};
		List<Product> productList = queryRunner.query(sql, new BeanListHandler<>(Product.class),params);
		return productList;
    }

//    查询最新商品
	public List<Product> findNewProductList() throws SQLException {
		QueryRunner queryRunner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from product order by pdate desc limit ?,?";
		Object[] params = {0,9};
		List<Product> productList = queryRunner.query(sql, new BeanListHandler<>(Product.class),params);
		return productList;
	}
}
