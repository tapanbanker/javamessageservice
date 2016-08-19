package Servant;

import GUI.*;
import Main.*;
import Misc.*;
import Conn.*;
import Servant.*;

import java.util.StringTokenizer;


public class CourseServant{
	
	private String section          = null;
	private String units            = null;
	private String instructorID       = null;
	private String location        = null;
	private String courseName      = null;
	private String students         =null;
	public static String validDays[]={"M","T","W","R","F","S","U"};
	private  int hourMinuteSchedule[][][] = new int[7][24][60];

//Getters
public String getCourseName() {return this.courseName;}
public String getSection() {return this.section;}
public String getUnits() {return this.units;}
public String getInstructorID(){return this.instructorID;}
public String getLocation() {return this.location;}
public String getStudents() {return this.students;}

//Setters
//public void setCourseName(String courseName) {this.courseName = courseName;}
//public void setSection(String section){this.section = section;}
//public void setUnits(String units){this.units = units;}
//public void setLocation(String location) {this.location = location;}


public static void main(String[] st){

	//System.out.println((new CourseServant()).createCourse("fdf\n3423423\n5\n208\n2\n343-244-343\nENGR 239 M 1800-2000\n3"));
	
	//System.out.println((new CourseServant()).createCourse("fdf\n3423423\n5\n202\n2\n343244343\nENGR 239 M 0800-1900\n3"));
	
	/*
	 * insert into instructor(p_id,instructor_ID,dept) values(1,343244343,"CMPE");

insert into person(fname,lname,address,city,state,zip) values('dan','harkey','dsaas','SanJose','CA','94132');

insert into office_hours values(2,0,'08:00','10:00','ENGR',239);

insert into course(c_section,c_name,i_id,c_units) values(2,'202',(select i_id from instructor where instructor_ID=343244343),3);

insert into as_course(p_id,c_id) values((select p_id from instructor where instructor_ID=343244343),(select max(c_id) from course));

"insert into as_course(p_id,c_id) values((select p_id from instructor where instructor_ID="+instructorID+"),(select max(c_id) from course));");
*/	 

	System.out.println((new CourseServant()).setCourseInstructor("set_course\n3423423\n5\n123-95-1786\n209\n2"));
}




//These Function sets the location of the course-Rajan
public String setLocationOfCourse(String message){
	System.out.println("message :"+message);
	StringTokenizer st=new StringTokenizer(message,"\n");
	String operation=st.nextToken();
	String requestID=st.nextToken();
	String parameter=st.nextToken();
	String[] location=getLocationString(st.nextToken());
	String courseName=st.nextToken();
	String courseSection=st.nextToken();
	String reply="";
	
	boolean	room_booked_exception_flag=false;
	if(location[0]==null){
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_location);
		return reply;
		}
	String building=location[0];
	String room=location[1];
	
	
    if((building.equalsIgnoreCase("TBA")) && !room.equalsIgnoreCase("TBA")){
		System.out.println("Invalid TBA with location:");
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_location);
		return reply;
	}
	if((!building.equalsIgnoreCase("TBA")) && room.equalsIgnoreCase("TBA")){
		System.out.println("Invalid TBA with location:");
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_location);
		return reply;
	}
	
	
	
	if(building.equalsIgnoreCase("TBA") && room.equalsIgnoreCase("TBA"))
	{
		room_booked_exception_flag=true;
	}
	
	System.out.println("location: "+location[2]+"  "+location[3]);
	
	if(location[2].equals("TBA") && location[3].equals("TBA")){
		Instructor_DB.setCourseLocation(courseName,courseSection,"-1","-1","-1",building,room);
		return requestID+"\n"+0+"\n"+0;
	}
	
	int noOfDays=location[2].length();
	if(noOfDays>7 || noOfDays<1){
		//Exception
		System.out.println("Exception: No Of days");
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_location);
		return reply;
		
	}
	String[][] days=getDays(location[2],noOfDays);
	if(days[0][0]==null){
		//Exception
		System.out.println("Days are not proper:");
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_location);
		return reply;
		
		}
	
	System.out.println("Here Days*********"+days[0][1]);
	
	boolean checkOrderOfDays=validateOrderOfDays(days,noOfDays);
	if(!checkOrderOfDays){
		//Exception
		System.out.println("Days are not ordered in proper way:");
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_location);
		return reply;
		// Raise Malformed_LOCATION Exception
		//return Exceptions.malformed_hours;
	}
	String[] validHours=validateHours(location[3]);
	String stime=location[3].substring(0,location[3].indexOf("-"));
	String etime=location[3].substring(location[3].indexOf("-")+1,location[3].length());

	if(validHours[0]==null){
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_hours);
		return reply;
		}
	
	if(!checkMalFormedHours(validHours[0],validHours[1])){
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_hours);
		return reply;
	}
	
	//Exception for room_booked
