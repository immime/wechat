package com.immine.wmall.repository;

import java.util.List;

import com.immine.wmall.entity.User;

public interface IUserRepository {
	
	List<User> findAll();
	
}
