package src.de.get;

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

import src.de.util.Util;


/**
 * Input: taskid + username + password
 * Output Message: "OK" or "sql Error  no Task deleted"
 *
 */
public final class DeleteTask extends HttpServlet  
{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String sqlDeleteTask = "Delete from task where TASK_ID =?";
		String sqlAuth = "SELECT name, password FROM users where name=? and password=?";

		PrintWriter pw=res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		String taskId, user, password;
		taskId = req.getParameter("taskid");        
		user=req.getParameter("username");
		password=req.getParameter("password");

		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection(Util.CON, Util.USER, Util.PW);    //darf alles!      

			PreparedStatement stmt = con.prepareStatement(sqlAuth);
			stmt.setString(1, user);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			String user_name = rs.getString("NAME");
			String user_pwd = rs.getString("PASSWORD");

			if (user_name != null && user_name != "" && user_pwd != null && user_pwd != "") {

				PreparedStatement stat = con.prepareStatement(sqlDeleteTask);
				stat.setString(1, taskId);

				if(stat.executeUpdate()>0){pw.print("OK");}else {
					pw.print("input error-no task deleted");
				}
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
			pw.print("sql Error  no Task deleted");
			e.printStackTrace();           
		}

	}
}