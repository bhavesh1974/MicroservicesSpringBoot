package com.bhaveshshah.productservice.controller;

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

import com.bhaveshshah.productservice.dao.ProductDao;
import com.bhaveshshah.productservice.model.Product;


@RestController
@RequestMapping("/products")
public class ServiceController {
	@Autowired
	ProductDao dao;
	
	@GetMapping("/")
	public ResponseEntity<Object> getAll(HttpServletRequest request){
		List<Product> products = dao.getAll();
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	
	@PostMapping("/")
	public ResponseEntity<Object> insert(@RequestBody Product model) {
		Product savedModel = dao.insert(model);
		
		if (savedModel != null) {
			return new ResponseEntity<>(savedModel, HttpStatus.CREATED);
		}
		
		return new ResponseEntity<>(new String("It cannot be added."), HttpStatus.FORBIDDEN);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> get(@PathVariable("id") Integer id) {
		Product product = dao.get(id);
		if (product != null) {
			return new ResponseEntity<>(product, HttpStatus.OK);
		}
		return new ResponseEntity<>(new String("Resource not found for provided ID."), HttpStatus.NOT_FOUND);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Object> update(@PathVariable("id") Integer id, @RequestBody Product model) {
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
