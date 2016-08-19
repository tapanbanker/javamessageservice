package Conn;

import GUI.*;
import Main.*;
import Misc.*;
import Servant.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Course_DB
{
	private static Connection con = null;
	
	   static Statement stmt = null;
	   Exceptions e = new Exceptions();
	   
	   public Course_DB() throws SQLException{
		   
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
	   
	   
	   public static String[][][] loadRoomOfficeHour(String building, int roomNo) {
			String query="select day,stime,etime from office_hours where building='"+building+"' AND room='"+roomNo+"';";
			String[][][] officeHours=null;
			try{StartDB_Connection();}catch(Exception e){System.out.println("Exception at connection:"+e);}
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

	   public static String[][][] loadRoomCourseHours(String building,String roomNo) {

			String query="select day,stime,etime from course_hours where building='"+building+"' AND room='"+roomNo+"';";
			String[][][] officeHours=null;
			try{StartDB_Connection();}catch(Exception e){System.out.println("Exception at connection:"+e);}
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
					String temp=rs.getString("stime");
					temp+="-"+rs.getString("etime");
					String[] validHours=CourseServant.validateHours(temp);
					officeHours[i][1][0]=validHours[0];
					officeHours[i][2][0]=validHours[1];
					System.out.println(" Office HOURS**: "+officeHours[i][0][0]+" "+officeHours[i][1][0]+" "+officeHours[i][2][0]);
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


	   public static int writeCreateCourse(String courseName,
				String courseSection, String instructorID, String building,
				String room, int day, String stime, String etime,
				String units) {

			
			
			try{StartDB_Connection();}catch(Exception e){System.out.println("Exception at connection:"+e);}

			try{
			
			String course="insert into course(c_section,c_name,i_id,c_units) values('"+courseSection+"','"+courseName+"',(select i_id from instructor where instructor_ID='"+instructorID+"'),"+units+");";
			String as_course="insert into as_course(p_id,c_id) values((select p_id from instructor where instructor_ID='"+instructorID+"'),(select max(c_id) from course));";
			String course_hours="insert into course_hours(c_id,day,stime,etime,building,room) values((select max(c_id) from course),"+day+",'"+stime+"','"+etime+"','"+building+"','"+room+"');";
			String testNull="select i_id from instructor where instructor_ID='"+instructorID+"';";
			
			java.sql.Statement s =  con.createStatement();
			java.sql.ResultSet rs=s.executeQuery(testNull);
			rs.next();
			if(rs.getString(1)==null){{throw new Exception();}}
			s.executeUpdate(course);
			s.executeUpdate(course_hours);
			s.executeUpdate(as_course);
			s.close();
			
		} 
		catch (Exception e)
		{
			System.out.println("Exception at insertOffice-Hour: "+e);
			return 1;
		}
		try{
			StopDB_Connection();
		}
		catch(SQLException sql)
		{
		  System.out.println("sql exception from"+sql.getMessage());	
		}
			return 0;
		}


		public static int getNumberOfCourse(String courseName,String courseSection) {
		
			try{StartDB_Connection();}catch(Exception e){System.out.println("Exception at connection:"+e);}
			
			String query="select count(*) from course where c_name='"+courseName+"' AND c_section="+courseSection;
			
			int count=-1;
			
			try 
			{
				java.sql.Statement s =  con.createStatement();
				java.sql.ResultSet rs=s.executeQuery(query);
				
				while(rs.next())
				{
					
					count=rs.getInt(1);
				
				}
				s.close();
				
				try{
					StopDB_Connection();
				}
				catch(SQLException sql)
				{
				  System.out.println("sql exception from"+sql.getMessage());	
				}
				return count;
			}
			catch(Exception e){
				System.out.println("COUNT IS AT COURSE :"+count);
				return count;
			}
		
		}
		
		/************************Snigdha***************/
	   public static String getCourses(String personID)
	   {
		   String reply;
		   try {
				StartDB_Connection();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		   try{
			   		//only to get p_id , no checking
			      int p_id = Person_DB.checkPersonExists(personID,con);

				  String query= "SELECT c.c_name, c.c_section FROM course c " +
				  		 		"JOIN as_course a ON c.c_id = a.c_id " +
				  		 		"WHERE a.p_id = "+p_id;
				  
				  System.out.println("Query - "+query);
				  stmt =  con.createStatement();
				  ResultSet rs = stmt.executeQuery(query);
		  
				  int i = 0;
				  String courseList = "";
				  while (rs.next()) {
					i++;
					String tmp = rs.getString("c_name") +" "+ rs.getString("c_section");
					courseList = courseList + tmp + "\n";
		          }
				  reply = Integer.toString(Exceptions.success)+"\n"+i+"\n"+ courseList;
				  stmt.close();
				  StopDB_Connection();
				  
			    }catch (Exception e) {
				  reply = Integer.toString(Exceptions.database_exception);
				  System.out.println(e);
			    }
		      return reply;			
	   }


	   public static String getLocation(String course_name, String section)
	   {
		   String reply;
		   try {
				StartDB_Connection();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		  int c_id;
		  //no_such_course
		  int ret = checkNoSuchCourse(course_name, section);
		  if (ret == -1)
			  return Integer.toString(Exceptions.no_such_course);
		  else
			  c_id = ret;	
		
		   
		try{
			  String query= "SELECT day,stime,etime,building,room FROM course_hours ch " +
			  		 		"WHERE ch.c_id = "+c_id +
			  		 		" ORDER BY day";
			  
			  System.out.println("Query - "+query);
			  stmt =  con.createStatement();
			  ResultSet rs = stmt.executeQuery(query);
			  String location = null;
			  String loc = null;
			  String time = null;
			  String day = null;
			  while (rs.next()) {
				  loc  = rs.getString("building") +" "+ rs.getString("room");
			      time = rs.getString("stime") +"-"+ rs.getString("etime");
			      System.out.println("conver days:");
			      if(day==null)
			    	  day = convertDay(rs.getInt("day"));
			      else
			    	  day = day + convertDay(rs.getInt("day"));
			  }
			  System.out.println("conver days done:");
			  if(day.contains("TBA"))
				  location = loc +" "+ day;
			  else
				  location = loc +" "+ day +" "+ time;
			  System.out.println("Integer:");
			  reply = Integer.toString(Exceptions.success)+"\n"+1+"\n"+location;
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
	   
	public static String convertDay(int day){
		if(day == -1)
			day = 7;
		String validDays[]={"M","T","W","R","F","S","U","TBA"};
		return validDays[day];
	}
	
	
	public static int checkNoSuchCourse(String course_name, String section){
	   int ret = -1;
	   
	   try{
		   stmt =  con.createStatement();
		   String query = "SELECT c_id FROM course " +
		   				  "WHERE c_name = '"+course_name + "' "+
		   				  "AND c_section = '"+section+ "' ";
		   System.out.println("Query - "+query);
		   ResultSet rs2 = stmt.executeQuery(query);
		   while(rs2.next()){
			   ret = rs2.getInt("c_id");
		   }
		  
		   stmt.close();			   
	    }catch(Exception e ){
	    	ret = Exceptions.database_exception;
			System.out.println(e);
		}
	return ret;
   }
	
	public static int checkHasStudent(String cname, String csec){
		int ret = 0;
		try{StartDB_Connection();}catch(Exception e){System.out.println("Exception at connection:"+e);}
		try{
			   stmt =  con.createStatement();
			   String query= "SELECT count(1) FROM as_course ac " +
			   				 "JOIN course c ON ac.c_id = c.c_id " +
			   				 "JOIN student s ON ac.p_id = s.p_id " +
			   				 "WHERE c.c_name = '"+cname +"' AND c.c_section = "+csec;

				System.out.println("Query - "+query);
				ResultSet rs = stmt.executeQuery(query);
					
				while (rs.next()) {
					ret = rs.getInt(1);
				}
				if(ret !=0 )
					ret = Exceptions.has_students;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	

///////////////////////////////////////////////////////////Shikha's part/////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////// setCourseName////////////////////////////

public static String setCourseName(String cName,String sec,String newCourseName)
{
ResultSet resultSet;
String reply=null;
String st1="";
String st2="";
int c_id = 0;
try {
StartDB_Connection();
} catch (SQLException e1) {
e1.printStackTrace();
}
 
	int ret = checkNoSuchCourse(cName, sec);
	  if (ret == -1)
	  {
		  String reply1=Integer.toString(Exceptions.no_such_course);
		  System.out.println(reply1);
		  return reply1;
	  }
	  else
		  c_id = ret;
 
try
{
reply="";
stmt=(Statement)con.createStatement();

st1="select c_id from course where c_name='"+newCourseName+"'and c_section='"+sec+"'";
st2="update course set c_name='"+newCourseName+"' where c_name='"+cName+"' and c_section='"+sec+"'";

resultSet=stmt.executeQuery(st1);

System.out.println(resultSet);
int i=0;
if(resultSet.next())
{
reply= Integer.toString(Exceptions.course_exists);
}
else
{   stmt.executeUpdate(st2);
reply= Integer.toString(Exceptions.success)+"\n"+0;
}
stmt.close();

}catch(Exception e ){
return Integer.toString(Exceptions.database_exception);

}
return reply;

}
/////////////////////////////////////////////////////////setCourseSection/////////////////////////////////////////////////////////////////////////

public static String setCourseSection(String cName,String sec,String newCourseSec)
{
ResultSet resultSet;
String reply=null;
String st1="";
String st2="";
int c_id=0;

try {
StartDB_Connection();
} catch (SQLException e1) {
e1.printStackTrace();
}

int ret = checkNoSuchCourse(cName, sec);
if (ret == -1)
{
	  String reply1=Integer.toString(Exceptions.no_such_course);
	  System.out.println(reply1);
	  return reply1;
}
else
	  c_id = ret;

try
{
reply="";
stmt=(Statement)con.createStatement();
st1="select c_id from course where c_name='"+cName+"' and c_section='"+newCourseSec+"'";
st2="update course set c_section='"+newCourseSec+"' WHERE c_name= "+cName+" AND c_section="+sec ; //where c_name='"+cName+"' and c_section='"+sec+"'";

resultSet=stmt.executeQuery(st1);

System.out.println(resultSet);
int i=0;
if(resultSet.next())
{
reply= Integer.toString(Exceptions.course_exists);
}
else 
{   stmt.executeUpdate(st2);
reply= Integer.toString(Exceptions.success)+"\n"+0;
}
stmt.close();

}catch(Exception e ){
return Integer.toString(Exceptions.database_exception);

}
return reply;

}
////////////////////////////////////////////////////////setCourseUnits/////////////////////////////////////////////////////////////////////   
public static String setCourseUnits(String cName,String sec,String units)
{
ResultSet resultSet;
String reply=null;
String nunits;
int c_id=0;
//int ret1=0;
String st2="";
try {
StartDB_Connection();
} catch (SQLException e1) {
e1.printStackTrace();
}
int ret = checkNoSuchCourse(cName, sec);

if (ret == -1)
{
	  String reply1=Integer.toString(Exceptions.no_such_course);
	  System.out.println(reply1);
	  return reply1;
}
else
	  c_id = ret;

try
{
reply="";
stmt=(Statement)con.createStatement();
//st1="select c_id from course where c_section='"+newCourseSec+"'";
st2="update course set c_units='"+units+"' where c_name='"+cName+"' and  c_section='"+sec+"'";
if(Integer.parseInt(units)<0)
{
//resultSet=stmt.executeQuery(st1);
reply= Integer.toString(Exceptions.malformed_units);
}

else 
{   stmt.executeUpdate(st2);
reply= Integer.toString(Exceptions.success)+"\n"+0;
}
stmt.close();

}catch(Exception e ){
return Integer.toString(Exceptions.database_exception);

}
return reply;

}
///////////////////////////////////////////////////getCourseUnits////////////////////////////////////////////////////////////////////////////////////
public static String getCourseUnits(String cName,String sec)
{
ResultSet resultSet;
String reply=null;
String nunits;;
String st2="";
int c_id=0;
try {
StartDB_Connection();
} catch (SQLException e1) {
e1.printStackTrace();
}

int ret = checkNoSuchCourse(cName, sec);

if (ret == -1)
{
	  String reply1=Integer.toString(Exceptions.no_such_course);
	  System.out.println(reply1);
	  return reply1;
}
else
	  c_id = ret;
try
{
reply="";
stmt=(Statement)con.createStatement();
//st1="select c_id from course where c_section='"+newCourseSec+"'";
st2="select c_units from course where c_name='"+cName+"'and c_section='"+sec+"'";

resultSet= stmt.executeQuery(st2);
if(resultSet.next())
{
//System.out.println("units are"+ resultSet.getString("c_units"));
reply= Integer.toString(Exceptions.success)+"\n"+1+"\n"+resultSet.getString("c_units");
}

stmt.close();

}catch(Exception e ){
return Integer.toString(Exceptions.database_exception);

}
return reply;

}
////////////////////////////////////////////////////getCourseInstructor///////////////////////////////////////////////////////////////////////////
public static String getCourseInstructor(String cName,String sec)
{
ResultSet resultSet;
String reply=null;
String st2="";
int c_id=0;

try {
StartDB_Connection();
} catch (SQLException e1) {
e1.printStackTrace();
}

int ret = checkNoSuchCourse(cName, sec);

if (ret == -1)
{
	  String reply1=Integer.toString(Exceptions.no_such_course);
	  System.out.println(reply1);
	  return reply1;
}
else
	  c_id = ret;

try
{
reply="";
stmt=(Statement)con.createStatement();

st2=
	 "SELECT i.instructor_ID FROM as_course a " +
	 "JOIN instructor i ON i.p_id=a.p_id JOIN course c "+
	 "ON c.c_id=a.c_id WHERE c.c_name= '"+cName+"' AND c.c_section ="+sec;

resultSet= stmt.executeQuery(st2);


if(resultSet.next())
{
//System.out.println("Instructor:"+ resultSet.getString("instructor_ID"));
reply= Integer.toString(Exceptions.success)+"\n"+1+"\n"+resultSet.getString("instructor_ID");
}

stmt.close();

}catch(Exception e ){
return Integer.toString(Exceptions.database_exception);

}
return reply;

}
///////////////////////////////////////////////////////////removeCourse///////////////////////////////////////////////////////////////////////////

public static String removeCourse(String cName,String cSec, String force)
{
	//no_such_course,has_students, remove_exception
	String query = null;
	String reply = null;
	Statement stmt = null;
	   try {
			StartDB_Connection();
			stmt = con.createStatement();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			

	int c_id = 0;
	int ret = checkNoSuchCourse(cName, cSec);
	  if (ret == -1)
		  return Integer.toString(Exceptions.no_such_course);
	  else
		  c_id = ret;	
	
	ret = checkHasStudent(cName, cSec);
	  if (ret !=0)
		  return Integer.toString(ret);

	try{
		con.setAutoCommit(false);
		System.out.println("Force - "+Integer.parseInt(force));
		//check if any students are enrolled in courses, can't be removed
		if(Integer.parseInt(force) == 0){
			stmt = con.createStatement();
			query= "SELECT count(1) as id FROM student s " +
		 		   "JOIN as_course a ON s.p_id = a.p_id " +
		 		   "WHERE a.c_id = "+c_id;
			System.out.println("Query - "+query);
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())
			{
				ret = rs.getInt(1);
			}
			if (ret != 0){
				con.setAutoCommit(true);
				stmt.close();
				return Integer.toString(Exceptions.has_students);
			}
		}
		else //force un-enroll. Remove enrollment and then delete course
		{
			query= "DELETE ac FROM as_course ac " +
		 		   "WHERE c_id = "+c_id;
			System.out.println("Query - "+query);
			stmt.executeUpdate(query);
		}
	 //remove course

	  //Delete from course_hours table
	  query= "DELETE FROM course_hours WHERE c_id = "+c_id;
	  System.out.println("Query - "+query);
	  stmt.executeUpdate(query);
	  
	  //Delete from as_course
	  query= "DELETE ac FROM as_course ac " +
	   "WHERE c_id = "+c_id;
System.out.println("Query - "+query);
stmt.executeUpdate(query);
	  
	  //Delete from course table
	  query= "DELETE FROM course WHERE c_id = "+c_id;
	  System.out.println("Query - "+query);
	  stmt.executeUpdate(query);

	  reply = Integer.toString(Exceptions.success)+"\n"+0;
	  con.commit();
	 
	  } catch (Exception e) {
		  reply = Integer.toString(Exceptions.remove_exception);
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
			StopDB_Connection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
return reply;
}

/////////////////////////////////////////////////////findallCourses////////////////////////////////////////////////////////////////////////////////////	   
public static String findAllCourses()
{
ResultSet resultSet;
String ret="";
String st1=null;

try {
StartDB_Connection();
} catch (SQLException e1) {
e1.printStackTrace();
}

try
{
stmt=(Statement)con.createStatement();
st1="select c_name,c_section from course";
resultSet=stmt.executeQuery(st1);
System.out.println(resultSet);
int i=0;
if(resultSet.next())
{
resultSet.beforeFirst();
while(resultSet.next())
{
i++;
ret+=resultSet.getString("c_name")+" "+resultSet.getString("c_section")+"\n";
//System.out.println(resultSet.getString("c_name")+" "+resultSet.getInt("c_section")+"\n");

}
ret=Integer.toString(Exceptions.success)+"\n"+i+"\n"+ret;
stmt.close();
}

}catch(Exception e ){
return Integer.toString(Exceptions.database_exception);

}
return ret;
}
/////////////////////////////////////////////////////////////////findcoursesbyInstructor///////////////////////////////////////////////////////
public static String findCoursesByInstructor(String insid)
{
	
	 return Course_DB.getCourses(insid);
}



//Method to remove course


/////////////////////////////////////////////findCoursesbyLocation////////////////////////////////////////////////////////////////////
public static String findCoursesByLocation(String loc)
{
   try {
		StartDB_Connection();
	} catch (SQLException e1) {
		e1.printStackTrace();
	}
	System.out.println("Location :************"+loc);
	String ret="";
	String st1="select c.c_name ,c.c_section from course c ";
	try
	{
		stmt=(Statement)con.createStatement();
		ResultSet rs1=stmt.executeQuery(st1);
		System.out.println(loc);
		int i=0;
		while(rs1.next())
		{
			String[] location = getLocation(rs1.getString("c_name"),rs1.getString("c_section")).substring(2).split("\n");	
			System.out.println("Location :************######"+location[1]);
			if(location[1].startsWith(loc)==true){
				i++;
				ret = ret + rs1.getString("c_name") + " "+rs1.getString("c_section")+"\n";
			}
		}
		ret=Exceptions.success+"\n"+i+"\n"+ret;	
		StopDB_Connection();
	}
	catch(Exception e)
	{
		ret=Integer.toString(Exceptions.database_exception);
	}
	return ret;
}

/////////////////////////////////////////////////////////findcoursesbyName//////////////////////////////////////////////////////////////////
public static String findCoursesByCourseName(String name)
{
ResultSet resultSet;
String ret="";

String st1=null;

try {
StartDB_Connection();
} catch (SQLException e1) {
e1.printStackTrace();
}
int i=0;
try
{
stmt=(Statement)con.createStatement();
st1="select c_name ,c_section from course where c_name like '%"+name+"%'";
resultSet=stmt.executeQuery(st1);
System.out.println(resultSet);

if(resultSet.next())
{
resultSet.beforeFirst();
while(resultSet.next())
{
i++;
ret=ret+resultSet.getString("c_name")+" "+resultSet.getString("c_section")+"\n";
}
stmt.close();
}

}catch(Exception e ){
ret= Integer.toString(Exceptions.database_exception);
System.out.println(e);
}
return (Exceptions.success+"\n"+i+"\n"+ret);
}
}

/////////////////////////////////////////////////////////////End of shikha's part/////////////////////////////////////////////////////////////////////////




