package ua.elements.model;

import java.util.*;

public interface ProductManagementListener extends EventListener {
    public void productInserted(ProductManagementEvent event);
    public void productChargeInserted(ProductManagementEvent event);
}
