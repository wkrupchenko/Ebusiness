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
 * Input: tasklistName + ownerid(USERID)+  username + password
 * Output: Message: created or error
 */
public final class CreateTasklist extends HttpServlet  
{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String sqlCreateTasklist = "Insert into tasklist(tl_name,fk_owner) values(?,?)";
		
		PrintWriter pw=res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		String tlName, userEmail;
		tlName=req.getParameter("tasklistname");
		userEmail =req.getParameter("ownerid");
		

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(Util.CON, Util.USER, Util.PW); // darf // alles!

			

				PreparedStatement stat = con.prepareStatement(sqlCreateTasklist);
				stat.setString(1, tlName);
				stat.setString(2, userEmail);

				if(stat.executeUpdate()<1){
					pw.print("Error"); 			// - No tasklist created
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