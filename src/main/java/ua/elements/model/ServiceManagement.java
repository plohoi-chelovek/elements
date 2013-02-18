package ua.elements.model;

import java.util.*;
import java.sql.*;

import javax.swing.event.*;

import org.springframework.jdbc.core.*;

public class ServiceManagement {
    private DataManagement dm;

    private EventListenerList listeners = new EventListenerList();

    public ServiceManagement(DataManagement dm) {
    	this.dm = dm;
    }
    
    public void insert(Service service) {
    	dm.template.update("INSERT INTO service (name, price, time) VALUES(?,?,?)",
    			   new Object[] {service.getName(), service.getPrice(), 
    					 service.getTime()});
	fireServiceInserted(service);
    }

    // public void insertToCharge(Service product) {
    // 	dm.template.update("INSERT INTO charge (name, count, time) VALUES(?,?,?)",
    // 			   new Object[] {product.getName(), product.getCount(), product.getTime()});
    // 	fireServiceChargeInserted(product);
    // }
    
    public List<Service> selectAll() {
    	return dm.template.query("SELECT name, price, time FROM service",
    				 new RowMapper<Service>() {
    				     public Service mapRow(ResultSet rs, int rofwNum) {
    					 try {
    					     return new Service(rs.getString("name"),
    								rs.getDouble("price"),
    								rs.getTimestamp("time"));
    					 } catch (SQLException e) {
    					     throw new RuntimeException(e.getMessage());
    					 }
    				     }
    				 });
    }

    public List<Service> selectByMonth(int year, int month) {
	Calendar cal = new GregorianCalendar(year, month - 1, 1);
	java.util.Date begin = cal.getTime();
	cal.add(Calendar.MONTH, 1);
	java.util.Date end = cal.getTime();

	return dm.template.query("SELECT name, price, time FROM service " +
				 "WHERE time BETWEEN ? AND ? " +
				 "ORDER BY time DESC",
				 new Object[]{begin, end},
				 new int[]{Types.TIMESTAMP, Types.TIMESTAMP},
    				 new RowMapper<Service>() {
    				     public Service mapRow(ResultSet rs, int rofwNum) {
    					 try {
					     return new Service(rs.getString("name"),
    								rs.getDouble("price"),
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

	for (Service service : selectByMonth(year, month)) {
	    cal.setTime(service.getTime());
	    values[cal.get(Calendar.DAY_OF_MONTH) - 1] += service.getPrice();
	}

	return values;
    }


    public void addServiceManagementListener(ServiceManagementListener l) {
    	listeners.add(ServiceManagementListener.class, l);
    }

    public void removeServiceManagementListener(ServiceManagementListener l) {
    	listeners.remove(ServiceManagementListener.class, l);
    }
    
    private void fireServiceInserted(Service service) {
    	ServiceManagementEvent event = new ServiceManagementEvent(this, service);
    	Object[] l = listeners.getListenerList();
    	for (int i = l.length-2; i>=0; i-=2) {
    	    if (l[i]==ServiceManagementListener.class) {
    		((ServiceManagementListener)l[i+1]).serviceInserted(event);
    	    }
    	}
    }

      /* TESTING */
    public static void main(String[] args) {
    	ServiceManagement sm = new DataManagement().getServiceManagement();
    	System.out.println(sm.selectAll());
    	sm.insert(new Service("Прокладка сети", 120.0, new java.util.Date()));
    }
}
	
