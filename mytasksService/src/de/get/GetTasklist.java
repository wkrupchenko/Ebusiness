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

import de.domain.Task;
import de.domain.TaskList;

/**
 *  Input: userId + username + password
 *  Output: Json Liste mit Tasklist -> mit Tasks
 */
public final class GetTasklist extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String sqlGetTasklists = "Select fk_tasklist from user_tasklist where fk_user=?";
		String sqlGetTasks = "Select * from task where fk_tasklist=?";
		String sqlAuth = "SELECT name, encrypted_password FROM system.users where us.name=? and us.encrypted_password=?";

		PrintWriter pw = res.getWriter();
		res.setContentType("text/html;charset=UTF-8");
		String userId, user, password;
		userId = "'" + req.getParameter("userId") + "'";
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

				PreparedStatement stat = con.prepareStatement(sqlGetTasklists);
				stat.setString(1, userId);
				ResultSet rst = stat.executeQuery();
				
				PreparedStatement pstmt = con.prepareStatement(sqlGetTasks);
				
				ArrayList<TaskList> tasklists = new ArrayList<TaskList>();
				
				while (rst.next()) {
					ArrayList<Task> tasks = new ArrayList<Task>();
					TaskList tl = new TaskList();
					tl.setId(rst.getLong("TL_ID"));
					tl.setName(rst.getString("TASKLISTNAME"));
					tl.setOwnerId(rst.getLong("FK_OWNER"));
					
					pstmt.setString(1, tl.getId().toString());
					ResultSet resTasks = pstmt.executeQuery();
					
					while (resTasks.next()) {
						Task t = new Task();
						t.setTask_id(resTasks.getLong("T_ID"));
						t.setName(resTasks.getString("NAME"));
						t.setChecked(resTasks.getInt("CHECKED"));
						t.setTasklist(resTasks.getLong("FK_TASKLIST"));
						tasks.add(t);
					}					
					tasklists.add(tl);
				}

				pw.println(new Gson().toJson(tasklists));
				pw.close();
				rs.close();
				rst.close();
				con.close();
			} else { throw new Exception("Unauthorized!");}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}