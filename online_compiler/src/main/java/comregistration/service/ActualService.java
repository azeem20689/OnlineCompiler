package comregistration.service;

import java.io.*;

import org.aspectj.apache.bcel.classfile.annotation.RuntimeAnnos;
import org.hibernate.boot.jaxb.internal.stax.BufferedXMLEventReader;

public class ActualService {
	String error = null;
	public String compile(String fileName, String code, String parameter) {
		
//		
		String ext = fileName.split("\\.")[1];
		String compiler = "";
		String runner = "";
		String replacer = "";
		boolean doWeNeedToCompile = true;

		if (ext.equals("java")) {
			compiler = "javac";
			runner = "java";
			replacer = ".java";

		} else if (ext.equals("sh")) {
			runner = "bash";
			doWeNeedToCompile = false;
			replacer = "";

		} else if (ext.equals("py")) {
			runner = "python3";
			doWeNeedToCompile = false;
			replacer = "";

		} else if (ext.equals("c")) {
			compiler = "gcc";
			replacer = "";
			runner = "./";
		} else if (ext.equals("cpp")) {
			compiler = "g++";
			replacer = "cpp";
			runner = "./";
		} else if (ext.equals("js")) {
			compiler = "";
			replacer = "";
			runner = "node";
			doWeNeedToCompile = false;
		}

		this.writeCode(fileName, code);
		Process pro = null;
		int exitValue = 1;
		
		try {
			if (doWeNeedToCompile) {

				if (ext.equals("c")) {
					

					pro = Runtime.getRuntime().exec(compiler + " -o " + fileName.replace(".c", "") + " " + fileName);

				} else if (ext.equals("java")) {

					pro = Runtime.getRuntime().exec(compiler + " " + fileName);
				} else if (ext.equals("cpp")) {
					pro = Runtime.getRuntime()
							.exec(compiler + " -o " + fileName.replace(replacer, "") + " " + fileName);
				}
				try {
					exitValue = pro.waitFor();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					 return  e.toString();
				}

			}
			if (exitValue == 0 || !(doWeNeedToCompile)) {

				if (ext.equals("c")) {

					Process run = Runtime.getRuntime().exec(runner + fileName.replace(".c", "") + " " + parameter);
					return this.output(run);
				} else if (ext.equals("java")) {

					Process run = Runtime.getRuntime()
							.exec(runner + " " + fileName.replace(replacer, "") + " " + parameter);
					return this.output(run);
				} else if (ext.equals("cpp")) {

					Process run = Runtime.getRuntime().exec(runner + fileName.replace(replacer, "") + " " + parameter);
					return this.output(run);
				} else if (ext.equals("py")) {
					Process run = Runtime.getRuntime().exec(runner + " " + fileName + " " + parameter);
					return this.output(run);
				} else if (ext.equals("sh")) {
					Process run = Runtime.getRuntime().exec(runner + " " + fileName + " " + parameter);
					return this.output(run);
				} else if (ext.equals("js")) {
					Process run = Runtime.getRuntime().exec(runner + " " + fileName + " " + parameter);
					return this.output(run);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			 error = e.toString();
			 return error;
		}
		System.out.println("sent the blow error ");
		return error;
//		return null + "\n" + "Error Occured , please recheck the syntax";
	}

	public void writeCode(String fileName, String code) {

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			bw.write(code);

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.error = e.toString(); 	
			
		}
	}

	String output = "";

	public String output(Process pro) {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				output += line + "\n";
			}

			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return e.toString();
		}

		
			
		return output;
	}

}
