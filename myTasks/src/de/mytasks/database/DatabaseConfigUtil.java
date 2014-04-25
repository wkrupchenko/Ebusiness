package de.mytasks.database;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import de.mytasks.domain.Tasklist;
import de.mytasks.domain.User;
import de.mytasks.domain.Task;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
	
	private static final Class<?>[] classes = new Class[]{User.class, Tasklist.class, Task.class};

	public static void main(String[] args) throws SQLException, IOException {
		writeConfigFile("ormlite_config.txt", classes);
	}

}
