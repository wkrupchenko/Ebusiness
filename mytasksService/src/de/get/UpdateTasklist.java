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
 * Input: TL_ID , TL_NAME , ARCHIVED + (User-Auth: username+password)
 * Output: Statusmessage: OK or ERROR
 *
 */
public final class UpdateTasklist extends HttpServlet  
{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String sqlUpdateTasklist = "Update tasklist SET TL_NAME=?, ARCHIVED=? where TL_ID =?";
		
		PrintWriter pw=res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		String taskListId, tasklistName, archived;
		taskListId = req.getParameter("tasklistid");
		tasklistName = req.getParameter("tasklistname");
		archived = req.getParameter("archived");

		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection(Util.CON, Util.USER, Util.PW);    //darf alles!      

			

				PreparedStatement stat = con.prepareStatement(sqlUpdateTasklist);
				stat.setString(1, tasklistName);
				stat.setString(2, archived);
				stat.setString(3, taskListId);
				if(stat.executeUpdate()<1){
					pw.print("Error - Tasklist not Update ");
				}else{ pw.print("Tasklist updated");}
				pw.close();
				stat.close();
				con.close();
					}         
		catch (Exception e){
			e.printStackTrace();           
		}

	}
}