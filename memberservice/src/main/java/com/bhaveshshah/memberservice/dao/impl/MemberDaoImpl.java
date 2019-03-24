package com.bhaveshshah.memberservice.dao.impl;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.bhaveshshah.memberservice.dao.MemberDao;
import com.bhaveshshah.memberservice.model.Member;

@Repository
public class MemberDaoImpl extends BaseDaoImpl implements MemberDao {
	@Override
	public Member get(Integer id) {
		List<Member> members = this.getJdbcTemplate().query("select * from member where id = ?", new Object[] {id}, new MemberRowMapper());
		if (members.size() == 0) return null;
		return members.get(0);
	}

	@Override
	public Member insert(Member model) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
	    if (this.getJdbcTemplate().update(connection -> {
	        PreparedStatement ps = connection
	          .prepareStatement("insert into member(name) values (?)", Statement.RETURN_GENERATED_KEYS);
	          ps.setString(1, model.getName());
	          return ps;
	        }, keyHolder) > 0) {

	    	model.setId((Integer) keyHolder.getKey().intValue());
	    	return model;
	    }
		
		return null;
	}

	@Override
	public boolean delete(Integer id) {
		if (this.getJdbcTemplate().update("delete from member where id = ?", new Object[] {id}) > 0) {
			return true; 
		}

		return false;
	}

	@Override
	public List<Member> getAll() {
		List<Member> members = this.getJdbcTemplate().query("select * from member", new MemberRowMapper());
		return members;
	}

	@Override
	public boolean update(Integer id, Member model) {
		if (this.getJdbcTemplate().update("update member set name = ? where id = ?", new Object[] {model.getName(), id}) > 0) {
			return true; 
		}

		return false;
	}

}
