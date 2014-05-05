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
 
public final class UpdateTask extends HttpServlet  
{
   
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
    {
		PrintWriter pw=res.getWriter();
        res.setContentType("text/html;charset=UTF-8");        
        String taskId, taskName, taskCheckBox, user, password;
        taskId = "'" + req.getParameter("task_id") + "'";
        taskName = "'" + req.getParameter("taskname") + "'";
        taskCheckBox = "'" + req.getParameter("Taskbox") + "'";
        
        // ="'" + req.getParameter("taskdescription") + "'";
        user="'" + req.getParameter("username") + "'";
        password="'" + req.getParameter("password") + "'";
 
        try
        {
             Class.forName("oracle.jdbc.driver.OracleDriver");
             Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","SYSTEM","0000");    //darf alles!      
             Statement st=con.createStatement();
              
            	 ResultSet rs = st.executeQuery("SELECT name, encrypted_password FROM system.users where us.name="+ user + " and us.encrypted_password=" + password + ")");
                 rs.next();
                 String user_name = rs.getString("NAME");
                 String user_pwd = rs.getString("ENCRYPTED_PASSWORD");                 
              
                 if (user_name != null && user_name != "" && user_pwd != null && user_pwd != "") {
                	 int i = st.executeUpdate("Update system.task SET NAME="+ taskName +", CHECKED="+ taskCheckBox +" where T_ID ="+ taskId +"");             
                     if (i==1) {
                    	 pw.print("Success");
                     }
                     
                     else if (i==0) {
                    	 pw.print("No task updated");
                     }
                     
                     else {
                    	 return;
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