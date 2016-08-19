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

public class Person_DB
{

	   private static Connection con = null;
	   static Statement stmt = null;
	   Exceptions e = new Exceptions();
	   
	  
	   
	   public Person_DB() throws SQLException{
		   
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
	   
	   public static String findAllPersons(String searchType)
	   {
		   ResultSet resultSet;
		   String query=null, personList = null, reply = null;
		   try{
			   StartDB_Connection();
		   }
		   catch (SQLException e1)
			{
			   e1.printStackTrace();
			}
		   if(searchType.equals("2"))
			   query = "SELECT i.instructor_id AS id " +
	   		   		   "FROM person p JOIN instructor i " +
	   		           "ON p.p_id=i.p_id " +
	   		           "UNION " +
	   		           "SELECT s.student_id AS id " +
			   		   "FROM person p JOIN student s " +
			   		   "ON p.p_id=s.p_id";
		   else if(searchType.equals("1"))
			   query = "SELECT p.p_id,i.instructor_id AS id " +
			   		   "FROM person p JOIN instructor i " +
			   		   "ON p.p_id=i.p_id";
		   else if(searchType.equals("0"))
			   query = "SELECT p.p_id,s.student_id AS id " +
			   		   "FROM person p JOIN student s " +
			   		   "ON p.p_id=s.p_id";
		   else
			   return Integer.toString(Exceptions.malformed_message);
		   	 
		   if(reply == null)
		   {
			try 
			{
				stmt =  con.createStatement();
				resultSet = stmt.executeQuery(query);
				System.out.println("find all persons : "+ query);
				System.out.println("find all persons : "+ con);
				int i = 0;
				personList = "";
				while (resultSet.next()) {
					i++;
					personList = personList + resultSet.getString("id") + "\n";
	            }
				reply = Integer.toString(Exceptions.success)+"\n" + i +"\n"+ personList;
				stmt.close();
			} 
			catch (Exception e)
			{
				Integer.toString(Exceptions.database_exception);
			}
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

	   
	   public static String setXXX(String person_id,String new_XXX, String column_Name)
	   {
		   String reply = null;
		   try{
			   StartDB_Connection();
		   }
		   catch (SQLException e1)
			{
			   e1.printStackTrace();
			}
	   
		   //malformed ID
		   int ret = checkMalformedID(person_id);
		   if(ret!=0)
			  return Integer.toString(Exceptions.malformed_id);
		   
		   if(column_Name.equalsIgnoreCase("id")){
			   ret = checkMalformedID(new_XXX);
			   if(ret!=0)
				  return Integer.toString(Exceptions.malformed_id);
		   }
		   
		   //no_such_person
		   ret = checkPersonExists(person_id,con);
		   if(ret==0)	//no person with this person_id is found
			   return Integer.toString(Exceptions.no_such_person);
		   
		   //malformed state
		   if(column_Name.equalsIgnoreCase("state")){
			  ret = validateState(new_XXX);
			  if(ret!=0)
				  return Integer.toString(Exceptions.malformed_state);
		   }

		   //malformed zip
		   if(column_Name.equalsIgnoreCase("zip")){
			  ret = validateZip(new_XXX);		   
			  if(ret!=0)
				  return Integer.toString(Exceptions.malformed_zip);
		   }
		   
		   //Update ID 
		   if(column_Name.equalsIgnoreCase("id")){
				  return setID(person_id, new_XXX);
			   }
			   

		   //Update other attributes
		   String query = "UPDATE person " +
		   			  "SET "+column_Name+" = '"+new_XXX+"' " +
		   			  "WHERE p_id = (SELECT p_id FROM student s WHERE s.student_id = '"+ person_id + "' "+
		   							"UNION " +
		   							"SELECT p_id FROM instructor i WHERE i.instructor_id = '"+ person_id + "') ";
	   
		   try{
			   Statement stmt =  con.createStatement();
			   stmt.executeUpdate(query);
			   reply = Integer.toString(Exceptions.success) + "\n" + 0;
			   stmt.close();
		   } catch (Exception e) {
			   Integer.toString(Exceptions.database_exception);
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
	   
	   public static String getXXX(String person_id,String column_Name)
	   {
		   System.out.print("person id :"+ person_id);
		   System.out.print("col name :"+ column_Name);
		   ResultSet resultSet;
		   String query=null, personList = null, reply = null;
		   try{
			   StartDB_Connection();
		   }
		   catch (SQLException e1)
			{
			   e1.printStackTrace();
			}
	   
		   //malformed ID
		   int ret = checkMalformedID(person_id);
		   if(ret!=0)
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
		   //retrieve person's data
		    query = "SELECT "+column_Name+" FROM person WHERE p_id = (SELECT p_id FROM student s WHERE s.student_id = "+'"'+ person_id +'"'+ " "+
		   							"UNION " +
		   							"SELECT p_id FROM instructor i WHERE i.instructor_id = "+'"'+ person_id +'"'+ ") ";
	   
		   System.out.println("Query - "+query);
		   if(reply == null)
		   {
			try 
			{
				System.out.println("creating statement ");
				stmt =  con.createStatement();
				System.out.println("statement ");
				resultSet = stmt.executeQuery(query);

				int i = 0;
				personList = "";
				
				if(!resultSet.next())
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
				resultSet.beforeFirst();
				while (resultSet.next()) {
					i++;
					personList = personList + resultSet.getString(1)+ "\n";
	            }
				reply = Integer.toString(Exceptions.success)+"\n" + i +"\n"+ personList;
				stmt.close();
				System.out.println("");
			} 
			catch (Exception e)
			{
				System.out.println("Exception  "+e +"   : ::");
			}
			try{
				StopDB_Connection();
			}
			catch(SQLException sql)
			{
			  System.out.println("sql exception from"+sql.getMessage());	
			}
		}   return reply;
	   }
	   

	   //Method to update the ID
	   public static String setID(String person_id, String new_id)
	   {
		 String ret = null;
		  
		 //if old and new ID are the same, dont do anything. Just return success.
		 if(person_id.equals(new_id))
			   ret = Integer.toString(Exceptions.success) + "\n" + 0;
		 else{	  
			  //check if person exists, return exception.
			  int r = checkPersonExists(new_id,con);
			  if(r!=0)	//new person id already in use
				   return Integer.toString(Exceptions.person_exists);

			  //New PersonID not used already, update
			  try {
				   String query1 = "UPDATE student " +
				  				  "SET student_ID = '"+new_id+"' " +
				  				  "WHERE student_ID = '"+person_id+"'";
				  
				   String query2 = "UPDATE instructor " +
				  				  "SET instructor_ID = '"+new_id+"' " +
						   		  "WHERE instructor_ID = '"+person_id+"'";
	   
				   Statement stmt;
				   stmt = con.createStatement();
				   stmt.addBatch(query1);
				   stmt.addBatch(query2);
				   stmt.executeBatch();
				   
				   ret = Integer.toString(Exceptions.success) + "\n" + 0;
				   
				   stmt.close();
				   
			  }
			  catch (SQLException e) {
				  ret = Integer.toString(Exceptions.database_exception);
			  } 
		 }
		 return ret;
	   }
	   
	   //method to check malformed id
	   public static int checkMalformedID(String id){
		 /* int ret = 0;

		  String idPattern = "[0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9][0-9][0-9]";
		  if(!id.matches(idPattern))
				ret = -1;
		  return ret;*/
		   
		   boolean checkIDFlag=(new InstructorServant()).validateInstructorID(id);
			if(!checkIDFlag){
				//Exception 
				return -1;
			}
			else{return 0;}
		   
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
	           if (validStates[i].contains(new_state))
	               return 0;
	       }
		   return -1;
	   }
	   
	   //method to check if zip is valid
	   public static int validateZip(String new_zip){
		  /*int ret = 0;

		  String zipCodePattern = "[0-9][0-9][0-9][0-9][0-9](-[0-9][0-9][0-9][0-9])?";
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

	   //method to check person_exists and no_such_person
	   public static int checkPersonExists(String id, Connection con)
	   {
		   int ret = 0;
		   try {
			   String query1 = "SELECT p_id FROM student WHERE student_ID = '"+ id+"'";
			   String query2 = "SELECT p_id FROM instructor WHERE instructor_ID = '"+ id+"'";
		
			   Statement stmt;
			   stmt = con.createStatement();
			   ResultSet rs1= stmt.executeQuery(query1);
			   System.out.println("1")	  ; 

			   while(rs1.next()){
				   System.out.println("in r1")	  ; 
				   ret = rs1.getInt("p_id");
				   System.out.println(ret)	  ; 
			   }
			   
			   if(ret == 0){
				   ResultSet rs2= stmt.executeQuery(query2);
				   while(rs2.next()){
					   ret = rs2.getInt("p_id");
					   System.out.println(ret)	  ; 
			   }}
			   stmt.close();
		   } catch (SQLException e) {
				e.printStackTrace();
			}
		   return ret;
		}
	   
	   public static String findPersonsByName(String pattern , String searchType)
	   {
		   System.out.println("find persons by name db");
		   pattern = pattern+"%";
		   ResultSet resultSet;
		   String query=null, personList = null, reply = null;
		   try {
			   StartDB_Connection();
			   //StartDB_Connection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		   System.out.println("searchType - "+searchType);
		   if(searchType.equals("2"))
			   query = "SELECT i.instructor_id AS id " +
	   		   		   "FROM person p JOIN instructor i " +
	   		           "ON p.p_id=i.p_id where  p.lname LIKE '"+pattern+"'" +
	   		           "UNION " +
	   		           "SELECT s.student_id AS id " +
			   		   "FROM person p JOIN student s " +
			   		   "ON p.p_id=s.p_id where  p.lname LIKE '"+pattern+"'";
		   else if(searchType.equals("1"))
			   query = "SELECT p.p_id,i.instructor_id AS id " +
			   		   "FROM person p JOIN instructor i " +
			   		   "ON p.p_id=i.p_id where  p.lname LIKE '"+pattern+"'";
		   else if(searchType.equals("0"))
			   query = "SELECT p.p_id,s.student_id AS id " +
			   		   "FROM person p JOIN student s " +
			   		   "ON p.p_id=s.p_id where  p.lname LIKE '"+pattern+"'";
		   else
			   reply = Integer.toString(Exceptions.malformed_message);
			   
		   System.out.println("Query - "+query);
		   	 
		   if(reply == null)
		   {
			try 
			{
				System.out.println("creating statement ");
				stmt =  con.createStatement();
				System.out.println("statement ");
				resultSet = stmt.executeQuery(query);

				int i = 0;
				personList = "";
				while (resultSet.next()) {
					i++;
					personList = personList + resultSet.getString("id") + "\n";
	            }
				reply = Integer.toString(Exceptions.success)+"\n" + i +"\n"+ personList;
				stmt.close();
				System.out.println("");
			} 
			catch (Exception e)
			{
				System.out.println("Exception  "+e +"   : ::");
			}
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
	   public static String findPersonsByState(String pattern , String searchType )
	   {
		      System.out.print("find persons by state");
		      
		      int ret = validateState(pattern);
			  if(ret!=0)
			  {
				   
				  String s = Integer.toString(Exceptions.malformed_state);
				  System.out.print(s);
				  return s.toString();
			  }
			  ResultSet resultSet;
			   String query=null, personList = null, reply = null;
			   try {
				   StartDB_Connection();
				   //StartDB_Connection();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			   System.out.println("searchType - "+searchType);
			   if(searchType.equals("2"))
				   query = "SELECT i.instructor_id AS id " +
		   		   		   "FROM person p JOIN instructor i " +
		   		           "ON p.p_id=i.p_id where  p.state LIKE'"+pattern+"'" +
		   		           "UNION " +
		   		           "SELECT s.student_id AS id " +
				   		   "FROM person p JOIN student s " +
				   		   "ON p.p_id=s.p_id where  p.state LIKE'"+pattern+"'";
			   else if(searchType.equals("1"))
				   query = "SELECT p.p_id,i.instructor_id AS id " +
				   		   "FROM person p JOIN instructor i " +
				   		   "ON p.p_id=i.p_id where  p.state LIKE'"+pattern+"'";
			   else if(searchType.equals("0"))
				   query = "SELECT p.p_id,s.student_id AS id " +
				   		   "FROM person p JOIN student s " +
				   		   "ON p.p_id=s.p_id where  p.state LIKE'"+pattern+"'";
			   else
				   reply = Integer.toString(Exceptions.malformed_message);
				   
			   
			   	 
			   if(reply == null)
			   {
				try 
				{
					System.out.println("creating statement ");
					stmt =  con.createStatement();
					System.out.println("statement ");
					System.out.println("Query - "+query);
					resultSet = stmt.executeQuery(query);
					System.out.println("executed the query ");
					int i = 0;
					personList = "";
					
					while (resultSet.next()) {
						i++;
						
						personList = personList + resultSet.getString("id") + "\n";
		            }
					reply = Integer.toString(Exceptions.success)+"\n" + i +"\n"+ personList;
					
					stmt.close();
					
				} 
				catch (Exception e)
				{
					System.out.println("Exception  "+e +"   : ::");
				}
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
	   public static String findPersonsByCity(String pattern, String searchType)
	   {
		   pattern = pattern+"%";
		   ResultSet resultSet;
		   String query=null, personList = null, reply = null;
		   try {
			   StartDB_Connection();
			   //StartDB_Connection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		   System.out.println("searchType - "+searchType);
		   if(searchType.equals("2"))
			   query = "SELECT i.instructor_id AS id " +
	   		   		   "FROM person p JOIN instructor i " +
	   		           "ON p.p_id=i.p_id where  p.city LIKE'"+pattern+"'"+
	   		           "UNION " +
	   		           "SELECT s.student_id AS id " +
			   		   "FROM person p JOIN student s " +
			   		   "ON p.p_id=s.p_id where  p.city LIKE'"+pattern+"'";
		   else if(searchType.equals("1"))
			   query = "SELECT p.p_id,i.instructor_id AS id " +
			   		   "FROM person p JOIN instructor i " +
			   		   "ON p.p_id=i.p_id where  p.city LIKE'"+pattern+"'";
		   else if(searchType.equals("0"))
			   query = "SELECT p.p_id,s.student_id AS id " +
			   		   "FROM person p JOIN student s " +
			   		   "ON p.p_id=s.p_id where  p.city LIKE'"+pattern+"'";
		   else
			   reply = Integer.toString(Exceptions.malformed_message);
			   
		   System.out.println("Query - "+query);
		   	 
		   if(reply == null)
		   {
			try 
			{
				System.out.println("creating statement ");
				stmt =  con.createStatement();
				System.out.println("statement ");
				resultSet = stmt.executeQuery(query);

				int i = 0;
				personList = "";
				while (resultSet.next()) {
					i++;
					personList = personList + resultSet.getString("id") + "\n";
	            }
				reply = Integer.toString(Exceptions.success)+"\n" + i +"\n"+ personList;
				stmt.close();
				System.out.println("");
			} 
			catch (Exception e)
			{
				System.out.println("Exception  "+e +"   : ::");
			}
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
	   public static String findPersonsByZip(String pattern, String searchType)
	   {
		   int ret = validateZip(pattern);		   
		   if(ret!=0)
		   {
				  return Integer.toString(Exceptions.malformed_zip);
		   }
		   ResultSet resultSet;
		   String query=null, personList = null, reply = null;
		   try {
			   StartDB_Connection();
			   //StartDB_Connection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		   System.out.println("searchType - "+searchType);
		   if(searchType.equals("2"))
			   query = "SELECT i.instructor_id AS id " +
	   		   		   "FROM person p JOIN instructor i " +
	   		           "ON p.p_id=i.p_id where p.zip LIKE '"+pattern+"%'"+
	   		           "UNION " +
	   		           "SELECT s.student_id AS id " +
			   		   "FROM person p JOIN student s " +
			   		   "ON p.p_id=s.p_id where  p.zip LIKE '"+pattern+"%'";
		   else if(searchType.equals("1"))
			   query = "SELECT p.p_id,i.instructor_id AS id " +
			   		   "FROM person p JOIN instructor i " +
			   		   "ON p.p_id=i.p_id where  p.zip LIKE '"+pattern+"%'";
		   else if(searchType.equals("0"))
			   query = "SELECT p.p_id,s.student_id AS id " +
			   		   "FROM person p JOIN student s " +
			   		   "ON p.p_id=s.p_id where  p.zip LIKE '"+pattern+"%'";
		   else
			   reply = Integer.toString(Exceptions.malformed_message);
			   
		   System.out.println("Query - "+query);
		   	 
		   if(reply == null)
		   {
			try 
			{
				System.out.println("creating statement ");
				stmt =  con.createStatement();
				System.out.println("statement ");
				resultSet = stmt.executeQuery(query);

				int i = 0;
				personList = "";
				while (resultSet.next()) {
					i++;
					personList = personList + resultSet.getString("id") + "\n";
	            }
				reply = Integer.toString(Exceptions.success)+"\n" + i +"\n"+ personList;
				stmt.close();
				System.out.println("");
			} 
			catch (Exception e)
			{
				System.out.println("Exception  "+e +"   : ::");
			}
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
	   
	   
	   public static Integer getNextPersonId(Connection con)
	   {
	   	String st="select max(p_id) from person";
	   	Integer num=0;
	   	try 
	   	{
	   		int x=1;
	   		java.sql.Statement s= con.createStatement();
	   		java.sql.ResultSet rs = s.executeQuery(st);
	   		
	   		while(rs.next())
	   		{
	   			num=rs.getInt(x);
	   		}
	   		s.close(); 
	   	} 
	   	catch (SQLException e) 
	   	{
	   		System.out.println("here : "+num);
	   	}
	       if(num==0)
	       {
	       	return 1;
	       }
	       return (num);
	   }


	   //Create Person
	   public static int insertPerson(String fname, String lname, String address, String city,String state, String zip,Connection con) 
	   { 
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

		int i;
	   	if (state.length()>2)
	   	{
	   		for(i = 0;i<validStates.length;i++){
	   			if(validStates[i].equalsIgnoreCase(state))
	   				break;
	   		}
	   		state = validStates[i-50];
	   	}

	   	String st1 = "insert into person(fname,lname,address,city,state,zip) values("+"'"+fname+"','"+lname+"','"+address+"','"+city+"','"+state+"','"+zip+"')";
	   	java.sql.Statement s=null;
	   	try 
	   	{
	   		s = con.createStatement();
	   		s.executeUpdate(st1);
	   		System.out.println("Row Added");
	   		s.close();
	   	} 
	   	catch (Exception e)
	   	{
	   		System.out.println("Exception at insertPerson: "+e +"   : ::");
	   	}
	   	int pid=getNextPersonId(con);
	   	return pid;
	   }	
	   
	}