boolean finalValidation=true;
	
	//Check for Room_Booked Exception
	if(!room_booked_exception_flag)
	{
		String[][][] officeHours=Course_DB.loadRoomCourseHours(building,(room));
		
	try{
		for(int i=0;i<noOfDays;i++){
		finalValidation=loadRoomOfficeHour(officeHours,Integer.parseInt(days[i][1]),validHours[0],validHours[1]);
		if(!finalValidation)
		{
			break;
		}
		}
	}
	catch(Exception e){
		reply=requestID+"\n"+Integer.toString(Exceptions.room_booked);
		return reply;
		}
	if(!finalValidation){
		reply=requestID+"\n"+Integer.toString(Exceptions.room_booked);
		return reply;
		}
	}
	
	
	int ret = Course_DB.checkHasStudent(courseName,courseSection);
	if(ret !=0){
	reply=requestID+"\n"+Exceptions.has_students;
	return reply;
	}
	
	System.out.println(courseName+courseSection+days[0][0]+stime+etime+building+room);
	Instructor_DB.setCourseLocation(courseName,courseSection,days[0][0],stime,etime,building,room);
	return requestID+"\n"+0+"\n"+0;
}



private boolean checkMalFormedHours(String startTime, String endTime) {
	int tempTime[]=getHourMinute(startTime);
	int startHour=tempTime[0];
	int startMinute=tempTime[1];
	tempTime=getHourMinute(endTime);
	int endHour=tempTime[0];
	int endMinute=tempTime[1];
	boolean conflictScheduleFlag=true;
	int endingMinute=60;

	boolean malformedHourFlag=validateMalFormedHours(startHour,endHour,startMinute,endMinute);
	if(!malformedHourFlag){
		System.out.println("malformed Hours:");
		return false;
		}
	return true;
	
}
//These function create the course--rajan
public String createCourse(String message)// throws SQLException
{	
	String[] inputString=getCourseParameter(message);
	int j=0;
	String reply="";
	String requestID=inputString[0];
	boolean room_booked_exception_flag=false;
	String courseName=inputString[1];
	System.out.println("Course Name: "+courseName);
	String courseSection=inputString[2];
	String instructorID=inputString[3];
	String[] location=getLocationString(inputString[4]);
	try{
	if(location[0]==null){
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_location);
	
		return reply;
		}
	String units=inputString[5];
	String building=location[0];
	String room=location[1];
	
	boolean checkIDFlag=(new InstructorServant()).validateInstructorID(instructorID);
	if(!checkIDFlag){
		//Exception 
		return requestID+"\n"+Exceptions.malformed_id;
	}
	
    if((building.equalsIgnoreCase("TBA")) && !room.equalsIgnoreCase("TBA")){
		System.out.println("Invalid TBA with location:");
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_location);
		return reply;
	}
	if((!building.equalsIgnoreCase("TBA")) && room.equalsIgnoreCase("TBA")){
		System.out.println("Invalid TBA with location:");
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_location);
		return reply;
	}
	
	
	if(building.equalsIgnoreCase("TBA") && room.equalsIgnoreCase("TBA"))
	{
		room_booked_exception_flag=true;
	}
	
	
	
	//Exception for course exist
	int count=Course_DB.getNumberOfCourse(courseName,courseSection);
	System.out.println("COUNT :"+count);
	if(count!=0)
	{
		reply=requestID+"\n"+Integer.toString(Exceptions.course_exists);
	    return reply;
	}
	//Exception for course unit
	try{
		int noOfUnits=Integer.parseInt(inputString[5]);
	if (noOfUnits<0)
	{
		System.out.println("Invalid Units:");
	    reply=requestID+"\n"+Integer.toString(Exceptions.malformed_units);
	    return reply;
	}
	}
	catch(Exception e){
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_units);
		return reply;
	}
	
	//System.out.println("OFFICE HOURS: __________"+location[3].substring(4,5)+"");
	if(!location[3].substring(4,5).equalsIgnoreCase("-")){
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_hours);
		return reply;
	}
	
	if(location[2].equalsIgnoreCase("TBA") && location[3].equalsIgnoreCase("TBA")){
		
		int error=Course_DB.writeCreateCourse(courseName,courseSection,instructorID,building,room,-1,"-1","-1",units);
		if(error!=0){
			reply=requestID+"\n"+Exceptions.create_exception;
			return reply;
		}
		else{
			reply=requestID+"\n"+0;
			return reply;
		}
	}
	
	
	int noOfDays=location[2].length();
	if(noOfDays>7 || noOfDays<1){
		//Exception
		System.out.println("Exception: No Of days");
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_location);
		return reply;
		
	}
	String[][] days=getDays(location[2],noOfDays);
	if(days[0][0]==null){
		//Exception
		System.out.println("Days are not proper:");
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_location);
		return reply;
		
		}
	boolean checkOrderOfDays=validateOrderOfDays(days,noOfDays);
	if(!checkOrderOfDays){
		//Exception
		System.out.println("Days are not ordered in proper way:");
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_location);
		return reply;
		// Raise Malformed_LOCATION Exception
		//return Exceptions.malformed_hours;
	}
	String[] validHours=validateHours(location[3]);
	String stime=location[3].substring(0,location[3].indexOf("-"));
	String etime=location[3].substring(location[3].indexOf("-")+1,location[3].length());

	if(validHours[0]==null){
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_hours);
		return reply;
		}

	
	boolean finalValidation=true;	
	//Check for Room_Booked Exception
	if(!room_booked_exception_flag)
	{
		CourseServant courseServant=new CourseServant();
		String[][][] officeHours=Course_DB.loadRoomCourseHours(building,(room));
		
	try{
		for(int i=0;i<noOfDays;i++){
		finalValidation=loadRoomOfficeHour(officeHours,Integer.parseInt(days[i][1]),validHours[0],validHours[1]);
		if(!finalValidation)
		{
			break;
		}
		}
	}
	
	catch(Exception e){
		System.out.println("Exception at room booked");
		reply=requestID+"\n"+Integer.toString(Exceptions.room_booked);
		return reply;
		}
	}
	
	int officeHourExceptionFlag=-1;
	try{
		System.out.println("Office Hours Course Servant:");
		String[][][] officeHours=Instructor_DB.loadOfficeHour(instructorID);
		InstructorServant instructor=loadOfficeHour(officeHours);
		instructor.setInstructorID(instructorID);
		for(int i=0;i<noOfDays;i++){
		officeHourExceptionFlag=instructor.validateOfficeHourConflict(Integer.parseInt(days[i][1]),validHours[0],validHours[1]);
		if(officeHourExceptionFlag!=-1){break;}
	}
	}
	catch(Exception e){
		System.out.println("Office Hours are invalid: 2"+e);
		reply=requestID+"\n"+Integer.toString(Exceptions.schedule_conflict);
		return reply;
	}
	if(officeHourExceptionFlag!=-1){
		if(officeHourExceptionFlag==0){
			reply=requestID+"\n"+Integer.toString(Exceptions.malformed_hours);
			return reply;
		}
		else{
			reply=requestID+"\n"+Integer.toString(Exceptions.schedule_conflict);
			return reply;
		}
	}
	
	if(!finalValidation){
		System.out.println("Exception at room booked 2");
		reply=requestID+"\n"+Integer.toString(Exceptions.room_booked);
		return reply;
		}

	if(!checkMidNightExtension(stime,etime)){
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_hours);
		System.out.println("I am midnight:"+reply);
		return reply;
	}
	
	try{
		//insert values into AS_COurse also..********************************88
		for(int i=0;i<noOfDays;i++){
			int error=Course_DB.writeCreateCourse(courseName,courseSection,instructorID,building,room,Integer.parseInt(days[i][1]),stime,etime,units);
			if(error!=0){
				reply=requestID+"\n"+Exceptions.create_exception;
				return reply;
			}
		}
		
		
	}
	catch(Exception e){}
	reply=requestID+"\n"+0+"\n"+0;
	return reply;
	}
	catch(Exception e){
		return requestID+"\n"+Exceptions.malformed_location;
	}
}



