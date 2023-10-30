package comregistration.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import comregistration.Dao.Dao;
import comregistration.entity.Code;
import comregistration.entity.OcDivConfig;
import comregistration.entity.UserDTO;
import comregistration.entity.UserDTO2;
import comregistration.repo.DivConfigRepo;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	DivConfigRepo divConfig;
	@Autowired
	DivConfigRepo repo;

//	public String getOutput(User user) {
//		ActualService s = new ActualService();
//		String output = "";
//		try {
//			output += s.compile(user.getFilename(), user.getCode(), user.getParameter());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			return e.toString();
//		}
//
//		return output;
//	}

	public String getOutput2(UserDTO user) {
		ActualService s = new ActualService();
		String output = "";
		String error = "";
		try {
			output += s.compile(user.getFilename(), user.getCode(), user.getParam());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			error = e.toString();
			return error;
		}

		return output;
	}

	public List<String> streamList(List<Code> codeList, List<String> newList) {

		for (Code obj : codeList) {
			newList.add(obj.getFileName());
		}
		return newList;
	}

	public String automaticPostrerunOnServersTables(String sql) {
		Dao dao = new Dao();
		try {

//				 Table structure 
			String data = dao.connectReturn(sql);
			BufferedReader br = new BufferedReader(new StringReader(data));
			String line = "";
			String tableSyntax = "<table>" + "\n" + "<thead>" + "\n" + "<tr>";
			String header = br.readLine();
			String[] tableHead = header.split("~");

			for (String s : tableHead) {
				tableSyntax += "<th>" + s + "</th>" + "\n";
			}

			tableSyntax += "</tr>" + "\n" + "</thead>" + "\n" + "<tbody>" + "\n";

			while ((line = br.readLine()) != null) {
				tableSyntax += "<tr>" + "\n";
				String[] temp = line.split("~");
				for (String s : temp) {
					tableSyntax += "<td>" + s + "</td>" + "\n";
				}
				tableSyntax += "</tr>" + "\n";
			}

			return tableSyntax += "</tbody>" + "</table>";

//				System.out.println(tableSyntax);
//				 
//				return dao.connectReturn(decodedRequestBody);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			return e.toString();
		}

	}

	@Override
	public String runOnServer(String userName, String password, String hostName, int port, UserDTO userDto,
			String language, String user, String userPassword) {
		JSch jsch = new JSch();
		StringBuilder output = new StringBuilder();

		Session session = null;
		ChannelExec channel = null;

		try {
			session = jsch.getSession(user, hostName, port);
			session.setPassword(userPassword);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			channel = (ChannelExec) session.openChannel("exec");

			String runner = "";
			String compiler = "";

			String fileName = userDto.getFilename();
			String code = userDto.getCode();
			String ext = fileName.split("\\.")[1];

			if (ext.equals("c")) {
				compiler = "gcc";
				runner = "./";
			}

			StringBuilder fullCommand = new StringBuilder();

			// Write code to a file

			fullCommand.append("mkdir -p /home/" + user + "/EqCompiler/" + language + "/").append("\n");
			fullCommand.append("cd /home/" + user + "/EqCompiler/" + language + "/").append("\n");

			fullCommand.append("echo '").append(code).append("' > ").append(fileName).append("\n");

			// Compile code if it's C
			if (language.equalsIgnoreCase("C")) {
				fullCommand.append(compiler).append(" -o ").append(fileName.replace(".c", "")).append(" ")
						.append(fileName).append("\n");
				// Run the compiled/interpreted code
				fullCommand.append(runner).append(fileName.replace(".c", "")).append(" ").append(userDto.getParam())
						.append("\n");
			} else if (language.equalsIgnoreCase("java")) {
				fullCommand.append("javac ").append(fileName).append("\n");
				fullCommand.append("java ").append(fileName.replace(".java", "")).append(" ").append(userDto.getParam())
						.append("\n");
			} else if (language.equalsIgnoreCase("python")) {
				fullCommand.append("python3 ").append(fileName).append("\n");

			} else if (language.equalsIgnoreCase("js")) {
				fullCommand.append("node ").append(fileName).append(" ").append(userDto.getParam()).append("\n");

			} else if (language.equalsIgnoreCase("bash")) {
				fullCommand.append("bash ").append(fileName).append(" ").append(userDto.getParam()).append("\n");

			} else if (language.equalsIgnoreCase("cpp")) {
				fullCommand.append("g++").append(" -o ").append(fileName.replace(".cpp", "")).append(" ")
						.append(fileName).append("\n");
				fullCommand.append("./").append(fileName.replace(".cpp", "")).append(" ").append(userDto.getParam())
						.append("\n");
			} else if (language.equalsIgnoreCase("perl")) {

				fullCommand.append("perl ").append(fileName).append(" ").append(userDto.getParam()).append("\n");
			} else if (language.equalsIgnoreCase("swift")) {
				fullCommand.append("swiftc ").append(fileName).append(" -o ").append(fileName.replace(".swift", ""))
						.append("\n");

				fullCommand.append("./").append(fileName.replace(".swift", "")).append("\n");
			} else if (language.equalsIgnoreCase("c_sharp")) {
				fullCommand.append("mcs ").append(fileName).append("\n");
				fullCommand.append("mono ").append(fileName.replace("cs", "exe")).append("\n");

			} else if (language.equalsIgnoreCase("kotlin")) {
				fullCommand.append("/home/azeem/.sdkman/candidates/kotlin/current/bin/kotlinc ").append(fileName)
						.append(" -include-runtime -d ").append(fileName.replace("kt", "jar")).append("\n");
				fullCommand.append("/home/azeem/.sdkman/candidates/kotlin/current/bin/kotlin ")
						.append(fileName.replace("kt", "jar")).append("\n");

			} else if (language.equalsIgnoreCase("php")) {
				fullCommand.append("php ").append(fileName).append(" ").append(userDto.getParam()).append("\n");

			} else if (language.equalsIgnoreCase("html")) {
				fullCommand.append("xdg-open ").append(fileName).append(" ").append(userDto.getParam()).append("\n");

			}

			channel.setCommand(fullCommand.toString());

			InputStream in = channel.getInputStream();
			InputStream err = channel.getErrStream();

			channel.connect();

			// Use BufferedReader for reading the output
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				String line;

				while ((line = reader.readLine()) != null) {
					output.append(line).append("\n");
				}
			}

			try (BufferedReader errReader = new BufferedReader(new InputStreamReader(err))) {
				String line;

				while ((line = errReader.readLine()) != null) {
					output.append(line).append("\n");
				}
			}

			if (channel != null) {
				channel.disconnect();
			}

			if (session != null) {
				session.disconnect();
			}

			System.out.println("Output received : " + output);
			return output.toString();

		} catch (JSchException | IOException e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		} finally {
			// Ensure that the channel and session are properly closed
			if (channel != null) {
				channel.disconnect();
			}

			if (session != null) {
				session.disconnect();
			}
		}
	}

