package activeMysql;

import java.io.IOException;
import java.sql.ResultSet;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class ActiveMySQL {
	private String url;
	private String name;
	private String user;
	private String password;
	
	private DBConnect dbConnect;

	private String sql;

	/**
	 * 无参构造函数
	 */
	public ActiveMySQL() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 有参构造函数
	 * @param url
	 * @param name
	 * @param user
	 * @param password
	 */
	public ActiveMySQL(String url, String name, String user, String password) {
		this.url = url;
		this.name = name;
		this.user = user;
		this.password = password;
		dbConnect = new DBConnect(url, name, user, password);
	}


	/**
	 * 作用：连接数据库
	 */
	public void connectDB(){
		dbConnect.connect();
	}
	
	/**
	 * 作用：断开数据库
	 */
	public void disconnectDB(){
		dbConnect.close();
	}
	
	/**
	 * 执行查询语句
	 * @param sql
	 * @return 查询结果集
	 */
	public ResultSet executeQuerySQL(String sql){
		return dbConnect.executeQuerySQL(sql);
	}
	
	/**
	 * 执行一般SQL语句
	 * @param sql
	 */
	public void executeSQL(String sql){
		dbConnect.executeSQL(sql);
	}


	/**
	 * 作用：根据配置文件创建相应的数据库表
	 */
	public void createTable(){
		TableGenerate tableGenerate = null;
		//1.连接数据库
		connectDB();
		
        //2.读取配置文件
		//获取一个SAXParserFactory的实例对象
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //通过factory的newSAXParser()方法获取一个SAXParser类的对象。
        try {
            SAXParser parser = factory.newSAXParser();
            //创建SAXParserHandler对象
            tableGenerate = new TableGenerate();
            parser.parse("config.xml", tableGenerate);
            
            //3.执行sql语句
            sql = tableGenerate.getSql();
            executeSQL(sql);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //4.关闭数据库
            disconnectDB();

        }
        
     }

	
	/**
	 * 作用：把对象实体存储在数据库中
	 * @param obj
	 */
	public void saveEntityToDB(Object obj){
		ORM_EntityToDB myOrm = new ORM_EntityToDB(obj);
		//1.连接数据库
		connectDB();
		
        //2.读取配置文件
		//获取一个SAXParserFactory的实例对象
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //通过factory的newSAXParser()方法获取一个SAXParser类的对象。
        try {
            SAXParser parser = factory.newSAXParser();
            //创建SAXParserHandler对象
            parser.parse("config.xml", myOrm);
            
            //3.执行sql语句
            sql = myOrm.getSql();
            executeSQL(sql);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //4.关闭数据库
            disconnectDB();

        }
	}
	
	/**
	 * 根据主键key值从数据库中读取信息并返回一个实体对象
	 * @param key
	 * @return
	 */
	public Object generateEntityFromDB(String key){
		Object obj = null;
		DBToEntitySQL dbSql = new DBToEntitySQL(key);
		ORM_DBToEntity myORM;
		//1.连接数据库
		connectDB();
		
        //2.读取配置文件
		//获取一个SAXParserFactory的实例对象
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //通过factory的newSAXParser()方法获取一个SAXParser类的对象。
        try {
            SAXParser parser = factory.newSAXParser();
            //第一次读配置文件，生成查询语句
            parser.parse("config.xml", dbSql);
            
            //3.执行sql语句
            sql = dbSql.getSql();
            ResultSet rs = executeQuerySQL(sql);
            
            //4.第二次读配置文件，生成相应的对象，并赋值
            myORM = new ORM_DBToEntity(rs);
            parser.parse("config.xml", myORM);
            obj = myORM.getObj();
    		return obj;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //5.关闭数据库
            disconnectDB();
    		return obj;
        }
	}
	
}
