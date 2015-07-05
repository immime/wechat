package com.immine.wmall.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.immine.wmall.entity.User;

@Repository
public class JpaUserRepository implements IUserRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<User> findAll() {
		return this.entityManager.createQuery("SELECT t FROM User t", User.class).getResultList();
	}

}
