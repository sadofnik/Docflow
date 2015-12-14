
package org.doverie.docflow;


import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class MySQLConnect {
    private Connection connect = null;
    private final String uName;
    private final String uPassword;
    private final String sName;
    private final String dbName;
    private final String pNumber;
    
    public MySQLConnect(String serverName, String databaseName, String portNumber, String userName, String password){
        this.uName=userName;
        this.uPassword=password;
        this.sName=serverName;
        this.dbName=databaseName;
        this.pNumber=portNumber;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            
            Properties properties=new Properties();
            properties.setProperty("user",this.uName);
            properties.setProperty("password",this.uPassword);
            properties.setProperty("useUnicode","true");
            properties.setProperty("characterEncoding","UTF-8");
            //+"?user="+this.uName+"&password="+this.uPassword
            
            connect = DriverManager.getConnection("jdbc:mysql://"+this.sName+":"+this.pNumber+"/"+this.dbName,properties);
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("Connection Db Error");
        }
        
    }
    public Connection getConnection(){
        return this.connect;
    }
}
