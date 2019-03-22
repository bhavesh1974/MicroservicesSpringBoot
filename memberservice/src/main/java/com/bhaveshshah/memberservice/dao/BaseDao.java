package com.bhaveshshah.memberservice.dao;

import java.util.List;

public interface BaseDao<T> {
	public T get(Integer id);
	public T insert(T model);
	public boolean update(Integer id, T model);
	public boolean delete(Integer id);
	public List<T> getAll();
}