private boolean checkMidNightExtension(String stime, String etime) {
	
	try{
		int sHour=Integer.parseInt(stime.substring(0,2));
		int eHour=Integer.parseInt(etime.substring(0,2));
		int eMin=Integer.parseInt(etime.substring(2,4));
		if(sHour>23){return false;}
		if(sHour>12){
			if(eHour==0 && eMin>0){return false;}
			if(eHour>0 && eHour<sHour){return false;}
		}
	}
	catch(Exception e){}
	return true;
}

public String setCourseInstructor(String message){
	
	String[] inputString=new String[4];
	StringTokenizer st=new StringTokenizer(message,"\n");
	st.nextToken();
	String requestID=st.nextToken();
	st.nextToken();
	String instructorID=st.nextToken();
	System.out.println("InstructorID :"+instructorID);
	String courseName=st.nextToken();
	String courseSection=st.nextToken();
	
	//No Such Person Exist
	System.out.println("Instructor: "+requestID+"  "+instructorID+" "+courseName+" "+courseSection);
	int count=Instructor_DB.getPersonExistance(instructorID);
	System.out.println("Person: "+count);
	if(count<=0){
		System.out.println("NO Such Person Exist: ");
		return requestID+"\n"+Exceptions.no_such_person;
	}
	
	//Schedule Conflict
//	InstructorServant instructor=new InstructorServant();
	int officeHourExceptionFlag=0;
	String reply="";
	try{
	System.out.println("Office Hours Course Servant:");
	String[][][] officeHours=Instructor_DB.loadOfficeHour(instructorID);
	System.out.println("hello:1");
	InstructorServant instructor=loadOfficeHour(officeHours);
	instructor.setInstructorID(instructorID);
	String[] daysAndTime=Instructor_DB.getDaysAndTime(courseName,courseSection);
	
	String stime=daysAndTime[1].substring(0, 2)+":"+daysAndTime[1].substring(2, 4);
	String etime=daysAndTime[2].substring(0, 2)+":"+daysAndTime[2].substring(2, 4);
	System.out.println("stime :"+stime+"  etime:"+etime);
	officeHourExceptionFlag=instructor.validateOfficeHourConflict(Integer.parseInt(daysAndTime[0]),stime,etime);
	
	}
	catch(Exception e){
	System.out.println("Office Hours are invalid: 1"+e);
	reply=requestID+"\n"+Integer.toString(Exceptions.schedule_conflict);
	return reply;
}
if(officeHourExceptionFlag!=-1){
	if(officeHourExceptionFlag==0){
		reply=requestID+"\n"+Integer.toString(Exceptions.malformed_hours);
		return reply;
	}
	else{
		reply=requestID+"\n"+Integer.toString(Exceptions.schedule_conflict);
		return reply;
	}
}
	
try{
	Instructor_DB.writeSetCourseInstructor(instructorID,courseName,courseSection);
}
catch(Exception e){
	return reply=requestID+"\n"+Exceptions.create_exception;
}
return reply=requestID+"\n"+0+"\n"+0;
	
}
	


