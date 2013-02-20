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

    public boolean isProductNameExist(String name) {
	return dm.template.queryForInt("SELECT COUNT(*) FROM product WHERE " +
				       "name = ?", new Object[]{name}) > 0;
    }
    
    public void insert(Product product) {
    	dm.template.update("INSERT INTO product (name, price, count, time) VALUES(?,?,?,?)",
    			   new Object[] {product.getName(), product.getPrice(), product.getCount(), 
    					 product.getTime()});
	fireProductInserted(product);
    }

    public boolean insertToCharge(Product product) {
	if (isProductNameExist(product.getName())) {
	    dm.template.update("INSERT INTO charge (name, count, time) VALUES(?,?,?)",
			       new Object[] {product.getName(), product.getCount(), 
					     product.getTime()});
	    fireProductChargeInserted(product);
	    return true;
	} else {
	    return false;
	}
    }

    public String getHint(String text) {
	List<Product> products = 
	    dm.template.query("SELECT name, price, count, time FROM product " +
			      "WHERE name LIKE ? " +
			      "ORDER BY time DESC",
			      new Object[]{text+"_%"},
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
	if (products.size() > 0) {
	    int min = products.get(0).getName().length();
	    int index = 0;
	    for (int i = 1; i < products.size(); i++)
		if (products.get(i).getName().length() < min) {
		    min = products.get(i).getName().length();
		    index = i;
		}
	    return products.get(index).getName();
	} else {
	    return null;
	}
    }
	
    
    public List<Product> selectAll() {
    	return dm.template.query("SELECT name, price, count, time FROM product " +
				 "ORDER BY time DESC",
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
				 "WHERE time BETWEEN ? AND ? " +
				 "ORDER BY time DESC",
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

    public List<Product> selectChargeByMonth(int year, int month) {
	Calendar cal = new GregorianCalendar(year, month - 1, 1);
	java.util.Date begin = cal.getTime();
	cal.add(Calendar.MONTH, 1);
	java.util.Date end = cal.getTime();

	return dm.template.query("SELECT name, count, time FROM charge " +
				 "WHERE time BETWEEN ? AND ? " +
				 "ORDER BY time DESC",
				 new Object[]{begin, end},
				 new int[]{Types.TIMESTAMP, Types.TIMESTAMP},
    				 new RowMapper<Product>() {
    				     public Product mapRow(ResultSet rs, int rofwNum) {
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

    public double[] getValues(int year, int month) {
	GregorianCalendar cal = new GregorianCalendar(year, month - 1, 1);

	double[] values = new double[cal.getActualMaximum(Calendar.DAY_OF_MONTH)];
	for (int i = 0; i < values.length; i++)
	    values[i] = 0.0;

	for (Product product : selectByMonth(year, month)) {
	    cal.setTime(product.getTime());
	    values[cal.get(Calendar.DAY_OF_MONTH) - 1] += product.getPrice() * product.getCount();
	}
	return values;
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
	System.out.println(Arrays.toString(pm.getValues(2013, 2)));
    }
}
	
