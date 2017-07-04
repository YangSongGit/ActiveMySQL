package activeMysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {
	private String url;
	private String name;
	private String user;
	private String password;
	
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	
	public DBConnect(String url, String name, String user, String password) {
		this.url = url;
		this.name = name;
		this.user = user;
		this.password = password;
	}
	
	public void connect(){
		try {  
            Class.forName(name);//指定连接类型  
            conn = DriverManager.getConnection(url, user, password);//获取连接  
			System.out.println("连接数据库成功");

        } catch (Exception e) {  
            e.printStackTrace();  
        } 

	}


	public ResultSet executeQuerySQL(String sql){
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("执行sql失败");
			e.printStackTrace();
		}
		return rs;
	}
	
	public void executeSQL(String sql){
		try {
			stmt = conn.createStatement();
		    stmt.execute(sql);
		    
		    conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("执行sql语句失败");
			e.printStackTrace();
		}

	}
	
	public void close() {  
        try {  
        	if(this.rs != null){
                this.rs.close();  
        	}
        	if(this.stmt != null){
                this.stmt.close();  
        	}
        	if(this.conn != null){
                this.conn.close();  
        	}
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
	}  

	
}
