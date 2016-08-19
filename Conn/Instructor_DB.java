package Conn;
import GUI.*;
import Main.*;
import Misc.*;
import Servant.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;


//import javax.resource.cci.ResultSet;



public class Instructor_DB
{

   private static Connection con = null;
   
   public Instructor_DB(){
	   
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

public static int getNextInstructorID(){
	return 1;
}

public static int insertInstructor(String instructorID,String fname, String lname, String address, String city,String state, String zip,String dept)
{
	
	try{
		   StartDB_Connection();
	   }
	   catch (SQLException e1)
		{
		   e1.printStackTrace();
		}

	
	String st2="";
	
	InstructorServant instructor=new InstructorServant();
	
	boolean checkState=instructor.validateInstructorState(state);
	if(!checkState){
		//Exception
		return Exceptions.malformed_state;
	}
	boolean checkZip=instructor.validateInstructorZip(zip);
	if(!checkZip){
		//Exception 
		return Exceptions.malformed_zip;
	}
	
	boolean checkIDFlag=instructor.validateInstructorID(instructorID);
	if(!checkIDFlag){
		//Exception 
		return Exceptions.malformed_id;
	}
	
	boolean checkUniqueInstructorID=validateInstructorID(instructorID);
	if(!checkUniqueInstructorID){
		//Exception 
		return Exceptions.person_exists;
	}
	System.out.println("Unique Instructor ID:");
	int pid=Person_DB.insertPerson(fname,lname,address,city,state,zip,con);
	st2 = "insert into instructor(p_id,instructor_ID,dept) values("+pid+",'"+instructorID+"','"+dept+"');";
	try 
	{
		java.sql.Statement s =  con.createStatement();
		s.executeUpdate(st2);
		s.close();
		System.out.println("Instructor Row Added");
	} 
	catch (Exception e)
	{
		System.out.println("Exception at insertInstructor: "+e +"   : 1");
		return Exceptions.create_exception;
	}
	 try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}
	System.out.println("Successfully Added:");
	return -1;
}


private static boolean validateInstructorID(String instructorID) {
	String st="select count(*) from instructor where instructor_ID='"+instructorID+"'";
	
	try 
	{
		java.sql.Statement s =  con.createStatement();
		java.sql.ResultSet rs=s.executeQuery(st);
		int count=-1;
		while(rs.next())
		{
			count=rs.getInt(1);
		}
		s.close();
		if(count!=0){
		System.out.println("Instructor is not unique:");
		return false;
		}
	} 
	catch (Exception e)
	{
		System.out.println("Exception at insertInstructor: "+e);
		return false;
	}
	
	
	return true;
}


public static String[][][] loadOfficeHour(String inID) {

	int c_id=0;
	String query="select day,stime,etime from office_hours where i_id=(select i_id from instructor where instructor_id='"+inID+"') AND day<>-1 AND stime<>-1 AND etime<>-1;";
	String query_class="select day,stime,etime from course_hours where c_id="+c_id;
	String query_cid="select DISTINCT c_id from as_course where p_id =(select DISTINCT p_id from instructor where i_id=(select i_id from instructor where instructor_id='"+inID+"'));";
	String[][][] officeHours=new String[500][3][1];
	String[][][] officeHours1=null;
	try
	{
	   StartDB_Connection();
	}
   catch (SQLException e1)
   {
	   e1.printStackTrace();
	}

	try 
	{
		
		java.sql.Statement s =  con.createStatement();
		java.sql.ResultSet rs=s.executeQuery(query);
		int i=0;
		System.out.println("I am start of office hours:");
		while(rs.next())
		{
			officeHours[i][0][0]=Integer.toString(rs.getInt("day"));
			officeHours[i][1][0]=rs.getString("stime");
			officeHours[i][2][0]=rs.getString("etime");
			System.out.println(" Office HOURS: "+officeHours[i][0][0]+" "+officeHours[i][1][0]+" "+officeHours[i][2][0]);
			i++;
		}
	
		java.sql.ResultSet rs3=s.executeQuery(query_cid);
		//System.out.println("I am start of office hours: Query ID");
		
		try{
			int j=0;
			int c_ids[]=new int[5000];
		while(rs3.next()){c_ids[j]=rs3.getInt("c_id"); j++;}
		for(int k=0;k<j;k++)
		{
			c_id=c_ids[k];
		query_class="select day,stime,etime from course_hours where c_id="+c_id;
		java.sql.ResultSet rs4=s.executeQuery(query_class);
		
		while(rs4.next())
		{
			
			System.out.println("I am start of office hours: Query CLASS :"+c_id);
			officeHours[i][0][0]=Integer.toString(rs4.getInt("day"));
			officeHours[i][1][0]=getColTime(rs4.getString("stime"));
			officeHours[i][2][0]=getColTime(rs4.getString("etime"));
			System.out.println(" ***************Office HOURS:*************** "+officeHours[i][0][0]+" "+officeHours[i][1][0]+" "+officeHours[i][2][0]);
			
			i++;
			
		}
		}
		}
		catch(Exception e){
			System.out.println(e);
		}
		System.out.println("I value"+i);
		officeHours1=new String[i][3][1];
		for(int j=0;j<i;j++){
			officeHours1[j][0][0]=officeHours[j][0][0];
			officeHours1[j][1][0]=officeHours[j][1][0];
			officeHours1[j][2][0]=officeHours[j][2][0];
			System.out.println("HOURS: "+officeHours1[j][0][0]+"   "+officeHours1[j][1][0]);
		}
		
		s.close();
		
	} 
	catch (Exception e)
	{
		System.out.println("Exception at retrieve Office Hour: "+e);
	}
	 try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}
	return officeHours1;
}


