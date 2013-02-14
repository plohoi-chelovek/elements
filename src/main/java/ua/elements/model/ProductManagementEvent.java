package ua.elements.model; 

import java.util.*;

public class ProductManagementEvent extends EventObject {
    private Product product;
    
    public ProductManagementEvent(Object source, Product product) {
	super(source);
	this.product = product;
    }

    public Product getProduct() {
	return product;
    }
}
