package de.get;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public final class CreateTask extends HttpServlet  
{
   
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
    {
		PrintWriter pw=res.getWriter();
        res.setContentType("text/html;charset=UTF-8");        
        String name, checked, user, password;
        name="'" + req.getParameter("taskname") + "'";
        checked ="'" + req.getParameter("checked") + "'";
        user="'" + req.getParameter("username") + "'";
        password="'" + req.getParameter("password") + "'";
 
        try
        {
             Class.forName("oracle.jdbc.driver.OracleDriver");
             Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","SYSTEM","0000");    //darf alles!      
               
             Statement st=con.createStatement();
              
            	 ResultSet rs = st.executeQuery("SELECT us.name, us.encrypted_password FROM system.users us where us.name="+ user + " and us.encrypted_password=" + password);
            	
            	 if (rs.next()==false) {
            		 System.out.println("No task added");  
            	 }
            	 
            	while (rs.next()) {
            		
            		String user_name = "'" + rs.getString("NAME") + "'";
                    String user_pwd = "'" + rs.getString("ENCRYPTED_PASSWORD") + "'";
                    System.out.println(user_name);
                    System.out.println(user_pwd);
                    if (user_name !="" && user_pwd !=null) {
                   	 Integer i = st.executeUpdate("Insert into system.task(name, checked) values(" + name + "," + checked + ")");              
                        if (i==1) {
                       	 pw.print("Success");
                        }
                      
                        pw.close();
                        
                    }
                    
                    else {
                   	  System.out.println("No task added");                  	 
                    }
            	}
            	
            	 
                
                 
               
        }         
        
        catch (Exception e){
            e.printStackTrace();           
        }
 
    }
}