//	Courses RUN ON SERVER

	public String coursesRunOnServer(String userName, String password, String hostName, int port, UserDTO2 userDto,
	        String language, String user, String userPassword, String courseCode, int section) {

	    JSch jsch = new JSch();
	    StringBuilder output = new StringBuilder();

	    Session session = null;
	    ChannelExec channel = null;

	    try {
	        session = jsch.getSession(user, hostName, port);
	        session.setPassword(userPassword);
	        session.setConfig("StrictHostKeyChecking", "no");
	        session.connect();

	        channel = (ChannelExec) session.openChannel("exec");

	        String runner = "";
	        String compiler = "";

	        String fileName = userDto.getFilename();
	        String code = userDto.getCode();
	        String ext = fileName.split("\\.")[1];

	        if (ext.equals("c")) {
	            compiler = "gcc";
	            runner = "./";
	        }

	        StringBuilder fullCommand = new StringBuilder();

	        // Write code to a file

	        fullCommand.append("mkdir -p /home/" + user + "/EqCompiler/" + language + "/" + courseCode + "/" + section + "/")
	                .append("\n");
	        System.out.println(
	                "mkdir -p /home/" + user + "/EqCompiler/" + language + "/" + courseCode + "/" + section + "/");
	        fullCommand.append(
	                "cd /home/" + user + "/EqCompiler/" + language + "/" + courseCode + "/" + section + "/").append("\n");

	        fullCommand.append("echo '").append(code).append("' > ").append(fileName).append("\n");

	        // Compile code if it's C
	        if (language.equalsIgnoreCase("C")) {
	            fullCommand.append(compiler).append(" -o ").append(fileName.replace(".c", "")).append(" ")
	                    .append(fileName).append("\n");
	            // Run the compiled/interpreted code
	            fullCommand.append(runner).append(fileName.replace(".c", "")).append(" ").append(userDto.getParam())
	                    .append("\n");
	        } else if (language.equalsIgnoreCase("java")) {
	            fullCommand.append("javac ").append(fileName).append("\n");
	            fullCommand.append("java ").append(fileName.replace(".java", "")).append(" ").append(userDto.getParam())
	                    .append("\n");
	        } else if (language.equalsIgnoreCase("python")) {
	            fullCommand.append("python3 ").append(fileName).append("\n");

	        } else if (language.equalsIgnoreCase("js")) {
	            fullCommand.append("node ").append(fileName).append(" ").append(userDto.getParam()).append("\n");

	        } else if (language.equalsIgnoreCase("bash")) {
	            fullCommand.append("bash ").append(fileName).append(" ").append(userDto.getParam()).append("\n");

	        } else if (language.equalsIgnoreCase("cpp")) {
	            fullCommand.append("g++").append(" -o ").append(fileName.replace(".cpp", "")).append(" ")
	                    .append(fileName).append("\n");
	            fullCommand.append("./").append(fileName.replace(".cpp", "")).append(" ").append(userDto.getParam())
	                    .append("\n");
	        } else if (language.equalsIgnoreCase("perl")) {

	            fullCommand.append("perl ").append(fileName).append(" ").append(userDto.getParam()).append("\n");
	        } else if (language.equalsIgnoreCase("swift")) {
	            fullCommand.append("swiftc ").append(fileName).append(" -o ").append(fileName.replace(".swift", ""))
	                    .append("\n");

	            fullCommand.append("./").append(fileName.replace(".swift", "")).append("\n");
	        } else if (language.equalsIgnoreCase("c_sharp")) {
	            fullCommand.append("mcs ").append(fileName).append("\n");
	            fullCommand.append("mono ").append(fileName.replace("cs", "exe")).append("\n");

	        } else if (language.equalsIgnoreCase("kotlin")) {
	            fullCommand.append("/home/azeem/.sdkman/candidates/kotlin/current/bin/kotlinc ").append(fileName)
	                    .append(" -include-runtime -d ").append(fileName.replace("kt", "jar")).append("\n");
	            fullCommand.append("/home/azeem/.sdkman/candidates/kotlin/current/bin/kotlin ")
	                    .append(fileName.replace("kt", "jar")).append("\n");

	        } else if (language.equalsIgnoreCase("php")) {
	            fullCommand.append("php ").append(fileName).append(" ").append(userDto.getParam()).append("\n");

	        } else if (language.equalsIgnoreCase("html")) {
	            fullCommand.append("xdg-open ").append(fileName).append(" ").append(userDto.getParam()).append("\n");

	        }

	        channel.setCommand(fullCommand.toString());

	        InputStream in = channel.getInputStream();
	        InputStream err = channel.getErrStream();

	        channel.connect();

	        // Use BufferedReader for reading the output
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        String line;

	        while ((line = reader.readLine()) != null) {
	            output.append(line).append("\n");
	        }

	        BufferedReader errReader = new BufferedReader(new InputStreamReader(err));

	        while ((line = errReader.readLine()) != null) {
	            output.append(line).append("\n");
	        }

	        reader.close();
	        errReader.close();

	        if (channel != null) {
	            channel.disconnect();
	        }

	        if (session != null) {
	            session.disconnect();
	        }

	        System.out.println("Output received : " + output);
	        return output.toString();

	    } catch (JSchException | IOException e) {
	        e.printStackTrace();
	        return "Error: " + e.getMessage();
	    } finally {
	        if (channel != null && channel.isConnected()) {
	            channel.disconnect();
	        }
	        if (session != null && session.isConnected()) {
	            session.disconnect();
	        }
	    }
	}



	@Override
	public String automaticPostresTables(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDivConfig() {
		OcDivConfig obj = new OcDivConfig();
		obj.setLanguage("Java");
		obj.setIconPath("images/java-logo-png-transparent.png");
		obj.setTitle("Java Compiler");
		obj.setOption("programming");
		repo.save(obj);

		OcDivConfig obj1 = new OcDivConfig();
		obj1.setLanguage("Python");
		obj1.setIconPath("images/python_logo.png");
		obj1.setTitle("Python Compiler");
		obj1.setOption("programming");
		repo.save(obj1);

		OcDivConfig obj2 = new OcDivConfig();
		obj2.setLanguage("C");
		obj2.setIconPath("images/c_logo.png");
		obj2.setTitle("C Compiler");
		obj2.setOption("programming");
		repo.save(obj2);

		OcDivConfig obj3 = new OcDivConfig();
		obj3.setLanguage("Cpp");
		obj3.setIconPath("images/c++_logo.svg");
		obj3.setTitle("Cpp compiler");
		obj3.setOption("programming");
		repo.save(obj3);

		OcDivConfig obj4 = new OcDivConfig();
		obj4.setLanguage("Js");
		obj4.setIconPath("images/js_logo.png");
		obj4.setTitle("Node Js Compiler");
		obj4.setOption("scripting");
		repo.save(obj4);

		OcDivConfig obj5 = new OcDivConfig();
		obj5.setLanguage("Bash");
		obj5.setIconPath("images/Bash_logo.png");
		obj5.setTitle("Bash Scripting");
		obj5.setOption("scripting");
		repo.save(obj5);

		OcDivConfig obj6 = new OcDivConfig();
		obj6.setLanguage("Postgres");
		obj6.setIconPath("images/postgres_logo.png");
		obj6.setTitle("Postgres");
		obj6.setOption("dataBase");
		repo.save(obj6);

		OcDivConfig obj7 = new OcDivConfig();
		obj7.setLanguage("Linux");
		obj7.setIconPath("images/Linux_logo.png");
		obj7.setTitle("Linux Os");
		obj7.setOption("operatingSystem");
		repo.save(obj7);

		OcDivConfig obj8 = new OcDivConfig();
		obj8.setLanguage("Windows");
		obj8.setIconPath("images/windows_logo.png");
		obj8.setTitle("Windows Os");
		obj8.setOption("operatingSystem");
		repo.save(obj8);

		OcDivConfig obj9 = new OcDivConfig();
		obj9.setLanguage("perl");
		obj9.setIconPath("images/perl_logo.png");
		obj9.setTitle("Perl");
		obj9.setOption("scripting");
		repo.save(obj9);

		OcDivConfig obj10 = new OcDivConfig();
		obj10.setLanguage("php");
		obj10.setIconPath("images/php.png");
		obj10.setTitle("php");
		obj10.setOption("scripting");
		repo.save(obj10);

		OcDivConfig obj11 = new OcDivConfig();
		obj11.setLanguage("swift");
		obj11.setIconPath("images/swift.png");
		obj11.setTitle("swift");
		obj11.setOption("programming");
		repo.save(obj11);

		OcDivConfig obj12 = new OcDivConfig();
		obj12.setLanguage("kotlin");
		obj12.setIconPath("images/kotlin.png");
		obj12.setTitle("kotlin");
		obj12.setOption("programming");
		repo.save(obj12);

		OcDivConfig obj13 = new OcDivConfig();
		obj13.setLanguage("css");
		obj13.setIconPath("images/css.png");
		obj13.setTitle("css");
		obj13.setOption("scripting");
		repo.save(obj13);

		OcDivConfig obj14 = new OcDivConfig();
		obj14.setLanguage("html");
		obj14.setIconPath("images/HTML.png");
		obj14.setTitle("html");
		obj14.setOption("scripting");
		repo.save(obj14);

		OcDivConfig obj15 = new OcDivConfig();
		obj15.setLanguage("mysql");
		obj15.setIconPath("images/logo-mysql.png");
		obj15.setTitle("mysql");
		obj15.setOption("dataBase");
		repo.save(obj15);

		OcDivConfig obj16 = new OcDivConfig();
		obj16.setLanguage("oracle");
		obj16.setIconPath("images/Oracle-Logo-PNG-Image.png");
		obj16.setTitle("oracle");
		obj16.setOption("dataBase");
		repo.save(obj16);

		OcDivConfig obj17 = new OcDivConfig();
		obj17.setLanguage("c_sharp");
		obj17.setIconPath("images/Logo_C_sharp.svg.png");
		obj17.setTitle("c_sharp");
		obj17.setOption("programming");
		repo.save(obj17);

	}

	public String directoryTreeFetchedFromServer(String userName, String password, String hostName, int port,
			String user, String treeHeader, String userPassword) {
		JSch jsch = new JSch();
		StringBuilder output = new StringBuilder();

		try {
			Session session = jsch.getSession(user, hostName, port);
			session.setPassword(userPassword);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			ChannelExec channel = (ChannelExec) session.openChannel("exec");

			StringBuilder fullCommand = new StringBuilder();

			// Write code to a file
			fullCommand.append("mkdir -p /home/" + user + "/EqCompiler/").append("\n");
			fullCommand.append("cd /home/" + user + "/EqCompiler/").append("\n");
			fullCommand.append("output=\"\"\n" + "for i in `ls`\n" + "do\n" + "    output+='<li class=\"clickable\">'\n"
					+ "    output+=`echo \"${i}\"`\n" + "    output+='<ul>'\n" + "    if [ -d ${i} ];then\n"
					+ "        cd ${i}\n" + "        for j in `ls`\n" + "        do\n" + "            output+='<li>'\n"
					+ "            output+=`echo $j`\n" + "            output+='</li>'\n" + "        done\n"
					+ "        cd ..\n" + "    else \n" + "        continue\n" + "    fi\n" + "    output+='</ul>'\n"
					+ "    output+='</li>'\n" + "done\n" + "echo ${output}\n").append("\n");
//			fullCommand.append("output=\"\"\n" + "for i in *\n" + "do\n" + "    if [ -d \"${i}\" ]; then\n"
//					+ "        output+=\"<li>${i}<ul>\"\n" + "        cd \"${i}\"\n" + "        for j in *\n"
//					+ "        do\n" + "            if [ -d \"${j}\" ]; then\n"
//					+ "                output+=\"<li>${j}<ul>\"\n" + "                cd \"${j}\"\n"
//					+ "                for k in *\n" + "                do\n"
//					+ "                    if [ -d \"${k}\" ]; then\n"
//					+ "                        output+=\"<li class=\\\"clickable\\\">${k}<ul>\"\n"
//					+ "                        cd \"${k}\"\n" + "                        for l in *\n"
//					+ "                        do\n" + "                            output+=\"<li>${l}</li>\"\n"
//					+ "                        done\n" + "                        cd ..\n"
//					+ "                        output+=\"</ul>\"\n" + "                    else\n"
//					+ "                        output+=\"<li>${k}</li>\"\n" + "                    fi\n"
//					+ "                done\n" + "                cd ..\n" + "                output+=\"</ul>\"\n"
//					+ "            else\n" + "                output+=\"<li>${j}</li>\"\n" + "            fi\n"
//					+ "        done\n" + "        cd ..\n" + "        output+=\"</ul></li>\"\n" + "    else\n"
//					+ "        output+=\"<li>${i}</li>\"\n" + "    fi\n" + "done\n" + "echo \"${output}\"\n" + "")
//					.append("\n");

			channel.setCommand(fullCommand.toString());

			InputStream in = channel.getInputStream();
			InputStream err = channel.getErrStream();

			channel.connect();

			// Use BufferedReader for reading the output
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;

			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}

			BufferedReader errReader = new BufferedReader(new InputStreamReader(err));

			while ((line = errReader.readLine()) != null) {
				output.append(line).append("\n");
			}

			reader.close();
			errReader.close();

			if (channel != null) {
				channel.disconnect();
			}

			if (session != null) {
				session.disconnect();
			}

			System.out.println("Output received : " + output);
			return output.toString();

		} catch (JSchException | IOException e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	public String wsshConnection(String userName, String password, String hostName, int port) {
		JSch jsch = new JSch();
		StringBuilder output = new StringBuilder();

		try {
			Session session = jsch.getSession(userName, hostName, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(1500);

			ChannelExec channel = (ChannelExec) session.openChannel("exec");

			StringBuilder fullCommand = new StringBuilder();

			// Write code to a file
//			fullCommand.append("mkdir -p /home/" + userName + "/EqCompiler/" + user + "/").append("\n");
//			fullCommand.append("cd /home/" + userName + "/EqCompiler/" + user + "/").append("\n");
			fullCommand.append("wssh &").append("\n");

			channel.setCommand(fullCommand.toString());

			InputStream in = channel.getInputStream();
			InputStream err = channel.getErrStream();

			channel.connect();

			// Use BufferedReader for reading the output
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;

			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}

			BufferedReader errReader = new BufferedReader(new InputStreamReader(err));

			while ((line = errReader.readLine()) != null) {
				output.append(line).append("\n");
			}

			reader.close();
			errReader.close();

			if (channel != null) {
				channel.disconnect();
			}

			if (session != null) {
				session.disconnect();
			}

			System.out.println("Output received : " + output);
			return output.toString();

		} catch (JSchException | IOException e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	public String readDirectoryTreeFiles(String userName, String password, String hostName, int port, String fileName,
			String user, String userPassword) {
		JSch jsch = new JSch();
		StringBuilder output = new StringBuilder();

		try {
			Session session = jsch.getSession(user, hostName, port);
			session.setPassword(userPassword);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			ChannelExec channel = (ChannelExec) session.openChannel("exec");

			StringBuilder fullCommand = new StringBuilder();
			fullCommand.append("a=$(find \"/home/").append(user).append("/EqCompiler/\" -type f -name \"")
					.append(fileName).append("\")").append("\n");
			fullCommand.append("var=$(echo \"$a\" | tr '/' '\\n' | wc -l)").append("\n");
			fullCommand.append("if [ $var -gt 6 ]; then").append("\n");
			fullCommand.append("    out=$(echo \"$a\" | awk -F '/' '{print $6}')").append("\n");
			fullCommand.append("fi").append("\n");
			fullCommand.append("echo \"${out}|\"").append("\n");
			fullCommand.append("p=`find /home/").append(user).append("/EqCompiler/").append("/ -name ").append(fileName)
					.append(" -type f`").append("\n");
			fullCommand.append("cat ${p}").append("\n");
			channel.setCommand(fullCommand.toString());

			InputStream in = channel.getInputStream();
			InputStream err = channel.getErrStream();

			channel.connect();

			// Use BufferedReader for reading the output
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;

			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}

			BufferedReader errReader = new BufferedReader(new InputStreamReader(err));

			while ((line = errReader.readLine()) != null) {
				output.append(line).append("\n");
			}

			reader.close();
			errReader.close();

			if (channel != null) {
				channel.disconnect();
			}

			if (session != null) {
				session.disconnect();
			}

			System.out.println("Output received : " + output);
			return output.toString();

		} catch (JSchException | IOException e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	public String linuxUser(String userName, String password, String hostName, int port, String linuxUser,
			String linuxPassword) {
		JSch jsch = new JSch();
		StringBuilder output = new StringBuilder();

		try {
			Session session = jsch.getSession(userName, hostName, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			ChannelExec channel = (ChannelExec) session.openChannel("exec");

			StringBuilder fullCommand = new StringBuilder();
			System.out.println("Before linuxUser script impl");
			fullCommand.append("echo ").append(password).append(" | ").append("sudo -S bash addUser.sh ")
					.append(linuxUser).append(" ").append(linuxPassword).append("\n");
			System.out.println("After script impl");
			channel.setCommand(fullCommand.toString());

			InputStream in = channel.getInputStream();
			InputStream err = channel.getErrStream();

			channel.connect();

			// Use BufferedReader for reading the output
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;

			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}

			BufferedReader errReader = new BufferedReader(new InputStreamReader(err));

			while ((line = errReader.readLine()) != null) {
				output.append(line).append("\n");
			}

			reader.close();
			errReader.close();

			if (channel != null) {
				channel.disconnect();
			}

			if (session != null) {
				session.disconnect();
			}

			System.out.println("Output received : " + output);
			return output.toString();

		} catch (JSchException | IOException e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	public String postgresUser(String userName, String password, String hostName, int port, String postgresUser,
			String postgresPassword) {
		System.out.println("INSIDE POSTGRES USER :-)");
		JSch jsch = new JSch();
		StringBuilder output = new StringBuilder();
		System.out.println("Inside postgresUser :" + userName + " " + password + " " + hostName + " " + port + " "
				+ postgresUser + " " + postgresPassword);
		try {
			Session session = jsch.getSession(userName, hostName, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			ChannelExec channel = (ChannelExec) session.openChannel("exec");

			StringBuilder fullCommand = new StringBuilder();
			System.out.println("Woring for Postgres");
			fullCommand.append("echo ").append(password).append(" | ").append("sudo -S bash addPostgresUser.sh ")
					.append(postgresUser).append(" ").append(postgresPassword).append("\n");
			System.out.println("Working for MySql");
			fullCommand.append("echo ").append(password).append(" | ").append("sudo -S bash addMySqlUser.sh ")
					.append(postgresUser).append(" ").append(postgresPassword).append("\n");

			channel.setCommand(fullCommand.toString());

			InputStream in = channel.getInputStream();
			InputStream err = channel.getErrStream();

			channel.connect();

			// Use BufferedReader for reading the output
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;

			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}

			BufferedReader errReader = new BufferedReader(new InputStreamReader(err));

			while ((line = errReader.readLine()) != null) {
				output.append(line).append("\n");
			}

			reader.close();
			errReader.close();

			if (channel != null) {
				channel.disconnect();
			}

			if (session != null) {
				session.disconnect();
			}

			System.out.println("Output received : " + output);
			return output.toString();

		} catch (JSchException | IOException e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	public String setServerConfiguration(String data) {

		return null;
	}

}
