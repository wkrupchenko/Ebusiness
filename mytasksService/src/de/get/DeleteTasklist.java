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
 * Request Parameter: tasklistid + username + password
 * output: Message: OK or "SQL error"
 */
public final class DeleteTasklist extends HttpServlet  
{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException {
		
		String sqlDeleteTasklist = "Delete from tasklist where TL_ID =?";
		
		PrintWriter pw=res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		String tasklistId, user, password;
		tasklistId = req.getParameter("tasklistid");        
		user=req.getParameter("username");
		password=req.getParameter("password");

		try	{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection(Util.CON, Util.USER, Util.PW);    //darf alles!      
			PreparedStatement stmt = con.prepareStatement(Util.SQLAUTH);
			stmt.setString(1, user);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			String user_name = rs.getString("NAME");
			String user_pwd = rs.getString("PASSWORD");                 

			if (user_name != null && user_name != "" && user_pwd != null && user_pwd != "") {

				PreparedStatement stat = con.prepareStatement(sqlDeleteTasklist);
				stat.setString(1, tasklistId);

				if(stat.executeUpdate()<1){
					pw.print("Error - No Tasklist added");
				}else{ pw.print("OK");}
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
			pw.print("SQL ERROR");
			e.printStackTrace();           
		}

	}
}