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

/**
 * Input: tasklistName, ownerEmail (Auth: username + password)
 * Output: Message: created or error
 */
public final class CreateTasklist extends HttpServlet  
{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String sqlCreateTasklist = "Insert into system.tasklists(name,owner_email) values(?,?)";
		String sqlAuth = "SELECT name, encrypted_password FROM system.users where name=? and encrypted_password=?";

		PrintWriter pw=res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		String tlName, userEmail, user, password;
		tlName="'" + req.getParameter("tasklistName") + "'";
		userEmail ="'" + req.getParameter("ownerEmail") + "'";
		user="'" + req.getParameter("username") + "'";
		password="'" + req.getParameter("password") + "'";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:XE", "SYSTEM", "0000"); // darf // alles!

			PreparedStatement stmt = con.prepareStatement(sqlAuth);
			stmt.setString(1, user);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			String user_name = rs.getString("NAME");
			String user_pwd = rs.getString("ENCRYPTED_PASSWORD");

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