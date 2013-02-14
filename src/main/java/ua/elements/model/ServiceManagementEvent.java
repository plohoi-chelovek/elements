package ua.elements.model; 

import java.util.*;

public class ServiceManagementEvent extends EventObject {
    private Service service;
    
    public ServiceManagementEvent(Object source, Service service) {
	super(source);
	this.service = service;
    }

    public Service getService() {
	return service;
    }
}
