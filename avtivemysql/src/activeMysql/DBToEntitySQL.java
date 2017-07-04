package activeMysql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * 该类用于将数据从数据库表中读取到实体对象里(仅一条数据)
 * 1.给定key值，查找数据库，找出数据
 * 2.根据配置文件，确定是哪个类
 * 3.根据配置文件的id，确定其属性名，从而得到方法名
 * 4.根据反射机制，调用set方法设置属性值
 * @author Mr_Song
 */
public class DBToEntitySQL  extends DefaultHandler{
	String value = null;
	String value_table;
	String sql = "";
	ResultSet rs = null;
	String key;
	
	//反射机制
	Class<?> clazz;
	Method method;

	//模板类
	Object obj = null;
	/**
	 * 构造方法，传入实体类的对象
	 * @param obj
	 */
	public DBToEntitySQL(String key) {
		super();
		this.key = key;
	}

	public String getSql(){
		return sql;
	}

    /**
     * 该方法将字符串的首字母变为大写
     * @param s
     * @return 首字母大写的字符串
     */
    public static String toUpperCaseFirstOne(String s){
    	  if(Character.isUpperCase(s.charAt(0)))
    	    return s;
    	  else
    	    return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
   	}

    /**
     * 用来标识解析开始
     */
    public void startDocument() throws SAXException {
        super.startDocument();
        System.out.println("SAX解析开始");

    }
    
    /**
     * 用来标识解析结束
     */
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("SAX解析结束");
    }

    /**
     * 用来遍历xml文件的开始标签
     * 解析xml元素
     */
    public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
        //调用DefaultHandler类的startElement方法
        super.startElement(uri, localName, qName, attributes);
        try {
 	        if (qName.equals("myclass")) {
	            System.out.println("========读取数据生成SQL=====开始遍历xml=================");
	           //解析myclass的name属性，找到对应的类，创建其对象
	        	
	           //已知节点的属性名时：根据属性名称获取属性值
	            //获取类名
	            String value_class = attributes.getValue("name");
	            System.out.println("实体类名是："+value_class);
	            
	            //获取该类的class对象
	            clazz = Class.forName(value_class);
	            //获取表名
	            value_table = attributes.getValue("table");
	            System.out.println("表名是："+value_table);
	
	        }else if(qName.equals("id")){
	            //获取属性名
	            String value_id = attributes.getValue("name");
	            System.out.println("id的属性name的值是："+value_id);
//	            //得到方法名
//	            String method_getId = "get"+toUpperCaseFirstOne(value_id);
//	            //声明Method对象
//	            method = clazz.getMethod(method_getId);
//	            //调用get方法
//	            value = (String)method.invoke(obj);
	            //写入sql
		        sql = "select * from "+value_table+" where "+value_id+" = "+key;
	            System.out.println("\nSQL为："+sql);
	           
	            return;
	        }
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

    }

 
    /**
     * 用来遍历xml文件的结束标签
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //调用DefaultHandler类的endElement方法
        super.endElement(uri, localName, qName);
        //判断是否针对一本书已经遍历结束
        if (qName.equals("myclass")) {
            System.out.println("\n=========读取数据生成SQL=====结束遍历=================");

        }
    }


   
}
