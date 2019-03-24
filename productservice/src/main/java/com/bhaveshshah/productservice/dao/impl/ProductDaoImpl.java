package com.bhaveshshah.productservice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bhaveshshah.productservice.dao.ProductDao;
import com.bhaveshshah.productservice.model.Product;

@Repository
public class ProductDaoImpl extends BaseDaoImpl implements ProductDao {
	@Override
	public Product get(Integer id) {
		List<Product> products = this.getJdbcTemplate().query("select * from product where id = ?", new Object[] {id}, new ProductRowMapper());
		if (products.size() == 0) return null;
		return products.get(0);
	}

	@Override
	public Product insert(Product model) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    if (this.getJdbcTemplate().update(connection -> {
	        PreparedStatement ps = connection
	          .prepareStatement("insert into product(name, category) values (?,?)", Statement.RETURN_GENERATED_KEYS);
	          ps.setString(1, model.getName());
	          ps.setString(2, model.getCategory());
	          return ps;
	        }, keyHolder) > 0) {

	    	model.setId((Integer) keyHolder.getKey().intValue());
	    	return model;
	    }
		
		return null;
	}

	@Override
	public boolean delete(Integer id) {
		if (this.getJdbcTemplate().update("delete from product where id = ?", new Object[] {id}) > 0) {
			return true; 
		}

		return false;
	}

	@Override
	public List<Product> getAll() {
		List<Product> products = this.getJdbcTemplate().query("select * from product", new ProductRowMapper());
		return products;
	}

	@Override
	public boolean update(Integer id, Product model) {
		if (this.getJdbcTemplate().update("update product set name = ?, category = ? where id = ?", new Object[] {model.getName(), model.getCategory(), id}) > 0) {
			return true; 
		}

		return false;
	}

}
