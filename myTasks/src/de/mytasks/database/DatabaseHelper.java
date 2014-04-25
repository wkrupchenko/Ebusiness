package de.mytasks.database;

import java.sql.SQLException;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import de.mytasks.R;
import de.mytasks.R.raw;
import de.mytasks.domain.Task;
import de.mytasks.domain.Tasklist;
import de.mytasks.domain.User;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application
	private static final String DATABASE_NAME = "taskDB.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 5;

	private DatabaseHelper databaseHelper = null;

	// the DAO object we use to access the SimpleData table
	private Dao<Task, Integer> taskDao = null;
	private RuntimeExceptionDao<Task, Integer> taskRuntimeDao = null;
	private Dao<User, Integer> userDao = null;
	private RuntimeExceptionDao<User, Integer> userRuntimeDao = null;
	private Dao<Tasklist, Integer> tasklistDao = null;
	private RuntimeExceptionDao<Tasklist, Integer> tasklistRuntimeDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		// TODO Auto-generated method stub
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Task.class);
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Tasklist.class);
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, User.class);
			
			// here we try inserting data in the on-create as a test
//			RuntimeExceptionDao<User, Integer> userDao = databaseHelper.getUserRuntimeExceptionDao();
//			userDao.create(new User("admin", "admin", "admin"));
		} 
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			e.printStackTrace();
		}
//		here we try inserting data in the on-create as a test
//		Log.i(DatabaseHelper.class.getName(), "onCreate");
//		RuntimeExceptionDao<User, Integer> userDao = databaseHelper.getUserRuntimeExceptionDao();
//		userDao.create(new User("admin", "admin", "admin"));
//		Log.i(DatabaseHelper.class.getName(), "Created Admin");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Task.class, true);
			TableUtils.dropTable(connectionSource, Tasklist.class, true);
			TableUtils.dropTable(connectionSource, User.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(database, connectionSource);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			e.printStackTrace();
		}

	}

	public Dao<Task, Integer> getTaskDao() throws SQLException {
		if (taskDao == null) {
			taskDao = getDao(Task.class);
		}

		return taskDao;
	}

	public RuntimeExceptionDao<Task, Integer> getTaskRuntimeExceptionDao() {
		if (taskRuntimeDao == null) {
			taskRuntimeDao = getRuntimeExceptionDao(Task.class);
		}

		return taskRuntimeDao;
	}

	public Dao<User, Integer> getUserDao() throws SQLException {
		if (userDao == null) {
			userDao = getDao(User.class);
		}

		return userDao;
	}

	public RuntimeExceptionDao<User, Integer> getUserRuntimeExceptionDao() {
		if (userRuntimeDao == null) {
			userRuntimeDao = getRuntimeExceptionDao(User.class);
		}

		return userRuntimeDao;

	}

	public Dao<Tasklist, Integer> getTasklistDao() throws SQLException {
		if (tasklistDao == null) {
			tasklistDao = getDao(Tasklist.class);
		}

		return tasklistDao;
	}

	public RuntimeExceptionDao<Tasklist, Integer> getTasklistRuntimeExceptionDao() {
		if (tasklistRuntimeDao == null) {
			tasklistRuntimeDao = getRuntimeExceptionDao(Tasklist.class);
		}

		return tasklistRuntimeDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		taskDao = null;
		taskRuntimeDao = null;
		userDao = null;
		userRuntimeDao = null;
		tasklistDao= null;
		tasklistRuntimeDao= null;
	}
}
