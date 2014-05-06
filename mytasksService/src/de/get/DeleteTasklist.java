package de.get;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

 
public final class DeleteTasklist extends HttpServlet  
{
   
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
    {
		PrintWriter pw=res.getWriter();
        res.setContentType("text/html;charset=UTF-8");        
        String tasklistId, user, password;
        tasklistId = "'" + req.getParameter("tasklist_id")+"'";        
        user="'" + req.getParameter("username") + "'";
        password="'" + req.getParameter("password") + "'";
        
        try
        {
             Class.forName("oracle.jdbc.driver.OracleDriver");
             Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","SYSTEM","0000");    //darf alles!      
             Statement st=con.createStatement();
              
            	 ResultSet rs = st.executeQuery("SELECT name, encrypted_password FROM system.users where name="+ user + " and us.encrypted_password=" + password + ")");
                 rs.next();
                 String user_name = rs.getString("NAME");
                 String user_pwd = rs.getString("ENCRYPTED_PASSWORD");                 
              
                 if (user_name != null && user_name != "" && user_pwd != null && user_pwd != "") {
                	 try
                	 {
                	 st.executeQuery("Delete system.tasklist where TL_ID ="+ tasklistId +""); 
                	 pw.print("Success");
                	 }
                	 catch(SQLException e)
                	 {
                		 pw.print("No tasklist deleted- Database doesnt work!");
                	 }

                     pw.close();
                 }
                 
                 else {
                	 throw new Exception("Unauthorized!");                	 
                 }
                          
        }         
        
        catch (Exception e){
            e.printStackTrace();           
        }
 
    }
}