public static void writeOfficeHours(String instructorID, int day,
		String stime, String etime,String building,String room) {
	
	try{
		   StartDB_Connection();
	   }
	   catch (SQLException e1)
		{
		   e1.printStackTrace();
		}


	try{
	String query="insert into office_hours(i_id,day,stime,etime,building,room) values((select i_id from instructor where instructor_id='"+instructorID+"'),"+day+",'"+stime+"','"+etime+"','"+building+"','"+room+"');";
	java.sql.Statement s =  con.createStatement();
	s.executeUpdate(query);
	s.close();
	
} 
catch (Exception e)
{
	System.out.println("Exception at insertOffice-Hour: "+e);
}
try{
	StopDB_Connection();
}
catch(SQLException sql)
{
  System.out.println("sql exception from"+sql.getMessage());	
}
}


public static String[][][] loadRoomOfficeHour(String building,String roomNo) {
	
	String query="select day,stime,etime from office_hours where building='"+building+"' AND room='"+roomNo+"' AND day<>-1 AND stime<>-1;";
	String[][][] officeHours=null;
	try{
		   StartDB_Connection();
	   }
	   catch (SQLException e1)
		{
		   e1.printStackTrace();
		}

	try 
	{
		System.out.println("here:");
		java.sql.Statement s =  con.createStatement();
		java.sql.ResultSet rs1=s.executeQuery(query);
		int size=0;
		while(rs1.next()){size++;}
		officeHours=new String[size][3][1];
		
		java.sql.ResultSet rs=s.executeQuery(query);
		int i=0;
		while(rs.next())
		{
			officeHours[i][0][0]=Integer.toString(rs.getInt("day"));
			officeHours[i][1][0]=rs.getString("stime");
			officeHours[i][2][0]=rs.getString("etime");
			System.out.println(" Office HOURS: "+officeHours[i][0][0]+" "+officeHours[i][1][0]+" "+officeHours[i][2][0]);
			i++;
		}
		s.close();
		
	} 
	catch (Exception e)
	{
		System.out.println("Exception at retrieve Office Hour: "+e);
	}
	 try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}
	return officeHours;

}



//Logic to remove Office Hour
public static boolean deleteOfficeHour(String i_id, String building,
		String room, int day, String stime, String etime) {
	String query="select count(*) from office_hours where i_id=(select i_id from instructor where instructor_id='"+i_id+"') AND building='"+building+"' AND day="+day+" AND stime='"+stime+"' AND etime='"+etime+"' AND room='"+room+"'";
	System.out.println("HERE:"+i_id+"  "+day+" "+stime+" "+etime);
	try{
		   StartDB_Connection();
	   }
	   catch (SQLException e1)
		{
		   e1.printStackTrace();
		}

	try 
	{
		java.sql.Statement s =  con.createStatement();
		java.sql.ResultSet rs=s.executeQuery(query);
	
		int count=0;
		while(rs.next())
		{
			count=rs.getInt(1);
		}
System.out.println("COUNT :"+count);
		if(count==0){
			return false;
		}
		
		else{
			
			String delete="delete from office_hours where i_id=(select i_id from instructor where instructor_id='"+i_id+"') AND building='"+building+"' AND day="+day+" AND stime='"+stime+"' AND etime='"+etime+"' AND building='"+building+"' AND room='"+room+"'";
			java.sql.Statement s1 =  con.createStatement();
			s1.executeUpdate(delete);
		}
		s.close();
		
	} 
	catch (Exception e)
	{
		System.out.println("Exception at retrieve Office Hour:3 "+e);
	}
	 try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}
	
	
	return true;
}


