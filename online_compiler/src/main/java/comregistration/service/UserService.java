package comregistration.service;

import comregistration.entity.Code;
import comregistration.entity.User;

import java.sql.SQLException;
import java.util.*;

import comregistration.entity.UserDTO;
import comregistration.entity.UserDTO2;

public interface UserService {

//	public String getOutput(User user);

	public String getOutput2(UserDTO userDto);

	public List<String> streamList(List<Code> codeList, List<String> newList);

	public String automaticPostresTables(String sql) throws SQLException;

	public String runOnServer(String userName, String password, String hostName, int port, UserDTO userDto,
			String language, String user, String userPassword);

	public void setDivConfig();

	public String directoryTreeFetchedFromServer(String userName, String password, String hostName, int port,
			String user, String treeHeader, String userPassword);

	public String wsshConnection(String userName, String password, String hostName, int port);

	public String readDirectoryTreeFiles(String userName, String password, String hostName, int port, String fileName,
			String user, String userPassword);

	public String linuxUser(String userName, String password, String hostName, int port, String linuxUser,
			String linuxPassword);

	public String postgresUser(String userName, String password, String hostName, int port, String postgresUser,
			String postgresPassword);

	public String setServerConfiguration(String data);

	public String coursesRunOnServer(String userName, String password, String hostName, int port, UserDTO2 userDto,
			String language, String user, String userPassword, String courseCode, int section);
}