private InstructorServant loadOfficeHour(String[][][] officeHours) {
	
	InstructorServant instructor=new InstructorServant();
	try{
		System.out.println("Office Hour Length:"+officeHours.length);
		for(int j=0;j<officeHours.length;j++){
			System.out.println("Servant :"+officeHours[j][0][0]+"   "+officeHours[j][1][0]+"  "+officeHours[j][2][0]);
			instructor.validateOfficeHourConflict(Integer.parseInt(officeHours[j][0][0]),officeHours[j][1][0], officeHours[j][2][0]);
		}
	}
	catch(Exception e){
	//Exception	
	}
	return instructor;
}



private String[] getLocationString(String message) {


	StringTokenizer st=new StringTokenizer(message," ");
	String[] inputString=new String[4];
	inputString[0]="";
	StringTokenizer st1=new StringTokenizer(message," ");
	String temp=null;
	
	while(st1.hasMoreTokens()){
		temp=st1.nextToken();
	}
	
	int length=0;
	if(temp.equals("TBA")){
		length=st.countTokens()-2;
	}
	else{
	length=st.countTokens()-3;
	}
	
	for(int i=0;i<length;i++){
		inputString[0]+=st.nextToken()+" ";
	}
	inputString[0]=inputString[0].trim();
	
	try{
	inputString[1]=st.nextToken();
	inputString[2]=st.nextToken();
	//inputString[3]=st.nextToken();
	if(temp.equals("TBA")){inputString[3]="TBA";}
	else{inputString[3]=st.nextToken();}
	}
	catch(Exception e){
		if(inputString[2].equalsIgnoreCase("TBA")){inputString[2]="NO_CONFLICT";}
		else{inputString[0]=null;}
		}
	
	return inputString;
}
private String[] getCourseParameter(String message) {
	String[] inputString=new String[6];
	StringTokenizer st = new StringTokenizer(message, "\n");
	st.nextToken();
	inputString[0]=st.nextToken();
	st.nextToken();
	for(int i=1;i<6;i++){
	inputString[i] = st.nextToken();
	System.out.println("Here:"+inputString[i]);
	}
	return inputString;
}
private boolean checkRoomValid(String room) {
	// TODO Auto-generated method stub
	return true;
}
	private static boolean validInteger(String st1) {
		try{
			int i=Integer.parseInt(st1);
			if(i<0){return false;}
		}
		catch(Exception e){
			return false;
		}
		return true;
	}