public static void writeDeleteOfficeHours(String building,String room, String i_id, int day, String stime, String etime) {
	
	try{
		   StartDB_Connection();
	   }
	   catch (SQLException e1)
		{
		   e1.printStackTrace();
		}

	try 
	{
		
			String delete="delete from office_hours where i_id=(select i_id from instructor where instructor_id='"+i_id+"') AND building='"+building+"' AND day="+day+" AND stime='"+stime+"' AND etime='"+etime+" AND room='"+room+"';";
			java.sql.Statement s =  con.createStatement();
			s.executeUpdate(delete);
		
		s.close();
		
	} 
	catch (Exception e)
	{
		System.out.println("Exception at retrieve Office Hour: "+e);
	}
	 try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}
	
}


public static String getOfficeHours(String i_id) {

	String query="select building,room,day,stime,etime from office_hours where i_id=(select i_id from instructor where instructor_id='"+i_id+"');";
	String officeHours="";
	try{
		   StartDB_Connection();
	   }
	   catch (SQLException e1)
		{
		   e1.printStackTrace();
		}

	try 
	{
		java.sql.Statement s =  con.createStatement();
		java.sql.ResultSet rs=s.executeQuery(query);
		System.out.println("at db"+query);
		int i=0;
		while(rs.next())
		{
			System.out.println("at db");
			officeHours+=(rs.getString("building"));
			officeHours+=" "+(rs.getString("room"));
			officeHours+=" "+getDayName((rs.getInt("day")));
			officeHours+=" "+getTime(rs.getString("stime"));
			officeHours+="-"+getTime(rs.getString("etime"))+"\n";
			System.out.println(" Office HOURS: "+officeHours);
			i++;
		}
		officeHours=Integer.toString(i)+"\n"+officeHours;
		s.close();
		
	} 
	catch (Exception e)
	{
		System.out.println("Exception at retrieve Office Hour: "+e);
	}
	 try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}
	return officeHours;

	
}


private static String getDayName(int day) {
	
	if(day == -1)
		day = 7;
	String validDays[]={"M","T","W","R","F","S","U","TBA"};
	return validDays[day];
	
	
}


private static String getTime(String time) {

	StringTokenizer st=new StringTokenizer(time,":");
	return st.nextToken()+st.nextToken();
	
}

private static String getColTime(String time) {

	String st=time.substring(0,2)+":"+time.substring(2,4);
	return st;
	
}


public static void setDepartment(String i_id,String newDepartmentName) {

	String query="update instructor set dept='"+newDepartmentName+"' where instructor_id='"+i_id+"';";
	String[] officeHours=null;
	try{
		   StartDB_Connection();
	   }
	   catch (SQLException e1)
		{
		   e1.printStackTrace();
		}

	try 
	{
		
		java.sql.Statement s =  con.createStatement();
		s.executeUpdate(query);
		s.close();
		
	} 
	catch (Exception e)
	{
		System.out.println("Exception at set Department: "+e);
	}
	 try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}

}


public static String getDepartment(String i_id) {

	String query="select dept from instructor where instructor_id='"+i_id+"'";
	String departmentName=null;
	
	try{
		   StartDB_Connection();
	   }
	   catch (SQLException e1)
		{
		   e1.printStackTrace();
		}

	try 
	{
		
		java.sql.Statement s =  con.createStatement();
		java.sql.ResultSet rs=s.executeQuery(query);
		
		while(rs.next())
		{
			departmentName=rs.getString(1);
		}
		s.close();
		
	} 
	catch (Exception e)
	{
		System.out.println("Exception at retrieve Office Hour: "+e);
		return departmentName;
	}
	
	 try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}
	
	return departmentName;
	
}


