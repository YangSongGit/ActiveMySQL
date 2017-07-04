package activeMysql;

import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * �������ڽ�ʵ�����洢�����ݿ����
 * 1.���������ļ���ȷ�����ĸ���
 * 2.���������ļ���id��ȷ�������������Ӷ��õ�������
 * 3.���ݷ�����ƣ����÷����õ�����ֵ���õ�sql���
 * 4.�������ݿ⣬д������
 * @author Mr_Song
 *
 */
public class ORM_EntityToDB extends DefaultHandler {
	String value = null;
	String value_class;
	String sql = "";
	
	//�������
	Class<?> clazz;
	Method method;

	//ģ����
	Object obj = null;
	/**
	 * ���췽��������ʵ����Ķ���
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
	            System.out.println("========д������֮=====��ʼ����xml=================");
	           //����myclass��name���ԣ��ҵ���Ӧ���࣬���������
	        	
	           //��֪�ڵ��������ʱ�������������ƻ�ȡ����ֵ
	            //��ȡ����
	            value_class = attributes.getValue("name");
	            System.out.println("ʵ�������ǣ�"+value_class);
	            
	            //��ȡ�����class����
	            clazz = Class.forName(value_class);
	            //��ȡ����
	            String value_table = attributes.getValue("table");
	            System.out.println("�����ǣ�"+value_table);
	
	           sql = "insert into "+value_table+" values(";
	        }else if(qName.equals("id")){
	            //��ȡ������
	            String value_id = attributes.getValue("name");
	            System.out.println("id������name��ֵ�ǣ�"+value_id);
	            //�õ�������
	            String method_getId = "get"+toUpperCaseFirstOne(value_id);
	            //����Method����
	            method = clazz.getMethod(method_getId);
	            //����get����
	            value = (String)method.invoke(obj);
	            //д��sql
	            sql +="'"+ value+"',"; 
	            
	        }else if(qName.equals("property")){
	            //��ȡ����
	            String value_pro = attributes.getValue("name");
	            System.out.println("property������name��ֵ�ǣ�"+value_pro);
	            //�õ�������
	            String method_getPro = "get"+toUpperCaseFirstOne(value_pro);
	            //����Method����
	            method = clazz.getMethod(method_getPro, null);
	            //����get����
	            value = (String)method.invoke(obj, null);
	            //д��sql
	            sql +="'"+ value+"',"; 
	 
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
	        sql = sql.substring(0,sql.length() - 1);
	        sql += ")";
            System.out.println("\nSQLΪ��"+sql);
        }
    }
    
}
