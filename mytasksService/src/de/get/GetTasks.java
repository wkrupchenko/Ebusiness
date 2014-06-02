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
import de.util.Util;

/**
 *  Request Parameter: tasklistid + username + password
 *  Output: Json Liste mit Tasklist -> mit Tasks
 */
public final class GetTasks extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String sqlGetTasks = "Select * from task where tl_fk=?";
				
		PrintWriter pw = res.getWriter();
		res.setContentType("text/html;charset=UTF-8");
		String tasklistid;
		tasklistid = req.getParameter("tasklistid");

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(Util.CON, Util.USER, Util.PW); // darf // alles!
			

				PreparedStatement stat = con.prepareStatement(sqlGetTasks);
				stat.setString(1, tasklistid);
				ResultSet resTasks = stat.executeQuery();
				
				ArrayList<Task> tasks = new ArrayList<Task>();
				
				while (resTasks.next()) {
						Task t = new Task();
						t.setTask_id(resTasks.getLong("TASK_ID"));
						t.setName(resTasks.getString("NAME"));
						t.setChecked(resTasks.getInt("CHECKED"));
						t.setTasklist(resTasks.getLong("TL_FK"));
						t.setLatitude(resTasks.getDouble("LATITUDE"));
						t.setLongitude(resTasks.getDouble("LONGITUDE"));
						tasks.add(t);					
				}
				
				pw.println(tasks.isEmpty()? "Error": new Gson().toJson(tasks));
				
				pw.close();
				con.close();

		} catch (Exception e) {
			pw.print("Error");
			e.printStackTrace();
		}
	}
}