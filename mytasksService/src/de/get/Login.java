package de.get;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
 
import java.util.ArrayList;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
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
        String un,up;
        un="'" + req.getParameter("username") + "'";
        up="'" + req.getParameter("password") + "'";
         
        try
        {
             Class.forName("oracle.jdbc.driver.OracleDriver");
             Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","SYSTEM","0000");          
               
             Statement st=con.createStatement();                                      
             ResultSet rs = st.executeQuery("SELECT * FROM system.users where name="+ un  + " and encrypted_password=" + up);
                 
             int i = 0; 
             while(rs.next())
             {  
            	 i++;              
             }
             
           if(i!=1)
            	 pw.print("wrong credentials! try again");
                 
             else {
            	 
            	 pw.print("authentificated");
             }
                          
             pw.close();
           
        }         
        
        catch (Exception e){
            e.printStackTrace();
        }
 
    }
}