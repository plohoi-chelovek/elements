package ua.elements.model;

import java.util.*;

public interface ServiceManagementListener extends EventListener {
    public void serviceInserted(ServiceManagementEvent event);
}
