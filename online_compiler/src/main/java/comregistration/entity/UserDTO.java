package comregistration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


public class UserDTO {

	public String filename;
	public String param;
	public String code;
	
	
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
		return "UserDTO [filename=" + filename + ", param=" + param + ", code=" + code + "]";
	}
	
	
	
}
