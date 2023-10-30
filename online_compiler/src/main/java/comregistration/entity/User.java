
package comregistration.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@jakarta.persistence.Entity
@Table(name = "onlineCompiler")
public class User {

	private String name;

	private String phone;
//	private String gender;
	private String email;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String username;
	private String password;
	private String confirmpassword;
	 @Column(nullable = false, columnDefinition = "int default 1")
	private int isStudent=1;
	 @Column(nullable = false, columnDefinition = "int default 0")
	private int isInstructor=0;
	 @Column(nullable = false, columnDefinition = "int default 0")
	private int isAdmin=0;
	 @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Courses> adaptedCourses;
	
	 
	
	
	
	
	public List<Courses> getAdaptedCourses() {
		return adaptedCourses;
	}

	public void setAdaptedCourses(List<Courses> adaptedCourses) {
		this.adaptedCourses = adaptedCourses;
	}

//	public void setAdaptedCourses(Courses adaptedCourses) {
//		this.adaptedCourses.add( adaptedCourses);
//	}
	public int getIsStudent() {
		return isStudent;
	}

	public void setIsStudent(int isStudent) {
		this.isStudent = isStudent;
	}

	public int getIsInstructor() {
		return isInstructor;
	}

	public void setIsInstructor(int isInstructor) {
		this.isInstructor = isInstructor;
	}

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	
	@OneToMany
	private List<Code> codeList;

	public String getConfirmpassword() {
		return confirmpassword;
	}

	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}

	public List<Code> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<Code> codeList) {
		this.codeList = codeList;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

//	public String getGender() {
//		return gender;
//	}
//
//	public void setGender(String gender) {
//		this.gender = gender;
//	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



	@Override
	public String toString() {
		return "User [name=" + name + ", phone=" + phone + ", email=" + email + ", id=" + id + ", username=" + username
				+ ", password=" + password + ", confirmpassword=" + confirmpassword + ", isStudent=" + isStudent
				+ ", isInstructor=" + isInstructor + ", isAdmin=" + isAdmin + ", adaptedCourses=" + adaptedCourses
				+ ", codeList=" + codeList + "]";
	}

}
