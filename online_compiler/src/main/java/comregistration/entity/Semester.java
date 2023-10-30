package comregistration.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Semester {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String semester;
	private int year;
	private int isActive;
	private String createdBy;
	private Date createdDateTime;
	private String updatedBy;
	private Date updatedDateTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDateTime() {
		return updatedDateTime;
	}
	public void setUpdatedDateTime(Date updatedDateTime) {
		this.updatedDateTime = updatedDateTime;
	}
	@Override
	public String toString() {
		return "Semester [id=" + id + ", semester=" + semester + ", year=" + year + ", isActive=" + isActive
				+ ", createdBy=" + createdBy + ", createdDateTime=" + createdDateTime + ", updatedBy=" + updatedBy
				+ ", updatedDateTime=" + updatedDateTime + "]";
	}
	public Semester() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Semester(int id, String semester, int year, int isActive, String createdBy, Date createdDateTime,
			String updatedBy, Date updatedDateTime) {
		super();
		this.id = id;
		this.semester = semester;
		this.year = year;
		this.isActive = isActive;
		this.createdBy = createdBy;
		this.createdDateTime = createdDateTime;
		this.updatedBy = updatedBy;
		this.updatedDateTime = updatedDateTime;
	}
	
	
}
