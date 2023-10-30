package comregistration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


public class UserDTO2 {

	public String filename;
	public String param;
	public String code;
	public String courseCode;
	
	
	
	
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "UserDTO2 [filename=" + filename + ", param=" + param + ", code=" + code + ", courseCode=" + courseCode
				+ "]";
	}
	
	
	
}
