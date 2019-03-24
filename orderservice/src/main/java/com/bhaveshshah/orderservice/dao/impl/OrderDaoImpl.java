package com.bhaveshshah.orderservice.dao.impl;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.bhaveshshah.orderservice.dao.OrderDao;
import com.bhaveshshah.orderservice.model.Order;

@Repository
public class OrderDaoImpl extends BaseDaoImpl implements OrderDao {
	@Override
	public Order get(Integer id) {
		List<Order> orders = this.getJdbcTemplate().query("select * from order where id = ?", new Object[] {id}, new OrderRowMapper());
		if (orders.size() == 0) return null;
		return orders.get(0);
	}

	@Override
	public Order insert(Order model) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    if (this.getJdbcTemplate().update(connection -> {
	        PreparedStatement ps = connection
	          .prepareStatement("insert into orders(productId, memberId, qty, rate, note) values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
	          ps.setInt(1, model.getProductId());
	          ps.setInt(2, model.getMemberId());
	          ps.setDouble(3, model.getQty());
	          ps.setDouble(4, model.getRate());
	          //ps.setObject(5, new Date(System.currentTimeMillis()));
	          ps.setString(5, model.getNote());
	          
	          return ps;
	        }, keyHolder) > 0) {

	    	model.setId((Integer) keyHolder.getKey().intValue());
	    	return model;
	    }
		
		return null;
	}

	@Override
	public boolean delete(Integer id) {
		if (this.getJdbcTemplate().update("delete from orders where id = ?", new Object[] {id}) > 0) {
			return true; 
		}

		return false;
	}

	@Override
	public List<Order> getAll() {
		List<Order> orders = this.getJdbcTemplate().query("select * from orders", new OrderRowMapper());
		return orders;
	}

	@Override
	public boolean update(Integer id, Order model) {
		if (this.getJdbcTemplate().update("update orders set productId = ?, qty = ?, rate = ?, note = ? where id = ?", new Object[] {model.getProductId(), model.getQty(), model.getRate(), model.getNote(), id}) > 0) {
			return true; 
		}

		return false;
	}

}
