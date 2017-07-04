package activeMysql;

import org.xml.sax.helpers.DefaultHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


public class TableGenerate extends DefaultHandler {
	String sql = "";
	//模板类
	Object obj = null;
	
	public String getSql(){
		return sql;
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
        if (qName.equals("myclass")) {
            System.out.println("======================开始遍历xml=================");
           //解析myclass的name属性，找到对应的类，创建其对象
        	
           //已知节点的属性名时：根据属性名称获取属性值
            //获取类名
            String value_class = attributes.getValue("name");
            System.out.println("myclass的属性name的值是："+value_class);
            //获取表名
            String value_table = attributes.getValue("table");
            System.out.println("myclass的属性table的值是："+value_table);
          //未知节点的属性名时，获取属性名和属性值
//            int num=attributes.getLength();
//            for(int i=0;i<num;i++){
//                System.out.print("该元素的第"+(i+1)+"个属性名是："+attributes.getQName(i));
//                System.out.println("---属性值是："+attributes.getValue(i));
//            }
            sql = "create table if not exists "+value_table+" (";
        }else if(qName.equals("id")){
            //获取类名
            String value_id = attributes.getValue("name");
            System.out.println("id的属性name的值是："+value_id);
            //获取表名
            String value_id_type = attributes.getValue("type");
            System.out.println("id的属性type的值是："+value_id_type);
            //写入sql
            sql += value_id+" "+value_id_type+" primary key,"; 
            
        }else if(qName.equals("property")){
            //获取类名
            String value_pro = attributes.getValue("name");
            System.out.println("property的属性name的值是："+value_pro);
            //获取表名
            String value_pro_type = attributes.getValue("type");
            System.out.println("property的属性type的值是："+value_pro_type);
            //写入sql
            sql += value_pro+" "+value_pro_type+","; 
 
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
