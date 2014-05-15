package de.mytasks.service;

import java.util.List;

import de.mytasks.domain.User;
import android.widget.TextView;

public class GetCurrentUserInformation {
	
	private String name;
	private String email;
	private Long id;

	//Session Manager Class
	SessionManager session;
	
	public User getUserInformation() {
		User user = new User();
		
		session.checkLogin();
        
        // get user data from session
        List<String> userInformation = session.getUserDetails();
         
        // name an 1. Position in der Liste
        String name = userInformation.get(0);
        // email an 2. Position in der Liste
        String email = userInformation.get(1);
        // id an 3. Position in der Liste
        Long id = Long.valueOf(userInformation.get(2)).longValue();
        
        user.setName(name);
        user.setEmail(email);
        user.setId(id);
		
		return user;
	}
}
