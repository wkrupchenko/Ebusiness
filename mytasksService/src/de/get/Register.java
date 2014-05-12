package src.de.get;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.de.util.Util;

public class Register extends HttpServlet  
{
	/**
	 * Input: username, passwword, email
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String sqlNewUser = "Insert into users(name, email, password) values(?,?,?)";

		PrintWriter pw = res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		//String tb = req.getParameter("table"); 

		String un,ue,up;
		un="'" + req.getParameter("username") + "'";
		up="'" + req.getParameter("password") + "'";
		ue="'" + req.getParameter("email") + "'";         

		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con=DriverManager.getConnection(Util.CON, Util.USER, Util.PW);          

			PreparedStatement stmt = con.prepareStatement(sqlNewUser);
			stmt.setString(1, un);
			stmt.setString(2, ue);
			stmt.setString(3, up);

			if(stmt.executeUpdate()<1){
				pw.print("Error - not registered ");
			}else{ pw.print("Registered OK");}
			pw.close();
			stmt.close();
			con.close();
		}         
		catch (Exception e){
			e.printStackTrace();
		}

	}
}
