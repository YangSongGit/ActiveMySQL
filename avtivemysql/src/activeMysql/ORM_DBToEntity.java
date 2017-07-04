package activeMysql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * �������ڽ����ݴ����ݿ���ж�ȡ��ʵ�������(��һ������)
 * 1.����keyֵ���������ݿ⣬�ҳ�����
 * 2.���������ļ���ȷ�����ĸ���
 * 3.���������ļ���id��ȷ�������������Ӷ��õ�������
 * 4.���ݷ�����ƣ�����set������������ֵ
 * @author Mr_Song
 */
public class ORM_DBToEntity  extends DefaultHandler{
	String value = null;
	String value_table;
	String sql = "";
	boolean isNext = false;
    int i = 1;

	ResultSet rs = null;
	
	//�������
	Class<?> clazz;
	Method method;

	//ģ����
	Object obj = null;
	/**
	 * ���췽��������ʵ����Ķ���
	 * @param obj
	 */
	public ORM_DBToEntity(ResultSet rs) {
		super();
		this.rs = rs;
	}

	
    public Object getObj() {
		return obj;
	}


	/**
     * �÷������ַ���������ĸ��Ϊ��д
     * @param s
     * @return ����ĸ��д���ַ���
     */
    public static String toUpperCaseFirstOne(String s){
    	  if(Character.isUpperCase(s.charAt(0)))
    	    return s;
    	  else
    	    return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
   	}

    /**
     * ������ʶ������ʼ
     */
    public void startDocument() throws SAXException {
        super.startDocument();
        System.out.println("SAX������ʼ");

    }
    
    /**
     * ������ʶ��������
     */
    public void endDocument() throws SAXException {
        super.endDocument();
        System.out.println("SAX��������");
    }

    /**
     * ��������xml�ļ��Ŀ�ʼ��ǩ
     * ����xmlԪ��
     */
    public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException {
        //����DefaultHandler���startElement����
        super.startElement(uri, localName, qName, attributes);
        try {
            if(!isNext){
            	rs.next();
            	isNext = true;
                String id = rs.getString(1);  
                String name = rs.getString(2);  
               System.out.println(id + "\t" + name );  
            }
 	        if (qName.equals("myclass")) {
	            System.out.println("========��ȡ����֮=====��ʼ����xml=================");
	           //����myclass��name���ԣ��ҵ���Ӧ���࣬���������
	        	
	           //��֪�ڵ��������ʱ�������������ƻ�ȡ����ֵ
	            //��ȡ����
	            String value_class = attributes.getValue("name");
	            System.out.println("ʵ�������ǣ�"+value_class);
	            
	            //��ȡ����,�����������
	            clazz = Class.forName(value_class);
	            obj = clazz.newInstance();
//	            //��ȡ����
//	            value_table = attributes.getValue("table");
//	            System.out.println("�����ǣ�"+value_table);
	
	        }else if(qName.equals("id")){
	            //��ȡ������
	            String value_id = attributes.getValue("name");
	            System.out.println("id������name��ֵ�ǣ�"+value_id);
	            //��ȡ���Ե�ֵ
                value = rs.getString(i++);  

	            //�õ�������
	            String method_setId = "set"+toUpperCaseFirstOne(value_id);
	            //����Method����
	            method = clazz.getMethod(method_setId,String.class);
	            //����set����
	            method.invoke(obj,value);

	        }else if(qName.equals("property")){
	            //��ȡ����
	            String value_pro = attributes.getValue("name");
	            System.out.println("property������name��ֵ�ǣ�"+value_pro);
	            //��ȡ���Ե�ֵ
                value = rs.getString(i++);  

	            //�õ�������
	            String method_setPro = "set"+toUpperCaseFirstOne(value_pro);
	            //����Method����
	            method = clazz.getMethod(method_setPro,String.class);
	            //����set����
	            method.invoke(obj,value);
	 
	        }else if (!qName.equals("myclass") && !qName.equals("mysql-adapter")) {
	            System.out.print("�ڵ����ǣ�" + qName + "---");//��ʱqName��ȡ���ǽڵ�������ǩ��
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

 
    /**
     * ��������xml�ļ��Ľ�����ǩ
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //����DefaultHandler���endElement����
        super.endElement(uri, localName, qName);
        //�ж��Ƿ����һ�����Ѿ���������
        if (qName.equals("myclass")) {
            System.out.println("\n======================��������=================");
          //ȥ�����һ������
//	        sql = sql.substring(0,sql.length() - 1);
//	        sql += ")";
        }
    }
    
}
