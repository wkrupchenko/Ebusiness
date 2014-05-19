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
		String tasklistId;
		tasklistId = req.getParameter("tasklistid");

		try	{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection(Util.CON, Util.USER, Util.PW);    //darf alles!                    

			PreparedStatement stat = con.prepareStatement(sqlDeleteTasklist);
			stat.setString(1, tasklistId);

			if(stat.executeUpdate()<1){
				pw.print("Error");
			}else{ pw.print("OK");}
			pw.close();
			stat.close();
			con.close();
		}         
		catch (Exception e){
			pw.print("ERROR");
			e.printStackTrace();           
		}

	}
}