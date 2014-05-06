package de.get;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class Register extends HttpServlet  
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException
    {
		PrintWriter pw = res.getWriter();
        res.setContentType("text/html;charset=UTF-8");        
        String tb = req.getParameter("table"); 
        
        String un,ue,up;
        un="'" + req.getParameter("username") + "'";
        up="'" + req.getParameter("password") + "'";
        ue="'" + req.getParameter("email") + "'";         
 
        try
        {
             Class.forName("oracle.jdbc.driver.OracleDriver");
             Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","SYSTEM","0000");          
               
             Statement st=con.createStatement();             
             st.executeUpdate("Insert into system.users(name, email,encrypted_password) values(" + un + "," + ue + "," + up + ")");
             ResultSet rs = st.executeQuery("SELECT email FROM system.users where email =" + ue);             
             int i = 0;
             while(rs.next())
             {
           	  i++;               
             }
             if(i==1)
                 pw.print(1);
             else {
                 pw.print(0);
             }
             
             pw.close();
           
        }         
        
        catch (Exception e){
            e.printStackTrace();
        }
 
    }
}
