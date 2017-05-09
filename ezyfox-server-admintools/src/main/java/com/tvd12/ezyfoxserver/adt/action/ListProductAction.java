package com.tvd12.ezyfoxserver.adt.action;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.tvd12.ezyfoxserver.adt.dao.ProductDAO;
import com.tvd12.ezyfoxserver.adt.model.Product;

import lombok.Getter;
import lombok.Setter;
 
@Getter
@Setter
public class ListProductAction extends ActionSupport {
	private static final long serialVersionUID = 8858337894805627088L;
	
	private ProductDAO productDAO;
    private List<Product> products;
 
    public String execute() {
    	products = productDAO.list();
        return SUCCESS;
    }
}
