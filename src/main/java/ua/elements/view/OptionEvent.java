package ua.elements.view; 

import java.util.*;

public class OptionEvent extends EventObject {
    private String option;
    
    public OptionEvent(Object source, String option) {
	super(source);
	this.option = option;
    }

    public String getOption() {
	return option;
    }
}
