package com.bhaveshshah.orderservice.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.bhaveshshah.orderservice.model.Order;

public class OrderRowMapper implements RowMapper<Order> {

	@Override
	public Order mapRow(ResultSet rs, int record) throws SQLException {
		Order order = new Order();
		order.setId(rs.getInt("id"));
		order.setProductId(rs.getInt("productId"));
		order.setMemberId(rs.getInt("memberId"));
		order.setQty(rs.getDouble("qty"));
		order.setRate(rs.getDouble("rate"));
		order.setNote(rs.getString("note"));
		order.setOrderDate(rs.getDate("orderDate"));
		return order;
	}

}
