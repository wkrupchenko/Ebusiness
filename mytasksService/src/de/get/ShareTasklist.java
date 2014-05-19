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
 * Request Parameter: email, tasklistid, username , password
 * Output Message: Error or Created
 */
public final class ShareTasklist extends HttpServlet  
{

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
	{
		String sqlShareTasklist = "Insert into user_tasklist(user_fk, tasklist_fk) values(?,?)";
		String sqlFindUserID = "Select u_id from users where email=?";
		String userid=null;
		
		PrintWriter pw=res.getWriter();
		res.setContentType("text/html;charset=UTF-8");        
		String email, tasklistid;
		email=		req.getParameter("email");
		tasklistid =req.getParameter("tasklistid");


		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(Util.CON, Util.USER, Util.PW); // darf // alles!

			
				PreparedStatement stat = con.prepareStatement(sqlFindUserID);
				stat.setString(1, email);
				
				ResultSet user = stat.executeQuery();
				while(user.next()){
					userid = user.getString("U_ID");
				}
				if(userid != null){
					PreparedStatement pstat = con.prepareStatement(sqlShareTasklist);
					pstat.setString(1, userid);
					pstat.setString(2, tasklistid);
					if(pstat.executeUpdate()<1){
						pw.print("Error");			// - No task added
					}else{ pw.print("Shared");}
				}
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