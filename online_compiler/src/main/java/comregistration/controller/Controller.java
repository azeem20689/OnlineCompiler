package comregistration.controller;

import java.io.BufferedReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import org.springframework.http.MediaType;
import org.apache.catalina.Server;
import org.hibernate.Hibernate;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import comregistration.Dao.Dao;
import comregistration.entity.Admin;
import comregistration.entity.Code;
import comregistration.entity.Courses;
import comregistration.entity.FormArray;
import comregistration.entity.FormData;
import comregistration.entity.OcDivConfig;
import comregistration.entity.RequestData;
import comregistration.entity.Semester;
import comregistration.entity.ServerConfig;
import comregistration.entity.Technology;
import comregistration.entity.User;
import comregistration.entity.UserDTO;
import comregistration.entity.UserDTO2;
import comregistration.entity.UserStatus;
import comregistration.repo.AdminRepo;
import comregistration.repo.CodeRepo;
import comregistration.repo.CoursesRepo;
import comregistration.repo.DivConfigRepo;
import comregistration.repo.Repo;
import comregistration.repo.SemesterRepo;
import comregistration.repo.ServerConfigRepo;
import comregistration.repo.TechnologyRepo;
import comregistration.repo.UserStatusRepo;
import comregistration.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@org.springframework.stereotype.Controller
public class Controller {

	@Autowired
	private Repo r;
	@Autowired
	private CodeRepo cr;
//	@Autowired
//	private Dao dao;

	@Autowired
	private UserServiceImpl usrImpl;
	@Autowired
	private DivConfigRepo divCr;
	@Autowired
	private AdminRepo adminRepo;
	@Autowired
	private ServerConfigRepo serverConfigRepo;
	@Autowired
	private TechnologyRepo technologyRepo;
	@Autowired
	private SemesterRepo semesterRepo;
	@Autowired
	private CoursesRepo coursesRepo;
	@Autowired
	private UserStatusRepo userStatusRepo;
//	Dao dao = new Dao();
	@Autowired
	private Dao dao;


//	@GetMapping("/")
//	public String logIn() {
//
//		return "login";
//	}

//	Log in 

	@GetMapping("/")
	public String startLogin(Model model) {

		User user = new User();
		model.addAttribute("user", user);

//		return "register2";
		return "login";
	}

//	User finalUser;

