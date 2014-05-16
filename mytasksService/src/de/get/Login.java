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

import com.google.gson.Gson;

import de.domain.User;
import de.util.Util;
 
/**
 * Requestparameter: username, password
 * Response: User object with ID, name and email (Gson)
 *
 */
public class Login extends HttpServlet  
{
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
    {
		PrintWriter pw=res.getWriter();
        res.setContentType("text/html;charset=UTF-8");        
        String user, password;
        user =     req.getParameter("username");
        password = req.getParameter("password");
         
        try { 
        	Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(Util.CON, Util.USER, Util.PW); // darf // alles!

			PreparedStatement stmt = con.prepareStatement(Util.SQLAUTH, ResultSet.TYPE_SCROLL_SENSITIVE);
			stmt.setString(1, user);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
//			pw.print(user+password);
        	
			String out = "Error";
			while(rs.next()){
				User u = new User(rs.getLong("U_ID"), rs.getString("NAME"), rs.getString("EMAIL"));
				out = new Gson().toJson(u);
			}
			pw.print(out); //wrong credentials! try again" : "authentificated"
				
			pw.close();
			rs.close();
			stmt.close();
			con.close();  
		}         
        
        catch (Exception e){
            e.printStackTrace();
        }
 
    }
}