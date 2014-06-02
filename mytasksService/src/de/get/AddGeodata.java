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
 * Request Parameter: taskid , taskname , taskcheckbox + (User-Auth: username+password)
 * Output: Statusmessage
 *
 */
public final class AddGeodata extends HttpServlet  
{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String sqlUpdateTask = "Update task SET LATITUDE=?, LONGITUDE=? where TASK_ID =?";
		
		PrintWriter pw=res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		String taskId;
		String longitude, latitude;
		latitude = req.getParameter("latitude");
		longitude = req.getParameter("longitude");
		taskId = req.getParameter("taskid");


		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection(Util.CON, Util.USER, Util.PW);    //darf alles!      

				PreparedStatement stat = con.prepareStatement(sqlUpdateTask);
				stat.setString(1, latitude);
				stat.setString(2, longitude);
				stat.setString(3, taskId);
				if(stat.executeUpdate()<1){
					pw.print("Error");
				}else{ pw.print("Map Informations updated");}
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