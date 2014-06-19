package de.get;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.util.Util;

/**
 * Request Parameter: email, tasklistid, username , password
 * Output Message: Error or Created
 */
public final class UnsubscribeTasklist extends HttpServlet  
{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String sqlUnsubscribeTasklist = "Delete from user_tasklist where user_fk = ? and tasklist_fk = ?";		 
		
		PrintWriter pw=res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		String user, tasklistid;
		user =req.getParameter("user");
		tasklistid =req.getParameter("tasklistid");


		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(Util.CON, Util.USER, Util.PW); // darf // alles!				
			 
				if(user != null && user !="" && tasklistid !=null && tasklistid !=""){
					PreparedStatement stat = con.prepareStatement(sqlUnsubscribeTasklist);
					stat.setString(1, user);
					stat.setString(2,tasklistid);
					if(stat.executeUpdate()<1){
						pw.print("Error");			// - No task added
					}else{ pw.print("Unsubscribed");}
					stat.close();
				}
				pw.close();				
				con.close();
			
		}         
		catch (Exception e){
			pw.print("Error");
			e.printStackTrace();           
		}

	}
}