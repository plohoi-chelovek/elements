package ua.elements.view; 

import java.util.*;

public class ChoiceEvent extends EventObject {
    private String choice;
    
    public ChoiceEvent(Object source, String choice) {
	super(source);
	this.choice = choice;
    }

    public String getChoice() {
	return choice;
    }
}
