package com.tvd12.ezyfoxserver.adt.dao.impl;

/** 
 * Copyright CodeJava.net To Present
 * All rights reserved. 
 */
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.tvd12.ezyfoxserver.adt.dao.ProductDAO;
import com.tvd12.ezyfoxserver.adt.model.Product;
 
public class ProductDAOImpl implements ProductDAO {
	
    private SessionFactory sessionFactory;
 
    public ProductDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
 
    @Transactional
	@Override
    public List<Product> list() {
        CriteriaQuery<Product> query = 
        		createQuery().distinct(true);
        query.from(Product.class);
        return getCurrentSession().createQuery(query).list();
    }
    
    private Session getCurrentSession() {
    	return sessionFactory.getCurrentSession();
    }
    
    private CriteriaBuilder getCriteriaBuilder() {
    	return getCurrentSession().getCriteriaBuilder();
    }
    
    private CriteriaQuery<Product> createQuery() {
    	return getCriteriaBuilder().createQuery(Product.class);
    }
}
