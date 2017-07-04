package activeMysql;

import org.xml.sax.helpers.DefaultHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


public class TableGenerate extends DefaultHandler {
	String sql = "";
	//ģ����
	Object obj = null;
	
	public String getSql(){
		return sql;
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
        if (qName.equals("myclass")) {
            System.out.println("======================��ʼ����xml=================");
           //����myclass��name���ԣ��ҵ���Ӧ���࣬���������
        	
           //��֪�ڵ��������ʱ�������������ƻ�ȡ����ֵ
            //��ȡ����
            String value_class = attributes.getValue("name");
            System.out.println("myclass������name��ֵ�ǣ�"+value_class);
            //��ȡ����
            String value_table = attributes.getValue("table");
            System.out.println("myclass������table��ֵ�ǣ�"+value_table);
          //δ֪�ڵ��������ʱ����ȡ������������ֵ
//            int num=attributes.getLength();
//            for(int i=0;i<num;i++){
//                System.out.print("��Ԫ�صĵ�"+(i+1)+"���������ǣ�"+attributes.getQName(i));
//                System.out.println("---����ֵ�ǣ�"+attributes.getValue(i));
//            }
            sql = "create table if not exists "+value_table+" (";
        }else if(qName.equals("id")){
            //��ȡ����
            String value_id = attributes.getValue("name");
            System.out.println("id������name��ֵ�ǣ�"+value_id);
            //��ȡ����
            String value_id_type = attributes.getValue("type");
            System.out.println("id������type��ֵ�ǣ�"+value_id_type);
            //д��sql
            sql += value_id+" "+value_id_type+" primary key,"; 
            
        }else if(qName.equals("property")){
            //��ȡ����
            String value_pro = attributes.getValue("name");
            System.out.println("property������name��ֵ�ǣ�"+value_pro);
            //��ȡ����
            String value_pro_type = attributes.getValue("type");
            System.out.println("property������type��ֵ�ǣ�"+value_pro_type);
            //д��sql
            sql += value_pro+" "+value_pro_type+","; 
 
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
