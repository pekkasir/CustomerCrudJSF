/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author ppp
 */
@ManagedBean
@SessionScoped
public class Controller {
    
    private List<Customer> customers;
    private Db db;
    private Logger logger = Logger.getLogger(getClass().getName());
    
    public Controller() throws Exception {
        customers = new ArrayList<>();
        db = db.getInstance();
        //logger.info(db.getClass().getName());
    }
    
    public void loadCustomers() {

        customers.clear();
        
        try {
            customers = db.getCustomers();
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Error loading customers", e);
            //System.out.println(e.getMessage());
        }
    }
    
    public String loadCustomer(int id) {
        
        try {
            logger.log(Level.INFO, "Loading Customer");
            Customer customer = db.getCustomer(id);
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            Map<String, Object> rm = ec.getRequestMap();
            rm.put("customer", customer);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        
        return "update.xhtml";
    }
    
    public List<Customer> getCustomers() {
        return customers;
    }
    
    public String addCustomer(Customer c) {
        
        try {
            db.addCustomer(c);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return "index?faces-redirect=true";
    }
    
    public String updateCustomer(Customer c) {
        try {
            db.updateCustomer(c);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return "index?faces-redirect=true";
    }
    
    public String deleteCustomer(int id) {
        
        try {
            db.deleteCustomer(id);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return "index?face-redirect=true";
    }
}
