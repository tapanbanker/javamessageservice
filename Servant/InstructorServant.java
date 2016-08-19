package Servant;
import GUI.*;
import Main.*;
import Misc.*;
import Conn.*;
import Servant.*;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class InstructorServant {

private String firstName     = null;
private String lastName      = null;
private String address        = null;
private String city           = null;
private String state          = null;
private String zipCode        = null;
private Hashtable<Integer, String> associatedCourseID = new Hashtable<Integer, String>();
private Hashtable<Integer, String> officeHourDay =  new Hashtable<Integer, String>();
private Hashtable<Integer, String> officeHourStartTime =  new Hashtable<Integer, String>();
private Hashtable<Integer, String> officeHourEndTime =  new Hashtable<Integer, String>();
private String departmentName = null;
private String instructorID   = null;
private  int hourMinuteSchedule[][][] = new int[7][24][60];
private  int roomHourMinuteSchedule[][][] = new int[500][24][60];
public static String validStates[] = { "AL", "AK", "AZ", "AR", "CA", "CO", "CT",
	"DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY",
	"LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE",
	"NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR",
	"PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA",
	"WV", "WI", "WY","Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut",
	"Delaware","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas",
	"Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota",
	"Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey",
	"New Mexico","New York","North Carolina","North Dakota","Ohio","Oklahoma","Oregon",
	"Pennsylvania","Rhode Island","South Carolina","South Dakota","Tennessee","Texas",
	"Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"};
public static String validDays[]={"M","T","W","R","F","S","U"};
public static int exception_code=-1;


public InstructorServant(){}
public InstructorServant(String firstname,String lastName,String address, String city,String state,String zipCode,String courseID, String departmentName, String instructorID){
	this.firstName=firstName;
	this.lastName=lastName;
	this.address=address;
	this.city=city;
	this.state=state;
	this.zipCode=zipCode;
	this.associatedCourseID.put(associatedCourseID.size(),courseID);
	this.departmentName=departmentName;
	this.instructorID=instructorID;
	}

//getters
public String getFirstName(){return this.firstName;}
public String getLastName(){return this.lastName;}
public String getAddress(){return this.address;}
public String getCity(){return this.city;}
public String getState(){return this.state;}
public String getZipCode(){return this.zipCode;}
public Hashtable getAssociatedCourseID(){return this.associatedCourseID;}
public String getDepartmentName(){return this.departmentName;}
public String getInstructorID() {return this.instructorID;}
public Hashtable getOfficeHourDay(){return this.officeHourDay;}
public Hashtable getOfficeHourStartTime(){return this.officeHourStartTime;}
public Hashtable getOfficeHourEndTime(){return this.officeHourEndTime;}
//Setters
public void setFirstName(String firstName){this.firstName=firstName;}
public void setLastName(String lastName){this.lastName=lastName;}
public void setAddress(String address){this.address=address;}
public void setCity(String city){this.city=city;}
public void setState(String state){this.state=state;}
public void setZipCode(String  zipCode){this.zipCode=zipCode;}
public void setAssociatedCourseID(Hashtable associatedCourseID){this.associatedCourseID=associatedCourseID;}
public void setDepartmentName(String departmentName){this.departmentName=departmentName;}
public void setInstructorID(String instructorID) {this.instructorID=instructorID;}
//Methods

/*
 * add new instructor
 * may raises 
 * malformed_state, 
 * malformed_zip, 
 * create_exception
 */
//public void createInstructor(String firstname,String lastName,String address, String city,String state,String zipCode,String courseID, String departmentName, String instructorID) throws Exception
public static String createInstructor(String message)
{
String[] inputString=getInstructorParameter(message);
if(inputString[1]==null){
	return inputString[0]+"\n"+Exceptions.malformed_message;
}
int i=1;
int exception_code=Instructor_DB.insertInstructor(inputString[i++],inputString[i++],inputString[i++],inputString[i++],inputString[i++],inputString[i++],inputString[i++],inputString[i++]);
System.out.println("Exception code :"+exception_code);
if(exception_code!=-1)
return inputString[0]+"\n"+exception_code;
else
return inputString[0]+"\n"+0+"\n"+0;
}

/*
 * Get parameter
 */
public static String[] getInstructorParameter(String message){
	String[] inputString=new String[20];
	try{
	StringTokenizer st = new StringTokenizer(message, "\n");
	int length=st.countTokens();
	if(length!=11){
		inputString[1]=null;
		return (inputString);
	}
	String operation=st.nextToken();
	inputString[0]=st.nextToken();
	st.nextToken();
	for(int i=1;i<9;i++){
	inputString[i] = st.nextToken();
	System.out.println(inputString[i]);
	}
	}
	catch(Exception e){}
	return inputString;
}

/*
 * Remove Instructor may throw
 * has_courses, 
 * remove_exception
 */
public void removeInstructor( ){} 

/*
 * Add office Hour
 * may throw 
 * malformed_location
 * malformed_hours
 * room_booked
 * schedule_conflict
 */
public void addOfficeHour(String day, String startTime, String endTime)throws Exception{
	
}
public boolean validateDuplicateInstructor(){
	boolean duplicate=false;
	return duplicate;
} 
public boolean validateCourseSchedule(){
	boolean courseSchedule=false;
	return courseSchedule;
}
/*
 * validate malformed Hour and hour schedule
 */
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
	System.out.println("malformed Hours: I am here");
	return 0;
	}
