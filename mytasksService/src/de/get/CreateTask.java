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
 * Input: tasklistid, taskname, checked  
 * Output: Message: Error or Created
 */
public final class CreateTask extends HttpServlet  
{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String sqlCreateTask = "Insert into task(name, checked,tl_fk) values(?,?,?)";
		
		PrintWriter pw=res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		String name, checked, tasklistid;
		tasklistid =req.getParameter("tasklistid");
		name =		req.getParameter("taskname");
		checked =	req.getParameter("checked");

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection( Util.CON, Util.USER, Util.PW); // darf // alles!

			PreparedStatement stat = con.prepareStatement(sqlCreateTask);
			stat.setString(1, name);
			stat.setString(2, checked);
			stat.setString(3, tasklistid);
			
			if(stat.executeUpdate()<1){
				pw.print("Error");			// - No task added
			}else{ pw.print("Created");}
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