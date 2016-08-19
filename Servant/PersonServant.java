package Servant;

import Conn.Person_DB;
import GUI.*;
import Main.*;
import Misc.*;
import Conn.*;
import Servant.*;

//import Misc.Exceptions;



public class PersonServant {

	public String findAllPersons(String message)  
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length != 4)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Person_DB.findAllPersons(inputString[3]);
		
		return reply;	
	}
	
	
	public PersonServant(){}
	
	
	
	public String findPersonsByName(String message) 		//need to confirm this
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length != 5)
		{
			System.out.println("it is getting failed here");
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		}
		else
		{
			reply = inputString[1]+"\n"+Person_DB.findPersonsByName(inputString[3],inputString[4]);
		}
		return reply;	
	}
	public String findPersonsByCity(String message) 		//need to confirm this
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length != 5)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Person_DB.findPersonsByCity(inputString[3],inputString[4]);
		
		return reply;	
	}
	public String findPersonsByState(String message) 		//need to confirm this
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length != 5)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Person_DB.findPersonsByState(inputString[3],inputString[4]);
		
		return reply;	
	}
	public String findPersonsByZip(String message) 		//need to confirm this
	{
		String[] inputString=message.split("\n");
		String reply = null;
		System.out.println("Input string");
		for(int i=0;i<inputString.length;i++){
			System.out.println(inputString[i]);
		}
		
		if(inputString.length != 5)
			reply = inputString[1]+"\n"+Integer.toString(Exceptions.malformed_message);
		else
			reply = inputString[1]+"\n"+Person_DB.findPersonsByZip(inputString[3],inputString[4]);
		
		return reply;	
	}

	//getters
	public String getFirstName( String message )
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
			reply = inputString[1]+"\n"+Person_DB.getXXX(inputString[3],"fname");
		
		return reply;	
	}
	public String getLastName( String message )
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
			reply = inputString[1]+"\n"+Person_DB.getXXX(inputString[3],"lname");
		
		return reply;	
	}
	public String getAddress( String message )
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
			reply = inputString[1]+"\n"+Person_DB.getXXX(inputString[3],"address");
		
		return reply;	
	}
	public String getCity(String message)
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
			reply = inputString[1]+"\n"+Person_DB.getXXX(inputString[3],"city");
		
		return reply;	
	}
	public String getState(String message)
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
			reply = inputString[1]+"\n"+Person_DB.getXXX(inputString[3],"state");
		
		return reply;
	}
	public String getZipCode(String message)
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
			reply = inputString[1]+"\n"+Person_DB.getXXX(inputString[3],"zip");
		
		return reply;
	}

	public String getID(String message)
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
			reply = inputString[1]+"\n"+Person_DB.getXXX(inputString[3],"p_id");
		
		return reply;
	}
	//Setters
	public String setFirstName(String message){
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
			reply = inputString[1]+"\n"+Person_DB.setXXX(inputString[3],inputString[4],"fname");
		
		return reply;	
	}
	
	
	public String setLastName(String message){
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
			reply = inputString[1]+"\n"+Person_DB.setXXX(inputString[3],inputString[4],"lname");
		
		return reply;
	}
	
	public String setAddress(String message){
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
			reply = inputString[1]+"\n"+Person_DB.setXXX(inputString[3],inputString[4],"address");
		
		return reply;
	}
	
	public String setCity(String message){
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
			reply = inputString[1]+"\n"+Person_DB.setXXX(inputString[3],inputString[4],"city");
		
		return reply;
	}
	
	public String setState(String message){
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
			reply = inputString[1]+"\n"+Person_DB.setXXX(inputString[3],inputString[4],"state");
		
		return reply;
	}
	
	public String setZipCode(String message){
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
			reply = inputString[1]+"\n"+Person_DB.setXXX(inputString[3],inputString[4],"zip");
		
		return reply;
	}
	
	public String setID(String message){
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
			reply = inputString[1]+"\n"+Person_DB.setXXX(inputString[3],inputString[4],"id");
		
		return reply;
	}
	
}