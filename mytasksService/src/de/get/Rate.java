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

public class Rate extends HttpServlet  
{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String sqlRateTasklist = "Update user_tasklist SET RATE=? WHERE USER_FK=? AND TASKLIST_FK =?";
		
		PrintWriter pw=res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		String rate, userid, tasklistid;
		rate =req.getParameter("rate");
		userid =		req.getParameter("userid");
		tasklistid =	req.getParameter("tasklistid");

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection( Util.CON, Util.USER, Util.PW); // darf // alles!

			PreparedStatement stat = con.prepareStatement(sqlRateTasklist);
			stat.setString(1, rate);
			stat.setString(2, userid);
			stat.setString(3, tasklistid);
			
			if(stat.executeUpdate()<1){
				pw.print("Error");			// - Rating failed
			}else{ pw.print("Rated");}
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
