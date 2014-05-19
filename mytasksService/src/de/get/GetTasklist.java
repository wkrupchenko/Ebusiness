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
import de.util.Util;

/**
 *  Request Parameter: userid + username + password
 *  Output: Json Liste mit Tasklist -> mit Tasks
 */
public final class GetTasklist extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String sqlGetTasklists = "Select * from tasklist where TL_ID in (select tasklist_fk from user_tasklist where user_fk=?)";
		String sqlGetTasks = "Select * from task where tl_fk=?";
		
		PrintWriter pw = res.getWriter();
		res.setContentType("text/html;charset=UTF-8");
		String userId;
		userId = req.getParameter("userid");

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(Util.CON, Util.USER, Util.PW); // darf // alles!
			

				PreparedStatement stat = con.prepareStatement(sqlGetTasklists);
				stat.setString(1, userId);
				ResultSet rst = stat.executeQuery();
				
				PreparedStatement pstmt = con.prepareStatement(sqlGetTasks);
				
				ArrayList<TaskList> tasklists = new ArrayList<TaskList>();
				
				while (rst.next()) {
					ArrayList<Task> tasks = new ArrayList<Task>();
					TaskList tl = new TaskList();
					tl.setId(rst.getLong("TL_ID"));
					tl.setName(rst.getString("TL_NAME"));
					tl.setOwnerId(rst.getLong("FK_OWNER"));
					
					pstmt.setString(1, tl.getId().toString());
					ResultSet resTasks = pstmt.executeQuery();
					
					while (resTasks.next()) {
						Task t = new Task();
						t.setTask_id(resTasks.getLong("TASK_ID"));
						t.setName(resTasks.getString("NAME"));
						t.setChecked(resTasks.getInt("CHECKED"));
						t.setTasklist(resTasks.getLong("TL_FK"));
						tasks.add(t);
					}
					tl.setTasks(tasks);
//					pw.print(tl);
					tasklists.add(tl);
				}
				String out = new Gson().toJson(tasklists);
				pw.println(out);
				
				//TaskList taskList = new Gson().fromJson(out, TaskList.class);
				
				
				pw.close();

				rst.close();
				con.close();

		} catch (Exception e) {
			pw.print("Error");
			e.printStackTrace();
		}
	}
}