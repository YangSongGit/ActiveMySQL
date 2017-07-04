package myTest;

import activeMysql.ActiveMySQL;

public class UserTest {
	   public static void main(String[] args) {
		   String url = "jdbc:mysql://127.0.0.1/test";
		   String name = "com.mysql.jdbc.Driver";
		   String user = "root";
		   String password = "";
		   
		   ActiveMySQL demo = new ActiveMySQL(url,name,user,password);
		   demo.createTable();
		   
		   
//		   //–¥»Î
//		   User us = new User();
//		   us.setUid("2014004");
//		   us.setUname("SOngYnag");
//		   demo.saveEntityToDB(us);
		   
		   //∂¡»°
		   User us2;
		   us2 = (User)demo.generateEntityFromDB("2014003");
		   
		   System.out.println("Us2 ID:"+us2.getUid());
		   System.out.println("Us2 Name:"+us2.getUname());
	   }

}
