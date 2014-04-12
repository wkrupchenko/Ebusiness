package de.mytasks; 

import java.io.Serializable; 
import android.graphics.Bitmap;

public class Task  implements  Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public int version;	
	private int _id;
	private String title;
	private String description;
	private String geotag;
	private byte[] picture;
	

	public Task() {
		super();
	}
	
	public Task(int id, String title, String description, String geotag, byte[] picture) {
		super();
		this._id = id;
		this.title = "";
		this.description = "";
		this.geotag = "";
		this.picture = picture;
	}
			 	
	public void setID(int id) {
		this._id = id;
	}
	
	public int getID() {
		return this._id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setGeotag(String geotag) {
		this.geotag = geotag;
	}
	
	public String getGeotag() {
		return this.geotag;
	}
	
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}
	
	public byte[] getPicture() {
		return this.picture;
	}
			
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		final Task other = (Task) obj;
		if (_id != other._id)
			return false;
		
		if (!title.equals(other.title))
			return false;
		
		if (!description.equals(other.description))
			return false;
		
		if (!geotag.equals(other.geotag))
			return false;		 
	
		return true;
	}

	@Override
	public String toString() {
		return "Task [_id=" + _id + ", title=" + title + ", description=" + description + ", geotag=" + geotag + "]";
	}
}