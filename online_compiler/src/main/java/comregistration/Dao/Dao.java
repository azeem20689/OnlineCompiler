package comregistration.Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.springframework.context.annotation.Configuration;

import comregistration.entity.User;
import jakarta.persistence.Column;

@Configuration
public class Dao {
	private Connection cn;
	private Statement stm;

	public Dao() {

		try {
//			cn = DriverManager.getConnection(url, userName, password);
			cn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "");
			stm = cn.createStatement();

		} catch (Exception e) {
			System.out.println(e);

		}
	}

//	public void setConnection(String url, String userName, String password) {
//
//		System.out.println();
//		try {
//			cn = DriverManager.getConnection(url, userName, password);
////			cn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/OnlineCompiler", "postgres", "");
//			stm = cn.createStatement();
//
//		} catch (Exception e) {
//			System.out.println(e);
//
//		}
//	}

	public String connectReturn(String sql) throws SQLException {
		String header = "";
		try {
			if (sql.toLowerCase().startsWith("select")) {

				ResultSet rs = stm.executeQuery(sql);

				ResultSetMetaData rd = rs.getMetaData();

				for (int i = 1; i <= rd.getColumnCount(); i++) {
					header += rd.getColumnLabel(i) + "~";
				}

				header += "\n";

				int numCol = rd.getColumnCount();
				String realOutput = header;

				while (rs.next()) {

					for (int i = 1; i <= numCol; i++) {

						if (rd.getColumnType(i) == java.sql.Types.INTEGER) {
							realOutput += +rs.getInt(i) + "~";
						} else if (rd.getColumnType(i) == java.sql.Types.VARCHAR) {
							realOutput += rs.getString(i) + "~";
						} else if (rd.getColumnType(i) == java.sql.Types.BOOLEAN) {
							realOutput += rs.getBoolean(i) + "~";
						} else if (rd.getColumnType(i) == java.sql.Types.DATE) {
							realOutput += rs.getDate(i) + "~";
						} else if (rd.getColumnType(i) == java.sql.Types.NUMERIC) {
							realOutput += rs.getBigDecimal(i) + "~";
						} else if (rd.getColumnType(i) == java.sql.Types.BIGINT) {
							realOutput += rs.getLong(i) + "~";
						} else if (rd.getColumnType(i) == java.sql.Types.TIMESTAMP) {
							realOutput += rs.getTimestamp(i) + "~";
//						    realOutput += timestamp.toString() + "~"; // Convert to string for output
						}

					}
					realOutput += "\n";

				}

				return realOutput;

			} else {
				int rs = stm.executeUpdate(sql);

				return rs + " number of rows updated, Query ran successfully";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block

			return e.toString();

		}

	}
	
	
	public boolean checkData(String sql) throws SQLException {
		boolean isEmpty=true; 
		ResultSet rs = stm.executeQuery(sql);
		if (rs.next()) {
			isEmpty=false;
		}
		return isEmpty;
	}

	
	
	public void checkAutoTalbles(String tableName) {

		boolean createTable = true;
		try {

			ResultSet rs = cn.getMetaData().getTables(null, null, tableName, null);

//			ResultSet output = stm.executeQuery(sql);

			if (rs.next()) {
				createTable = false;

			}

			if (createTable) {
				if (tableName.equalsIgnoreCase("customers")) {

					stm.executeUpdate("CREATE TABLE public.customers (\n" + "    customer_id numeric,\n"
							+ "    first_name character varying(25),\n" + "    last_name character varying(25),\n"
							+ "    age numeric,\n" + "    country character varying(25)\n" + ");");
					stm.executeUpdate("Insert into customers (customer_id,first_name,last_name,age,country)\n"
							+ " values \n" + "(1,'John','Doe',31,'USA'),\n" + "(2,'Robert','Luna',22,'USA'),\n"
							+ "(3,'David','Robinson',22,'UK'),\n" + "(4,'John','Reinhardt',25,'UK'),\n"
							+ "(5,'Betty','Doe',28,'UAE')");
				} else if (tableName.equalsIgnoreCase("orders")) {

					stm.executeUpdate("CREATE TABLE public.orders (\n" + "    order_id numeric,\n"
							+ "    item character varying(20),\n" + "    amount numeric,\n"
							+ "    customer_id numeric\n" + ");\n" + "");
					stm.executeUpdate("Insert into orders (order_id,item,amount,customer_id)\n" + " values \n"
							+ "(1,'Keyboard',400,4),\n" + "(2,'Mouse',300,4),\n" + "(3,'Monitor',12000,3),\n"
							+ "(4,'Keyboard',400,1),\n" + "(5,'Mousepad',250,2)");
				} else if (tableName.equalsIgnoreCase("shippings")) {

					stm.executeUpdate("CREATE TABLE public.shippings (\n" + "    shipping_id numeric,\n"
							+ "    status character varying(15),\n" + "    customer numeric\n" + ");\n" + "");
					stm.executeUpdate("Insert into shippings (shipping_id,status,customer)\n" + " values \n"
							+ "(1,'Pending',2),\n" + "(2,'Pending',4),\n" + "(3,'Delivered',3),\n"
							+ "(4,'Pending',5),\n" + "(5,'Delivered',1)");
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String tableNames(String query) {
		String output = "";
		try {
			ResultSet rs = stm.executeQuery(query);
			while (rs.next()) {
				output += rs.getString(1) + " ";
			}
			return output;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public int updateUserStatus(String username, Timestamp loginTime) throws SQLException {
	    String updateQuery = "UPDATE user_status SET active_status = 1, logout_time = null, login_time = ? WHERE user_name = ?";
	    try (PreparedStatement preparedStatement = cn.prepareStatement(updateQuery)) {
	        preparedStatement.setTimestamp(1, loginTime);
	        preparedStatement.setString(2, username);
	        return preparedStatement.executeUpdate();
	    }
	}
	public int updateUserStatus2(String username, Timestamp logoutTime) throws SQLException {
	    String updateQuery = "UPDATE user_status SET active_status = 0, logout_time = ? WHERE user_name = ?";
	    try (PreparedStatement preparedStatement = cn.prepareStatement(updateQuery)) {
	        preparedStatement.setTimestamp(1, logoutTime);
	        preparedStatement.setString(2, username);
	        return preparedStatement.executeUpdate();
	    }
	}


	
	public void closeConn() throws SQLException {
		cn.close();
	}

}
