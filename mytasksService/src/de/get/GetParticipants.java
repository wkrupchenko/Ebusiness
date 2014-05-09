package de.get;

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
import de.domain.User;

/**
 * Schickt JSON liste mit USERN zurueck
 *
 */
public final class GetParticipants extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String sqlGetParticipants = "Select * from USERS u where U.EMAIL in (select UT.USER_EMAIL_FK from USER_TASKLIST UT where UT.TASKLIST_FK = ?)";
		String sqlAuth = "SELECT name, encrypted_password FROM system.users where us.name=? and us.encrypted_password=?";

		PrintWriter pw = res.getWriter();
		res.setContentType("text/html;charset=UTF-8");
		String taskListId, user, password;
		taskListId = "'" + req.getParameter("task_id") + "'";

		// ="'" + req.getParameter("taskdescription") + "'";
		user = "'" + req.getParameter("username") + "'";
		password = "'" + req.getParameter("password") + "'";

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

				PreparedStatement stat = con.prepareStatement(sqlGetParticipants);
				stat.setString(1, taskListId);
				ResultSet rst = stat.executeQuery();

				ArrayList<Object> list = new ArrayList<Object>();
				while (rst.next()) {
					User u = new User();
					u.setId(rst.getLong("U_ID"));
					u.setName(rst.getString("NAME"));
					u.setEmail(rst.getString("EMAIL"));
					// u.getPassword(rst.getString("MARKS"));

					list.add(u);
				}

				pw.println(new Gson().toJson(list));
				pw.close();
				rs.close();
				rst.close();

			}// else { throw new Exception("Unauthorized!");}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}