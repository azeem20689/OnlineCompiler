package comregistration.entity;

public class FormData {

	private String userName;
	private String ip;
	private int port;
	private String password;
	private String language;
	private String type;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public FormData(String userName, String ip, int port, String password, String language, String type) {
		super();
		this.userName = userName;
		this.ip = ip;
		this.port = port;
		this.password = password;
		this.language = language;
		this.type = type;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	public FormData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getType() {
		return type;
	}
	@Override
	public String toString() {
		return "FormData [userName=" + userName + ", ip=" + ip + ", port=" + port + ", password=" + password
				+ ", language=" + language + ", type=" + type + "]";
	}
	public void setType(String type) {
		this.type = type;
	}
}
