/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author ppp
 */
public class Db {

    private static Db instance;
    private DataSource dataSource;
    private String jdni = "java:comp/env/jdbc/customerCrud";
    private Logger logger = Logger.getLogger(getClass().getName());
    
    public static Db getInstance() throws Exception {
        System.out.println("luodaan instanssia");
        if(instance == null) {
            instance = new Db();
        }
        return instance;
    }
    
    private Db() throws Exception {
        dataSource = getDataSource();
    }
    
    private DataSource getDataSource() throws Exception {
        Context context = new InitialContext();
        DataSource dataSource = (DataSource)context.lookup(jdni);
        return dataSource;
    }
    
    public List<Customer> getCustomers() throws Exception {
    
        List<Customer> customers = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            logger.info("Db getCustomers");
            conn = dataSource.getConnection();
            String sql = "SELECT * FROM customer";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while(rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                
                Customer c = new Customer(id, firstName, lastName, email);
                customers.add(c);
            }
            return customers;
        } finally {
            conn.close();
            stmt.close();
            rs.close();
        }
    }
    
    public void addCustomer(Customer c) throws SQLException {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dataSource.getConnection();
            String sql = "INSERT INTO customer(first_name, last_name, email) VALUES(?,?,?)";
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, c.getFirstName());
            stmt.setString(2, c.getLastName());
            stmt.setString(3, c.getEmail());
            
            stmt.execute();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            conn.close();
            stmt.close();
        }
    }
    
    public Customer getCustomer(int customerId) throws Exception {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Customer c = null;
        
        try {
            conn = dataSource.getConnection();
            String sql = "SELECT * FROM customer WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customerId);
            rs = stmt.executeQuery();
            
            if(rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                
                c = new Customer(id, firstName, lastName, email);
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            conn.close();
            stmt.close();
        }
        return c;
    }
    
    public void updateCustomer(Customer c) throws Exception {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        System.out.println("UPDATE CUSTOMER");
        System.out.println(c);
        try {
            logger.log(Level.INFO, c.getFirstName());
            logger.log(Level.INFO, c.getLastName());
            logger.log(Level.INFO, c.getEmail());
            conn = dataSource.getConnection();
            String sql = "UPDATE customer set first_name = ?, last_name = ?, email = ? "
                    + "WHERE id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, c.getFirstName());
            stmt.setString(2, c.getLastName());
            stmt.setString(3, c.getEmail());
            stmt.setInt(4, c.getId());
            stmt.execute();
        } catch(SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            conn.close();
            stmt.close();
        }
    }
    
    public void deleteCustomer(int id) {
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            logger.log(Level.INFO, "DELETING CUSTOMER");
            conn = dataSource.getConnection();
            String sql = "DELETE FROM customer WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
        }catch(SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
