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



/**
 * Input: TaskID , taskName , TaskCheckbox + (User-Auth: username+password)
 * Output: Statusmessage
 *
 */
public final class UpdateTask extends HttpServlet  
{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String sqlUpdateTask = "Update system.task SET T_NAME=?, T_CHECKED=? where TASK_ID =?";
		String sqlAuth = "SELECT name, encrypted_password FROM system.users where name=? and encrypted_password=?";

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

			PreparedStatement stmt = con.prepareStatement(sqlAuth);
			stmt.setString(1, user);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			String user_name = rs.getString("NAME");
			String user_pwd = rs.getString("ENCRYPTED_PASSWORD");               

			if (user_name != null && user_name != "" && user_pwd != null && user_pwd != "") {

				PreparedStatement stat = con.prepareStatement(sqlUpdateTask);
				stat.setString(1, taskName);
				stat.setString(2, taskCheckBox);
				stat.setString(3, taskId);
				if(stat.executeUpdate()<1){
					pw.print("Error - Task not Update ");
				}else{ pw.print("Task updated");}
				pw.close();
				rs.close();
				stmt.close();
				stat.close();
				con.close();
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