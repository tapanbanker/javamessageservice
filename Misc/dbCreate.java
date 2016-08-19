package Misc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class dbCreate {
	public static void main(String args[]) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		
		try{
		conn = DriverManager.getConnection("jdbc:mysql://localhost?user=root&password=admin");
		}catch(Exception e){
			System.out.println("Could not connect to database!!");
			return;
		}
		System.out.println("Connected to database");
		stmt = conn.createStatement();
		String dbName = "university";
		try{
			String createString = "create database "+dbName;
			stmt.executeUpdate(createString);
		}catch(SQLException e){
			System.out.println("Database already exists");
		}
		conn.close();
		conn = DriverManager.getConnection("jdbc:mysql://localhost/"+dbName+"?user=root&password=admin");
		stmt = conn.createStatement();
		System.out.println("Database changed22!");
		
		conn.setAutoCommit(false);
	
		try{
		dropTablesIfExists(conn,stmt);
		
		System.out.println("Creating Person Table");
		createPersonTable(conn,stmt);
		
		System.out.println("Creating Student Table");
		createStudentTable(conn,stmt);
		
		System.out.println("Creating Instructor Table");
		createInstructorTable(conn,stmt);
		
		System.out.println("Creating Course Table");
		createCourseTable(conn,stmt);
		
		System.out.println("Creating AS_Course Table");
		createAssociatedCourseTable(conn,stmt);
		
		System.out.println("Creating Office_Hours Table");
		createOfficeHoursTable(conn,stmt);	
		
		System.out.println("Creating Course_Hours Table");
		createCourseHoursTable(conn,stmt);
	}catch(Exception e){
		e.printStackTrace();
		conn.rollback();
	}
	finally{
		if(conn!=null){
		conn.commit();
		stmt.close();
		conn.setAutoCommit(true);
		conn.close();}
	}
}

	static void createPersonTable(Connection conn,Statement stmt) throws SQLException {
			String createString = "create table person "
					+ "(p_id int(10) UNSIGNED NOT NULL AUTO_INCREMENT,"
					+ "fname varchar(100) DEFAULT NULL,"
					+ "lname varchar(100) DEFAULT NULL,"
					+ "address varchar(100) DEFAULT NULL,"
					+ "city varchar(100) DEFAULT NULL,"
					+ "state varchar(10) DEFAULT NULL,"
					+ "zip varchar(15) DEFAULT NULL," 
					+ "PRIMARY KEY (p_id))";
			stmt.executeUpdate(createString);
			System.out.println("Created!");
	}

	static void createStudentTable(Connection conn,Statement stmt) throws SQLException {
		String createString = "create table student "
				+ "(s_id int(10) UNSIGNED NOT NULL AUTO_INCREMENT," 
				+ "p_id int(10) UNSIGNED NOT NULL,"
				+ "student_ID varchar(15) DEFAULT NULL,"
				+ "PRIMARY KEY (s_id),"
				+ "FOREIGN KEY (p_id) REFERENCES person (p_id))";
	
			stmt.executeUpdate(createString);
			System.out.println("Created!");
	}
	
	static void createInstructorTable(Connection conn, Statement stmt) throws SQLException {
			String createString = "create table instructor "
				+ "(i_id int(10) UNSIGNED NOT NULL AUTO_INCREMENT," 
				+ "p_id int(10) UNSIGNED NOT NULL,"
				+ "instructor_ID varchar(15) DEFAULT NULL,"
				+ "dept varchar(100) DEFAULT NULL,"
				+ "PRIMARY KEY (i_id),"
				+ "FOREIGN KEY (p_id) REFERENCES person (p_id))";

			stmt.executeUpdate(createString);
			System.out.println("Created!");
	}
	
	static void createCourseTable(Connection conn, Statement stmt) throws SQLException {	
			String createString = "create table course "
				+ "(c_id int(10) UNSIGNED NOT NULL AUTO_INCREMENT," 
				+ "c_section int(5) NOT NULL," 
				+ "c_name varchar(100) DEFAULT NULL,"
				+ "i_id int(10) UNSIGNED DEFAULT NULL,"
				+ "c_units int(10) DEFAULT NULL,"
				+ "PRIMARY KEY (c_id),"
				+ "FOREIGN KEY (i_id) REFERENCES instructor (i_id))";

				stmt.executeUpdate(createString);
				System.out.println("Created!");
	}
	
	static void createAssociatedCourseTable(Connection conn, Statement stmt) throws SQLException {
			String createString = "create table as_course "
				+ "(p_id int(10) UNSIGNED NOT NULL," 
				+ "c_id int(10) UNSIGNED NOT NULL," 
				+ "PRIMARY KEY (p_id,c_id),"
				+ "FOREIGN KEY (p_id) REFERENCES person (p_id),"
				+ "FOREIGN KEY (c_id) REFERENCES course (c_id))";

				stmt.executeUpdate(createString);
				System.out.println("Created!");
	}
	
	static void createOfficeHoursTable(Connection conn, Statement stmt) throws SQLException {
			String createString = "create table office_hours "
				+ "(i_id int(10) UNSIGNED NOT NULL," 
				+ "day varchar(10) NOT NULL," 
				+ "stime varchar(100) DEFAULT NULL,"
				+ "etime varchar(100) DEFAULT NULL,"
				+ "building varchar(100) DEFAULT NULL,"
				+ "room varchar(100) DEFAULT NULL,"
				+ "FOREIGN KEY (i_id) REFERENCES instructor (i_id))";

				stmt.executeUpdate(createString);
				System.out.println("Created!");
	}

	static void createCourseHoursTable(Connection conn, Statement stmt) throws SQLException {
			String createString = "create table course_hours "
				+ "(c_id int(10) UNSIGNED NOT NULL," 
				+ "day varchar(10) NOT NULL," 
				+ "stime varchar(100) DEFAULT NULL,"
				+ "etime varchar(100) DEFAULT NULL,"
				+ "building varchar(100) DEFAULT NULL,"
				+ "room varchar(100) DEFAULT NULL,"
				+ "FOREIGN KEY (c_id) REFERENCES course (c_id))";

				stmt.executeUpdate(createString);
				System.out.println("Created!");
	}

