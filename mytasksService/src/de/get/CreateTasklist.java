package de.get;

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
		String tlName, userEmail, user, password;
		tlName=req.getParameter("tasklistname");
		userEmail =req.getParameter("ownerid");
		user=req.getParameter("username");
		password=req.getParameter("password");

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(Util.CON, Util.USER, Util.PW); // darf // alles!

			PreparedStatement stmt = con.prepareStatement(Util.SQLAUTH);
			stmt.setString(1, user);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			String user_name = rs.getString("NAME");
			String user_pwd = rs.getString("PASSWORD");

			if (user_name != null && user_name != "" && user_pwd != null && user_pwd != "") {

				PreparedStatement stat = con.prepareStatement(sqlCreateTasklist);
				stat.setString(1, tlName);
				stat.setString(2, userEmail);

				if(stat.executeUpdate()<1){
					pw.print("Error"); 			// - No tasklist created
				}else{ pw.print("Created");}
				pw.close();
				rs.close();
				stmt.close();
				stat.close();
				con.close();
			}else {
				System.out.println("Authentication error - No tasked added");                  	 
			}
		}         

		catch (Exception e){
			e.printStackTrace();           
		}

	}
}