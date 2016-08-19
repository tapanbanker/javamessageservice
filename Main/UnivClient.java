package Main;
import java.awt.event.*;
import java.awt.*;
import java.util.Properties;
import javax.jms.*;
import javax.naming.*;
import javax.swing.*;


public class UnivClient
{	
	private Connection con;
	private Session session;
	private Topic uniTopic;
	private MessageConsumer consumer;
	private Topic repTopic;
	private MessageProducer producer;
	private TextMessage tm,reply;
	
	
	public static void main(String args[])
	{
		new UnivClient();
	}
	
	public UnivClient()
	{
		try
		{			
			Properties properties = new Properties();
		    properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		    properties.put(Context.URL_PKG_PREFIXES, "org.jnp.interfaces");
		    properties.put(Context.PROVIDER_URL, "localhost");
			
			InitialContext jndi = new InitialContext(properties);
			ConnectionFactory conFactory = (ConnectionFactory)jndi.lookup("XAConnectionFactory");
			con = conFactory.createConnection();
			
			session = con.createSession( false, Session.AUTO_ACKNOWLEDGE );
			uniTopic = (Topic)jndi.lookup("Univ_Request");
			repTopic = session.createTemporaryTopic();
			consumer= session.createConsumer(repTopic);
			
			con.start();
		}
		catch(NamingException NE)
		{
			System.out.println("Naming Exception: "+NE);
		}
		catch(JMSException JMSE)
		{
			System.out.println("JMS Exception: "+JMSE);
		}	
		int k=10;
		try{
			//Malformed Location at 150015000
				// System.out.println(addInstructor("123-45-5179","Dan"+k,"Harkey"+k,"San Jose State University","San","CA","95113","CMPE"));
				//System.out.println("reply: "+addCourse("208","2","123-45-9999","ENGR HALL L223 F 1900-2000","3"));
				//System.out.println("reply: "+addOfficeHours("123-45-9999","ENGR 300 T 0820-0900"));
				//System.out.println("reply: "+remHours("123-45-9999","ENGR 300 T 0820-0900"));
				//System.out.println("reply: "+setCourseInstructor("123-45-6788","202","2"));
				//System.out.println("reply: "+setDepartment("123-45-6789","CMPE"));
				//System.out.println("reply: "+getDepartment("123-45-6787"));
				//System.out.println("reply: "+getOfficeHours("123-45-5179"));
				//System.out.println("reply: "+Find_Instructors_By_Department("D"));
				//System.out.println("reply: "+setLocation("ENGR HALL L223 M 0900-1000","208","02"));
				
				
	            
	               //removeCourse("208","02","0"); //202 2
	               //removeCourse(insertedCourse1,section2,"0"); //202 2
	               //removeCourse(insertedCourse1,section2,"1"); //202 2
	               //testMethod(UniClient.removeCourse(insertedCourse1,section2,"1\n2"),Exceptions.malformed_message);
	               
	 

				
				
			}
			catch(Exception e){}
	}
	
