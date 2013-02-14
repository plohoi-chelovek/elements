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
	
