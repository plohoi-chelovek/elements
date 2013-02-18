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

    public List<Product> selectByMonth(int year, int month) {
	Calendar cal = new GregorianCalendar(year, month - 1, 1);
	java.util.Date begin = cal.getTime();
	cal.add(Calendar.MONTH, 1);
	java.util.Date end = cal.getTime();

	return dm.template.query("SELECT name, price, count, time FROM product " +
				 "WHERE time BETWEEN ? AND ?",
				 new Object[]{begin, end},
				 new int[]{Types.TIMESTAMP, Types.TIMESTAMP},
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

    /* TESTING */
    public static void main(String[] args) {
	DataManagement dataManagement = new DataManagement();
	ProductManagement pm = dataManagement.getProductManagement();
	System.out.println(pm.selectByMonth(2013, 2));
    }
}
	
