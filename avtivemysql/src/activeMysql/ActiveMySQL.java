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
	 * �޲ι��캯��
	 */
	public ActiveMySQL() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * �вι��캯��
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
	 * ���ã��������ݿ�
	 */
	public void connectDB(){
		dbConnect.connect();
	}
	
	/**
	 * ���ã��Ͽ����ݿ�
	 */
	public void disconnectDB(){
		dbConnect.close();
	}
	
	/**
	 * ִ�в�ѯ���
	 * @param sql
	 * @return ��ѯ�����
	 */
	public ResultSet executeQuerySQL(String sql){
		return dbConnect.executeQuerySQL(sql);
	}
	
	/**
	 * ִ��һ��SQL���
	 * @param sql
	 */
	public void executeSQL(String sql){
		dbConnect.executeSQL(sql);
	}


	/**
	 * ���ã����������ļ�������Ӧ�����ݿ��
	 */
	public void createTable(){
		TableGenerate tableGenerate = null;
		//1.�������ݿ�
		connectDB();
		
        //2.��ȡ�����ļ�
		//��ȡһ��SAXParserFactory��ʵ������
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //ͨ��factory��newSAXParser()������ȡһ��SAXParser��Ķ���
        try {
            SAXParser parser = factory.newSAXParser();
            //����SAXParserHandler����
            tableGenerate = new TableGenerate();
            parser.parse("config.xml", tableGenerate);
            
            //3.ִ��sql���
            sql = tableGenerate.getSql();
            executeSQL(sql);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //4.�ر����ݿ�
            disconnectDB();

        }
        
     }

	
	/**
	 * ���ã��Ѷ���ʵ��洢�����ݿ���
	 * @param obj
	 */
	public void saveEntityToDB(Object obj){
		ORM_EntityToDB myOrm = new ORM_EntityToDB(obj);
		//1.�������ݿ�
		connectDB();
		
        //2.��ȡ�����ļ�
		//��ȡһ��SAXParserFactory��ʵ������
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //ͨ��factory��newSAXParser()������ȡһ��SAXParser��Ķ���
        try {
            SAXParser parser = factory.newSAXParser();
            //����SAXParserHandler����
            parser.parse("config.xml", myOrm);
            
            //3.ִ��sql���
            sql = myOrm.getSql();
            executeSQL(sql);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //4.�ر����ݿ�
            disconnectDB();

        }
	}
	
	/**
	 * ��������keyֵ�����ݿ��ж�ȡ��Ϣ������һ��ʵ�����
	 * @param key
	 * @return
	 */
	public Object generateEntityFromDB(String key){
		Object obj = null;
		DBToEntitySQL dbSql = new DBToEntitySQL(key);
		ORM_DBToEntity myORM;
		//1.�������ݿ�
		connectDB();
		
        //2.��ȡ�����ļ�
		//��ȡһ��SAXParserFactory��ʵ������
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //ͨ��factory��newSAXParser()������ȡһ��SAXParser��Ķ���
        try {
            SAXParser parser = factory.newSAXParser();
            //��һ�ζ������ļ������ɲ�ѯ���
            parser.parse("config.xml", dbSql);
            
            //3.ִ��sql���
            sql = dbSql.getSql();
            ResultSet rs = executeQuerySQL(sql);
            
            //4.�ڶ��ζ������ļ���������Ӧ�Ķ��󣬲���ֵ
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
            //5.�ر����ݿ�
            disconnectDB();
    		return obj;
        }
	}
	
}
