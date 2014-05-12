package src.de.get;

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

import src.de.util.Util;

/**
 * Input: tasklistid, taskname, checked  + (Auth: username , password
 * Output: Message: Error or Created
 */
public final class CreateTask extends HttpServlet  
{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String sqlCreateTask = "Insert into task(name, checked,tl_fk) values(?,?,?)";
		
		PrintWriter pw=res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		String name, checked, tasklistid, user, password;
		tasklistid =req.getParameter("tasklistid");
		name =		req.getParameter("taskname");
		checked =	req.getParameter("checked");
		user =		req.getParameter("username");
		password=	req.getParameter("password");

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection( Util.CON, Util.USER, Util.PW); // darf // alles!

			PreparedStatement stmt = con.prepareStatement(Util.SQLAUTH);
			stmt.setString(1, user);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			rs.next();
			String user_name = rs.getString("NAME");
			String user_pwd = rs.getString("PASSWORD");

			if (user_name != null && user_name != "" && user_pwd != null && user_pwd != "") {

				PreparedStatement stat = con.prepareStatement(sqlCreateTask);
				stat.setString(1, name);
				stat.setString(2, checked);
				stat.setString(3, tasklistid);
				
				if(stat.executeUpdate()<1){
					pw.print("Error");			// - No task added
				}else{ pw.print("Created");}
				pw.close();
				rs.close();
				stmt.close();
				stat.close();
				con.close();
			}else {
				System.out.println("Error");                  	 
			}
		}         

		catch (Exception e){
			e.printStackTrace();           
		}

	}
}