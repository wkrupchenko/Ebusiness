package de.mytasks.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {

	public User(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	private String id;			// U_ID
	private String name;		// NAME
	private String email;		// EMAIL
	private String password;	// PASSWORD
	private Date creation;		// CREATED_ON
	private Date updated;		// UPDATED_ON
	private List<Tasklist> taskLists;
	private Float rating;
	
	
	public Float getRating() {
		return rating;
	}
	public void setRating(Float rating) {
		this.rating = rating;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreation() {
		return creation;
	}
	public void setCreation(Date creation) {
		this.creation = creation;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public List<Tasklist> getTaskLists() {
		return taskLists;
	}
	public void setTaskLists(List<Tasklist> taskLists) {
		this.taskLists = taskLists;
	}
	public User setTaskList(Tasklist TaskList) {
		if (taskLists == null) {
			taskLists = new ArrayList<Tasklist>();
		}
		taskLists.add(TaskList);
		return this;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((creation == null) ? 0 : creation.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((taskLists == null) ? 0 : taskLists.hashCode());
		result = prime * result + ((updated == null) ? 0 : updated.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (creation == null) {
			if (other.creation != null)
				return false;
		} else if (!creation.equals(other.creation))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (taskLists == null) {
			if (other.taskLists != null)
				return false;
		} else if (!taskLists.equals(other.taskLists))
			return false;
		if (updated == null) {
			if (other.updated != null)
				return false;
		} else if (!updated.equals(other.updated))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email
				+ ", password=" + password + ", creation=" + creation
				+ ", updated=" + updated + "]";
	}
	
}