static void insertInPersonTable(Connection conn, String fname, String lname, String address, String city , String state, String zip  ) throws SQLException {
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			
			String createString = "INSERT INTO person(fname, lname, address, city, state, zip) VALUES('"+fname+"', '"+lname+"','"+address+"' , '"+city+"','"+state+"', '"+zip+"' )";
			System.out.println(createString);
			stmt.executeUpdate(createString);
			System.out.println("Inserted!");
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			stmt.close();
		}
	

	}
static void insertInStudentTable(Connection conn,int p_id , String student_ID  ) throws SQLException {
	
	Statement stmt = null;
	try {
		stmt = conn.createStatement();
		
		String createString = "INSERT INTO student(p_id ,student_ID ) VALUES ( '"+p_id+"' , '"+student_ID+"' )";
		stmt.executeUpdate(createString);
		System.out.println("Inserted!");
	} catch (SQLException e) {
		System.out.println(e);
	} finally {
		stmt.close();
	}
}
static void insertInstructorTable(Connection conn,int p_id , String instructor_ID, String dept ) throws SQLException {
	
	Statement stmt = null;
	try {
		stmt = conn.createStatement();
		
		String createString = "INSERT INTO instructor(p_id,instructor_ID ,dept) VALUES( '"+p_id+"' , '"+instructor_ID+"' , '"+dept+"' )";
		stmt.executeUpdate(createString);
		System.out.println("Inserted!");
	} catch (SQLException e) {
		System.out.println(e);
	} finally {
		stmt.close();
	}


}
static void insertInCourseTable(Connection conn,int c_section , String c_name,int i_id , int c_units  ) throws SQLException {
	
	Statement stmt = null;
	try {
		stmt = conn.createStatement();
		
		String createString = "INSERT INTO course(c_section,c_name, i_id , c_units  ) VALUES ( '"+c_section+"' , '"+c_name+"', '"+i_id+"','"+c_units+"' )";
		stmt.executeUpdate(createString);
		System.out.println("Inserted!");
	} catch (SQLException e) {
		System.out.println(e);
	} finally {
		stmt.close();
	}


}

static void insertInAssociatedCourseTable(Connection conn,int p_id , int c_id ) throws SQLException {
	
	Statement stmt = null;
	try {
		stmt = conn.createStatement();
		
		String createString = "INSERT INTO as_course(p_id, c_id ) VALUES ( '"+p_id+"' , '"+c_id+"' )";
		stmt.executeUpdate(createString);
		System.out.println("Inserted!");
	} catch (SQLException e) {
		System.out.println(e);
	} finally {
		stmt.close();
	}


}

	
static void dropTablesIfExists(Connection conn, Statement stmt) throws SQLException{
	try{
	    DatabaseMetaData dbm = conn.getMetaData();
	    ResultSet rs;
	    rs=	dbm.getTables(null, null, "as_course", null);
	    if (rs.next()) {
	      System.out.println("Table 'as_course' exists. Dropping"); 
	      stmt.execute("Drop table as_course");
	    }
	      
	    rs = dbm.getTables(null, null, "office_hours", null);
		if (rs.next()) {
		   System.out.println("Table 'office_hours' exists. Dropping"); 
		   stmt.execute("Drop table office_hours");
	    }
		
		rs = dbm.getTables(null, null, "course_hours", null);
		if (rs.next()) {
		   System.out.println("Table 'course_hours' exists. Dropping"); 
		   stmt.execute("Drop table course_hours");
	    }

	    rs = dbm.getTables(null, null, "course", null);
		if (rs.next()) {
		   System.out.println("Table 'course' exists. Dropping"); 
		   stmt.execute("Drop table course");
	    }
		
	    rs = dbm.getTables(null, null, "instructor", null);
		if (rs.next()) {
		   System.out.println("Table 'instructor' exists. Dropping"); 
		   stmt.execute("Drop table instructor");
	    }
		
	    rs = dbm.getTables(null, null, "student", null);
		if (rs.next()) {
		   System.out.println("Table 'student' exists. Dropping"); 
		   stmt.execute("Drop table student");
	    }
	    rs = dbm.getTables(null, null, "person", null);
		if (rs.next()) {
		   System.out.println("Table 'person' exists. Dropping"); 
		   stmt.execute("Drop table person");
	    }
	}
	    catch(SQLException e){
	    	System.out.println(e);
	    }
}
}

