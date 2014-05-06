package de.domain;

import java.util.Date;

public class Task {

	public Task(String name, boolean checked) {
		super();
		this.name = name;
		this.checked = checked;
	}
	
	private Long id;			// TASK_ID
	private String name;		// T_NAME
	private boolean checked;	// CHECKED
	private Date creation;		// CREATED_ON
	private Date updated;		// UPDATED_ON
	private String updatedBy;	// UPDATED_BY
	
	public Long getTask_id() {
		return id;
	}
	public void setTask_id(Long task_id) {
		this.id = task_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUpdated_by() {
		return updatedBy;
	}
	public void setUpdated_by(String updated_by) {
		this.updatedBy = updated_by;
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
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (checked ? 1231 : 1237);
		result = prime * result
				+ ((creation == null) ? 0 : creation.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Task other = (Task) obj;
		if (checked != other.checked)
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
		return "Task [id=" + id + ", name=" + name + ", checked=" + checked
				+ ", creation=" + creation + ", updated=" + updated
				+ ", updatedBy=" + updatedBy + "]";
	}
}
