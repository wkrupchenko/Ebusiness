package de.mytasks.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tasklist {

	public Tasklist(String name, Long id, boolean archived) {
		super();
		this.name = name;
		this.ownerId = id;
		this.archived = archived;
	}
	
	public Tasklist() {
		// TODO Auto-generated constructor stub
	}

	private Long id; 			// TL_ID
	private String name;		// TL_NAME
	private Long ownerId;	// OWNER_EMAIL
	private List<Task> tasks;	
	private boolean archived;	// ARCHIVED
	private Date creation;		// CREATED_ON
	private Date updated;		// UPDATED_ON
	private String updatedBy;	// UPDATED_BY
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isArchived() {
		return archived;
	}
	public void setArchived(boolean archived) {
		this.archived = archived;
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
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public Tasklist setTask(Task task) {
		if (tasks == null) {
			tasks = new ArrayList<Task>();
		}
		tasks.add(task);
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (archived ? 1231 : 1237);
		result = prime * result
				+ ((creation == null) ? 0 : creation.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
		result = prime * result + ((tasks == null) ? 0 : tasks.hashCode());
		result = prime * result + ((updated == null) ? 0 : updated.hashCode());
		result = prime * result
				+ ((updatedBy == null) ? 0 : updatedBy.hashCode());
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
		Tasklist other = (Tasklist) obj;
		if (archived != other.archived)
			return false;
		if (creation == null) {
			if (other.creation != null)
				return false;
		} else if (!creation.equals(other.creation))
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
		if (ownerId == null) {
			if (other.ownerId != null)
				return false;
		} else if (!ownerId.equals(other.ownerId))
			return false;
		if (tasks == null) {
			if (other.tasks != null)
				return false;
		} else if (!tasks.equals(other.tasks))
			return false;
		if (updated == null) {
			if (other.updated != null)
				return false;
		} else if (!updated.equals(other.updated))
			return false;
		if (updatedBy == null) {
			if (other.updatedBy != null)
				return false;
		} else if (!updatedBy.equals(other.updatedBy))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "TaskList [id=" + id + ", name=" + name + ", ownerId=" + ownerId
				+ ", tasks=" + tasks + ", archived=" + archived + ", creation="
				+ creation + ", updated=" + updated + ", updatedBy="
				+ updatedBy + "]";
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	
	
	
}
