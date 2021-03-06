package ua.elements.model;

import java.util.*;
import java.sql.*;

import javax.swing.event.*;

import org.springframework.jdbc.core.*;

public class ProductManagement {
    private DataManagement dm;

    private EventListenerList listeners = new EventListenerList();

    public ProductManagement(DataManagement dm) {
    	this.dm = dm;
    }
    
    public void insert(Product product) {
    	dm.template.update("INSERT INTO product (name, price, count, time) VALUES(?,?,?,?)",
    			   new Object[] {product.getName(), product.getPrice(), product.getCount(), 
    					 product.getTime()});
	fireProductInserted(product);
    }

    public void insertToCharge(Product product) {
    	dm.template.update("INSERT INTO charge (name, count, time) VALUES(?,?,?)",
    			   new Object[] {product.getName(), product.getCount(), product.getTime()});
    	fireProductChargeInserted(product);
    }
    
    public List<Product> selectAll() {
    	return dm.template.query("SELECT name, price, count, time FROM product",
    				 new RowMapper<Product>() {
    				     public Product mapRow(ResultSet rs, int rofwNum) {
    					 try {
    					     return new Product(rs.getString("name"),
    								rs.getDouble("price"),
								rs.getInt("count"),
    								rs.getTimestamp("time"));
    					 } catch (SQLException e) {
    					     throw new RuntimeException(e.getMessage());
    					 }
    				     }
    				 });
    }


    public List<Product> selectAllCharge() {
    	return dm.template.query("SELECT name, count, time FROM charge",
    				 new RowMapper<Product>() {
    				     public Product mapRow(ResultSet rs, int rowNum) {
    					 try {
    					     return new Product(rs.getString("name"),
    								0.0,
								rs.getInt("count"),
								rs.getTimestamp("time"));
    					 } catch (SQLException e) {
    					     throw new RuntimeException(e.getMessage());
    					 }
    				     }
    				 });
    }

    public double[] getValues() {
	GregorianCalendar cal = new GregorianCalendar();
	cal.setTime(new java.util.Date());
	int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	double[] values = new double[days];

	for (int i = 0; i < values.length; i++)
	    values[i] = 0.0;

	for (Product product : selectAll()) {
	    cal.setTime(product.getTime());
	    values[cal.get(Calendar.DAY_OF_MONTH) - 1] += product.getPrice();
	}
	    
	return values;
    }


    public void addProductManagementListener(ProductManagementListener l) {
    	listeners.add(ProductManagementListener.class, l);
    }

    public void removeProductManagementListener(ProductManagementListener l) {
    	listeners.remove(ProductManagementListener.class, l);
    }
    
    private void fireProductInserted(Product product) {
    	ProductManagementEvent event = new ProductManagementEvent(this, product);
    	Object[] l = listeners.getListenerList();
    	for (int i = l.length-2; i>=0; i-=2) {
    	    if (l[i]==ProductManagementListener.class) {
    		((ProductManagementListener)l[i+1]).productInserted(event);
    	    }
    	}
    }

    private void fireProductChargeInserted(Product product) {
    	ProductManagementEvent event = new ProductManagementEvent(this, product);
    	Object[] l = listeners.getListenerList();
    	for (int i = l.length-2; i>=0; i-=2) {
    	    if (l[i]==ProductManagementListener.class) {
    		((ProductManagementListener)l[i+1]).productChargeInserted(event);
    	    }
    	}
    }
}
	
