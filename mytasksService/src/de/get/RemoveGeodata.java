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

public class RemoveGeodata extends HttpServlet  
{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String sqlRemoveGeodata = "Update task SET LATITUDE=0.0, LONGITUDE=0.0 where TASK_ID =?";
		
		PrintWriter pw=res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		String taskId;
		taskId = req.getParameter("taskid");
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection(Util.CON, Util.USER, Util.PW);    //darf alles!      

				PreparedStatement stat = con.prepareStatement(sqlRemoveGeodata);
				stat.setString(1, taskId);
				if(stat.executeUpdate()<1){
					pw.print("Error");
				}else{ pw.print("Geodata deleted");}
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