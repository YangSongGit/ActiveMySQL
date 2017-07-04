package activeMysql;

import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * 该类用于将实体对象存储在数据库表中
 * 1.根据配置文件，确定是哪个类
 * 2.根据配置文件的id，确定其属性名，从而得到方法名
 * 3.根据反射机制，调用方法得到属性值，得到sql语句
 * 4.连接数据库，写入数据
 * @author Mr_Song
 *
 */
public class ORM_EntityToDB extends DefaultHandler {
	String value = null;
	String value_class;
	String sql = "";
	
	//反射机制
	Class<?> clazz;
	Method method;

	//模板类
	Object obj = null;
	/**
	 * 构造方法，传入实体类的对象
	 * @param obj
	 */
	public ORM_EntityToDB(Object obj) {
		super();
		this.obj = obj;
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
	            System.out.println("========写入数据之=====开始遍历xml=================");
	           //解析myclass的name属性，找到对应的类，创建其对象
	        	
	           //已知节点的属性名时：根据属性名称获取属性值
	            //获取类名
	            value_class = attributes.getValue("name");
	            System.out.println("实体类名是："+value_class);
	            
	            //获取该类的class对象
	            clazz = Class.forName(value_class);
	            //获取表名
	            String value_table = attributes.getValue("table");
	            System.out.println("表名是："+value_table);
	
	           sql = "insert into "+value_table+" values(";
	        }else if(qName.equals("id")){
	            //获取属性名
	            String value_id = attributes.getValue("name");
	            System.out.println("id的属性name的值是："+value_id);
	            //得到方法名
	            String method_getId = "get"+toUpperCaseFirstOne(value_id);
	            //声明Method对象
	            method = clazz.getMethod(method_getId);
	            //调用get方法
	            value = (String)method.invoke(obj);
	            //写入sql
	            sql +="'"+ value+"',"; 
	            
	        }else if(qName.equals("property")){
	            //获取类名
	            String value_pro = attributes.getValue("name");
	            System.out.println("property的属性name的值是："+value_pro);
	            //得到方法名
	            String method_getPro = "get"+toUpperCaseFirstOne(value_pro);
	            //声明Method对象
	            method = clazz.getMethod(method_getPro, null);
	            //调用get方法
	            value = (String)method.invoke(obj, null);
	            //写入sql
	            sql +="'"+ value+"',"; 
	 
	        }else if (!qName.equals("myclass") && !qName.equals("mysql-adapter")) {
	            System.out.print("节点名是：" + qName + "---");//此时qName获取的是节点名（标签）
	        } 
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
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
            System.out.println("\n======================结束遍历=================");
          //去掉最后一个逗号
	        sql = sql.substring(0,sql.length() - 1);
	        sql += ")";
            System.out.println("\nSQL为："+sql);
        }
    }
    
}
