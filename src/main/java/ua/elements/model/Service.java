package ua.elements.model;

import java.util.*;

public class Service {
    private String name;
    private Double price;
    private Date time;

    public Service() {
	this("", 0.0, new Date());
    }

    public Service(String name, Double price, Date time) {
	this.name = name;
	this.price = price;
	this.time = time;
    }

    public void setName(String name) {
	this.name = name;
    }
    
    public String getName() {
	return name;
    }

    public void setPrice(Double price) {
	this.price = price;
    }
    
    public Double getPrice() {
	return price;
    }

    public void setTime(Date time) {
	this.time = time;
    }
    
    public Date getTime() {
	return time;
    }

    public String toString() {
	return String.format("(%s:%f:%s)", name, price, time);
    }
}
