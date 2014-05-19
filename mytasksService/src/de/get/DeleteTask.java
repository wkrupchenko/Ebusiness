package de.get;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.util.Util;


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

		PrintWriter pw=res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		String taskId;
		taskId = req.getParameter("taskid");        


		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection(Util.CON, Util.USER, Util.PW);    //darf alles!      


				PreparedStatement stat = con.prepareStatement(sqlDeleteTask);
				stat.setString(1, taskId);

				if(stat.executeUpdate()>0){pw.print("OK");}else {
					pw.print("Error");
				}
				pw.close();
				stat.close();
				con.close();
		}
		catch (Exception e){
			pw.print("Error");
			e.printStackTrace();           
		}

	}
}