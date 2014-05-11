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
 
public class Login extends HttpServlet  
{
    /**
	 * 
	 */
		
	 
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
    {
		
		
		PrintWriter pw=res.getWriter();
        res.setContentType("text/html;charset=UTF-8");        
        String user, password;
        user =     req.getParameter("username");
        password = req.getParameter("password");
         
        try {
//          String url = "jdbc:oracle:thin:@//iwi-w-vm-dbo.hs-karlsruhe.de:1521/oracledbwi.hs-karlsruhe.de"; 
//          String user = "eBS14Db01"; 
//          String passwd = "eBs14Db";
//          Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","SYSTEM","0000");  
        	Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(Util.CON, Util.USER, Util.PW); // darf // alles!

			PreparedStatement stmt = con.prepareStatement(Util.SQLAUTH, ResultSet.TYPE_SCROLL_SENSITIVE);
			stmt.setString(1, user);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
//			pw.print(user+password);
        	
			pw.print(rs.next()? "OK" : "ERR"); //wrong credentials! try again" : "authentificated"
			
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