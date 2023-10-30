package comregistration.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(unique = true)
	private String userName;
	private Date loginTime;
	private Date logoutTime;
	private int activeStatus;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public Date getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}
	public int getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}
	@Override
	public String toString() {
		return "UserStatus [id=" + id + ", userName=" + userName + ", loginTime=" + loginTime + ", logoutTime="
				+ logoutTime + ", activeStatus=" + activeStatus + "]";
	}
	public UserStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserStatus(int id, String userName, Date loginTime, Date logoutTime, int activeStatus) {
		super();
		this.id = id;
		this.userName = userName;
		this.loginTime = loginTime;
		this.logoutTime = logoutTime;
		this.activeStatus = activeStatus;
	}
	
	
	
}
