package comregistration.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Courses {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String courseCode;
	private int section;
	private int numberOfStudents;
	private String language;
	private String semester;
	private int year;
	private int isActive;
	private String createdBy;
	private Date createDateTime;
	private String updatedBy;
	private Date updatedDateTime;
	private int labStartTime;
	private int labEndTime;
	private String weekDay;
	@ManyToOne
    @JoinColumn(name = "user_id",  referencedColumnName = "id")
	 private User user;

   
	
	
	
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public int getLabStartTime() {
		return labStartTime;
	}
	public void setLabStartTime(int labStartTime) {
		this.labStartTime = labStartTime;
	}
	public int getLabEndTime() {
		return labEndTime;
	}
	public void setLabEndTime(int labEndTime) {
		this.labEndTime = labEndTime;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public int getSection() {
		return section;
	}
	public void setSection(int section) {
		this.section = section;
	}
	public int getNumberOfStudents() {
		return numberOfStudents;
	}
	public void setNumberOfStudents(int numberOfStudents) {
		this.numberOfStudents = numberOfStudents;
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
	public Date getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(Date date) {
		this.createDateTime = date;
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

		public Courses() {
		super();
		// TODO Auto-generated constructor stub
	}
		@Override
		public String toString() {
			return "Courses [id=" + id + ", courseCode=" + courseCode + ", section=" + section + ", numberOfStudents="
					+ numberOfStudents + ", language=" + language + ", semester=" + semester + ", year=" + year
					+ ", isActive=" + isActive + ", createdBy=" + createdBy + ", createDateTime=" + createDateTime
					+ ", updatedBy=" + updatedBy + ", updatedDateTime=" + updatedDateTime + ", labStartTime="
					+ labStartTime + ", labEndTime=" + labEndTime + ", weekDay=" + weekDay + ", user=" + user + "]";
		}
		
		

	
	
}
