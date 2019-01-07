/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.Serializable;

/**
 *
 * @author Ofir
 */
public class DBConfig implements Serializable {
    private String jdbcDriver;
    private String dbUrl;
    private String user;
    private String password;
    
    public DBConfig(String drv, String url, String usr, String pass) {
        this.jdbcDriver = drv;
        this.dbUrl = url;
        this.user = usr;
        this.password = pass;
    }
    
    public String getJdbcDriver() {
        return this.jdbcDriver;
    }
    
    public void setJdbcDriver(String drv) {
        this.jdbcDriver = drv;
    }
    
    public String getDBUrl() {
        return this.dbUrl;
    }
    
    public void setDBUrl(String url) {
        this.dbUrl = url;
    }
    
    public String getUser() {
        return this.user;
    }
    
    public void setUser(String usr) {
        this.user = usr;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String pass) {
        this.password = pass;
    }
}
