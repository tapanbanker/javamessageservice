package Main;
import GUI.*;
import Misc.*;
import Conn.*;
import Servant.*;

import java.sql.SQLException;
import java.util.*;
import javax.jms.*;
import javax.naming.*;

public class UnivServer implements MessageListener
{
	private Connection con;
	private Session session;
	private Topic uniTopic;
	private MessageConsumer consumer;
	private PersonServant person = new PersonServant();
	private StudentServant student = new StudentServant();
	private CourseServant course = new CourseServant();
	private InstructorServant instructor = new InstructorServant();

	
	public static void main(String args[])
	{
		String con_name = "root";
		String con_pass = "admin";
		String url = "jdbc:mysql://localhost/university?user="+con_name+ "&password="+con_pass;
		dbConnectionPool.EstablishConnectionPool(url);
		new UnivServer();
	}
	
	public void sendReply(Message request, String replyMsg)
	{
		try
		{
			MessageProducer MP = session.createProducer(null);
			Destination reply = request.getJMSReplyTo();
			TextMessage TM = session.createTextMessage();
			TM.setText(replyMsg);
			MP.send(reply, TM);
		}
		catch(JMSException JMSE)
		{
			System.out.println("JMS Exception: "+JMSE);
		}
	}
	

	
	public void onMessage(Message message)
	{
		TextMessage TM = (TextMessage)message;
		try 
		{
			if( TM.getText().startsWith("Create_Instructor"))
			{
				String reply= instructor.createInstructor(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Find_Persons_By_Name"))
			{
				String reply = person.findPersonsByName(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Find_Persons_By_City"))
			{
				String reply = person.findPersonsByCity(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Find_Persons_By_State"))
			{
				String reply = person.findPersonsByState(TM.getText());
				
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Find_Persons_By_Zip"))
			{
				String reply = person.findPersonsByZip(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Get_First_Name"))
			{
				String reply = person.getFirstName(TM.getText());
				sendReply(message,reply);
			}
			
			else if(TM.getText().startsWith("Get_Last_Name"))
			{
				String reply = person.getLastName(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Get_Address"))
			{
				String reply = person.getAddress(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Get_City"))
			{
				String reply = person.getCity(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Get_State"))
			{
				String reply = person.getState(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Get_Zip"))
			{
				String reply = person.getZipCode(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Get_ID"))
			{
				String reply = person.getID(TM.getText());
				sendReply(message,reply);
			}
			
			else if(TM.getText().startsWith("Unenroll_Student"))
			{
				String reply = student.unenrollStudentFromCourse(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Add_Office_Hours"))
			{
				String reply = instructor.Add_Office_Hours(TM.getText());
				System.out.println("Message from add office hour:"+reply);
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Remove_Office_Hours"))
			{
				String reply = instructor.Remove_Office_Hours(TM.getText());
				sendReply(message,reply);
			}
			
			else if(TM.getText().startsWith("Get_Office_Hours"))
			{
				String reply = instructor.Get_Office_Hours(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Set_Department"))
			{
				String reply = instructor.Set_Department(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Set_Course_Instructor"))
			{
				String reply = course.setCourseInstructor(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Get_Department"))
			{
				String reply = instructor.Get_Department(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Find_Instructors_By_Department"))
			{
				String reply = instructor.Find_Instructors_By_Department(TM.getText());
				sendReply(message,reply);
			}
			//Snigdha
			else if(TM.getText().startsWith("Find_All_Persons"))
			{
				String reply = person.findAllPersons(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Set_First_Name"))
			{
				String reply = person.setFirstName(TM.getText());
				sendReply(message,reply);
			}
			
			else if(TM.getText().startsWith("Set_Last_Name"))
			{
				String reply = person.setLastName(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Set_Address"))
			{
				String reply = person.setAddress(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Set_City"))
			{
				String reply = person.setCity(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Set_State"))
			{
				String reply = person.setState(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Set_Zip"))
			{
				String reply = person.setZipCode(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Set_ID"))
			{
				String reply = person.setID(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Enroll_Student"))
			{
				String reply = student.enrollStudentInCourse(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Create_Course"))
			{
				System.out.println(TM.getText());
				String reply = course.createCourse(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Get_Enrolled_Units"))
			{
				String reply = student.getEnrolledUnits(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Get_Students"))
			{
				String reply = student.getStudentsEnrolledInCourse(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Get_Location"))
			{
				String reply = course.getLocation(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Get_Courses"))
			{
				String reply = course.getCourses(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Remove_Student"))
			{
				String reply = student.removeStudent(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Calculate_Bill"))
			{
				String reply = student.calculateBill(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Set_Course_Name"))
			{
				String reply = course.setCourseName(TM.getText());
				sendReply(message,reply);
			}
			
			else if(TM.getText().startsWith("Set_Course_Section"))
			{
				String reply = course.setCourseSection(TM.getText());
				sendReply(message,reply);
			}
			
			else if(TM.getText().startsWith("Set_Course_Units"))
			{
				String reply = course.setCourseUnits(TM.getText());
				sendReply(message,reply);
			}
			
			else if(TM.getText().startsWith("Get_Course_Units"))
			{
				String reply = course.getCourseUnits(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Get_Course_Instructor"))
			{
				String reply = course.getCourseInstructor(TM.getText());
				sendReply(message,reply);
			}		
			else if(TM.getText().startsWith("Find_All_Courses"))
			{
				String reply = course.findAllCourses(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Find_Courses_By_Course_Name"))
			{
				String reply = course.findCoursesByCourseName(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Remove_Course"))
			{
				String reply = course.removeCourse(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Find_Courses_By_Instructor"))
			{
				String reply = course.findCoursesByInstructor(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Find_Courses_By_Location"))
			{
				String reply = course.findCoursesByLocation(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Create_Student"))
			{
				String reply = student.createStudent(TM.getText());
				sendReply(message,reply);
			}
			else if(TM.getText().startsWith("Set_Location"))
			{
				String reply = course.setLocationOfCourse(TM.getText());
				sendReply(message,reply);
			}
			
     }
		catch(JMSException JMSE)
		{
			System.out.println("JMS Exception: "+JMSE);
		} 
		catch (Exception se) 
		{
			System.out.println("Exception: "+se);
		}
	}
	
	public UnivServer()
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
			
			try
			{
				uniTopic = (Topic)jndi.lookup("Univ_Request");
			}
			catch(NamingException NE1)
			{
				System.out.println("NamingException: "+NE1+ " : Continuing anyway...");
			}
			
			if( null == uniTopic )
			{
				uniTopic = session.createTopic("UniversityTopic");
				jndi.bind("UniversityTopic", uniTopic);
			}
			
			consumer = session.createConsumer(uniTopic);
			consumer.setMessageListener(this);
			
			System.out.println("Server Start");
			con.start();
			try{
				Thread.currentThread().join();
				}
				catch(Exception e){}
		
		}
		catch(NamingException NE)
		{
			System.out.println("Naming Exception: "+NE);
		}
		catch(JMSException JMSE)
		{
			System.out.println("JMS Exception: "+JMSE);
			JMSE.printStackTrace();
		}
	}
}