for(int i=startHour;i<=endHour;i++){
	//System.out.println("End Hour: *************************"+i);
	if(i==endHour){
		endingMinute=endMinute;
		startMinute=0;
		}
	for(int j=startMinute;j<endingMinute;j++){
		//System.out.println("Checking Hour: "+i+" "+j);
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
				//System.out.println("Added Hour: "+i+" "+j);
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

//Check Room_Booked Exception
public boolean validateRoomBookedConflict(int day,String startTime, String endTime){
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
	for(int i=startHour;i<=endHour;i++){
		//System.out.println("End Hour: *************************"+i);
		if(i==endHour){
			endingMinute=endMinute;
			startMinute=0;
			}
		for(int j=startMinute;j<endingMinute;j++){
			//System.out.println("Checking Hour: "+i+" "+j);
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
					//System.out.println("Added Hour: "+i+" "+j);
			}
		}	
		System.out.println("Successfully added:");
		return true;
	}
	else{
		System.out.println("Conflict: Exception:");
		return false;
	}


	}


/*
 * retrieve Hour and Minute
 */
public int[] getHourMinute(String time){
	int hourMinute[] = new int[2];
	hourMinute[0] = Integer.parseInt(time.substring(0, time.indexOf(":")));
	hourMinute[1] = Integer.parseInt(time.substring(time.indexOf(":")+1));
	System.out.println(hourMinute[0]+"  "+hourMinute[1]);
	return hourMinute;
}
/*
 * validate malformed Hour
 */
public static boolean validateMalFormedHours(int startHour,int endHour,int startMinute,int endMinute){

	if(startHour>endHour){return false;}
	else if(startHour==endHour && startMinute>=endMinute){return false;}
	else{
	return true;	
	}
}

public static void main(String args[]){
	InstructorServant i=new InstructorServant();
	
	
	//System.out.println("Testing-Create-Instructor"+ createInstructor("Create_Instructor\n234234\n4\n312-43-5454\ndan\nharkey\nsan jose su\nsan jose\nCA\n94132\nCMPE"));
	
	//Office-Hour Testing
	/*i.validateOfficeHourConflict(0,"09:20","08:20");
	i.validateOfficeHourConflict(0,"12:10","13:00");*/
	
	
	//Zip Testing
	/*if(i.validateInstructorZip("94138-4343")){System.out.println("true");}
	else{System.out.println("false");}
	*/
	
	//State Testing
	/*if(i.validateInstructorState("CA")){System.out.println("true");}
	else{System.out.println("false");}*/
	
	//Office Hour Syntax Checking
	/*if(add_hours("ENGR 232 MWF 2200-2300")){System.out.println("true");}
	else{System.out.println("false");}
	if(add_hours("ENGR 232 MWF 2200-2500")){System.out.println("true");}
	else{System.out.println("false");}*/
	
	//Add_Office_Hours
	//if((new InstructorServant()).Add_Office_Hours("Add_OfficeHour\n125345\n223456778\nENGR 232 M 0801-0905").equalsIgnoreCase("success")){System.out.println("true");}
	//else{System.out.println("false");}
	
	//Loading office Hour and Testing
	System.out.println("REPLY:"+ i.Add_Office_Hours("102\n102\n3\n123-45-5678\nENGR 233 TBA"));
	//i.removeOfficeHours("remove_hour\n322455\n223316769\nENGR 236 M 1220-1500");

	//223-31-6768
	//i.Add_Office_Hours("add_hors\n4245341\n2\n223-31-6768\nENGR 221 M 1220-1310");
	//System.out.println("REPLY:"+i.Get_Office_Hours("get_office_hours\n32423\n2\n223-31-6768"));
}

//Add Office Hour
public static int add_hours(String message,InstructorServant instructor){
	
	String[] inputString=getOfficeHourString(message);
	String building=inputString[0];
	String room=inputString[1];
	boolean room_booked_exception_flag=false;
	
	if(inputString[0]==null){
		System.out.println("Message format: missing something");
		//Create Exception for invalid message format:
		return Exceptions.malformed_message;
	}
	if((building.equalsIgnoreCase("TBA")) && !room.equalsIgnoreCase("TBA")){
		System.out.println("Invalid TBA with location:");
		return Exceptions.malformed_location;
	}
	
	if((!building.equalsIgnoreCase("TBA")) && room.equalsIgnoreCase("TBA")){
		System.out.println("Invalid TBA with location:");
		return Exceptions.malformed_location;
	}
	
	
	if(building.equalsIgnoreCase("TBA") && room.equalsIgnoreCase("TBA")){
		System.out.println("**********************HERE*********************");
		room_booked_exception_flag=true;
	}

	
	boolean dayTBA=true;
	boolean hoursTBA=true;
	String[][] days=null;
	if(inputString[2].equalsIgnoreCase("TBA")){dayTBA=false;}
	if(inputString[3].equalsIgnoreCase("TBA")){hoursTBA=false;}
	//else
	
	int noOfDays=inputString[2].length();
	
	if(dayTBA){
	if(noOfDays>7 || noOfDays<1){
		//Exception
		System.out.println("Exception: No Of days");
		return Exceptions.malformed_location;	
	}
	}
	
	days=getDays(inputString[2],noOfDays);
	if(dayTBA){
	if(days[0][0]==null){
		//Exception
		System.out.println("Days are not proper:");
		return Exceptions.malformed_location;
	}
	
	boolean checkOrderOfDays=validateOrderOfDays(days,noOfDays);
	if(!checkOrderOfDays){
		//Exception
		System.out.println("Days are not ordered in proper way:");
		return Exceptions.malformed_location;
	}
	}
	
	String[] validHours=null;
	
	validHours=validateHours(inputString[3]);
	if(hoursTBA){
	if(validHours[0]==null){
		return Exceptions.malformed_hours;
	}
	}
	
	
	if(!dayTBA)
	{
		noOfDays=1;
		days[0][0]=Integer.toString(-1);
	}
	
	if(!hoursTBA)
	{
		noOfDays=1;
		validHours[0]=Integer.toString(-1);
		validHours[1]=Integer.toString(-1);
	}
	
	if(!hoursTBA || !dayTBA){
	Instructor_DB.writeOfficeHours(instructor.getInstructorID(),Integer.parseInt(days[0][0]),validHours[0],validHours[1],building,room);
	return -1;
	}
	
	boolean finalValidation=true;
	System.out.println("**********************HIIII******************");
	//Check for Room_Booked Exception
	if(!room_booked_exception_flag)
	{
	try{
		for(int i=0;i<noOfDays;i++){
			finalValidation=(new InstructorServant()).validOfficeHoursRoom(building,(room),Integer.parseInt(days[i][1]),validHours[0],validHours[1]);
		if(!finalValidation){
			break;
		}
		}
	}
	catch(Exception e){return Exceptions.room_booked;}
	}

	int officeHourExceptionFlag=-1;
	try{
	for(int i=0;i<noOfDays;i++){
		officeHourExceptionFlag=instructor.validateOfficeHourConflict(Integer.parseInt(days[i][1]),validHours[0],validHours[1]);
		if(officeHourExceptionFlag!=-1){break;}
	}
	}
	catch(Exception e){
		System.out.println("Office Hours are invalid: 1st"+e);
		
		return Exceptions.schedule_conflict;
	}
	if(officeHourExceptionFlag!=-1){
		if(officeHourExceptionFlag==0){
			return Exceptions.malformed_hours;
		}
		else{
		return Exceptions.schedule_conflict;
		}
	}

	if(!finalValidation){
		return Exceptions.room_booked;
	}
	
	for(int i=0;i<noOfDays;i++){
		Instructor_DB.writeOfficeHours(instructor.getInstructorID(),Integer.parseInt(days[i][1]),validHours[0],validHours[1],building,room);	
	}
	
	return -1;
	
}

private static String[] validateHours(String hours) {

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


private static boolean checkRoomValid(String room) {
return true;
}
private static String[] getOfficeHourString(String message) {

	
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
	if(temp.equals("TBA")){inputString[3]="TBA";}
	else{inputString[3]=st.nextToken();}
	}
	
	catch(Exception e){
		if(inputString[2].equalsIgnoreCase("TBA")){inputString[2]="NO_CONFLICT";}
		else{inputString[0]=null;}
		}
	
	return inputString;
}
public boolean validateInstructorCity(String city){return true;}
//Validate the state of the Instructor Object
public boolean validateInstructorState(String state){
	
	boolean valid=false;
	for(int i=0;i<validStates.length;i++){
		if(validStates[i].equalsIgnoreCase(state)){
			valid=true;
			break;
		}
	}
	System.out.println("state :"+valid);
	if(!valid){
		exception_code=Exceptions.malformed_state;
	}
	return valid;
	}

//Validate Zip code of Instructor Object
public boolean validateInstructorZip(String zip){
	
	boolean valid=false;
	if(zip.length()<5){return false;}
	if(zip.contains("-") && zip.length()==10){
		StringTokenizer st=new StringTokenizer(zip,"-");
		String st1=st.nextToken();
		if(st1.length()!=5){return false;}
		String st2=st.nextToken();
		if(st2.length()!=4){return false;}
		if(!validInteger(st1)){return false;}
		if(!validInteger(st2)){return false;}
		return true;
		}
	else if(zip.length()==5){
		if(zip.length()!=5){return false;}
		if(!validInteger(zip)){return false;}
		return true;
	}
	else{return false;}
	
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
public boolean validateInstructorID(String instructorID2) {
	//Instructor id must not negative and string
	try{
		if(instructorID2.length()!=11){
			exception_code=Exceptions.malformed_id;
			return false;
		}
		
		StringTokenizer st=new StringTokenizer(instructorID2,"-");
		
		if(st.countTokens()!=3){return false;}
		
		String tempString="";
		tempString=st.nextToken();
		if(tempString.length()!=3 || Integer.parseInt(tempString)>1000){return false;}
		
		tempString=st.nextToken();
		if(tempString.length()!=2 || Integer.parseInt(tempString)>100){return false;}
		
		tempString=st.nextToken();
		if(tempString.length()!=4 || Integer.parseInt(tempString)>10000){return false;}
		
	}
	catch(Exception e){
		exception_code=Exceptions.malformed_id;
		return false;
	}
	return true;
	}

public String Add_Office_Hours(String message) {

	StringTokenizer st=new StringTokenizer(message,"\n");
	if(st.countTokens()!=5){
		st.nextToken();
		return st.nextToken()+"\n"+Exceptions.malformed_message;
	}
	st.nextToken();
	String requestID=st.nextToken();
	st.nextToken();
	String inID=st.nextToken();
	String locationOfOfficeHours=st.nextToken();
	String[][][] officeHours=Instructor_DB.loadOfficeHour(inID);
	InstructorServant instructor=loadOfficeHour(officeHours);
	instructor.setInstructorID(inID);
	System.out.println("office Hour");
	int exception_code=add_hours(locationOfOfficeHours,instructor);
	System.out.println("Exception code at add office Hour: "+exception_code);
	if(exception_code!=-1)
	{return ""+requestID+"\n"+exception_code;}
	else{return ""+requestID+"\n"+0+"\n"+0;}
	
}
private static InstructorServant loadOfficeHour(String[][][] officeHours) {
	
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


//Retrieve office_hours according to room from the database
public boolean validOfficeHoursRoom(String building,String roomNo,int day,String stime,String etime) {

	String[][][] officeHours=Instructor_DB.loadRoomOfficeHour(building,roomNo);
	boolean flag=loadRoomOfficeHour(officeHours,day,stime,etime);
	System.out.println("ROOM VACANCY: "+flag);
	return flag;
	
}


//Load office_Hour according to Room
private boolean loadRoomOfficeHour(String[][][] officeHours,int day,String stime,String etime) {
	
	InstructorServant instructor=new InstructorServant();
	//boolean conflict=true;
	int conflict=-1;
	try{
	{
		for(int j=0;j<officeHours.length;j++){
			System.out.println("Servant :"+officeHours[j][0][0]+"   "+officeHours[j][1][0]+"  "+officeHours[j][2][0]);
			conflict=instructor.validateOfficeHourConflict(Integer.parseInt(officeHours[j][0][0]),officeHours[j][1][0], officeHours[j][2][0]);
			if(conflict!=-1){break;}
		}
	}
	}
	catch(Exception e){
	//Exception
		return false;
	}
	conflict=instructor.validateOfficeHourConflict(day,stime,etime);
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


public String Remove_Office_Hours(String message){
	StringTokenizer st=new StringTokenizer(message,"\n");
	if(st.countTokens()!=5){
		st.nextToken();
		return st.nextToken()+"\n"+Exceptions.malformed_message;
	}
	
	int exception=removeOfficeHours(message);
	String[] inputString=null;
	try{
		inputString=getParameterOfRemoveOfficeHours(message);
	}
	catch(Exception e){}
	if(exception==-1){
		return inputString[0]+"\n"+0+"\n"+0;
	}
	else{
		return inputString[0]+"\n"+exception;
	}
}

// Remove Office Hours

public int removeOfficeHours(String message){
	try{
	String[] inputString=getParameterOfRemoveOfficeHours(message);
	String requestID=inputString[0];
	String i_id=inputString[1];
	String building=inputString[2];
	String room=inputString[3];
	String day=(inputString[4]);
	String hours=inputString[5];
	
	if(inputString[1]==null){
		System.out.println("Message format: missing something");
		return 102;
	}
	if((building.equalsIgnoreCase("TBA")) && !room.equalsIgnoreCase("TBA")){
		System.out.println("Invalid TBA with location:");
		return Exceptions.malformed_location;
	}
	if((!building.equalsIgnoreCase("TBA")) && room.equalsIgnoreCase("TBA")){
		System.out.println("Invalid TBA with location:");
		return Exceptions.malformed_location;
	}
	
	
		int noOfDays=day.length();
		if(noOfDays>7 || noOfDays<1){
			//Exception
			System.out.println("Exception: No Of days");
			return Exceptions.malformed_location;	
		}
		String[][] days=getDays(day,noOfDays);
		if(days[0][0]==null){
			//Exception
			System.out.println("Days are not proper:");
			return Exceptions.malformed_location;
		}
		
		boolean checkOrderOfDays=validateOrderOfDays(days,noOfDays);
		if(!checkOrderOfDays){
			//Exception
			System.out.println("Days are not ordered in proper way:");
			return Exceptions.malformed_location;
		}
		String[] validHours=validateHours(inputString[5]);
		System.out.println("HOURS: "+inputString[5]);
		if(validHours[0]==null){
			return Exceptions.malformed_hours;
		}
		boolean finalValidation=true;
		try{
		//	InstructorServant instructor=new InstructorServant();
		for(int i=0;i<noOfDays;i++){
			finalValidation=Instructor_DB.deleteOfficeHour(i_id,building,room,Integer.parseInt(days[i][1]),validHours[0],validHours[1]);
			if(!finalValidation){
				System.out.println("No Such Office Hour:");
				break;
				}
		}
		}
		catch(Exception e){
			System.out.println("Office Hours are invalid: 2nd"+e);
			return Exceptions.no_such_office_hours;
		}
		
		if(!finalValidation){return Exceptions.no_such_office_hours;}
		System.out.println("Final :"+finalValidation+" \n\n Order ="+checkOrderOfDays);
		for(int i=0;i<noOfDays;i++){
			Instructor_DB.writeDeleteOfficeHours(building,(room),i_id,Integer.parseInt(days[i][1]),validHours[0],validHours[1]);	
		}
		
		return -1;
	
	
	}
	catch(Exception e){}

	return -1;
}
private String[] getParameterOfRemoveOfficeHours(String message) {
	
	String[] inputString=new String[6];
	
try{	
	StringTokenizer st=new StringTokenizer(message,"\n");
	st.nextToken();
	inputString[0]=st.nextToken();
	st.nextToken();
	inputString[1]=st.nextToken();
	StringTokenizer st1=new StringTokenizer(st.nextToken()," ");
	inputString[2]="";
	int length=st1.countTokens()-3;
	for(int i=0;i<length;i++){
		inputString[2]+=st1.nextToken()+" ";
	}
	inputString[2]=inputString[2].trim();
	inputString[3]=st1.nextToken();
	inputString[4]=st1.nextToken();
	inputString[5]=st1.nextToken();
}
catch(Exception e){
	System.out.println("Exception at remove office Hour 2:");
	inputString[1]=null;
}
	return inputString;
}
public String Get_Office_Hours(String message) {

	String reply=null;
	String requestID="";
	String instructorID="";
	try{	
		StringTokenizer st=new StringTokenizer(message,"\n");
		if(st.countTokens()!=4){
			st.nextToken();
			return st.nextToken()+"\n"+Exceptions.malformed_message;
		}
		st.nextToken();
		requestID=st.nextToken()+"\n";
		st.nextToken();
		instructorID=st.nextToken();
		System.out.println("RequestID"+requestID+" Instructor_ID"+instructorID);
		
	}
	catch(Exception e){
		System.out.println("Exception at remove office Hour 2:");
		//Exception
		return requestID+"\n"+Exceptions.malformed_message;
	}
	
	try{
	System.out.println("Before:");
	reply=requestID+0+"\n"+Instructor_DB.getOfficeHours(instructorID);
	System.out.println("After:"+reply);
	}
	catch(Exception e){
		return requestID+"\n"+Exceptions.malformed_message; 
	}
	return reply;
}
public String Set_Department(String message) {
	
	String reply=null;
	String instructorId="";
	String requestId=null;
	String newDepartmentName=null;
	try{	
		StringTokenizer st=new StringTokenizer(message,"\n");
		if(st.countTokens()!=5){
			st.nextToken();
			return st.nextToken()+"\n"+Exceptions.malformed_message;
		}
		st.nextToken();
		requestId=st.nextToken();
		st.nextToken();
		instructorId=(st.nextToken());
		newDepartmentName=st.nextToken();
		
	}
	catch(Exception e){
		System.out.println("Exception at remove office Hour 2:");
		//Exception
		return requestId;
	}
	try{
		
		Instructor_DB.setDepartment(instructorId,newDepartmentName);
		reply=requestId+"\n"+0+"\n"+0;
		System.out.println("Server reply :"+reply);
		return reply;
		}
		catch(Exception e){
			return reply; 
		}
	}
public String Get_Department(String message) {
	
	String[] inputString=new String[2];
	String reply="";
	try{	
		StringTokenizer st=new StringTokenizer(message,"\n");
		if(st.countTokens()!=4){
			st.nextToken();
			return st.nextToken()+"\n"+Exceptions.malformed_message;
		}
		st.nextToken();
		inputString[0]=st.nextToken();
		st.nextToken();
		inputString[1]=st.nextToken();
		reply=inputString[0];
		
	}
	catch(Exception e){
		return inputString[0]+"\n"+Exceptions.malformed_message+inputString[0];
	}
	
	try{
	String i_id=inputString[1];
	
	String deptName=Instructor_DB.getDepartment(i_id);
	if(deptName==null){reply+="\n"+Exceptions.no_such_person;}
	else{reply+="\n"+0+"\n"+1+"\n"+deptName;}
	}
	catch(Exception e){
		return reply; 
	}
	return reply;
}

public boolean checkMalformedHours(String days){
	
	int flagValidDays=0;
	for(int i=0;i<days.length();i++){
	String day=days.substring(0,i+1);
	flagValidDays=0;	
	for(int j=0;j<7;j++){
		if(day.equals(validDays[j])){
			flagValidDays=1;
			break;
		}
	}
	if(flagValidDays==0){break;}
	}
	
	if(flagValidDays==0){return false;}
	else return true;
}

public String Find_Instructors_By_Department(String message) {

	String[] inputString=new String[2];
	String instructorIDList=null;
	try{	
		StringTokenizer st=new StringTokenizer(message,"\n");
		if(st.countTokens()!=4){
			st.nextToken();
			return st.nextToken()+"\n"+Exceptions.malformed_message;
		}
		st.nextToken();
		inputString[0]=st.nextToken();
		st.nextToken();
		inputString[1]=st.nextToken();
		instructorIDList=inputString[0]+"\n"+0+"\n";
	}
	catch(Exception e){
		System.out.println("Exception at Find office Hours:");
		//Exception
		return inputString[0]+"\n"+0;
	}
	System.out.println(" request ID:"+inputString[0]+"  dept :"+inputString[1]+"  message:"+message);
	try{
	instructorIDList+=Instructor_DB.getInstructorByName(inputString[1]);
	}
	catch(Exception e){
		return inputString[0]+"\n"+0;
	}
	return instructorIDList;

	}

}
