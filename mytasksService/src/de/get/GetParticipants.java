package src.de.get;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import src.de.domain.User;
import src.de.util.Util;

/** 
 * Request Parameter: tasklistid + username + password
 * RESPONS: Schickt JSON liste mit USERN zurueck: Klasse User Member: Id , name , email
 * 
 *Zeigt alle Benutzer einer Tasklist an bzw mit wem sie geshared ist
 */
public final class GetParticipants extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String sqlGetParticipants = "Select * from USERS where U_ID in (select USER_FK from USER_TASKLIST where TASKLIST_FK = ?)";
		
		PrintWriter pw = res.getWriter();
		res.setContentType("text/html;charset=UTF-8");
		String taskListId, user, password;
		taskListId =req.getParameter("tasklistid");

		user = req.getParameter("username");
		password = req.getParameter("password");

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

				PreparedStatement stat = con.prepareStatement(sqlGetParticipants);
				stat.setString(1, taskListId);
				ResultSet rst = stat.executeQuery();

				ArrayList<Object> list = new ArrayList<Object>();
				if (rst.next()) {
					while(rst.next()){
					User u = new User();
					u.setId(rst.getLong("U_ID"));
					u.setName(rst.getString("NAME"));
					u.setEmail(rst.getString("EMAIL"));

					list.add(u);
					}
					pw.println(new Gson().toJson(list));
				}else{
					pw.print("SQL-error, check input tasklistid correct?");
				}
			
				pw.close();
				rs.close();
				rst.close();
				con.close();

			}// else { throw new Exception("Unauthorized!");}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}