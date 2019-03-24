package com.bhaveshshah.orderservice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhaveshshah.orderservice.dao.OrderDao;
import com.bhaveshshah.orderservice.model.Order;


@RestController
@RequestMapping("/orders")
public class ServiceController {
	@Autowired
	OrderDao dao;
	
	@GetMapping("/")
	public ResponseEntity<Object> getAll(HttpServletRequest request){
		List<Order> orders = dao.getAll();
		return new ResponseEntity<>(orders,HttpStatus.OK);
	}
	
	@PostMapping("/")
	public ResponseEntity<Object> insert(@RequestBody Order model, HttpServletRequest request) {
		if (CallAPI.call("http://localhost:3000/members/" + model.getMemberId(), "GET", request) == "HTTP_NOT_FOUND") {
			return new ResponseEntity<>(new String("Member Id is not valid."), HttpStatus.FORBIDDEN);
		}
		
		if (CallAPI.call("http://localhost:3000/products/" + model.getProductId(), "GET", request) == "HTTP_NOT_FOUND") {
			return new ResponseEntity<>(new String("Product Id is not valid."), HttpStatus.FORBIDDEN);
		}

		Order savedModel = dao.insert(model);
		if (savedModel != null) {
			return new ResponseEntity<>(savedModel, HttpStatus.CREATED);
		}
		
		return new ResponseEntity<>(new String("It cannot be added."), HttpStatus.FORBIDDEN);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> get(@PathVariable("id") Integer id) {
		Order order = dao.get(id);
		if (order != null) {
			return new ResponseEntity<>(order, HttpStatus.OK);
		}
		return new ResponseEntity<>(new String("Resource not found for provided ID."), HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> update(@PathVariable("id") Integer id, @RequestBody Order model) {
		if (dao.update(id, model) == true) {
			return new ResponseEntity<>(new String("It is successfully updated."), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(new String("It cannot be updated"), HttpStatus.FORBIDDEN);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable("id") Integer id) {
		if (dao.delete(id) == true) {
			return new ResponseEntity<>(new String("It is successfully deleted."), HttpStatus.OK);
		}
		
		return new ResponseEntity<>(new String("It cannot be deleted."), HttpStatus.FORBIDDEN);
	}
	
}
