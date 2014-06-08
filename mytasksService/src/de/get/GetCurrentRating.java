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
import javax.xml.crypto.Data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import de.domain.User;
import de.util.Util;

public class GetCurrentRating extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String sqlGetCerrentRating = "select avg(rate) from user_tasklist where tasklist_fk = ?";
		
		PrintWriter pw = res.getWriter();
		res.setContentType("text/html;charset=UTF-8");
		String taskListId;
		taskListId = req.getParameter("tasklistid");


		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection( Util.CON, Util.USER, Util.PW); // darf // alles!

			 

				PreparedStatement stat = con.prepareStatement(sqlGetCerrentRating);
				stat.setString(1, taskListId);
				ResultSet rst = stat.executeQuery();
	
				if (rst.next()) {
					Float currentRating = rst.getFloat(1);
					String jsonLine ="{data: {rating: [{ratingAverage:" +currentRating+"}]}}";
					
					Gson gson = new Gson();
					JsonElement element = gson.fromJson (jsonLine, JsonElement.class);
					JsonObject jsonObj = element.getAsJsonObject();
					
					pw.println(jsonObj);
				} else
					pw.print("Error");
			
				pw.close();
				
				rst.close();
				con.close();

			
		} catch (Exception e) {
			pw.print("Error");
			e.printStackTrace();
		}
	}
}