private static String[] getOfficeHourString(String message) {

	String[] inputString=new String[4];
	StringTokenizer st=new StringTokenizer(message," ");
	try{
	inputString[0]=st.nextToken();// ENGR
	inputString[1]=st.nextToken();//489
	inputString[2]=st.nextToken();//MWF
	inputString[3]=st.nextToken();//1000-1200
	}
	catch(Exception e){
		if(inputString[2].equalsIgnoreCase("TBA")){inputString[2]="NO_CONFLICT";}
		else{inputString[0]=null;}
		}
	return inputString;
}

private static String[][] getDays(String days,int noOfDays) {
	String[][] day=new String[noOfDays][2];
	int begin=0;
	int end=1;
	for(int i=0;i<days.length();i++){
		//Store days into the array
		day[i][0]=days.substring(begin++,end++);
		boolean flag=false;
		//Check for valid days
		for(int j=0;j<7;j++){
			if(day[i][0].equalsIgnoreCase(validDays[j])){
				day[i][1]=Integer.toString(j);
				System.out.println("DAYS ********"+day[i][1]);
				flag=true;
				break;
			}
		}
		if(!flag){
		day[0][0]=null;
		break;
		}
	}
	return day;
}



private static boolean validateOrderOfDays(String[][] days,int noOfDays) {
	boolean checkOrder=true;
	for(int i=0;i<noOfDays;i++){
		for(int j=i+1;j<noOfDays;j++){
			if(Integer.parseInt(days[i][1])>=Integer.parseInt(days[j][1])){
				checkOrder=false;
				break;
			}
		}
		if(!checkOrder){
			break;
		}
	}
	return checkOrder;
}
public static String[] validateHours(String hours) {
	// TODO Auto-generated method stub
	String[] hourString=new String[2];
	StringTokenizer st=new StringTokenizer(hours,"-");
	try{
		hourString[0]=st.nextToken();
		hourString[1]=st.nextToken();
	}
	catch(Exception e){
		System.out.println("Hours are not proper:");
		hourString[0]=null;
		return hourString;
	}
	boolean validHour=validInteger(hourString[0]);
	if(!validHour || hourString[0].length()!=4){
		System.out.println("Invalid Start Time");
		hourString[0]=null;
		return hourString;
	}
	validHour=validInteger(hourString[1]);
	if(!validHour || hourString[1].length()!=4){
		System.out.println("Invalid End Time Time");
		hourString[0]=null;
		return hourString;
	}
	String time=hourString[0].substring(0, 2)+":"+hourString[0].substring(2, 4);
	hourString[0]=time;
	System.out.println("Start Time : "+time);
	time=null;
	time=hourString[1].substring(0, 2)+":"+hourString[1].substring(2, 4);
	hourString[1]=time;
	return hourString;
}

