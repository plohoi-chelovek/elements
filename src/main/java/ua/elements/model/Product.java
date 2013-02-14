package ua.elements.model;

import java.util.*;

public class Product {
    private String name;
    private Double price;
    private int count;
    private Date time;

    public Product() {
	this("", 0.0, 0, new Date());
    }

    public Product(String name, Double price, int count, Date time) {
	this.name = name;
	this.price = price;
	this.count = count;
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

    public void setCount(int count) {
	this.count = count;
    }
    
    public int getCount() {
	return count;
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
