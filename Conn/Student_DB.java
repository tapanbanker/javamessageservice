package Conn;
import GUI.*;
import Main.*;
import Misc.*;
import Servant.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


//import Misc.Exceptions;

public class Student_DB
{
   private static Connection con = null;
   Exceptions e = new Exceptions();
   
   public Student_DB() throws SQLException{
	   
   }

   private static void StartDB_Connection() throws SQLException
	{
		if(con!=null && con.isClosed()==false)
		{
			return;
		}
		else
		{
			try 
			{
			   con = dbConnectionPool.getConnectionFromPool();
			} 
			catch (SQLException e1)
			{
			   e1.printStackTrace();
			}

		}
	}	
  private static void StopDB_Connection() throws SQLException
	{
		if(con!=null )
		{
			dbConnectionPool.returnConnectionToPool(con);
			con = null;
		}

	}	
  
   public static String unenrollStudentFromCourse(String student_id, String course_name,String section)
   {
	   String query=null, reply = null;
	   int p_id, c_id;
	   Statement stmt = null;
	   try {
		StartDB_Connection();
		stmt = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int ret = checkMalformedID(student_id);
		 System.out.println("REURN VALUE :"+ret);
		 if (ret == -1)
		 {
			 System.out.println("REURN VALUE IN:"+ret);
			 try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
			return Integer.toString(Exceptions.malformed_id);
		 } 
		 
		 //no_such_person   
		 ret = checkNoSuchStudent(student_id,stmt);
		 if (ret == -1)
		 {
			 try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
			 return Integer.toString(Exceptions.no_such_person);
		 }
		 else
		 {
			 p_id = ret;
		 }
		 
		
		 //student_not_enrolled
		  ret = checkStudentAlreadyEnrolled(student_id, course_name,stmt);
		  if (ret ==0)
		  {
			  try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
			  return Integer.toString(Exceptions.student_not_enrolled);
		  }
		 //malformed_id, 		 
		 /*ret = checkMalformedID(student_id);
		 System.out.println("REURN VALUE :"+ret);
		 if (ret == -1)
		 {
			 System.out.println("REURN VALUE IN:"+ret);
			 try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
			return Integer.toString(Exceptions.malformed_id);
		 } 
		 
		 //no_such_person   
		 ret = checkNoSuchStudent(student_id,stmt);
		 if (ret == -1)
		 {
			 try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
			 return Integer.toString(Exceptions.no_such_person);
		 }
		 else
		 {
			 p_id = ret;
		 }*/
		 
		  //too_many_units
		  ret = checkNoSuchCourse(course_name, section,stmt);
		  if (ret == -2)
		  {
			  try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
			  return Integer.toString(Exceptions.no_such_course);
		  }
		   else
		   {
			  c_id = ret;
		   }
		  //Everything ok..delete
		  try{
			  System.out.println("Query - "+query);
			  query= "DELETE FROM as_course WHERE p_id = '"+p_id+"' AND c_id = '"+c_id+ "'";
			  System.out.println("Query - "+query);
			  System.out.println("Query - "+query);
			  stmt.executeUpdate(query);
			  stmt.close();
			  reply = Integer.toString(Exceptions.success);
		  } catch (Exception e) {
			e.printStackTrace();
	   }
		  try{
				StopDB_Connection();
			}
			catch(SQLException sql)
			{
			  System.out.println("sql exception from"+sql.getMessage());	
			}  
	return reply;
   }
   
   //Method to enroll student in course
   
   public static String enrollStudentInCourse(String student_id, String course_name,String section)
   {
	   String query=null, reply = null;
	   int p_id, c_id;
	   Statement stmt = null;
	   try {
		StartDB_Connection();
		stmt = con.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		 //malformed_id, 	
		System.out.println("checkMalformedID");
		 int ret = checkMalformedID(student_id);
		 if (ret == -1){
			  try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
			 return Integer.toString(Exceptions.malformed_id);
		 }

		  //no_such_person   
		 System.out.println("checkNoSuchStudent");
		  ret = checkNoSuchStudent(student_id,stmt);
		  if (ret == -1){
			  try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
		  
			 return Integer.toString(Exceptions.no_such_person);
		  }else 
			 p_id = ret;
		 
		  //no_such_course
		  System.out.println("checkNoSuchCourse");
		  ret = checkNoSuchCourse(course_name, section,stmt);
		  if (ret == -2){

			  try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
		  
			  return Integer.toString(Exceptions.no_such_course);
		  }
		  //schedule_conflict
		  System.out.println("checkScheduleConflict");
		  ret = checkScheduleConflict(student_id, course_name,section,stmt);
		  if (ret !=0){

			  try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
		 
			  return Integer.toString(ret);
		   }
		  //student_already_enrolled
		  ret = checkStudentAlreadyEnrolled(student_id, course_name,stmt);
		  if (ret != 0){

			  try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
		  
			  return Integer.toString(ret);
		  }
		  //too_many_units
		  ret = checkTooManyUnits(student_id, course_name, section,stmt);
		  if (ret == -1){

			  try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
		  
			  return Integer.toString(Exceptions.too_many_units);
		  }else
			  c_id = ret;

		  
		  //Everything ok..insert
		  try{
			  query= "INSERT INTO as_course VALUES ( "+p_id+" , "+c_id+ ")";
			  System.out.println("Query - "+query);
			  System.out.println("Query - "+query);
			  stmt.executeUpdate(query);
			  stmt.close();
			  reply = Integer.toString(Exceptions.success) +"\n" + 0;
		  } catch (Exception e) {
			  reply = Integer.toString(Exceptions.database_exception);
			  System.out.println(e);
	   }
		  try{
				StopDB_Connection();
			}
			catch(SQLException sql)
			{
			  System.out.println("sql exception from"+sql.getMessage());	
			}
	return reply;
   }
   
   public static String getEnrolledUnits(String student_id)
   {
	   Statement stmt = null;
	   try {
			StartDB_Connection();
			stmt = con.createStatement();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	 //malformed_id, 	

	 int ret = checkMalformedID(student_id);

	 if (ret == -1){
			try{
				StopDB_Connection();
			}
			catch(SQLException sql)
			{
			  System.out.println("sql exception from"+sql.getMessage());	
			}
		 return Integer.toString(Exceptions.malformed_id);
	 }
	//no_such_person   
	 ret = checkNoSuchStudent(student_id,stmt);
	if (ret == -1){
		try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}
	
		 return Integer.toString(Exceptions.no_such_person);
	}
	
	return (Integer.toString(Exceptions.success)+"\n"+1+"\n"+Integer.toString(calculateEnrolledUnits(student_id,stmt)));
			
   }
   
   
   public static String getStudentsEnrolledInCourse(String course_name, String section)
   {   
	   Statement stmt = null;
	   String reply = null;

	   try {
			StartDB_Connection();
			stmt = con.createStatement();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
	
	  //function to check no_such_course
	  int ret = checkNoSuchCourse(course_name, section,stmt);
	  int c_id;
	  if (ret == -2){
			try{
				StopDB_Connection();
			}
			catch(SQLException sql)
			{
			  System.out.println("sql exception from"+sql.getMessage());	
			}
	  
		  return Integer.toString(Exceptions.no_such_course);
	 } else
		  c_id = ret;
	  
	  
	   try{
		  String query= "SELECT s.student_id as id FROM student s " +
		  		 		"JOIN as_course a ON s.p_id = a.p_id " +
		  		 		"WHERE a.c_id = "+c_id;
		  
		  System.out.println("Query - "+query);
		  ResultSet rs = stmt.executeQuery(query);
  
		  int i = 0;
		  String studentList = "";
		  while (rs.next()) {
			i++;
			studentList = studentList + rs.getString("id") + "\n";
          }
		  reply = Integer.toString(Exceptions.success)+"\n"+i+"\n"+ studentList;
		  stmt.close();
	    }catch (Exception e) {
		  reply = Integer.toString(Exceptions.database_exception);
		  System.out.println(e);
	    }
	    try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}
      return reply;			
   }
	  
   //method to check malformed_id 
   public static int checkMalformedID(String id){
		  /*int ret = 0;
		  String idPattern = "[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9][0-9][0-9]";
		  if(!id.matches(idPattern))
				ret = -1;
		  System.out.println("test "+ret);
		  return ret;*/
	   System.out.println("ID::::::::::"+id);
	   boolean checkIDFlag=(new InstructorServant()).validateInstructorID(id);
		if(!checkIDFlag){
			//Exception 
			return -1;
		}
		else{return 0;}
	   
	   }
  
   //method to check no_such_person
   public static int checkNoSuchStudent(String student_id,Statement stmt){
	   int ret = -1;
	   try{
		   String query = "SELECT p_id FROM student WHERE student_id = '"+student_id+"'";
		   System.out.println("Query - "+query);
		   ResultSet rs = stmt.executeQuery(query);
		   if(rs.next())
			   ret = rs.getInt("p_id");
		   else
			   ret = -1;
	   } catch (Exception e) {
				e.printStackTrace();
	   }
	   return ret;
   }
   
   //method to check student already enrolled
   public static int checkStudentAlreadyEnrolled(String student_id, String course_name, Statement stmt){
	   int ret = 0;
	   try{
		   String query = "SELECT ac.c_id FROM as_course ac " +
		   				  "JOIN course c ON ac.c_id = c.c_id " +
		   				  "JOIN student s ON ac.p_id = s.p_id " +
		   				  "WHERE c.c_name = '"+course_name+"' AND s.student_id = '"+student_id+"'";
		   
		   System.out.println("Query - "+query);
		   ResultSet rs = stmt.executeQuery(query);
		   //rs.beforeFirst();
		   if(rs.next()){
			 ret = Exceptions.student_already_enrolled;
		   }		   
		  }catch(Exception e ){
	    	ret = Exceptions.database_exception;
	    	System.out.println(e);
		}
		return ret;
   }
   
   public static int calculateEnrolledUnits(String student_id, Statement stmt){
	   int enrolledUnits = 0;
	 try{
	   String query = "SELECT sum(c_units) as sum FROM as_course ac " +
	   				  "JOIN student s ON ac.p_id = s.p_id " +
	   				  "JOIN course c ON ac.c_id = c.c_id " +
	   				  "WHERE s.student_id = '"+student_id + "' "+
	   				  "GROUP BY s.student_id";
	   
	   System.out.println("Query - "+query);
	   ResultSet rs = stmt.executeQuery(query);
	   
	   rs.beforeFirst();
	  
	   while(rs.next())
		   enrolledUnits = rs.getInt("sum");
	 }catch(Exception e ){
			System.out.println(e);
		}
	 
	 System.out.println(enrolledUnits);
	 return enrolledUnits;
   }
   
   
   //method to check too_many_units
   public static int checkTooManyUnits(String student_id, String course_name, String section, Statement stmt){
	   int ret = 0;
	   int enrolledUnits = 0, toEnrollUnits =0;
	   
	   
	   try{
		   enrolledUnits = calculateEnrolledUnits(student_id,stmt);
		   
		   String query = "SELECT c_id, c_units FROM course " +
		   				  "WHERE c_name = '"+course_name + "' "+
		   				  "AND c_section = '"+section+ "' ";
		   System.out.println("Query - "+query);
		   ResultSet rs2 = stmt.executeQuery(query);
		   
		   rs2.beforeFirst();
		   while(rs2.next()){
			   toEnrollUnits = rs2.getInt("c_units");
			   ret = rs2.getInt("c_id");
		   }

		   if( enrolledUnits + toEnrollUnits > 24 )
			   ret = -1; 

	    }catch(Exception e ){
			System.out.println(e);
		}
		return ret;
   }
   
   //method to check No_such_course
   public static int checkNoSuchCourse(String course_name, String section, Statement stmt){
	   int ret = -2;
      
	   try{   
		   String query = "SELECT c_id, c_units FROM course " +
		   				  "WHERE c_name = '"+course_name + "' "+
		   				  "AND c_section = '"+section+ "' ";
		   System.out.println("Query - "+query);
		   ResultSet rs2 = stmt.executeQuery(query);
		   
		   rs2.beforeFirst();
		   while(rs2.next()){
			   ret = rs2.getInt("c_id");
		   }

	    }catch(Exception e ){
			System.out.println(e);
		}
		return ret;
   }
   
  //method to check Schedule conflict
  public static int checkScheduleConflict(String studentID, String course_name, String section, Statement stmt){
		String toAddDay = null,enrolledDay;
		int toAddStime = 0,toAddEtime = 0,enrolledStime,enrolledEtime;
	   int ret=0;
	   try {
		   //get to-add course times
		   String query = "SELECT day,stime,etime FROM course_hours ch " +
		   				  "JOIN course c on ch.c_id = c.c_id "+
                          "WHERE c.c_name= '"+course_name+"' AND c.c_section = '"+section+"'";
		   System.out.println("Query - "+query);
		   ResultSet rs = stmt.executeQuery(query);

		   rs.beforeFirst();
		   if(rs.next()){
			   toAddDay = rs.getString("day");
			   toAddStime = rs.getInt("stime");
			   toAddEtime = rs.getInt("etime");
		   }	
		   
		   //get enrolled course times
		   query = "SELECT ch.day,ch.stime,ch.etime FROM course_hours ch " +
		   		   "JOIN as_course ac on ac.c_id = ch.c_id " +
		   		   "JOIN course c ON ac.c_id = c.c_id " +
			  	   "JOIN student s ON ac.p_id = s.p_id " +
			       "WHERE s.student_id = '"+studentID+"'";
		   
		   System.out.println("Query - "+query);
		   rs = stmt.executeQuery(query);
		   System.out.println("Result");

		   while(rs.next()){
			   enrolledDay = rs.getString("day");
			   enrolledStime = rs.getInt("stime");
			   enrolledEtime = rs.getInt("etime");
			   if(enrolledDay.equalsIgnoreCase("-1") || toAddDay.equalsIgnoreCase("-1")){}//i.e. TBA, so no conflict check	   
			   else{   //check conflict
				   System.out.println("in else");
				   ret = isConflict(toAddDay,toAddStime,toAddEtime,enrolledDay,enrolledStime,enrolledEtime);
				   if(ret==-1){
					  ret = Exceptions.schedule_conflict;
					  break;
				   }
			   }
		   }	   
		} catch (SQLException e) {
			ret = Exceptions.database_exception;
			  System.out.println(e);
		}			   
	   
	   return ret;
  }
  
	public static int isConflict(String toAddDay,int toAddStime,int toAddEtime,String enrolledDay,int enrolledStime,int enrolledEtime){
		int ret=0;
		
		System.out.println("check conflict -- >");
		System.out.println(toAddDay);
		System.out.println(toAddStime);
		System.out.println(toAddEtime);
		System.out.println(enrolledDay);
		System.out.println(enrolledStime);
		System.out.println(enrolledEtime);
		
		if(toAddDay.equalsIgnoreCase(enrolledDay)){
			if((toAddStime>enrolledStime) &&  (toAddStime<enrolledEtime)){ //if start time falls between enrolled class's timings				ret = -1;
			}
			if((toAddEtime>enrolledStime) &&  (toAddEtime<enrolledEtime)){ //if end time falls between enrolled class's timings
				ret = -1;
			}
			if((enrolledStime>toAddStime) &&  (enrolledStime<toAddEtime)){ //if enrolled class's start time falls between new class's timings
				ret = -1;
			}
			if((enrolledEtime>toAddStime) &&  (enrolledEtime<toAddEtime)){ //if enrolled class's end time falls between new class's timings
				ret = -1;
			}
			if((toAddStime == enrolledStime) && (toAddEtime == enrolledEtime)){
				ret = -1;
			}
		}
		return ret;
	}

	//Method to remove student
	public static String removeStudent(String student_id, String force)
	   {
		String query = null;
		String reply = null;
		Statement stmt = null;
		   try {
				StartDB_Connection();
				stmt = con.createStatement();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
		 //malformed_id, 		 
		 int ret = checkMalformedID(student_id);
		 if (ret == -1){
				try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
		 
			 return Integer.toString(Exceptions.malformed_id);
		 }
		//no_such_person   
		 ret = checkNoSuchStudent(student_id,stmt);
		 if (ret == -1){
				try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
		
			 return Integer.toString(Exceptions.no_such_person);
		 }
	try{
		con.setAutoCommit(false);
		
		 //check if student is enrolled in courses, can't be removed
		 if(Integer.parseInt(force) == 0){
			 ret = calculateEnrolledUnits(student_id,stmt);
			 if (ret != 0){
				 con.setAutoCommit(true);
					stmt.close();
					return Integer.toString(Exceptions.has_courses);
			 }
		 }
		 else //force un-enroll. Remove enrollment and then delete
		 {
			 query= "DELETE ac FROM as_course ac " +
			 		"JOIN student s ON ac.p_id = s.p_id " +
			 		"WHERE student_id = '"+student_id+"'";
			  System.out.println("Query - "+query);
			  stmt.executeUpdate(query);
		 }
		 //remove student
		  
		  int p_id = 0;
		  //Get p_id for student
		  query = "SELECT p_id FROM student WHERE student_id = '"+student_id+"'";
		  ResultSet rs = stmt.executeQuery(query);
		  while(rs.next()){
			  p_id = rs.getInt("p_id");
		  }
		  //Delete from student table
		  query= "DELETE FROM student WHERE p_id = "+p_id;
		  System.out.println("Query - "+query);
		  stmt.executeUpdate(query);
		  
		  //Delete from person table
		  query= "DELETE FROM person WHERE p_id = "+p_id;
		  System.out.println("Query - "+query);
		  stmt.executeUpdate(query);
		  
		  stmt.close();
		  reply = Integer.toString(Exceptions.success) +"\n"+0;
		  con.commit();
		 
		  } catch (Exception e) {
			  reply = Integer.toString(Exceptions.database_exception);
			  e.printStackTrace();
			  try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	   }
		finally{
			 try {
				con.setAutoCommit(true);
				stmt.close();
				try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	return reply;
	}
	
	//Method to calculate bill
	public static String calculateBill(String student_id)
	   {
		String query = null;
		String reply = null;
		Statement stmt = null;
		int p_id;
		   try {
				StartDB_Connection();
				stmt =  con.createStatement();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
		 //malformed_id, 		 
		 int ret = checkMalformedID(student_id);
		 if (ret == -1){
				try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
		 
			 return Integer.toString(Exceptions.malformed_id);
		 }
		//no_such_person   
		 ret = checkNoSuchStudent(student_id,stmt);
		 if (ret == -1){
				try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
		 
			 return Integer.toString(Exceptions.no_such_person);
		} else 
			 p_id = ret;
		
		 try {
			  
			  //Get state of student
			  query = "SELECT state FROM Person WHERE p_id = "+p_id;
			  ResultSet rs = stmt.executeQuery(query);
			  
			  String state = null;
			  double fees = 0, mandatoryFees = 243.60;
			 
			  while(rs.next()){
				  state = rs.getString("state");
			  }
			  int enrolledUnits = calculateEnrolledUnits(student_id,stmt);  
			  if(enrolledUnits == 0)
				  fees = 0;
			  else{
				  if(state.equalsIgnoreCase("CA")){
					  if (enrolledUnits >=1 && enrolledUnits <=6)
						  fees = 456 + mandatoryFees;
					  else 
						  fees = 786 + mandatoryFees;
					}
				  else{
					  fees = enrolledUnits*282.00 + mandatoryFees;
				  }
			  }
			  stmt.close();
			  reply = Integer.toString(Exceptions.success) + "\n"+ 1+"\n"+ fees;
		  }
		  catch (SQLException e) {
			  reply = Integer.toString(Exceptions.database_exception);
			  e.printStackTrace();
			}
		  try{
				StopDB_Connection();
			}
			catch(SQLException sql)
			{
			  System.out.println("sql exception from"+sql.getMessage());	
			}
		 return reply;
	   }
	
	public static String insertStudent(String fname, String lname,String studentID, String address, String city,String state, String zip)
	{
		//malformed_id, malformed_state, malformed_zip, person_exists, create_exception
		Statement stmt = null;
		try{StartDB_Connection();
			stmt =  con.createStatement();
		}catch(Exception e){System.out.println("Exception at connection:"+e);}
		
		String query = null;
		 //malformed_id, 		 
		 int ret = checkMalformedID(studentID);
		 System.out.println("ret "+ret);
		 if (ret == -1)
		 {
			 try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
			return Integer.toString(Exceptions.malformed_id);
		 } 
		 
		   //malformed state
		  ret = validateState(state);
		  if(ret!=0){
			  try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
			return Integer.toString(Exceptions.malformed_state);
		 } 
			
		   //malformed zip
		  ret = validateZip(zip);		   
		  if(ret!=0){
			  try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
			return Integer.toString(Exceptions.malformed_zip);
		 } 
		
		ret = checkNoSuchStudent(studentID,stmt);
		if(ret!=-1){
			 try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
			return Integer.toString(Exceptions.person_exists);
		}
		//if all ok, insert student in a transaction
		try 
		{		
			stmt = con.createStatement();
			con.setAutoCommit(false);
		
			int pid=Person_DB.insertPerson(fname,lname,address,city,state,zip,con);
			query = "insert into student(p_id,student_ID) values("+pid+",'"+studentID+"')";
			stmt.executeUpdate(query);
			stmt.close();
			ret = Exceptions.success;
		} 
		catch(Exception e){
		 try {
			 ret = Exceptions.create_exception;
			con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		finally{
			 try {
				con.setAutoCommit(true);
				stmt.close();
				try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return Integer.toString(ret) +"\n" +0;
	}

	   //method to check if state is valid
	   public static int validateState(String new_state){  
		   String validStates[] = { "AL", "AK", "AZ", "AR", "CA", "CO", "CT",
					"DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY",
					"LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE",
					"NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR",
					"PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA",
					"WV", "WI", "WY" , 
					"Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut",
					"Delaware","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas",
					"Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota",
					"Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey",
					"New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon",
					"Pennsylvania","Rhode Island","South Carolina","South Dakota","Tennessee","Texas",
					"Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"};
		   
		   for (int i=0; i<validStates.length; i++)
	       {
	           if (validStates[i].equalsIgnoreCase(new_state))
	               return 0;
	       }
		   return -1;
	   }
	   
	   //method to check if zip is valid
	   public static int validateZip(String new_zip){
		  		  /*String zipCodePattern = "[0-9][0-9][0-9][0-9][0-9](-[0-9][0-9][0-9][0-9])?";
		  if(!new_zip.matches(zipCodePattern))
				ret = -1;
		  
		  return ret;*/
		  boolean checkZip=(new InstructorServant()).validateInstructorZip(new_zip);
			if(!checkZip){
				//Exception 
				return -1;
			}
			else {return 0;}
	}	  
	
}