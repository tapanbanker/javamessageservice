package Servant;

import GUI.*;
import Main.*;
import Misc.*;
import Conn.*;
import Servant.*;

import java.util.Hashtable;
import java.util.StringTokenizer;

import Conn.*;

public class StudentServant {

	public static String validStates[] = { "AL", "AK", "AZ", "AR", "CA", "CO", "CT",
		"DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY",
		"LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE",
		"NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR",
		"PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA",
		"WV", "WI", "WY"};
	public static int exception_code=-1;
	
	public String enrollStudentInCourse(String message)  
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		System.out.println(inputString.length);
		if(inputString.length !=6)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Student_DB.enrollStudentInCourse(inputString[3],inputString[4],inputString[5]);
		System.out.println(reply);
		return reply;
	}
	
	public String unenrollStudentFromCourse(String message)  
	{
		
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length !=6)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Student_DB.unenrollStudentFromCourse(inputString[3],inputString[4],inputString[5]);
		
		return reply;
	}
	
	public String getEnrolledUnits(String message)  
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length !=4)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Student_DB.getEnrolledUnits(inputString[3]);
		
		return reply;
	}
	
	
	public String getStudentsEnrolledInCourse(String message)  
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length !=5)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Student_DB.getStudentsEnrolledInCourse(inputString[3],inputString[4]);
		
		return reply;
	}
	
	public String removeStudent(String message)  
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length !=5 || (Integer.parseInt(inputString[4]) !=0 && Integer.parseInt(inputString[4]) != 1))
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Student_DB.removeStudent(inputString[3],inputString[4]);
		
		return reply;
	}
	
	public String calculateBill(String message)  
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length !=4)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Student_DB.calculateBill(inputString[3]);
		
		return reply;
	}
	
	
	
	public static String createStudent(String message)
	{
		
		String[] inputString = message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length !=10)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Student_DB.insertStudent(inputString[3],inputString[4],inputString[5],inputString[6],inputString[7],inputString[8],inputString[9]);
		
		return reply;
	}
}