  public String createStudent(String id,String fname,String lname,String address,String city,String state,String zip) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s= "Create_Student"+"\n"+reqid+"\n"+7+"\n"+fname+"\n"+lname+"\n"+id+"\n"+address+"\n"+city+"\n"+state+"\n"+zip+"\n";
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
		}
		catch(JMSException je)
		{
			System.out.println("JMS ADD : "+je);
		}
		return reply.getText();
	}
	
	public String addInstructor(String id,String fname,String lname,String address,String city,String state,String zip,String dept) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s= "Create_Instructor"+"\n"+reqid+"\n"+8+"\n"+id+"\n"+fname+"\n"+lname+"\n"+address+"\n"+city+"\n"+state+"\n"+zip+"\n"+dept+"\n";
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
		}
		catch(JMSException je)
		{
			System.out.println("JMS ADD : "+je);
		}
		return reply.getText();
	}
	
	public String getDept(Integer id) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s= "Get_Department"+"\n"+reqid+"\n"+"1"+"\n"+id+"\n";
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(JMSException je)
		{
			System.out.println("JMS ADD : "+je);
		}
		return reply.getText();
	}
	
	public String remHours(String id,String loc) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Remove_Office_Hours"+"\n"+reqid+"\n"+2+"\n"+id+"\n"+loc;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
	
	public String getOfficeHours(String id) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_Office_Hours"+"\n"+reqid+"\n"+1+"\n"+id;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}

	
	//Set the department
	public String setDepartment(String instructorId,String departmentName) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Set_Department"+"\n"+reqid+"\n"+2+"\n"+instructorId+"\n"+departmentName;
			//System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	

	//Get Department
	
	public String getDepartment(String instructorId) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_Department"+"\n"+reqid+"\n"+1+"\n"+instructorId;
			//System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}


	public String Find_Instructors_By_Department(String departmentNamePattern)throws JMSException
	{

		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Find_Instructors_By_Department"+"\n"+reqid+"\n"+1+"\n"+departmentNamePattern;
			//System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();

	
	}

	public String setCourseInstructor(String iid,String cid,String csec) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Set_Course_Instructor"+"\n"+reqid+"\n"+"3"+"\n"+iid+"\n"+cid+"\n"+csec;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	
	public String findCoursesByCourseName(String cname) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Find_Courses_By_Course_Name"+"\n"+reqid+"\n"+"1"+"\n"+cname;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}

	
	public String findPersonsByName(String pattern,String searchType) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Find_Persons_By_Name"+"\n"+reqid+"\n"+"2"+"\n"+pattern+"\n"+searchType;
 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
				}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}

	
	public String findPersonsByCity(String pattern,String searchType) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Find_Persons_By_City"+"\n"+reqid+"\n"+"2"+"\n"+pattern+"\n"+searchType;
 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
				}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}

	
	public String findPersonsByState(String pattern,String searchType) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Find_Persons_By_State"+"\n"+reqid+"\n"+"2"+"\n"+pattern+"\n"+searchType;
 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	

	public String findPersonsByZip(String pattern,String searchType) throws JMSException
	{
		System.out.println("Client :"+searchType);
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Find_Persons_By_Zip"+"\n"+reqid+"\n"+"2"+"\n"+pattern+"\n"+searchType;
 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
//retrieving person's attributes and courses
	
	public String getFirstName(String p_id) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_First_Name"+"\n"+reqid+"\n"+"1"+"\n"+p_id;
 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
					}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	//Change a person’s information - Last Name
	public String getLastName(String p_id) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_Last_Name"+"\n"+reqid+"\n"+"1"+"\n"+p_id;
 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	//Change a person’s information - Address
	public String getAddress(String p_id) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_Address"+"\n"+reqid+"\n"+"1"+"\n"+p_id;
 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
					}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	//Change a person’s information - City
	public String getCity(String p_id) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_City"+"\n"+reqid+"\n"+"1"+"\n"+p_id;
 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	//Change a person’s information - State
	public String getState(String p_id) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_State"+"\n"+reqid+"\n"+"1"+"\n"+p_id;
 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	
	//Change a person’s information - Zip
	public String getZip(String p_id) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_Zip"+"\n"+reqid+"\n"+"1"+"\n"+p_id;
 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
					}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	/*
	//Change a person’s information - ID
	public String getID(String p_id) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_ID"+"\n"+reqid+"\n"+"1"+"\n"+p_id;
 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	*/
	
	public String getCourses(String id) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_Courses"+"\n"+reqid+"\n"+"1"+"\n"+id;
 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}	
	//for unenrolling the student
	public String unenroll(String student_id, String course_name, String course_section) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Unenroll_Student"+"\n"+reqid+"\n"+3+"\n"+student_id+"\n"+course_name+"\n"+course_section;
 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			
		}
		catch(JMSException je)
		{
			je.printStackTrace();
		}
		return reply.getText();
	}

	//List all persons known by the system
	public String findAllPersons(String searchType) throws JMSException
	{
		try 
		{
			int reqid=(int)(Math.random()*100000);
			String s="Find_All_Persons"+"\n"+reqid+"\n"+"1"+"\n"+searchType;
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			 
			System.out.println(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			 
		} 
		catch (JMSException e) 
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
	//Change a person’s information - First Name
	public String setFirstName(String p_id,String new_first_name) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Set_First_Name"+"\n"+reqid+"\n"+"2"+"\n"+p_id+"\n"+new_first_name;
			 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	//Change a person’s information - Last Name
	public String setLastName(String p_id,String new_last_name) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Set_Last_Name"+"\n"+reqid+"\n"+"2"+"\n"+p_id+"\n"+new_last_name;
			 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	//Change a person’s information - Address
	public String setAddress(String p_id,String new_address) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Set_Address"+"\n"+reqid+"\n"+"2"+"\n"+p_id+"\n"+new_address;
			 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	//Change a person’s information - City
	public String setCity(String p_id,String new_city) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Set_City"+"\n"+reqid+"\n"+"2"+"\n"+p_id+"\n"+new_city;
			 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	//Change a person’s information - State
	public String setState(String p_id,String new_state) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Set_State"+"\n"+reqid+"\n"+"2"+"\n"+p_id+"\n"+new_state;
			 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	
	//Change a person’s information - Zip
	public String setZip(String p_id,String new_zip) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Set_Zip"+"\n"+reqid+"\n"+"2"+"\n"+p_id+"\n"+new_zip;
			 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	//Change a person’s information - ID
	public String setID(String p_id,String new_id) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Set_ID"+"\n"+reqid+"\n"+"2"+"\n"+p_id+"\n"+new_id;
			 
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	//enroll a student in a course
	public String enroll(String student_id, String course_name,String section) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Enroll_Student"+"\n"+reqid+"\n"+3+"\n"+student_id+"\n"+course_name+"\n"+section;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
		}
		catch(JMSException e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
	//get enrolled units for a student
	public String getEnrolledUnits(String student_id) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_Enrolled_Units"+"\n"+reqid+"\n"+1+"\n"+student_id;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
		}
		catch(JMSException e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
	//get enrolled units for a student
	public String getStudents(String course_name,String section) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_Students"+"\n"+reqid+"\n"+2+"\n"+course_name+"\n"+section;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
		}
		catch(JMSException e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
	public String getLocation(String cName,String cSec) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_Location"+"\n"+reqid+"\n"+"2"+"\n"+cName+"\n"+cSec;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}

	public String removeStudent(String studentID,Integer force) throws JMSException
	{
		int sum=0;
		try 
		{
			int reqid=(int)(Math.random()*100000);
			String s="Remove_Student"+"\n"+reqid+"\n"+"2"+"\n"+studentID+"\n"+force;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
		} 
		catch (JMSException je) 
		{
			je.printStackTrace();
		}
		return reply.getText();
	}

	public String calculateBill(String studentID) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Calculate_Bill"+"\n"+reqid+"\n"+1+"\n"+studentID;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
		}
		catch(JMSException je)
		{
			je.printStackTrace();
		}
		return reply.getText();
	}
	
	public String setCourseName(String cName,String csec,String nname) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Set_Course_Name"+"\n"+reqid+"\n"+"3"+"\n"+cName+"\n"+csec+"\n"+nname;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	
	public String setCourseSection(String cName,String csec,String nsec) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Set_Course_Section"+"\n"+reqid+"\n"+"3"+"\n"+cName+"\n"+csec+"\n"+nsec;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	
	public String setCourseUnits(String cName,String csec,String units) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Set_Course_Units"+"\n"+reqid+"\n"+"3"+"\n"+cName+"\n"+csec+"\n"+units;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
	public String getCourseUnits(String cName,String csec) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_Course_Units"+"\n"+reqid+"\n"+"2"+"\n"+cName+"\n"+csec+"\n";
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
	
	public String getCourseInstructor(String cName,String sec) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Get_Course_Instructor"+"\n"+reqid+"\n"+"2"+"\n"+cName+"\n"+sec;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
	public String findAllCourses() throws JMSException
	{
		try 
		{
			int reqid=(int)(Math.random()*100000);
			String s="Find_All_Courses"+"\n"+reqid+"\n";
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		} 
		catch (JMSException e) 
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
		
	public String findCoursesByName(String cname) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Find_Courses_By_Course_Name"+"\n"+reqid+"\n"+"1"+"\n"+cname;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
	public String findCoursesByLocation(String loc) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Find_Courses_By_Location"+"\n"+reqid+"\n"+"1"+"\n"+loc;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
	public String findCoursesByInstructor(String instid) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Find_Courses_By_Instructor"+"\n"+reqid+"\n"+"1"+"\n"+instid;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
	public String removeCourse(String cid,String csec,String force) throws JMSException
	{
		int sum=0;
		try 
		{
			int reqid=(int)(Math.random()*100000);
			String s="Remove_Course"+"\n"+reqid+"\n"+3+"\n"+cid+"\n"+csec+"\n"+force;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		} 
		catch (JMSException e) 
		{
			e.printStackTrace();
		}
		
		return reply.getText();
	}
	public String addCourse(String courseName,String courseSec,String id,String loc, String units) throws JMSException
	{
		int sum=0;
		try 
		{
			int reqid=(int)(Math.random()*100000);
		
			String s="Create_Course"+"\n"+reqid+"\n"+"5"+"\n"+courseName+"\n"+courseSec+"\n"+id+"\n"+loc+"\n"+units;

			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		} 
		catch (JMSException e) 
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
	public String addOfficeHours(String id,String loc) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			
			String s="Add_Office_Hours"+"\n"+reqid+"\n"+2+"\n"+id+"\n"+loc;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
	
	public String setLocation(String loc,String cName,String csec) throws JMSException
	{
		try
		{
			int reqid=(int)(Math.random()*100000);
			String s="Set_Location"+"\n"+reqid+"\n"+"3"+"\n"+loc+"\n"+cName+"\n"+csec;
			System.out.println(s);
			producer = session.createProducer(uniTopic);
			tm = session.createTextMessage(s);
			tm.setJMSReplyTo(repTopic);
			producer.send(tm);
			reply=(TextMessage) consumer.receive();
			System.out.println(reply.getText());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return reply.getText();
	}
}