	@PostMapping("/getLoginDetails")
	public ModelAndView getLoginDetails(@ModelAttribute("user") User user, Model m, HttpSession session) {
		ModelAndView mov = new ModelAndView("login");

		String message;
		User u = null;
		try {
			u = r.getIdByName(user.getUsername());

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (u == null) {
			message = user.getUsername() + " User does not Exist!";
			mov.addObject("message", message);
			return mov;

		}

//		finalUser = u;

//		if (u.getIsInstructor() == 1) {
//			finalInstructor = u;
//		}

		if (u.getPassword().equals(user.getPassword())) {
			String finalUserKey = "finalUser_"+session.getId();
			session.setAttribute("finalUserKey", u);
			System.out.println("Login Success");

			m.addAttribute("test", u);

//			tree

//			List<Code> bashList = cr.bashCodeList("sh", finalUser.getUsername());
//			List<Code> javaList = cr.bashCodeList("java", finalUser.getUsername());
//			List<Code> pythonList = cr.bashCodeList("py", finalUser.getUsername());
//			List<Code> cList = cr.bashCodeList("c", finalUser.getUsername());
//			List<Code> cppList = cr.bashCodeList("cpp", finalUser.getUsername());
//			List<Code> jsList = cr.bashCodeList("js", finalUser.getUsername());
//
//			UserServiceImpl service = new UserServiceImpl();
//
//			List<String> javaFiles = new ArrayList();
//
//			javaFiles = service.streamList(javaList, javaFiles);
//
//			List<String> bashFiles = new ArrayList<>();
//
//			bashFiles = service.streamList(bashList, bashFiles);
//
//			List<String> pythonFiles = new ArrayList<>();
//			pythonFiles = service.streamList(pythonList, pythonFiles);
//
//			List<String> cppFiles = new ArrayList<>();
//			cppFiles = service.streamList(cppList, cppFiles);
//
//			List<String> cFiles = new ArrayList<>();
//			cFiles = service.streamList(cList, cFiles);
//
//			List<String> jsFiles = new ArrayList<>();
//			jsFiles = service.streamList(jsList, jsFiles);
//
//			HashMap<String, List<String>> codeRepoMap = new HashMap<>();
//			codeRepoMap.put("java", javaFiles);
//			codeRepoMap.put("python", pythonFiles);
//			codeRepoMap.put("c", cFiles);
//			codeRepoMap.put("cpp", cppFiles);
//			codeRepoMap.put("bash", bashFiles);
//			codeRepoMap.put("js", jsFiles);
//
//			m.addAttribute("map", codeRepoMap);
			m.addAttribute("userName", u.getUsername());

//			Tree From Server
			Technology treeProgramming = serverConfigRepo.getServerConfig("java");

//			Write code here for forming the tree structure on server base 
//			dao.setConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "");
			User finalUser = (User) session.getAttribute("finalUserKey");
			System.out.println("????????????? FNAL USER "+finalUser.getUsername());
			Timestamp date = new Timestamp(System.currentTimeMillis());
			session.setAttribute("userLoginTime", date);
			Timestamp date1 = (Timestamp) session.getAttribute("userLoginTime");

			try {
				dao.updateUserStatus(finalUser.getUsername(), date1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String treeStructureProgramming = usrImpl.directoryTreeFetchedFromServer(
					treeProgramming.getServerConfig().getUserName(), treeProgramming.getServerConfig().getPassword(),
					treeProgramming.getServerConfig().getIp(), treeProgramming.getServerConfig().getPort(),
					finalUser.getUsername(), "Programming Languages", finalUser.getConfirmpassword());
//			String treeStructureScripting = usrImpl.directoryTreeFetchedFromServer(treeScripting.getUserName(),treeScripting.getPassword(),treeScripting.getIp(),treeScripting.getport(),finalUser.getUsername());

			String tree = "<ul>" + treeStructureProgramming + "</ul>".trim();
			System.out.println("tree :" + tree);
			m.addAttribute("treeStructure", tree);

//			tree structure code closed 

//			Setting up the configuration for Div reflecting on home page for programming languages. 
//			usrImpl.setDivConfig();

			List<OcDivConfig> divConfListScripting = divCr.divConf("scripting");
			List<OcDivConfig> divConfListProgramming = divCr.divConf("programming");
			List<OcDivConfig> divConfListOperatingSystem = divCr.divConf("operatingSystem");
			List<OcDivConfig> divConfListDataBase = divCr.divConf("dataBase");
			m.addAttribute("divConfListScripting", divConfListScripting);
			m.addAttribute("divConfListProgramming", divConfListProgramming);
			m.addAttribute("divConfListOperatingSystem", divConfListOperatingSystem);
			m.addAttribute("divConfListDataBase", divConfListDataBase);

//			wssh turn on 
//			ServerConfig serverConf = serverConfigRepo.getServerConfig("linux");
//			usrImpl.wsshConnection(serverConf.getUserName(), serverConf.getPassword(), serverConf.getIp(),
//					serverConf.getport());

//			wssh code closed 
			mov.setViewName("home");
			return mov;
		} else {
			System.out.println("Login Failed");
			message = "Username Password does not match ! ";
			mov.addObject("message", message);
			return mov;
		}

		// return "loginFailed";

	}

//	SIGN UP
	@GetMapping("/signup")
	public String signup(Model model) {
		User user = new User();
		model.addAttribute("user", user);

		return "register";
	}

	@PostMapping("/registerUser")
	public ModelAndView registerUser(@ModelAttribute("user") User user) {
		ModelAndView mov = new ModelAndView("register");
		String message;
		if (user.getPassword().equals(user.getConfirmpassword())) {
			try {
//				dao.setConnection("jdbc:postgresql://localhost:5432/OnlineCompiler", "postgres", "");
				String linuxPassword = user.getPassword();
//				user.setPassword(Integer.toString(user.getPassword().hashCode()));
//				user.setConfirmpassword(Integer.toString(user.getConfirmpassword().hashCode()));

				Optional<User> tempUser = r.duplicateUser(user.getUsername());
				System.out.println(tempUser);
				if (tempUser.isEmpty()) {
					UserStatus us = new UserStatus();
					us.setUserName(user.getUsername());
					userStatusRepo.save(us);
					System.out.println("Linux User Creation : " + user.getUsername());
					System.out.println("Before ");
					Technology tec = serverConfigRepo.getServerConfig("linux");
					System.out.println("Teck object received : -" + tec.getServerConfig());
//					ServerConfig con = tec.getServerConfig().getu

					String taskCmpl = usrImpl.linuxUser(tec.getServerConfig().getUserName(),
							tec.getServerConfig().getPassword(), tec.getServerConfig().getIp(),
							tec.getServerConfig().getPort(), user.getUsername(), linuxPassword);
					System.out.println(taskCmpl);

					System.out.println("Postgres & MySql User Creation : " + user.getUsername());

					tec = serverConfigRepo.getServerConfig("postgres");
//					ServerConfig conf = tec.getServerConfig();
//					conf = tec.getServerConfig();
//					System.out.println("Conf received for postgres : " + conf);
					String postUser = usrImpl.postgresUser(tec.getServerConfig().getUserName(),
							tec.getServerConfig().getPassword(), tec.getServerConfig().getIp(),
							tec.getServerConfig().getPort(), user.getUsername(), linuxPassword);
					System.out.println(postUser);

//					System.out.println("My Sql User Creation : "+user.getUsername());
//					ServerConfig mySqlConf = serverConfigRepo.getServerConfig("mysql");
//					System.out.println("Conf received for postgres : "+mySqlConf);
					r.save(user);
					message = "User Registered Successfully ";
					mov.addObject("message", message);
					return mov;
				} else {
					message = "Username already exist" + "\n" + "Please choose a different Username";
					mov.addObject("message", message);
					return mov;
				}

			} catch (Exception e) {
				message = e.toString();
				mov.addObject("message", message);
				return mov;
			}
		}
		message = "Password Does not match with Confirm password";
		mov.addObject("message", message);
		return mov;
	}

//	Compiler Start from here:-
	@GetMapping("/startService")
	public String start() {

		return "home";
	}

	@GetMapping("/java")
	public String java() {
//		User u = finalUser;

//		List<Code> list = cr.getCodeByUsername(finalUser.getUsername());
//		finalUser.setCodeList(list);
//		int size = list.size();
//
//		model.addAttribute("user", u);
//		model.addAttribute("list", list);
//		model.addAttribute("size", size);

		return "compiler";
	}

	@PostMapping("/compiler")
	public String compiler(@ModelAttribute("user") User user, Model model) {

		UserServiceImpl obj = new UserServiceImpl();
//		System.out.println("Code output" + "\n" + obj.getOutput(user));
//		User u = new User();
//		Code code = new Code();
//		String output = obj.getOutput(user);
//		u.setOutput(output);
//		code.setOutput(output);
////		=====
//		code.setCode(user.getCode());
//		code.setFileName(user.getFilename());
//		code.setName(finalUser.getUsername());
//		code.setParameter(user.getParameter());
//
//		Code object = cr.getCodeByFileNameAndName(user.getFilename(), finalUser.getUsername());
//
//		if (object == null) {
//
//			cr.save(code);
//		} else {
//
//			cr.delete(object);
//			cr.save(code);
//		}
//
////		====
//		model.addAttribute("user", u);

		return "output";
	}

	@PostMapping("/ajax")
	@ResponseBody
	public HashMap<String, String> handlePostRequest(@RequestBody String requestBody, HttpSession session) {
		// Process the request and generate the response
		HashMap<String, String> map = new HashMap<>();
		System.out.println("/ajax has been fired");
		String file = requestBody;
//		System.out.println(file);
		file = file.split("=")[1];
		System.out.println("File Received : - >>>> " + file);
		String desiredTech = "";
		if (file.split("\\.")[1].equals("java")) {
			desiredTech = "java";
		} else if (file.split("\\.")[1].equals("c")) {
			desiredTech = "c";
		} else if (file.split("\\.")[1].equals("cpp")) {
			desiredTech = "cpp";
		} else if (file.split("\\.")[1].equals("cs")) {
			desiredTech = "cs";
		} else if (file.split("\\.")[1].equals("js")) {
			desiredTech = "js";
		} else if (file.split("\\.")[1].equals("sh")) {
			desiredTech = "bash";
		} else if (file.split("\\.")[1].equals("py")) {
			desiredTech = "python";
		} else if (file.split("\\.")[1].equals("pl")) {
			desiredTech = "perl";
		} else if (file.split("\\.")[1].equals("html")) {
			desiredTech = "html";
		} else if (file.split("\\.")[1].equals("css")) {
			desiredTech = "css";
		} else if (file.split("\\.")[1].equals("php")) {
			desiredTech = "php";
		} else if (file.split("\\.")[1].equals("kt")) {
			desiredTech = "kotlin";
		} else if (file.split("\\.")[1].equals("swift")) {
			desiredTech = "swift";
		}
		System.out.println("Desired TECH >>>>>>>" + desiredTech);

		Technology tec = serverConfigRepo.getServerConfig(desiredTech);
		ServerConfig serverConf = tec.getServerConfig();

		System.out.println(serverConf);
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println(serverConf.getUserName());
		System.out.println(serverConf.getPassword());
		System.out.println(serverConf.getPort());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
		User finalUser = (User) session.getAttribute("finalUserKey");
		String output = usrImpl.readDirectoryTreeFiles(serverConf.getUserName(), serverConf.getPassword(),
				serverConf.getIp(), serverConf.getPort(), file, finalUser.getUsername(),
				finalUser.getConfirmpassword());
		System.out.println("OUTPUT /ajax : " + output);

		if (output.split("\\|").length > 1) {
			String ary[] = output.split("\\|");
			String courseCode = ary[0];
			String code = ary[1];

			map.put("code", output.split("\\|")[1]);
			map.put("fileName", file);
			map.put("courseCode", courseCode);
			return map;

		}

		map.put("code", output);
		map.put("fileName", file);
//		map.put("out", c.getOutput());

		return map;

	}

	@PostMapping("/testing")
	@ResponseBody
	public HashMap<String, String> Testing(@RequestBody String language) {
		String len[] = language.split("=");
		if (len.length >= 2) {

			language = language.toString().split("=")[1];

			System.out.println("Response Language " + language);
			HashMap<String, String> map = new HashMap<>();
			if (language.equalsIgnoreCase("java")) {
				map.clear();
				map.put("fileName", "Example.java");
				map.put("code",
						" import java.util.*;\n" + "import java.io.*;\n \n " + "class Example {\n"
								+ "    public static void main(String[] args) {\n"
								+ "        System.out.println(\"Hello, Java!\");\n" + "    }\n" + "}");

			} else if (language.equalsIgnoreCase("Bash")) {
				map.clear();
				map.put("fileName", "Example.sh");
				map.put("code", "echo Hello World..!!");
			} else if (language.equalsIgnoreCase("Python")) {
				map.clear();
				map.put("fileName", "Example.py");
				map.put("code", "print(\"Hello World..!!\");");
			} else if (language.equalsIgnoreCase("C")) {
				map.clear();
				map.put("fileName", "Example.c");
				map.put("code", "#include <stdio.h>\n" + "\n" + "int main() {\n" + "    // Write C code here\n"
						+ "    printf(\"Hello world\");\n" + "\n" + "    return 0;\n" + "}");
			} else if (language.equalsIgnoreCase("cpp")) {
				map.clear();
				map.put("fileName", "Example.cpp");
				map.put("code", "#include <iostream>\n" + "\n" + "int main() {\n"
						+ "    std::cout << \"Hello, World!\" << std::endl;\n" + "    return 0;\n" + "}\n" + "");
			} else if (language.equalsIgnoreCase("js")) {
				map.clear();
				map.put("fileName", "Example.js");
				map.put("code", "console.log(\"Hello, World!\");\n" + "");
			} else if (language.equalsIgnoreCase("perl")) {
				map.clear();
				map.put("fileName", "Example.pl");
				map.put("code", "print(\"Hello, World from Perl!\");\n" + "");

			} else if (language.equalsIgnoreCase("swift")) {
				map.clear();
				map.put("fileName", "Example.swift");
				map.put("code", "print(\"Hello, World from Swift!\");\n" + "");

			} else if (language.equalsIgnoreCase("c_sharp")) {
				map.clear();
				map.put("fileName", "Example.cs");
				map.put("code",
						"using System;\n" + "\n" + "namespace MyCSharpApp\n" + "{\n" + "    class Program\n" + "    {\n"
								+ "        static void Main(string[] args)\n" + "        {\n"
								+ "            Console.WriteLine(\"Hello, C# on OnlineCompiler!\");\n" + "        }\n"
								+ "    }\n" + "}\n" + "");

			} else if (language.equalsIgnoreCase("kotlin")) {
				map.clear();
				map.put("fileName", "Example.kt");
				map.put("code", "fun main() {\n" + "    println(\"Hello, Kotlin on Linux!\")\n" + "}\n" + "");

			} else if (language.equalsIgnoreCase("php")) {
				map.clear();
				map.put("fileName", "Example.php");
				map.put("code", "<?php\n" + "echo \"Hello, World!\";\n" + "?>\n" + "");

			} else if (language.equalsIgnoreCase("html")) {
				map.clear();
				map.put("fileName", "Example.html");
				map.put("code", "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "    <title>Hello, World!</title>\n"
						+ "</head>\n" + "<body>\n" + "    <h1>Hello, World!</h1>\n" + "</body>\n" + "</html>\n" + "");

			}
			return map;
		}
		return null;
	}

	@PostMapping("/ajaxCompiler")
	@ResponseBody
	public String ajaxCompiler(@RequestBody UserDTO userDTO, HttpSession session) {
		String languageConnector = "";
//		UserServiceImpl obj = new UserServiceImpl();
		System.out.println("/ajaxCompiler URL fired");
		String ext = userDTO.getFilename().toString().split("\\.")[1];
		System.out.println("Ext :" + ext);
		if (ext.equalsIgnoreCase("c")) {
			languageConnector = "c";
		} else if (ext.equalsIgnoreCase("java")) {
			languageConnector = "java";
		} else if (ext.equalsIgnoreCase("py")) {
			languageConnector = "python";
		} else if (ext.equalsIgnoreCase("js")) {
			languageConnector = "js";
		} else if (ext.equalsIgnoreCase("sh")) {
			languageConnector = "bash";
		} else if (ext.equalsIgnoreCase("cpp")) {
			languageConnector = "cpp";
		} else if (ext.equalsIgnoreCase("pl")) {
			languageConnector = "perl";
		} else if (ext.equalsIgnoreCase("swift")) {
			languageConnector = "swift";
		} else if (ext.equalsIgnoreCase("cs")) {
			languageConnector = "c_sharp";
		} else if (ext.equalsIgnoreCase("kt")) {
			languageConnector = "kotlin";
		} else if (ext.equalsIgnoreCase("php")) {
			languageConnector = "php";
		} else if (ext.equalsIgnoreCase("html")) {
			languageConnector = "html";
		}
		String languageConnectorKey = "languageConnector_"+session.getId();
		session.setAttribute("languageConnectorKey", languageConnector);
		 languageConnector = (String) session.getAttribute("languageConnectorKey");
		Technology tec = serverConfigRepo.getServerConfig(languageConnector);
		ServerConfig serverConfig = tec.getServerConfig();

		System.out.println("Server Conf : " + serverConfig);
		User finalUser = (User) session.getAttribute("finalUserKey");
		System.out.println(finalUser.getUsername());
		return usrImpl.runOnServer(serverConfig.getUserName(), serverConfig.getPassword(), serverConfig.getIp(),
				serverConfig.getPort(), userDTO, languageConnector, finalUser.getUsername(),
				finalUser.getConfirmpassword());
//		return usrImpl.runOnServer("strangebook", "book@123", "192.168.0.48", 22, userDTO);

	}

	@PostMapping("/saveCompiler")
	@ResponseBody
	public String ajaxCompiler(@RequestBody User user) {
//		UserServiceImpl obj = new UserServiceImpl();
		return "response from server";

	}

//	@PostMapping("/saveOutput")
//	@ResponseBody
//	public String saveOutput(@RequestBody Code code, HttpSession session) {
////		Code c = new Code();
//		User finalUser = (User) session.getAttribute("finalUserKey");
//		c.setName(finalUser.getUsername());
//		c.setFileName(code.getFileName());
//		c.setParameter(code.getParameter());
//		c.setOutput(code.getOutput());
//		c.setCode(code.getCode());
//
//		Code object = cr.getCodeByFileNameAndName(code.getFileName(), finalUser.getUsername());
//
//		System.out.println("Object fetched from DB :- " + object);
//
//		if (object == null) {
//
//			cr.save(c);
//		} else {
//
//			cr.delete(object);
//			cr.save(c);
//		}
//
//		return code.getFileName() + " saved Successfully";
//
//	}

	@PostMapping("/delete")
	@ResponseBody
	public String delete(@RequestBody String file, HttpSession session) {
		file = file.split("=")[1];
		System.out.println(file);
		User finalUser = (User) session.getAttribute("finalUserKey");
		try {
			cr.deleteFromCodeRepo(file, finalUser.getUsername());
		} catch (Exception e) {
			System.out.println(" Below Exception handeled already");
			System.out.println(e);
		}
		return file + " deleted Successfully";
	}

//	@GetMapping("/admin")
//	public String admin() {
////		m.addAttribute("admin", new Admin());
//		return "admin";
//	}

	@GetMapping("/registeradmin")
	public String registeradmin(Model m) {
		m.addAttribute("admin", new Admin());
		return "adminregistration";
	}

	@PostMapping("/adminregister")
	@ResponseBody
	public ModelAndView adminregistration(@ModelAttribute("admin") Admin a) {
		ModelAndView mav = new ModelAndView("adminregistration");
		String responseMessage;
		if (a.getPassword().equals(a.getConfirmPassword())) {
//			a.setConfirmPassword(Integer.toString(a.getConfirmPassword().hashCode()));
//			a.setPassword(Integer.toString(a.getPassword().hashCode()));

			Admin checkAdminAvail = adminRepo.getAdmin(a.getUserName());

			if (checkAdminAvail == null) {
				adminRepo.save(a);
				responseMessage = (" : " + a.getUserName() + " Admin user registered Successfully");

			} else {
				responseMessage = (" : " + a.getUserName() + "Admin user registration falied " + "\n"
						+ "Username Already Exist");
			}

		} else {
			responseMessage = (" : " + a.getUserName() + "Admin user registration falied" + "\n"
					+ " Password and Confirm password does not match");
		}
		mav.addObject("responseMessage", responseMessage);
		return mav;

	}

	@GetMapping("/admin")
	public String adminLogin(Model m) {

		m.addAttribute("admin", new Admin());

		return "adminUserPass";
//		return "admin2";
	}

//	Admin finalAdmin;
//	User finalInstructor;
	@PostMapping("/authenticateAdmin")
	public ModelAndView authenticateAdmin(@ModelAttribute("admin") Admin a, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		String message;

//		if(finalAdmin !=null) {
//			mav.setViewName("admin2");
//			return mav;
//		
//		}

		Admin ad = adminRepo.getAdmin(a.getUserName());
		System.out.println("Admin received " + ad);
		if (ad == null) {
			User instructor = r.getInstructor(a.getUserName());
			if (instructor != null && a.getPassword().equals(instructor.getPassword())) {
				String finalInstructorKey = "finalInstructor_"+session.getId();
				session.setAttribute("finalInstructorKey", instructor);
//				finalInstructor = instructor;
				User finalInstructor = (User) session.getAttribute("finalInstructorKey");
				System.out.println("FINAL INSTRUCTOR AUTH :" + finalInstructor);
				mav.setViewName("admin2");
				return mav;
			} else {
				String messages = "Login Falied" + "\n" + "Please recheck your credentials";
				mav.addObject("message", messages);
				mav.setViewName("adminUserPass");
				return mav;
			}
		}

		if (ad != null && ad.getPassword().equals((a.getPassword()))) {
			String finalAdminKey = "finalAdmin_+"+session.getId();
			session.setAttribute("finalAdminKey", a);
//			finalAdmin = a;
			mav.setViewName("admin2");
		} else {
			message = "Login Falied" + "\n" + "Please recheck your credentials";
			mav.addObject("message", message);
			mav.setViewName("adminUserPass");

		}

		return mav;
	}

//	@PostMapping("/saveServerConfig")
//	@ResponseBody	
//	public String saveServerConfig(@RequestBody ServerConfig serverConfig) {
//		System.out.println("Saving to serverRepo received rb:" + serverConfig);
//
//		serverConfigRepo.save(serverConfig);
//
//		return "Conf saved for language :" + serverConfig.getLanguage();
//	}

	@GetMapping("/postgres")
	public String postgres() {

//		dao.setConnection("jdbc:postgresql://localhost:5432/OnlineCompiler", "postgres", "");
		return "postgres2";
	}

	@GetMapping("/oracle")
	public String oracle() {

		return "postgres2";
	}

	@GetMapping("/mysql")
	public String mysql() {

		return "postgres2";
	}

	@PostMapping("/postgresWork")
	@ResponseBody
	public HashMap<String, HashMap<String, String>> postgresWork(@RequestBody String sql, Model model) {
		HashMap<String, String> tabels = new HashMap<>();
		HashMap<String, String> queryMap = new HashMap<>();
		HashMap<String, HashMap<String, String>> finalMap = new HashMap<>();

		List<String> tableNames = new ArrayList<>();
		String decodedRequestBody = "";
		try {
			decodedRequestBody = URLDecoder.decode(sql, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		 sql = sql.split("=")[1].replace("+", " ");
		try {
			System.out.println("query from user " + "\n" + sql);

//			 Table structure 
			String data = dao.connectReturn(decodedRequestBody);
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

			tableSyntax += "</tbody>" + "</table>";

			queryMap.put("queryOutput", tableSyntax);
//			
			String output = dao.tableNames("select tablename from pg_tables where schemaname = 'public'");
			String[] tables = output.split(" ");

			for (String t : tables) {
//				if (t.equalsIgnoreCase("customers") || t.equalsIgnoreCase("shippings")
//						|| t.equalsIgnoreCase("orders")) {
//					continue;
//				}
				tableNames.add(t);
			}

			for (String name : tableNames) {
				tabels.put(name, prepareTableMap(name));
			}
//			model.addAttribute("tabelMap", tables);
			finalMap.put("queryOutputMap", queryMap);
			finalMap.put("tableMap", tabels);

//			
			br.close();
			return finalMap;
//			System.out.println(tableSyntax);
//			 
//			return dao.connectReturn(decodedRequestBody);
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			queryMap.put("queryOutput", e.toString());
		}
		return finalMap;

	}

	@GetMapping("/beforePostgresWork")
	@ResponseBody
	public HashMap<String, String> beforePostgresWork() throws SQLException, IOException {
		HashMap<String, String> map = new HashMap<>();

//		checking automatic table existence
		dao.checkAutoTalbles("customers");
		dao.checkAutoTalbles("orders");
		dao.checkAutoTalbles("shippings");

//		
//		map.put("customer", usrImpl.automaticPostresTables("select * from Customers"));
		map.put("customer", this.prepareTableMap("customers"));
		map.put("orders", this.prepareTableMap("shippings"));
		map.put("shippings", this.prepareTableMap("orders"));

		for (Entry<String, String> s : map.entrySet()) {
			System.out.println(s.getKey() + "  :  " + s.getValue());
		}
		return map;

	}

//	@PostMapping("/closeDataBase")
//	public ResponseEntity<String> closeDataBase() throws SQLException {
//		String output = dao.tableNames("select tablename from pg_tables where schemaname = 'public'");
//		String[] tables = output.split(" ");
//
//		for (String t : tables) {
//			if (t.equalsIgnoreCase("customers") || t.equalsIgnoreCase("shippings") || t.equalsIgnoreCase("orders")) {
//				continue;
//			}
//			System.out.println(t);
//			System.out.println("Before querry");
//			dao.connectReturn("drop table " + t);
//			System.out.println("After querry ");
//		}
//		dao.closeConn();
//		return ResponseEntity.ok().build();
//
//	}

	@GetMapping("/getLinuxConf")
	@ResponseBody
	public HashMap<String, String> getLinuxConf(HttpSession session) {
		User finalUser = (User) session.getAttribute("finalUserKey");
		String userName = finalUser.getUsername();
		HashMap<String, String> confMap = new HashMap<>();
		User user = r.getIdByName(userName);
		String password = user.getConfirmpassword();
		password = Base64.getEncoder().encodeToString(password.getBytes());

		Technology tec = serverConfigRepo.getServerConfig("linux");
		ServerConfig conf = tec.getServerConfig();

		confMap.put("ip", conf.getIp());
		confMap.put("port", Integer.toString(conf.getPort()));
		confMap.put("userName", userName);
		confMap.put("password", password);

		return confMap;
	}

	@GetMapping("/getMySqlConf")
	@ResponseBody
	public HashMap<String, String> getMySqlConf(HttpSession session) {
		User finalUser = (User) session.getAttribute("finalUserKey");
		String userName = finalUser.getUsername();
		HashMap<String, String> confMap = new HashMap<>();
		User user = r.getIdByName(userName);
		String password = user.getConfirmpassword();
		password = Base64.getEncoder().encodeToString(password.getBytes());

		Technology tec = serverConfigRepo.getServerConfig("linux");
		ServerConfig conf = tec.getServerConfig();

		confMap.put("ip", conf.getIp());
		confMap.put("port", Integer.toString(conf.getPort()));
		confMap.put("userName", userName);
		confMap.put("password", password);

		return confMap;
	}

//	@PostMapping(value = "/setServerConfig")
//	@ResponseBody
//	public String setServerConfig(@RequestBody RequestData requestData) {
//		ModelAndView mav = new ModelAndView("admin2");
//		FormData formData = requestData.getFormData();
//		FormArray formArray = requestData.getFormArray();
//		String message;
//		List<Technology> techList = new ArrayList<>();
//
//		System.out.println("FormData : " + formData);
//		System.out.println("FormArray : " + formArray);
//
//		ServerConfig config = new ServerConfig();
//		config.setUserName(formData.getUserName());
//		config.setIp(formData.getIp());
//		config.setPort(formData.getPort());
//		config.setPassword(formData.getPassword());
//		config.setCreatedBy(finalAdmin.getUserName());
//		Date d = new Date();
//		config.setCreatedTime(d);
//		config.setActive(0);
//		for (String tech : formArray.getTechList()) {
//			System.out.println(tech);
//			if (tech.equalsIgnoreCase("linux")) {
//				Technology technology = new Technology();
//				technology.setTechnology(tech);
//				technology.setTechType("operatingSystem");
//				techList.add(technology);
//				technologyRepo.save(technology);
//			} else if (tech.equalsIgnoreCase("js") || tech.equalsIgnoreCase("bash") || tech.equalsIgnoreCase("perl")
//					|| tech.equalsIgnoreCase("php") || tech.equalsIgnoreCase("css")) {
//
//				Technology technology = new Technology();
//				technology.setTechnology(tech);
//				technology.setTechType("scripting");
//				techList.add(technology);
//				technologyRepo.save(technology);
//
//			} else if (tech.equalsIgnoreCase("postgres") || tech.equalsIgnoreCase("oracle")
//					|| tech.equalsIgnoreCase("H2") || tech.equalsIgnoreCase("mysql")
//					|| tech.equalsIgnoreCase("mongodb")) {
//
//				Technology technology = new Technology();
//				technology.setTechnology(tech);
//				technology.setTechType("dbms");
//				techList.add(technology);
//				technologyRepo.save(technology);
//
//			} else {
//
//				Technology technology = new Technology();
//				technology.setTechnology(tech);
//				technology.setTechType("programming");
//				techList.add(technology);
//				technologyRepo.save(technology);
//			}
//
//		}
//		config.setTechnology(techList);
//		serverConfigRepo.save(config);
//		System.out.println(config);
//
//		return "response from server";
//	}

	@PostMapping(value = "/setServerConfig")
	@ResponseBody
	public String setServerConfig(@RequestBody RequestData requestData, HttpSession session) {

		Admin finalAdmin = (Admin) session.getAttribute("finalAdminKey");
		FormData formData = requestData.getFormData();
		FormArray formArray = requestData.getFormArray();

		ServerConfig config = new ServerConfig();
		config.setUserName(formData.getUserName());
		config.setIp(formData.getIp());
		config.setPort(formData.getPort());
		config.setPassword(formData.getPassword());
		config.setCreatedBy(finalAdmin.getUserName());
		Date d = new Date();
		config.setCreatedTime(d);
		config.setActive(0);

		// Save the ServerConfig object first
		ServerConfig savedConfig = serverConfigRepo.save(config);

		List<Technology> techList = new ArrayList<>();

		for (String tech : formArray.getTechList()) {
			Technology technology = new Technology();
			technology.setTechnology(tech);

			// Determine the techType based on the tech value
			String techType;
			if (tech.equalsIgnoreCase("linux")) {
				techType = "operatingSystem";
			} else if (tech.equalsIgnoreCase("js") || tech.equalsIgnoreCase("bash") || tech.equalsIgnoreCase("perl")
					|| tech.equalsIgnoreCase("php") || tech.equalsIgnoreCase("css")) {
				techType = "scripting";
			} else if (tech.equalsIgnoreCase("postgres") || tech.equalsIgnoreCase("oracle")
					|| tech.equalsIgnoreCase("H2") || tech.equalsIgnoreCase("mysql")
					|| tech.equalsIgnoreCase("mongodb")) {
				techType = "dbms";
			} else {
				techType = "programming";
			}
			technology.setTechType(techType);

			// Associate Technology with the saved ServerConfig
			technology.setServerConfig(savedConfig);

			techList.add(technology);
		}

		// Save each Technology object
		for (Technology technology : techList) {
			technologyRepo.save(technology);
		}

		savedConfig.setTechnology(techList);
		serverConfigRepo.save(savedConfig); // Update ServerConfig with associated Technology
		System.out.println(savedConfig);

		return "response from server";
	}

	public String prepareTableMap(String sql) throws IOException {
		try {
			String header = "";
			String line = "";
			String tableSyntax = "<table>" + "\n" + "<thead>" + "\n" + "<tr>";
			String tableData = dao.connectReturn(sql);
			BufferedReader br = new BufferedReader(new StringReader(tableData));
			header = br.readLine();
			String prepareHeader[] = header.split("~");
			for (String ph : prepareHeader) {
				tableSyntax += "<td>" + ph + "</td>" + "\n";
			}
			tableSyntax += "</tr>" + "\n" + "</thead>" + "\n" + "<tbody>";

			while ((line = br.readLine()) != null) {
				tableSyntax += "<tr>";
				String prepare[] = line.split("~");
				for (String l : prepare) {
					tableSyntax += "<td>" + l + "</td>";
				}

				tableSyntax += "</tr>";
			}

			br.close();
			return tableSyntax + "</tbody></table>";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

//	Shows port username, ip on the Admin dashboard 
	@GetMapping("/adminSummaryTable")
	@ResponseBody
	public String adminSummaryTable() {
//		dao.setConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "");
		String tableSyntax = "";
		try {
			tableSyntax = this.serverSummaryTable("select user_name , ip , port from server_config sc");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tableSyntax;
	}

//	Preparing map for above table 
	public String serverSummaryTable(String sql) throws IOException {
		try {
			String header = "";
			String line = "";
			String tableSyntax = "<table class=\"table align-items-center table-flush table-borderless\">\n"
					+ "  <thead>\n" + "    <tr>\n" + "      <th>Checkbox</th>\n" + "      <th>Username</th>\n"
					+ "      <th>Ip Address</th>\n" + "      <th>Port No</th>\n" + "    </tr>\n" + "  </thead>";

			String tableData = dao.connectReturn(sql);
			BufferedReader br = new BufferedReader(new StringReader(tableData));
			br.readLine();
			tableSyntax += "\n" + "  <tbody>";

			while ((line = br.readLine()) != null) {
				tableSyntax += "\n    <tr>";
				tableSyntax += "\n      <td>\n" + "        <input type=\"checkbox\" id=\"\" />\n" + "      </td>";

				String[] prepare = line.split("~");
				for (String l : prepare) {
					tableSyntax += "\n      <td>" + l + "</td>";
				}

				tableSyntax += "\n    </tr>";
			}

			br.close();
			return tableSyntax + "\n  </tbody>\n</table>";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

//	Shows complete configuration of the Server config on Admin dashboard 
	@GetMapping("/serverDetailedTable")
	@ResponseBody
	public String serverDetailedTable() {
//		dao.setConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "");
		String tableSyntax = "";
		try {
			tableSyntax = this.prepareServerDetailedTableMap(
					"select sc.id, sc.created_by ,sc.created_time::timestamp ,sc.ip, sc.is_active , sc.\"password\" ,sc.port, sc.type,sc.updated_by ,t.tech_type ,t.technology  from server_config sc join technology t on sc.id = t.server_config_id");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tableSyntax;
	}

//	preparing map for above table 
	public String prepareServerDetailedTableMap(String sql) throws IOException {
		try {
			String header = "";
			String line = "";
			String tableSyntax = "<table class=\"table align-items-center table-flush table-borderless\" >" + "\n"
					+ "<thead>" + "\n" + "<tr>";
			String tableData = dao.connectReturn(sql);
			BufferedReader br = new BufferedReader(new StringReader(tableData));
			header = br.readLine();
			String prepareHeader[] = header.split("~");
			for (String ph : prepareHeader) {
				tableSyntax += "<td>" + ph + "</td>" + "\n";
			}
			tableSyntax += "</tr>" + "\n" + "</thead>" + "\n" + "<tbody>";

			while ((line = br.readLine()) != null) {
				tableSyntax += "<tr>";
				String prepare[] = line.split("~");
				for (String l : prepare) {
					tableSyntax += "<td>" + l + "</td>";
				}

				tableSyntax += "</tr>";
			}

			br.close();
			return tableSyntax + "</tbody></table>";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

//	Instructor
	@GetMapping("/instructor")
	public String instructor(Model m) {
		Courses courses = new Courses();
		m.addAttribute("courses", courses);
		return "instructor";
	}

	@PostMapping("/getCourseDetails")
	public ModelAndView getCourseDetails(@ModelAttribute("courses") Courses courses, HttpSession session) {
		User finalInstructor = (User) session.getAttribute("finalInstructorKey");
		Admin finalAdmin = (Admin) session.getAttribute("finalAdminKey");
		ModelAndView mav = new ModelAndView("instructor");
		Courses c = courses;
		Date date = new Date();

		c.setCreateDateTime(date);
		if (finalInstructor == null) {
			c.setCreatedBy(finalAdmin.getUserName());
		} else if (finalAdmin == null) {
			c.setCreatedBy(finalInstructor.getUsername());
		}

//		Setting Semester 
		Semester sem = semesterRepo.getSemester(1);
//		-------------
		c.setSemester(sem.getSemester());
		c.setYear(sem.getYear());
		coursesRepo.save(c);

		System.out.println(courses);
		mav.addObject("message", "Course Detials for " + c.getCourseCode() + " submitted Successfully");
		return mav;
	}

//	Instructor Courses Detail Table 
	@GetMapping("/coursesDetailTable")
	@ResponseBody
	public String coursesDetailTable(HttpSession session) {
		User finalInstructor = (User) session.getAttribute("finalInstructorKey");
		Admin finalAdmin = (Admin) session.getAttribute("finalAdminKey");
		String tableSyntax = "";
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>");
//		System.out.println("FinalAdmin : "+finalAdmin);
//		System.out.println("FinalInstructor : "+finalInstructor);
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>");
		try {

			if (finalAdmin == null) {
				System.out.println("FOR SQL QUERY" + finalInstructor.getUsername());
				tableSyntax = this.prepareServerDetailedTableMap(
						"select * from courses where created_by = '" + finalInstructor.getUsername() + "'");
			} else
				tableSyntax = this.prepareServerDetailedTableMap("select * from courses");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tableSyntax;
	}

//	-------- INstructor Code End's here --------------

//	 Admin / Instructor Dashboard User Status

//	@GetMapping("/openUserStatus")
//	public String openUserStatus() {
//		return "userStatus";
//	}

	@PostMapping("/userLogoutTime")
	@ResponseBody
	public String userLogoutTime(HttpSession session) {

//		dao.setConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "");
		Timestamp d = new Timestamp(System.currentTimeMillis());
		session.setAttribute("userLogoutTime", d);
		Timestamp d1 = (Timestamp) session.getAttribute("userLogoutTime");
		User finalUser = (User) session.getAttribute("finalUserKey");

		try {
			dao.updateUserStatus2(finalUser.getUsername(), d1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Window would be closed";

	}

	@GetMapping("/userStatusInfo")
	@ResponseBody
	public String userStatusInfo(HttpSession session) {
		String tabelSyntax = "";
		try {
//			dao.setConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "");
			tabelSyntax = this.prepareServerDetailedTableMap(
					"select active_status , user_name , login_time , logout_time , id  from user_status us order by active_status desc ");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tabelSyntax;
	}

	@GetMapping("/backToDashBoard")
	public String backToDashBoard() {
		return "admin2";
	}

	@GetMapping("/backToInstructor")
	public String backToInstructor() {
		return "instructor";
	}

	@GetMapping("/backtoUserStatus")
	public String backtoUserStatus() {
		return "userStatus";
	}

	@GetMapping("/backtoCalander")
	public String backtoCalander() {
		return "calander";
	}

	@GetMapping("/backtoAuthorization")
	public String backtoAuthorization() {
		return "authorization";
	}

//	Sanction code begin

	@GetMapping("/sanction")
	@ResponseBody
	public HashMap<String, String> sanction() {
		System.out.println("/sanction FIRED >>>>>>>>>>");
//		dao.setConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "");
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			String tableUser = this
					.sanctionTable("select username, email  from online_compiler oc where is_instructor = 0");

			map.put("tableUser", tableUser);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String tableInstructor = this
					.sanctionTable("select username, email  from online_compiler oc where is_instructor = 1");

			map.put("tableInstructor", tableInstructor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(map);

		return map;
	}

	public String sanctionTable(String sql) throws IOException {
		try {
			String header = "";
			String line = "";
			String tableSyntax = "<table class=\"table align-items-center table-flush table-borderless\">\n"
					+ "  <thead>\n" + "    <tr>\n" + "      <th>Checkbox</th>\n" + "      <th>Username</th>\n"
					+ "      <th>Email</th>\n" + " </tr>\n" + "  </thead>";

			String tableData = dao.connectReturn(sql);
			BufferedReader br = new BufferedReader(new StringReader(tableData));
			header = br.readLine();
//	        String prepareHeader[] = header.split("~");
//	        for (String ph : prepareHeader) {
//	            tableSyntax += "<td>" + ph + "</td>";
//	        }
//	        tableSyntax += "</tr></thead><tbody>";

			System.out.println("Table Data" + tableData);
			while ((line = br.readLine()) != null) {
				tableSyntax += "<tr><td><input type=\"checkbox\" id=\"\" /></td>";

				String prepare[] = line.split("~");
				for (String l : prepare) {
					tableSyntax += "<td>" + l + "</td>";
				}

				tableSyntax += "</tr>";
			}

			br.close();
			return tableSyntax + "</tbody></table>";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

//	Course Code Availability/ signed in
//	@GetMapping("/courseList")
//	@ResponseBody
//	public String courseList() {
//		dao.setConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "");
//		String listSyntax = "";
//		try {
//			String list = dao.connectReturn("select course_code from courses c where is_active = 1");
//			try {
//				String line;
//				BufferedReader br = new BufferedReader(new StringReader(list));
//				br.readLine();
//
//				listSyntax += "<ul class=\"option-list\">";
//				while ((line = br.readLine()) != null) {
//					// Corrected the onclick attribute
//					listSyntax += "<li class=\"option-item\" onclick=\"toggleOptions(this, '"
//							+ line.toString().replace("~", "") + "')\">";
//					listSyntax += "\n" + line.toString().replace("~", "") + "</li>" + "<div class=\"options\">\n"
//							+ "        <label>\n" + "          <input\n" + "            type=\"radio\"\n"
//							+ "            name=\"item1-choice\"\n" + "            value=\"Add\"\n"
//							+ "            onclick=\"captureChoice('Add')\"\n" + "> Add </label>\n" + "</div>";
//				}
//				listSyntax += "</ul>";
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return listSyntax;
//	}

	@GetMapping("/courseList")
	@ResponseBody
	public String courseList() throws IOException {
//		dao.setConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "");
		String listSyntax = "";
		String list;
		try {
			list = dao.connectReturn("select course_code||':'||c.\"section\"  from courses c where is_active = 1");
			String line;
			BufferedReader br = new BufferedReader(new StringReader(list));
			br.readLine();
			listSyntax += "<table\n"
					+ "                        class=\"table align-items-center table-flush table-borderless\"\n"
					+ "                      >\n" + "                        <thead>\n"
					+ "                          <tr>\n" + "                            <th>Course name</th>\n"
					+ "                            <th>Add</th>\n"
					+ "                                                 </tr>\n" + "                        </thead>"
					+ "<tbody>";
			while ((line = br.readLine()) != null) {

				listSyntax += "<tr>";
				listSyntax += "<td>" + line.replace("~", "") + "</td>";
				listSyntax += " <td><button onclick=\"addCourse('" + line.replace("~", "") + "')\">Add</button></td>";
//						listSyntax+=" <td><button onclick=\"deleteCourse('"+line.split(":")[0]+"')\">Delete</button></td>";
				listSyntax += "</tr>";
			}
			listSyntax += "</tbody>" + "</table>";

			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listSyntax;
	}

//	@PostMapping("/addCourses")
//	@ResponseBody
//	@Transactional
//	public String addCourses(@RequestBody String courseName, HttpSession session) {
//		User finalUser = (User) session.getAttribute("finalUserKey");
////		System.out.println(finalUser+">>>>>>>>>>");
////		Courses course = new Courses();
//		Courses cours = coursesRepo.getCourseByName(courseName.toString().split("=")[1]);
//		session.setAttribute("finalCourse", cours);
//		Courses course = (Courses) session.getAttribute("finalCourse");
//
//		course.setCourseCode(courseName.toString().split("=")[1]);
//
////		finalUser.setAdaptedCourses(course);
////		User obj = r.getIdByName(finalUser.getName());
//		if (finalUser.getAdaptedCourses() == null) {
//			finalUser.setAdaptedCourses(new ArrayList<>());
//		}
////		List<Courses> list = new ArrayList<>();
////		list.add(course);
//		finalUser.getAdaptedCourses().add(course);
////		obj.setAdaptedCourses(finalUser);
//		Courses cou = coursesRepo.save(course);
//		User user = r.save(finalUser);
//
//		System.out.println("Course Saved >>>>>" + cou);
//		System.out.println("USER SAVED >>>>> " + user);
//		return courseName + " : Added Successfully";
//	}

	@PostMapping("/addCourses")
	@ResponseBody
	@Transactional
	public String addCourses(@RequestBody String courseName, HttpSession session) throws UnsupportedEncodingException {
		User finalUser = (User) session.getAttribute("finalUserKey");
		courseName = courseName.split("=")[1];
		courseName = URLDecoder.decode(courseName, "UTF-8");
//		System.out.println(courseName);
		String cours = courseName.split(":")[0];
		String section = courseName.split(":")[1];
		Courses course = coursesRepo.getCourseBySecionAndCourseCode(cours, Integer.parseInt(section));
		session.setAttribute("finalCourse", course);
		Courses courses = (Courses) session.getAttribute("finalCourse");
		boolean avail = false;
		try {
			System.out.println(
					"select user_id , adapted_courses_id  from online_compiler_adapted_courses ocac where user_id = "
							+ finalUser.getId() + " and adapted_courses_id =" + courses.getId());
			avail = dao.checkData(
					"select user_id , adapted_courses_id  from online_compiler_adapted_courses ocac where user_id = "
							+ finalUser.getId() + " and adapted_courses_id = " + courses.getId());
			System.out.println("AVAIL >>> " + avail);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (avail) {
			course.setCourseCode(cours);
			if (finalUser.getAdaptedCourses() == null) {
				finalUser.setAdaptedCourses(new ArrayList<>());
			}
			finalUser.getAdaptedCourses().add(courses);
			Courses cou = coursesRepo.save(course);
			User user = r.save(finalUser);
			System.out.println("Course Saved >>>>>" + cou);
			System.out.println("USER SAVED >>>>> " + user);
//		User finalUser = (User) session.getAttribute("finalUserKey");
////		System.out.println(finalUser+">>>>>>>>>>");
////		Courses course = new Courses();
//		Courses cours = coursesRepo.getCourseByName(courseName.toString().split("=")[1]);
//		session.setAttribute("finalCourse", cours);
//		Courses course = (Courses) session.getAttribute("finalCourse");
//
//		course.setCourseCode(courseName.toString().split("=")[1]);
//
////		finalUser.setAdaptedCourses(course);
////		User obj = r.getIdByName(finalUser.getName());
//		if (finalUser.getAdaptedCourses() == null) {
//			finalUser.setAdaptedCourses(new ArrayList<>());
//		}
////		List<Courses> list = new ArrayList<>();
////		list.add(course);
//		finalUser.getAdaptedCourses().add(course);
////		obj.setAdaptedCourses(finalUser);
//		Courses cou = coursesRepo.save(course);
//		User user = r.save(finalUser);

			return courseName + " : Added Successfully";
		} else {
			return courseName + " : Already Added ";
		}
	}

	@GetMapping("/adaptedCourseList")
	@ResponseBody
	public String adaptedCourseList(HttpSession session) {
//		dao.setConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "");
		User finalUser = (User) session.getAttribute("finalUserKey");
		String output = "<ul>";

		try {
			String tableSyntax = dao.connectReturn(
					"select course_code||':'||c.\"section\" from online_compiler oc  join online_compiler_adapted_courses ocac on  oc.id = ocac.user_id join courses c on c.id = ocac.adapted_courses_id where name = '"
							+ finalUser.getUsername() + "'");
			BufferedReader br = new BufferedReader(new StringReader(tableSyntax));
			try {
				br.readLine();
				String line;
				while ((line = br.readLine()) != null) {
					output += "<li class=\"clickable\">";
					output += line.toString().replace("~", "");
					output += "</li>";

				}
				output += "</ul>";

				System.out.println(output);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(output);
		return output;
	}

//	Selected Course code 
	@GetMapping("/courseAjax")
	@ResponseBody
	public HashMap<String, String> courseAjax(@RequestParam("requestBody") String requestBody, HttpSession session) {
		System.out.println(">>>> we are here" + requestBody);
		User finalUser = (User) session.getAttribute("finalUserKey");
		String courseCode = requestBody;
		courseCode = courseCode.split(":")[0];
		System.out.println("Course Code Received : - " + courseCode);
		Courses course = coursesRepo.getCourseByName(courseCode);
		String language = course.getLanguage();
		HashMap<String, String> map = this.Testing2("lan=" + language);
		map.put("courseCode", courseCode);
		System.out.println(map);
		return map;

	}

	@PostMapping("/courseAjaxOutput")
	@ResponseBody
	public String courseAjaxOutput(@RequestBody UserDTO2 userDTO, HttpSession session) {
		String languageConnector = "";
		UserServiceImpl obj = new UserServiceImpl();
		System.out.println("/ajaxCompiler URL fired");
		String ext = userDTO.getFilename().toString().split("\\.")[1];
		System.out.println("Ext :" + ext);
		if (ext.equalsIgnoreCase("c")) {
			languageConnector = "c";
		} else if (ext.equalsIgnoreCase("java")) {
			languageConnector = "java";
		} else if (ext.equalsIgnoreCase("py")) {
			languageConnector = "python";
		} else if (ext.equalsIgnoreCase("js")) {
			languageConnector = "js";
		} else if (ext.equalsIgnoreCase("sh")) {
			languageConnector = "bash";
		} else if (ext.equalsIgnoreCase("cpp")) {
			languageConnector = "cpp";
		} else if (ext.equalsIgnoreCase("pl")) {
			languageConnector = "perl";
		} else if (ext.equalsIgnoreCase("swift")) {
			languageConnector = "swift";
		} else if (ext.equalsIgnoreCase("cs")) {
			languageConnector = "c_sharp";
		} else if (ext.equalsIgnoreCase("kt")) {
			languageConnector = "kotlin";
		} else if (ext.equalsIgnoreCase("php")) {
			languageConnector = "php";
		} else if (ext.equalsIgnoreCase("html")) {
			languageConnector = "html";
		}

		Technology tec = serverConfigRepo.getServerConfig(languageConnector);
		ServerConfig serverConfig = tec.getServerConfig();

//		RUN COURSE ON SERVER (advance )
		System.out.println(">>>>>>>>>>>>>>>>>>HERE >>>>>>>" + userDTO.getCourseCode().toString());
		Courses course = coursesRepo.getCourseByName(userDTO.getCourseCode().toString());
		System.out.println(">>>>>>>>>>>>>>>>>>HERE 2>>>>>>>" + userDTO.getCourseCode().toString());
		String language = course.getLanguage();
		User finalUser = (User) session.getAttribute("finalUserKey");
		Technology tech = serverConfigRepo.getServerConfig(language);

		ServerConfig serverConfiguration = tech.getServerConfig();

		String output = usrImpl.coursesRunOnServer(serverConfiguration.getUserName(), serverConfiguration.getPassword(),
				serverConfiguration.getIp(), serverConfiguration.getPort(), userDTO, language, finalUser.getUsername(),
				finalUser.getPassword(), userDTO.getCourseCode().toString(), course.getSection());

		return output;
	}

	public HashMap<String, String> Testing2(@RequestBody String language) {
		String len[] = language.split("=");
		if (len.length >= 2) {

			language = language.toString().split("=")[1];

			System.out.println("Response Language " + language);
			HashMap<String, String> map = new HashMap<>();
			if (language.equalsIgnoreCase("java")) {
				map.clear();
				map.put("fileName", "Lab.java");
				map.put("code",
						" import java.util.*;\n" + "import java.io.*;\n \n " + "class Lab {\n"
								+ "    public static void main(String[] args) {\n"
								+ "        System.out.println(\"Hello, Java!\");\n" + "    }\n" + "}");

			} else if (language.equalsIgnoreCase("Bash")) {
				map.clear();
				map.put("fileName", "Lab.sh");
				map.put("code", "echo Hello World..!!");
			} else if (language.equalsIgnoreCase("Python")) {
				map.clear();
				map.put("fileName", "Lab.py");
				map.put("code", "print(\"Hello World..!!\");");
			} else if (language.equalsIgnoreCase("C")) {
				map.clear();
				map.put("fileName", "Lab.c");
				map.put("code", "#include <stdio.h>\n" + "\n" + "int main() {\n" + "    // Write C code here\n"
						+ "    printf(\"Hello world\");\n" + "\n" + "    return 0;\n" + "}");
			} else if (language.equalsIgnoreCase("cpp")) {
				map.clear();
				map.put("fileName", "Lab.cpp");
				map.put("code", "#include <iostream>\n" + "\n" + "int main() {\n"
						+ "    std::cout << \"Hello, World!\" << std::endl;\n" + "    return 0;\n" + "}\n" + "");
			} else if (language.equalsIgnoreCase("js")) {
				map.clear();
				map.put("fileName", "Lab.js");
				map.put("code", "console.log(\"Hello, World!\");\n" + "");
			} else if (language.equalsIgnoreCase("perl")) {
				map.clear();
				map.put("fileName", "Lab.pl");
				map.put("code", "print(\"Hello, World from Perl!\");\n" + "");

			} else if (language.equalsIgnoreCase("swift")) {
				map.clear();
				map.put("fileName", "Lab.swift");
				map.put("code", "print(\"Hello, World from Swift!\");\n" + "");

			} else if (language.equalsIgnoreCase("c_sharp")) {
				map.clear();
				map.put("fileName", "Lab.cs");
				map.put("code",
						"using System;\n" + "\n" + "namespace MyCSharpApp\n" + "{\n" + "    class Program\n" + "    {\n"
								+ "        static void Main(string[] args)\n" + "        {\n"
								+ "            Console.WriteLine(\"Hello, C# on OnlineCompiler!\");\n" + "        }\n"
								+ "    }\n" + "}\n" + "");

			} else if (language.equalsIgnoreCase("kotlin")) {
				map.clear();
				map.put("fileName", "Lab.kt");
				map.put("code", "fun main() {\n" + "    println(\"Hello, Kotlin on Linux!\")\n" + "}\n" + "");

			} else if (language.equalsIgnoreCase("php")) {
				map.clear();
				map.put("fileName", "Lab.php");
				map.put("code", "<?php\n" + "echo \"Hello, World!\";\n" + "?>\n" + "");

			} else if (language.equalsIgnoreCase("html")) {
				map.clear();
				map.put("fileName", "Lab.html");
				map.put("code", "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "    <title>Hello, World!</title>\n"
						+ "</head>\n" + "<body>\n" + "    <h1>Hello, World!</h1>\n" + "</body>\n" + "</html>\n" + "");

			}
			return map;
		}
		return null;
	}
	
	@GetMapping("/checkFinalUser")
	@ResponseBody
	@Transactional
	public String checkFinalUser(HttpSession session) {
		User finalUser = (User) session.getAttribute("finalUserKey");
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> FINAL USER>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println(finalUser.getUsername());
		return finalUser.toString();
	}
	

}