public boolean validOfficeHoursRoom(String building,String roomNo,int day,String stime,String etime) {

	String[][][] officeHours=Course_DB.loadRoomCourseHours(building,roomNo);
	boolean flag=loadRoomOfficeHour(officeHours,day,stime,etime);
	System.out.println("ROOM VACANCY: "+flag);
	return flag;
	
}

public int[] getHourMinute(String time){
	int hourMinute[] = new int[2];
	hourMinute[0] = Integer.parseInt(time.substring(0, time.indexOf(":")));
	hourMinute[1] = Integer.parseInt(time.substring(time.indexOf(":")+1));
	System.out.println(hourMinute[0]+"  "+hourMinute[1]);
	return hourMinute;
}


public static boolean validateMalFormedHours(int startHour,int endHour,int startMinute,int endMinute){

	if(startHour>endHour){return false;}
	else if(startHour==endHour && startMinute>=endMinute){return false;}
	else{
	return true;	
	}
}

private boolean loadRoomOfficeHour(String[][][] officeHours,int day,String stime,String etime) {
	
	CourseServant course=new CourseServant();
	//boolean conflict=true;
	int conflict=-1;
	try{
	{
		for(int j=0;j<officeHours.length;j++){
			System.out.println("Servant :"+officeHours[j][0][0]+"   "+officeHours[j][1][0]+"  "+officeHours[j][2][0]);
			conflict=course.validateOfficeHourConflict(Integer.parseInt(officeHours[j][0][0]),officeHours[j][1][0], officeHours[j][2][0]);
			if(conflict!=-1){break;}
		}
	}
	}
	catch(Exception e){
	//Exception
		return false;
	}
	conflict=course.validateOfficeHourConflict(day,stime,etime);
	if(conflict!=-1){
		System.out.println("Room Booked:");
	}
	
	if(conflict==-1){
		return true;
	}
	else{
	return false;
	}
}

