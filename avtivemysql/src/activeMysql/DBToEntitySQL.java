package activeMysql;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;

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
public class DBToEntitySQL  extends DefaultHandler{
	String value = null;
	String value_table;
	String sql = "";
	ResultSet rs = null;
	String key;
	
	//�������
	Class<?> clazz;
	Method method;

	//ģ����
	Object obj = null;
	/**
	 * ���췽��������ʵ����Ķ���
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
 	        if (qName.equals("myclass")) {
	            System.out.println("========��ȡ��������SQL=====��ʼ����xml=================");
	           //����myclass��name���ԣ��ҵ���Ӧ���࣬���������
	        	
	           //��֪�ڵ��������ʱ�������������ƻ�ȡ����ֵ
	            //��ȡ����
	            String value_class = attributes.getValue("name");
	            System.out.println("ʵ�������ǣ�"+value_class);
	            
	            //��ȡ�����class����
	            clazz = Class.forName(value_class);
	            //��ȡ����
	            value_table = attributes.getValue("table");
	            System.out.println("�����ǣ�"+value_table);
	
	        }else if(qName.equals("id")){
	            //��ȡ������
	            String value_id = attributes.getValue("name");
	            System.out.println("id������name��ֵ�ǣ�"+value_id);
//	            //�õ�������
//	            String method_getId = "get"+toUpperCaseFirstOne(value_id);
//	            //����Method����
//	            method = clazz.getMethod(method_getId);
//	            //����get����
//	            value = (String)method.invoke(obj);
	            //д��sql
		        sql = "select * from "+value_table+" where "+value_id+" = "+key;
	            System.out.println("\nSQLΪ��"+sql);
	           
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
     * ��������xml�ļ��Ľ�����ǩ
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //����DefaultHandler���endElement����
        super.endElement(uri, localName, qName);
        //�ж��Ƿ����һ�����Ѿ���������
        if (qName.equals("myclass")) {
            System.out.println("\n=========��ȡ��������SQL=====��������=================");

        }
    }


   
}