public static String getInstructorByName(String departmentNamePattern) {
	
	String query="select * from instructor where dept LIKE '"+departmentNamePattern+"%'";
	String instructor_ID_List="";
	System.out.println(query);
	
	try{
		   StartDB_Connection();
	   }
	   catch (SQLException e1)
		{
		   e1.printStackTrace();
		}

	try 
	{
		
		java.sql.Statement s =  con.createStatement();
		java.sql.ResultSet rs=s.executeQuery(query);
		int i=0;
		while(rs.next())
		{
			instructor_ID_List+=(rs.getString("instructor_id"))+"\n";
			System.out.println("LIST *****************"+instructor_ID_List);
			i++;
		}
		instructor_ID_List=Integer.toString(i)+"\n"+instructor_ID_List;
		s.close();
		
	} 
	catch (Exception e)
	{
		System.out.println("Exception at retrieve Office Hour: "+e);
	}
	 try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}
	return instructor_ID_List;

}


public static String[] getDaysAndTime(String courseSection, String courseName) {

	String query="select day, stime, etime from course_hours where c_id=(select c_id from course where c_section="+courseName+" AND c_name='"+courseSection+"')";
	System.out.println("Query:"+query);
	
	String daysAndTime[]=new String[3];
	try{
		   StartDB_Connection();
	   }
	   catch (SQLException e1)
		{
		   e1.printStackTrace();
		}

	try 
	{
		
		java.sql.Statement s =  con.createStatement();
		java.sql.ResultSet rs=s.executeQuery(query);
		int i=0;
		while(rs.next())
		{

			daysAndTime[0]=(rs.getString("day"));
			System.out.println("daytime:"+daysAndTime[0]);
			daysAndTime[1]=rs.getString("stime");
			daysAndTime[2]=rs.getString("etime");
			
		}
		
		s.close();
		
	} 
	catch (Exception e)
	{
		System.out.println("Exception at retrieve Office Hour: "+e);
	}
	 try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}

	
	return daysAndTime;
}


public static void writeSetCourseInstructor(String instructorID,
		String courseName, String courseSection) {
	
	String asCourseQuery="update as_course set p_id=(select p_id from instructor where i_id=(select i_id from instructor where instructor_id='"+instructorID+"')) where c_id=(select DISTINCT c_id from course where c_name='"+courseName+"' AND c_section="+courseSection+");";
	String courseQuery="update course set i_id=(select i_id from instructor where instructor_id='"+instructorID+"') where c_name='"+courseName+"' AND c_section="+courseSection+";";
	System.out.println(asCourseQuery);
	System.out.println(courseQuery);
	try{
		   StartDB_Connection();
	   }
	   catch (SQLException e1)
		{
		   e1.printStackTrace();
		}

	try 
	{
		
		java.sql.Statement s =  con.createStatement();
		s.executeUpdate(asCourseQuery);
		s.executeUpdate(courseQuery);
		
				s.close();
		
	} 
	catch (Exception e)
	{
		System.out.println("Exception at retrieve Office Hour: "+e);
	}
	 try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}
	


}


public static int getPersonExistance(String instructorID) {

	String query="select count(i_id) from instructor where instructor_id='"+instructorID+"';";
	System.out.println("Query of person: "+query);
	try{
		   StartDB_Connection();
	   }
	   catch (SQLException e1)
		{
		   e1.printStackTrace();
		}

		int count=0;
	try 
	{
		
		java.sql.Statement s =  con.createStatement();
		java.sql.ResultSet rs=s.executeQuery(query);

		while(rs.next())
		{
			count=rs.getInt(1);
			
		}
		s.close();
		
	} 
	catch (Exception e)
	{
		System.out.println("Exception at retrieve Office Hour: "+e);
	}
	 try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}

	return count;
}


public static void setCourseLocation(String courseName, String courseSection,
		String day, String stime, String etime, String building, String room) {
	String queryUpdate="update course_hours set day='"+day+"' , stime='"+stime+"' , etime='"+etime+"' where c_id=(select c_id from course where c_name='"+courseName+"' AND c_section="+courseSection+");";
	System.out.println(queryUpdate);
	try{
		   StartDB_Connection();
	   }
	   catch (SQLException e1)
		{
		   e1.printStackTrace();
		}

	try 
	{
		
		java.sql.Statement s =  con.createStatement();
		s.executeUpdate(queryUpdate);
		s.close();
		
	} 
	catch (Exception e)
	{
		System.out.println("Exception at update location: "+e);
	}
	 try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}
		
}

}