public int validateOfficeHourConflict(int day,String startTime, String endTime){
	int tempTime[]=getHourMinute(startTime);
	int startHour=tempTime[0];
	int startMinute=tempTime[1];
	tempTime=getHourMinute(endTime);
	int endHour=tempTime[0];
	int endMinute=tempTime[1];
	boolean conflictScheduleFlag=true;
	int endingMinute=60;

	boolean malformedHourFlag=validateMalFormedHours(startHour,endHour,startMinute,endMinute);
	if(!malformedHourFlag){
		System.out.println("malformed Hours:");
		return 0;
		}
	for(int i=startHour;i<=endHour;i++){
		//System.out.println("End Hour: *************************"+i);
		if(i==endHour){
			endingMinute=endMinute;
			startMinute=0;
			}
		for(int j=startMinute;j<endingMinute;j++){
			if(hourMinuteSchedule[day][i][j]==1){
				conflictScheduleFlag=false;
				break;
			}
			}
		if(conflictScheduleFlag==false){break;}
	}

	endingMinute=60;
	if(conflictScheduleFlag){
		for(int i=startHour;i<=endHour;i++){
			if(i==endHour){
				endingMinute=endMinute;
				startMinute=0;
			}
				for(int j=startMinute;j<endingMinute;j++){
					hourMinuteSchedule[day][i][j]=1;
			}
		}	
		System.out.println("Successfully added:");
		return -1;
	}
	else{
		System.out.println("Conflict: Exception:");
		return 1;
	}
 }



	/****************Snigdha***********************/
	public String getCourses(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		//exception for malformed message
		if(inputString.length != 4)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.getCourses(inputString[3]);
		
		return reply;
	}
	
	/****************Snigdha***********************/
	public String getLocation(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		//exception for malformed message
		if(inputString.length != 5)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.getLocation(inputString[3],inputString[4]);
		
		return reply;
	}

	

	
	
///////////////////////////////////////////////Shikha's part/////////////////
	public String setCourseName(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length!=6)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.setCourseName(inputString[3], inputString[4], inputString[5]);
		
		return reply;
	}

	public String setCourseSection(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length!=6)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.setCourseSection(inputString[3],inputString[4],inputString[5]);
		
		return reply;
	}

	public String setCourseUnits(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length!=6)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.setCourseUnits(inputString[3],inputString[4],inputString[5]);
		
		return reply;
	}

	public String getCourseUnits(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length!=5)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.getCourseUnits(inputString[3],inputString[4]);
		
		return reply;
	}

	public String getCourseInstructor(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length!=5)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.getCourseInstructor(inputString[3],inputString[4]);
		
		return reply;
	}


	/*public String removeCourse(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length!=6)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.removeCourse(inputString[3],inputString[4],Integer.parseInt(inputString[5]));
		
		return reply;
	}*/

	public String findAllCourses(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length!=2)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.findAllCourses();
		
		return reply;
	}

	/*public String findCoursesByIns(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length!=4)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.findcoursesbyInstructor(inputString[3]);
		
		return reply;
	}*/

	/*public String findCoursesByLocation(String message) {
	String[] inputString=message.split("\n");
	String reply = null;
	System.out.println("Input string");
	for(int i=0;i<inputString.length;i++){
		System.out.println(inputString[i]);
	}

	if(inputString.length!=4)
		reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
	else
		reply = inputString[1]+"\n"+Course_DB.findCoursesbyLocation(inputString[3]);

	return reply;

	}*/


	public String findCoursesByCourseName(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length!=4)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.findCoursesByCourseName(inputString[3]);
		
		return reply;
	}
	
	public String findCoursesByLocation(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		//exception for malformed message
		if(inputString.length != 4)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.findCoursesByLocation(inputString[3]);
		
		return reply;
	}
	public String findCoursesByInstructor(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		//exception for malformed message
		if(inputString.length != 4)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.findCoursesByInstructor(inputString[3]);
		
		return reply;
	}
	
	public String removeCourse(String message)
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		//exception for malformed message
		if(inputString.length !=6)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Course_DB.removeCourse(inputString[3],inputString[4],inputString[5]);
		
		return reply;
	}

	
}
	