/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import objects.User;
import objects.Coffee_Shop;

/**
 *
 * @author wlloyd
 */
public class Model {
    static final Logger logger = Logger.getLogger(Model.class.getName());
    private static Model instance;
    private Connection conn;
    
    public static Model singleton() throws Exception {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
    ////blabla
    Model() throws Exception
    {
        Class.forName("org.postgresql.Driver");
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        if ((dbUrl == null) || (dbUrl.length() < 1))
            dbUrl = System.getProperties().getProperty("DBCONN");
        logger.log(Level.INFO, "dbUrl=" + dbUrl);  
        logger.log(Level.INFO, "attempting db connection");
        conn = DriverManager.getConnection(dbUrl);
        logger.log(Level.INFO, "db connection ok.");
    }
    
    private Connection getConnection()
    {
        return conn;
    }
    
    private Statement createStatement() throws SQLException
    {
        Connection conn = getConnection();
        if ((conn != null) && (!conn.isClosed()))
        {
            logger.log(Level.INFO, "attempting statement create");
            Statement st = conn.createStatement();
            logger.log(Level.INFO, "statement created");
            return st;
        }
        else
        {
            // Handle connection failure
        }
        return null;
    }
    
    private PreparedStatement createPreparedStatement(String sql) throws SQLException
    {
        Connection conn = getConnection();
        if ((conn != null) && (!conn.isClosed()))
        {
            logger.log(Level.INFO, "attempting statement create");
            PreparedStatement pst = conn.prepareStatement(sql);
            logger.log(Level.INFO, "prepared statement created");
            return pst;
        }
        else
        {
            // Handle connection failure
        }
        return null;
    }
    
    public int newUser(User usr) throws SQLException
    {
        String sqlInsert="insert into users (name, age) values ('" + usr.getName() + "'" + "," + usr.getAge() + ");";
        Statement s = createStatement();
        logger.log(Level.INFO, "attempting statement execute");
        s.execute(sqlInsert,Statement.RETURN_GENERATED_KEYS);
        logger.log(Level.INFO, "statement executed.  atempting get generated keys");
        ResultSet rs = s.getGeneratedKeys();
        logger.log(Level.INFO, "retrieved keys from statement");
        int userid = -1;
        while (rs.next())
            userid = rs.getInt(3);   // assuming 3rd column is userid
        logger.log(Level.INFO, "The new user id=" + userid);
        return userid;
    }
    
    public void deleteUser(int userid) throws SQLException
    {
        String sqlDelete="delete from users where userid=?";
        PreparedStatement pst = createPreparedStatement(sqlDelete);
        pst.setInt(1, userid);
        pst.execute();
    }
    
    public User[] getUsers() throws SQLException
    {
        LinkedList<User> ll = new LinkedList<User>();
        String sqlQuery ="select * from users;";
        Statement st = createStatement();
        ResultSet rows = st.executeQuery(sqlQuery);
        while (rows.next())
        {
            logger.log(Level.INFO, "Reading row...");
            User usr = new User();
            usr.setName(rows.getString("name"));
            usr.setUserId(rows.getInt("userid"));
            usr.setAge(rows.getInt("age"));
            logger.log(Level.INFO, "Adding user to list with id=" + usr.getUserid());
            ll.add(usr);
        }
        return ll.toArray(new User[ll.size()]);
    }
    
    public boolean updateUser(User usr) throws SQLException
    {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("update users ");
        sqlQuery.append("set name='" + usr.getName() + "', ");
        sqlQuery.append("age=" + usr.getAge() + " ");
        sqlQuery.append("where userid=" + usr.getUserid() + ";");
        Statement st = createStatement();
        logger.log(Level.INFO, "UPDATE SQL=" + sqlQuery.toString());
        return st.execute(sqlQuery.toString());
    }
    
    public int newCoffeeShop(Coffee_Shop cs) throws SQLException
    {
        String sqlInsert="insert into shops (cid, name, address, rawreview, phone, getUrl) values ('" + cs.getCid() 
                + "'" + "," + cs.getCoffeeName() + "'" + "," + cs.getCoffeeAddress()
                + "'" + "," + cs.getRawReview() + "'" + "," + cs.getPhone()
                + "'" + "," + cs.getUrl() +");";
        Statement s = createStatement();
        logger.log(Level.INFO, "attempting statement execute");
        s.execute(sqlInsert,Statement.RETURN_GENERATED_KEYS);
        logger.log(Level.INFO, "statement executed.  atempting get generated keys");
        ResultSet rs = s.getGeneratedKeys();
        logger.log(Level.INFO, "retrieved keys from statement");
        int shopid = -1;
        while (rs.next())
            shopid = rs.getInt(3);   // assuming 3rd column is userid
        logger.log(Level.INFO, "The new shop id=" + shopid);
        return shopid;
    }
    
    public Coffee_Shop[] getCoffeeShops() throws SQLException {
        LinkedList<Coffee_Shop> ll = new LinkedList<Coffee_Shop>();
        String sqlQuery ="select * from shops;";
        Statement st = createStatement();
        ResultSet rows = st.executeQuery(sqlQuery);
        while (rows.next())
        {
            logger.log(Level.INFO, "Reading row...");
            Coffee_Shop cs = new Coffee_Shop();
            cs.setCid(rows.getInt("cid"));
            cs.setCoffeeName(rows.getString("coffeeName"));
            cs.setCoffeeAddress(rows.getString("coffeeAddress"));
            cs.setRawReview(rows.getInt("rawReview"));
            cs.setPhone(rows.getString("phone"));
            cs.setUrl(rows.getString("url"));
            logger.log(Level.INFO, "Adding shop to list with id=" + cs.getCid());
            ll.add(cs);
        }
        return ll.toArray(new Coffee_Shop[ll.size()]);
    }
    
    public Coffee_Shop[] getCS() throws SQLException
   {
       LinkedList<Coffee_Shop> ll = new LinkedList<Coffee_Shop>();
       String sqlQuery ="select * from shops;";
       Statement st = createStatement();
       ResultSet rows = st.executeQuery(sqlQuery);
       while (rows.next())
       {
           logger.log(Level.INFO, "Reading row...");
           Coffee_Shop cs = new Coffee_Shop();
           cs.setCoffeeName(rows.getString("coffeeName"));
           cs.setCid(rows.getInt("cid"));
           cs.setCoffeeAddress(rows.getString("coffeeAddress"));
           cs.setRawReview(rows.getInt("rawReview"));
           cs.setPhone(rows.getString("phone"));
           cs.setUrl(rows.getString("url"));
           logger.log(Level.INFO, "Adding shop to list with id=" + cs.getCid());
           ll.add(cs);
       }
       return ll.toArray(new Coffee_Shop[ll.size()]);
   }
    public boolean updateCoffeShop(Coffee_Shop cs) {
        return false;
    }
    
    public boolean updateCoffeeShop(Coffee_Shop cs) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("update shops ");
        sqlQuery.append("cid='" + cs.getCid() + "', ");
        sqlQuery.append("coffeeName=" + cs.getCoffeeName() + " ");
        sqlQuery.append("coffeeAddress=" + cs.getCoffeeAddress() + ",");
        sqlQuery.append("rawReview=" + cs.getRawReview() + ",");
        sqlQuery.append("phone=" + cs.getPhone() + ",");
        sqlQuery.append("url=" + cs.getUrl() + ";");
        Statement st = createStatement();
        logger.log(Level.INFO, "UPDATE SQL=" + sqlQuery.toString());
        return st.execute(sqlQuery.toString());
        
    }
    
    
    
}
