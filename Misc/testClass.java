package Misc;
import GUI.*;
import Main.*;
import Misc.*;
import Servant.*;
import Conn.*;
import javax.jms.JMSException;


public class testClass {
	public static void main(String args[])
	{
		try {
		UnivClient UniClient  = new UnivClient();

		String insertedInst = "123-45-0000";
		String notPresentInst = "123-45-0001";
		String malformedInst = "123450001";
		String location = "ENGR 236 M 1220-1500";
		String location2 = "ENGR 300 S 0820-0900";
		String locationConflict1 =  "ENGR 236 M 1300-1600";
		String locationConflict2 =  "ENGR 236 M 1220-1500";
		String locationRoomBooked = "ENGR 236 M 1300-1700";
		String malformedLocation1 = "ENGR TBA M 1500-1700";
		String malformedHours1 = "ENGR 236 M 1500-0200";
		String locationInst = "ENGR 300 T 0820-0900";
		
		String insertedStud = "123-45-6789";
		String notPresentStud = "123-45-6700";
		String malformedStud = "123456789";
		 String stud4 = "123-45-6780";
		String stud5 = "123-45-6000";
		
		String insertedCourse1 = "202";
		String insertedCourse2 = "273";
		String notPresentCourse = "Bio";

		String section1 = "1";
		String section2 = "2";
		
		int i = 1;

		System.out.println("\n\n"+i+") TEST - Create Instructor");
		System.out.println("***********************");
		i++;
		testMethod(UniClient.addInstructor(malformedInst,"Dan","Harkey","San Jose State University","San","CA","95113","CMPE"),Exceptions.malformed_id);//malformed_id
		testMethod(UniClient.addInstructor(insertedInst,"Dan","Harkey","San Jose State University","San","CA","95113","CMPE"),Exceptions.success);//add
		testMethod(UniClient.addInstructor(insertedInst,"Dan","Harkey","San Jose State University","San","CA","95113","CMPE"),Exceptions.person_exists);//persong_Exsit
		testMethod(UniClient.addInstructor(notPresentInst,"Son","Lname","San Jose State University","San","CA","951134","CMPE"),Exceptions.malformed_zip);//malformed_zip
		testMethod(UniClient.addInstructor(notPresentInst,"Son","Garry","San Jose State University","San","CALIFORNIA","95113","CMPE"), Exceptions.malformed_state);//malformed_state
		System.out.println("******************************************");

		
		System.out.println("\n\n"+i+") TEST - Create_Student");
		System.out.println("***********************");
		i++;
		testMethod(UniClient.createStudent(insertedStud,"Smith","James","535 S Market Street","San Jose","CA","95192-1000"),Exceptions.success);
		testMethod(UniClient.createStudent(malformedStud,"Smith","James","535 S Market Street","San Jose","CA","95192"),Exceptions.malformed_id);		
		testMethod(UniClient.createStudent(notPresentStud,"Smith","James","535 S Market Street","San Jose","CAA","95192"),Exceptions.malformed_state);
		testMethod(UniClient.createStudent(notPresentStud,"Smith","James","535 S Market Street","San Jose","CA","951"),Exceptions.malformed_zip);
		testMethod(UniClient.createStudent(insertedStud,"Smith","James","535 S Market Street","San Jose","CA","95192"),Exceptions.person_exists);
		System.out.println("**********************************************");		
	
		System.out.println("\n\n"+i+") TEST - Create_Course");
		System.out.println("***********************");
		i++;
		testMethod(UniClient.addCourse(insertedCourse1,section1,insertedInst,location,"3"),Exceptions.success);
		testMethod(UniClient.addCourse(notPresentCourse,section1,insertedInst,malformedLocation1,"3"),Exceptions.malformed_location);
		testMethod(UniClient.addCourse(notPresentCourse,section1,insertedInst,malformedHours1,"3"),Exceptions.malformed_hours);
		testMethod(UniClient.addCourse(notPresentCourse,section1,insertedInst,location2,"-3"),Exceptions.malformed_units);
		testMethod(UniClient.addCourse(insertedCourse1,section1,insertedInst,location2,"3"),Exceptions.course_exists);
		testMethod(UniClient.addCourse(notPresentCourse,section1,insertedInst,locationRoomBooked,"3"),Exceptions.schedule_conflict);
		testMethod(UniClient.addCourse(insertedCourse2,section1,insertedInst,location2,"3"),Exceptions.success);
		//testMethod(UniClient.addCourse(notPresentCourse,"2",insertedInst,"ENGR 300 S 0820-0900","3"),Exceptions.schedule_conflict);
		testMethod(UniClient.addCourse(notPresentCourse,section1,malformedInst,location2,"3"),Exceptions.malformed_id);
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Add Office Hours:");
		System.out.println("***********************");
		i++;	
		testMethod(UniClient.addOfficeHours(insertedInst,locationInst),Exceptions.success);//Successfully_Added
		testMethod(UniClient.addOfficeHours(insertedInst,locationConflict1),Exceptions.schedule_conflict);//Schedule_Conflict
		testMethod(UniClient.addOfficeHours(insertedInst,locationConflict2),Exceptions.schedule_conflict);//Schedule_Conflict
		testMethod(UniClient.addOfficeHours(insertedInst,malformedHours1),Exceptions.malformed_hours);//malformed_hours
		testMethod(UniClient.addOfficeHours(insertedInst,malformedLocation1),Exceptions.malformed_location);//malformed_hours
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Enroll_Student");
		System.out.println("***********************");
		i++;
		testMethod(UniClient.enroll(insertedStud,insertedCourse1,section1),Exceptions.success);
		testMethod(UniClient.enroll(insertedStud,notPresentCourse,section1),Exceptions.no_such_course);	
		testMethod(UniClient.enroll(malformedStud,notPresentCourse,section2),Exceptions.malformed_id);	//multiple exceptions.check for order of exception
		testMethod(UniClient.enroll(insertedStud,insertedCourse1,section1),Exceptions.schedule_conflict);	
		testMethod(UniClient.setCourseUnits(insertedCourse2,section1,"23"),Exceptions.success); 
		testMethod(UniClient.enroll(insertedStud,insertedCourse2,section1),Exceptions.too_many_units);	
		testMethod(UniClient.enroll(notPresentStud,notPresentCourse,section2),Exceptions.no_such_person);	
	    testMethod(UniClient.enroll(notPresentStud,notPresentCourse,"2\n1"),Exceptions.malformed_message);	
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Find_Persons_BY_NAME");
		System.out.println("***********************");
		i++;
		testMethod(UniClient.findPersonsByName("d", "0"),Exceptions.success);
		testMethod(UniClient.findPersonsByName("S","1"),Exceptions.success);
		testMethod(UniClient.findPersonsByName("Smith","1"),Exceptions.success);
		testMethod(UniClient.findPersonsByName("ish", "3"),Exceptions.malformed_message);
		System.out.println("**********************************************");

		
		System.out.println("\n\n"+i+") TEST - Find_Persons_BY_STATE");
		//MALFORMED_STATE EXCEPTION IS TO BE HANDLED
		System.out.println("***********************");
		i++;
		testMethod(UniClient.findPersonsByState( "CA", "0"),Exceptions.success);
		testMethod(UniClient.findPersonsByState("CA","1"),Exceptions.success);
		testMethod(UniClient.findPersonsByState("CA","2"),Exceptions.success);
		testMethod(UniClient.findPersonsByState("CA", "3"),Exceptions.malformed_message);
		testMethod(UniClient.findPersonsByState("QA%", "2"),Exceptions.malformed_state);
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Find_Persons_BY_CITY");
		System.out.println("***********************");
		i++;
		testMethod(UniClient.findPersonsByCity( "SANJOSE", "0"),Exceptions.success);
		testMethod(UniClient.findPersonsByCity("SANJOSE","1"),Exceptions.success);
		testMethod(UniClient.findPersonsByCity("sanjose", "2"),Exceptions.success);
		testMethod(UniClient.findPersonsByCity("S","2"),Exceptions.success);
		testMethod(UniClient.findPersonsByCity("SAN%", "3"),Exceptions.malformed_message);
		System.out.println("**********************************************");
		
		System.out.println("\n\n"+i+") TEST - Find_Persons_BY_ZIP");
		//MALFORMED_ZIP EXCEPTION IS TO BE HANDLED
		System.out.println("***********************");
		i++;
		testMethod(UniClient.findPersonsByZip( "95112", "2"),Exceptions.success);
		testMethod(UniClient.findPersonsByZip("95192-1000","2"),Exceptions.success);
		testMethod(UniClient.findPersonsByZip( "95112", "0"),Exceptions.success);
		testMethod(UniClient.findPersonsByZip("95192-1000","0"),Exceptions.success);
		testMethod(UniClient.findPersonsByZip( "95112", "1"),Exceptions.success);
		testMethod(UniClient.findPersonsByZip("95192-1000","1"),Exceptions.success);
		testMethod(UniClient.findPersonsByZip("ABC%", "0"),Exceptions.malformed_zip);
		System.out.println("**********************************************");

		
		System.out.println("\n\n"+i+") TEST - Find_All_Persons");
		System.out.println("***********************");
		i++;
		testMethod(UniClient.findAllPersons("0"),Exceptions.success);
		testMethod(UniClient.findAllPersons("1"),Exceptions.success);
		testMethod(UniClient.findAllPersons("2"),Exceptions.success);
		testMethod(UniClient.findAllPersons("3"),Exceptions.malformed_message);
		testMethod(UniClient.findAllPersons("abb"),Exceptions.malformed_message);
		System.out.println("**********************************************");


		
		System.out.println("\n\n"+i+") TEST - GETFNAME");
		//MALFORMED_ID , NO_SUCH_PERSON EXCEPTIONS ARE TO BE THROWN
		System.out.println("***********************");
		i++;
		testMethod((UniClient.getFirstName(insertedInst)),Exceptions.success);
		testMethod((UniClient.getFirstName(insertedStud)),Exceptions.success);
		testMethod((UniClient.getFirstName(malformedInst)),Exceptions.malformed_id);
		testMethod((UniClient.getFirstName(notPresentInst)),Exceptions.no_such_person);
		System.out.println("**********************************************");

		
		System.out.println("\n\n"+i+") TEST - GETLNAME");
		//MALFORMED_ID , NO_SUCH_PERSON EXCEPTIONS ARE TO BE THROWN
		System.out.println("***********************");
		i++;
		testMethod((UniClient.getLastName(insertedInst)),Exceptions.success);
		testMethod((UniClient.getLastName(insertedStud)),Exceptions.success);
		testMethod((UniClient.getLastName(malformedInst)),Exceptions.malformed_id);
		testMethod((UniClient.getLastName(notPresentInst)),Exceptions.no_such_person);
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - GETADDRESS");
		//MALFORMED_ID , NO_SUCH_PERSON EXCEPTIONS ARE TO BE THROWN
		System.out.println("***********************");
		i++;
		testMethod((UniClient.getAddress(insertedInst)),Exceptions.success);
		testMethod((UniClient.getAddress(insertedStud)),Exceptions.success);
		testMethod((UniClient.getAddress(malformedInst)),Exceptions.malformed_id);
		testMethod((UniClient.getAddress(notPresentInst)),Exceptions.no_such_person);
		System.out.println("**********************************************");

		
		System.out.println("\n\n"+i+") TEST - GETCITY");
		//MALFORMED_ID , NO_SUCH_PERSON EXCEPTIONS ARE TO BE THROWN
		System.out.println("***********************");
		i++;
		testMethod((UniClient.getCity(insertedInst)),Exceptions.success);
		testMethod((UniClient.getCity(insertedStud)),Exceptions.success);
		testMethod((UniClient.getCity(malformedInst)),Exceptions.malformed_id);
		testMethod((UniClient.getCity(notPresentInst)),Exceptions.no_such_person);
		System.out.println("**********************************************");
		
		
		System.out.println("\n\n"+i+") TEST - GETSTATE");
		//MALFORMED_ID , NO_SUCH_PERSON EXCEPTIONS ARE TO BE THROWN
		System.out.println("***********************");i++;
		testMethod((UniClient.getState(insertedInst)),Exceptions.success);
		testMethod((UniClient.getState(insertedStud)),Exceptions.success);
		testMethod((UniClient.getState(malformedInst)),Exceptions.malformed_id);
		testMethod((UniClient.getState(notPresentInst)),Exceptions.no_such_person);
		System.out.println("**********************************************");
	
		
		System.out.println("\n\n"+i+") TEST - GETZIP");
		//MALFORMED_ID , NO_SUCH_PERSON EXCEPTIONS ARE TO BE THROWN
		System.out.println("***********************");
		i++;
		testMethod((UniClient.getZip(insertedInst)),Exceptions.success);
		testMethod((UniClient.getZip(insertedStud)),Exceptions.success);
		testMethod((UniClient.getZip(malformedInst)),Exceptions.malformed_id);
		testMethod((UniClient.getZip(notPresentInst)),Exceptions.no_such_person);
		System.out.println("**********************************************");
 
		System.out.println("\n\n"+i+") TEST - Get_Enrolled_Units");
		System.out.println("***********************");
		i++;
		testMethod(UniClient.getEnrolledUnits(insertedStud),Exceptions.success);
		testMethod(UniClient.getEnrolledUnits(malformedStud),Exceptions.malformed_id);	
		testMethod(UniClient.getEnrolledUnits("a\nb"),Exceptions.malformed_message);	
		System.out.println("**********************************************");

		
		System.out.println("\n\n"+i+") TEST - Get_Students");
		System.out.println("***********************");
		i++;
		testMethod(UniClient.getStudents(insertedCourse1,section1),Exceptions.success);
		testMethod(UniClient.getStudents(notPresentCourse,section1),Exceptions.no_such_course);	
		testMethod(UniClient.getStudents(insertedCourse1,"1\n2"),Exceptions.malformed_message);	
		System.out.println("**********************************************");
		
		
		System.out.println("\n\n"+i+") TEST - Get_Courses");
		System.out.println("***********************");
		i++;
		testMethod(UniClient.getCourses(insertedStud),Exceptions.success);
		testMethod(UniClient.getCourses(insertedInst),Exceptions.success);
		testMethod(UniClient.getCourses("1\n2"),Exceptions.malformed_message);	
		System.out.println("**********************************************");
		
		
		System.out.println("\n\n"+i+") TEST - Get_Location");
		System.out.println("***********************");
		i++;
		testMethod(UniClient.getLocation(insertedCourse1,section1),Exceptions.success);
		testMethod(UniClient.getLocation(notPresentCourse,section1),Exceptions.no_such_course);	
		testMethod(UniClient.getLocation(insertedCourse1,"1\n2"),Exceptions.malformed_message);	
		System.out.println("**********************************************");

		
		System.out.println("\n\n"+i+") TEST - Get_Course_Units");
		System.out.println("***********************");
		i++;
		testMethod(UniClient.getCourseUnits(insertedCourse1,section1),Exceptions.success); 
		testMethod(UniClient.getCourseUnits(notPresentCourse,section1),Exceptions.no_such_course); 
		testMethod(UniClient.getCourseUnits(insertedCourse1,section2),Exceptions.no_such_course);
		System.out.println("**********************************************");

		 
		System.out.println("\n\n"+i+") TEST - Get_Course_Instructor");
		System.out.println("***********************");i++;
		testMethod(UniClient.getCourseInstructor(insertedCourse1,section1),Exceptions.success); 
	    testMethod(UniClient.getCourseInstructor(notPresentCourse,section1),Exceptions.no_such_course);
		testMethod(UniClient.getCourseInstructor(insertedCourse1,section2),Exceptions.no_such_course);
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Find_All_Courses");
		System.out.println("***********************");i++;
		testMethod(UniClient.findAllCourses(),Exceptions.success); 
		System.out.println("**********************************************");

		
		System.out.println("\n\n"+i+") TEST - Find_Courses_By_Instructor");
		System.out.println("***********************");i++;
		testMethod(UniClient.findCoursesByInstructor(insertedInst),Exceptions.success);
		testMethod(UniClient.findCoursesByInstructor(notPresentInst),Exceptions.success);
		testMethod(UniClient.findCoursesByInstructor(malformedInst),Exceptions.success);
		testMethod(UniClient.findCoursesByInstructor("1\n2"),Exceptions.malformed_message);
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Find_Courses_By_Location");
		System.out.println("***********************");i++;
		testMethod(UniClient.findCoursesByLocation("ENGR"),Exceptions.success);
		testMethod(UniClient.findCoursesByLocation(location),Exceptions.success); 
		testMethod(UniClient.findCoursesByLocation("3"),Exceptions.success);  //0
		testMethod(UniClient.findCoursesByLocation("TBA"),Exceptions.success);
		testMethod(UniClient.findCoursesByLocation("1\n2"),Exceptions.malformed_message);	
		System.out.println("**********************************************");

		
		System.out.println("\n\n"+i+") TEST - Find_Courses_By_Course_Name");
		System.out.println("***********************");i++;
		testMethod(UniClient.findCoursesByCourseName("2"),Exceptions.success); 
		testMethod(UniClient.findCoursesByCourseName(insertedCourse1),Exceptions.success);		
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Get_Office_Hours:");
		System.out.println("***********************");i++;
		testMethod(UniClient.getOfficeHours(insertedInst),Exceptions.success);//Successfully
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Get_Department:");
		System.out.println("***********************");i++;
		testMethod(UniClient.getDepartment(insertedInst),Exceptions.success);//Successfully
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Find instructor By Department:");
		System.out.println("***********************");i++;
		testMethod(UniClient.Find_Instructors_By_Department("C"),Exceptions.success);//Successfully
		System.out.println("**********************************************");
		

		System.out.println("\n\n"+i+") TEST - Set_First_Name");
		System.out.println("***********************");i++;
		testMethod(UniClient.setFirstName(insertedInst,"Jim"),Exceptions.success);
		testMethod(UniClient.setFirstName(malformedInst,"Jim"),Exceptions.malformed_id);
		testMethod(UniClient.setFirstName(notPresentInst,"Jim"),Exceptions.no_such_person);
		testMethod(UniClient.setFirstName(notPresentInst,"Jim\n1"),Exceptions.malformed_message);
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Set_Last_Name");
		System.out.println("***********************");i++;
		testMethod(UniClient.setLastName(insertedInst,"Jones"),Exceptions.success);
		testMethod(UniClient.setLastName(malformedInst,"Jones"),Exceptions.malformed_id);
		testMethod(UniClient.setLastName(notPresentInst,"Jones"),Exceptions.no_such_person);
		testMethod(UniClient.setLastName(notPresentInst,"Jones\n1"),Exceptions.malformed_message);		
		System.out.println("**********************************************");


		System.out.println("\n\n"+i+") TEST - Set_Address");
		System.out.println("***********************");i++;
		testMethod(UniClient.setAddress(insertedInst,"4th Street"),Exceptions.success);
		testMethod(UniClient.setAddress(malformedInst,"4th Street"),Exceptions.malformed_id);
		testMethod(UniClient.setAddress(notPresentInst,"4th Street"),Exceptions.no_such_person);
		testMethod(UniClient.setAddress(notPresentInst,"4th Street\n1"),Exceptions.malformed_message);
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Set_City");
		System.out.println("***********************");i++;
		testMethod(UniClient.setCity(insertedInst,"Fremont"),Exceptions.success);
		testMethod(UniClient.setCity(malformedInst,"Fremont"),Exceptions.malformed_id);
		testMethod(UniClient.setCity(notPresentInst,"Fremont"),Exceptions.no_such_person);
		testMethod(UniClient.setCity(notPresentInst,"Fremont\n1"),Exceptions.malformed_message);
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Set_State");
		System.out.println("***********************");i++;
		testMethod(UniClient.setState(insertedInst,"CA"),Exceptions.success);
		testMethod(UniClient.setState(malformedInst,"CA"),Exceptions.malformed_id);
		testMethod(UniClient.setState(notPresentInst,"CA"),Exceptions.no_such_person);
		testMethod(UniClient.setState(insertedInst,"ZZ"),Exceptions.malformed_state);
		testMethod(UniClient.setState(insertedInst,"ZZ\n1"),Exceptions.malformed_message);
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Set_Zip");
		System.out.println("***********************");i++;
		testMethod(UniClient.setZip(insertedInst,"94538"),Exceptions.success);
		testMethod(UniClient.setZip(insertedInst,"94538-1234"),Exceptions.success);
		testMethod(UniClient.setZip(malformedInst,"CA"),Exceptions.malformed_id);
		testMethod(UniClient.setZip(notPresentInst,"944"),Exceptions.no_such_person);
		testMethod(UniClient.setZip(insertedInst,"944"),Exceptions.malformed_zip);
		testMethod(UniClient.setZip(insertedInst,"94444-12"),Exceptions.malformed_zip);
		testMethod(UniClient.setZip(insertedInst,"94ert"),Exceptions.malformed_zip);
		testMethod(UniClient.setZip(insertedInst,"94ert\n1"),Exceptions.malformed_message);
		System.out.println("**********************************************");

		// TEST - Set_ID done later
		 
	 
		System.out.println("\n\n"+i+") TEST - Set_Location");
		System.out.println("***********************");i++;
		testMethod(UniClient.setLocation("ENGR 240 M 0800-1000",insertedCourse1,section1),Exceptions.has_students);
		testMethod(UniClient.setLocation("ENGR 240 M 0800-1000",insertedCourse2,section1),Exceptions.success);
		System.out.println("**********************************************");
		
		System.out.println("\n\n"+i+") TEST - Set_Course_Name");
		System.out.println("***********************");i++;
		testMethod(UniClient.setCourseName(notPresentCourse,section1,"212"),Exceptions.no_such_course); 
		testMethod(UniClient.setCourseName(insertedCourse1,section2,"207"),Exceptions.no_such_course);
		testMethod(UniClient.setCourseName(insertedCourse1,section1,insertedCourse2),Exceptions.course_exists); 
		testMethod(UniClient.setCourseName(insertedCourse1,section1,"302"),Exceptions.success); 
		System.out.println("**********************************************");
		
		System.out.println("\n\n"+i+") TEST - Set_Course_Section");
		System.out.println("***********************");i++;
		testMethod(UniClient.setCourseSection(notPresentCourse,section1,section2),Exceptions.no_such_course); 
		testMethod(UniClient.setCourseSection(insertedCourse1,section2,section1),Exceptions.no_such_course);
		testMethod(UniClient.setCourseSection(insertedCourse1,section1,section1),Exceptions.course_exists); 
		testMethod(UniClient.setCourseSection(insertedCourse1,section1,section2),Exceptions.success); 
		System.out.println("**********************************************");

		
		System.out.println("\n\n"+i+") TEST - Set_Course_Units");
		System.out.println("***********************");i++;
		testMethod(UniClient.setCourseUnits(notPresentCourse,section1,"3"),Exceptions.no_such_course); 
		testMethod(UniClient.setCourseUnits(insertedCourse1,section1,"3"),Exceptions.no_such_course);
		testMethod(UniClient.setCourseUnits(insertedCourse1,section1,"-3"),Exceptions.malformed_units); 
		testMethod(UniClient.setCourseUnits(insertedCourse1,section2,"4"),Exceptions.success); 
		System.out.println("**********************************************");		

		System.out.println("\n\n"+i+") TEST - Set Department:");
		testMethod(UniClient.setDepartment(insertedInst,"CS"),Exceptions.success);//Successfully_set
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Calculate_Bill");
		System.out.println("***********************");i++;
		testMethod(UniClient.calculateBill(insertedStud),Exceptions.success);
		testMethod(UniClient.calculateBill(notPresentStud),Exceptions.no_such_person);
		//changing course units  to check for more than 6 units.
		testMethod(UniClient.setCourseUnits(insertedCourse1,section2,"8"),Exceptions.success); 
		testMethod(UniClient.calculateBill(insertedStud),Exceptions.success);
		//changing state to check for non-resident student fees
		testMethod(UniClient.setState(insertedStud,"MA"),Exceptions.success);
		testMethod(UniClient.calculateBill(insertedStud),Exceptions.success);
		testMethod(UniClient.calculateBill(malformedStud),Exceptions.malformed_id);
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - UNENROLLSTUDENT");
		//STUDENT_NOT_ENROLLED, MALFORMED_ID , NO_SUCH_PERSON EXCEPTIONS ARE TO BE THROWN
		System.out.println("***********************");i++;
		testMethod((UniClient.unenroll(insertedStud, insertedCourse1, section1)),Exceptions.success);
		testMethod((UniClient.unenroll(insertedStud, insertedCourse1, section1)),Exceptions.student_not_enrolled);
		testMethod((UniClient.unenroll(notPresentStud, insertedCourse1, section1)),Exceptions.no_such_person);
		testMethod((UniClient.unenroll(malformedStud, insertedCourse1, section1)),Exceptions.malformed_id);
		System.out.println("**********************************************");


		System.out.println("\n\n"+i+") TEST - Remove_Student");
		System.out.println("***********************");i++;
		//Check force unenroll --enroll first
		testMethod(UniClient.enroll(insertedStud,insertedCourse1,section1),Exceptions.success);
		testMethod(UniClient.removeStudent(insertedStud,0),Exceptions.has_courses);
		testMethod(UniClient.removeStudent(insertedStud,1),Exceptions.success);//force unenroll
		testMethod(UniClient.removeStudent(insertedStud,0),Exceptions.no_such_person);
		testMethod(UniClient.removeStudent(malformedStud,0),Exceptions.malformed_id);	
		testMethod(UniClient.removeStudent("1\n2",0),Exceptions.malformed_message);
		testMethod(UniClient.removeStudent(insertedStud,3),Exceptions.malformed_message);
		System.out.println("**********************************************");

		System.out.println("\n\n"+i+") TEST - Remove_Course");
		System.out.println("***********************");i++;
		testMethod(UniClient.removeCourse(notPresentCourse,section2,"0"),Exceptions.no_such_course); //202 2
		testMethod(UniClient.removeCourse(insertedCourse1,section2,"0"),Exceptions.has_students); //202 2
		testMethod(UniClient.removeCourse(insertedCourse1,section2,"1"),Exceptions.success); //202 2
		testMethod(UniClient.removeCourse(insertedCourse1,section2,"1\n2"),Exceptions.malformed_message);
		System.out.println("**********************************************");
 
		System.out.println("\n\n"+i+") TEST - Remove Office Hours:");
		testMethod(UniClient.remHours(insertedInst,malformedLocation1),Exceptions.malformed_location);//malformed_location
		testMethod(UniClient.remHours(insertedInst,"ENGR 236 FM 1220-1500"),Exceptions.malformed_hours);//malformed_hours
		testMethod(UniClient.remHours(insertedInst,location2),Exceptions.no_such_office_hours);//no_such_office_hours
		testMethod(UniClient.remHours(insertedInst,locationInst),Exceptions.success);//remove
		System.out.println("**********************************************");		

	
		//Additional tests
		
		 final int NUM_COURSES=10000;
		 final int NUM_STUDENTS=10000;
		 final int NUM_INSTRUCTORS=10000;
	    
		 
		 long startTime=System.currentTimeMillis();
		 System.out.println("Creating students..");
		 int loop = 0;
		 for(int a=100;a<499;a++){
			 for(int b =10;b<99;b++){
				 for(int c = 1000;c<9999;c++){
					 String studID = a+"-"+b+"-"+c;
					 loop++;
					 if(loop==NUM_STUDENTS)
						 break;
			UniClient.createStudent(studID,"FName"+loop,"LName"+loop,"535 S Market Street","San Jose","CA","95192");
			}
		   }
		 }
		 
		 long stopTime=System.currentTimeMillis();
		 System.out.println("Time to create "+NUM_STUDENTS+" students: "+(stopTime-startTime)+"ms");
		 System.out.println("Average  :"+ (float)((stopTime-startTime)/NUM_STUDENTS)+"ms");

	
		 startTime=System.currentTimeMillis();
		 System.out.println("Creating instructors..");
		 loop = 0;
		 for(int a=500;a<999;a++){
			 for(int b =10;b<99;b++){
				 for(int c = 1000;c<9999;c++){
					 String instID = a+"-"+b+"-"+c;
					 loop++;
					 if(loop==NUM_INSTRUCTORS)
						 break;
						UniClient.addInstructor(instID,"Dan"+loop,"Harkey"+loop,"San Jose State University","San","CA","95113","CMPE");
				}
		     }
		 }		
		
		 stopTime=System.currentTimeMillis();
		 System.out.println("Time to create "+NUM_INSTRUCTORS+" instructor: "+(stopTime-startTime)+"ms");
		 System.out.println("Average per course :"+ (float)((stopTime-startTime)/NUM_STUDENTS)+"ms");
		 
		 
		 startTime=System.currentTimeMillis();
		 System.out.println("Creating courses..");
		 for(int k =0;k<NUM_COURSES;k++){
			 UniClient.addCourse("Course"+k,section1,insertedInst,location,"3");
		 }		
		 stopTime=System.currentTimeMillis();
		 System.out.println("Time to create "+NUM_COURSES+" courses: "+(stopTime-startTime)+"ms");
		 System.out.println("Average per course :"+ (float)((stopTime-startTime)/NUM_STUDENTS)+"ms");

		
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

	
	static void testMethod(String replyMsg, int expectedResult){
		//String reply = methodName;
		System.out.println("\nReply ----> Expected = "+expectedResult+"\n" + replyMsg);
		String[] output = replyMsg.split("\n");
		if(Integer.parseInt(output[1]) == expectedResult)
			System.out.println("Passed!");
		else{
			System.out.println("Failed!");
			return;
		}
		System.out.println("\n***********************");
	}
	

}