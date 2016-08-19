package Misc;
import java.util.*;
import java.sql.*; 

 public class dbConnectionPool implements Runnable
 {
	
	    private static int totalConnectionCount = 7;    
	    private static Vector connectionsAvailable = new Vector();
	    private static Vector connectionsUsed = new Vector();
	    private static String url = null;
	    private static Thread cleaningThread = new Thread();
	        
	                                              
	    public static void  EstablishConnectionPool(String urlString)
	    {
	    	
	        url = urlString;
	        for(int cnt=0; cnt<totalConnectionCount; cnt++)
	        {
	        	try{
	        		connectionsAvailable.addElement(getConnection());
	        	}catch(SQLException e)
	        	{
	        		System.out.print("sql exception from EstablishConnectionPool : "+e.getMessage());
	        	}
	        	
	        }
	         
	        cleaningThread.start();
	    }
	    private static Connection getConnection() throws SQLException
	    {
	        return DriverManager.getConnection(url);
	    }
	    
	    public static synchronized Connection getConnectionFromPool() throws SQLException
	    {
	        Connection connect = null;
	        
	        if(connectionsAvailable.size() == 0)
	        {
	           
	        	connect = getConnection();
	             connectionsUsed.addElement(connect);
	           
	        }
	        else
	        {
	        	connect = (Connection)connectionsAvailable.lastElement();
	            connectionsAvailable.removeElement(connect);
	            connectionsUsed.addElement(connect);            
	        }        
	        
	        
	        return connect;
	    }
	    

	    public static synchronized void returnConnectionToPool(Connection c)
	    {
	        if(c != null)
	        {
	            connectionsUsed.removeElement(c);
	            connectionsAvailable.addElement(c);        
	        }
	    }            
	    
	    public static int getAvailableConnectionCount()
	    {
	        return connectionsAvailable.size();
	    }
	    
	    public void run()
	    {
	        try
	        {
	            while(true)
	            {
	                synchronized(this)
	                {
	                    while(connectionsAvailable.size() > totalConnectionCount)
	                    {
	                       Connection c = (Connection)connectionsAvailable.lastElement();
	                       connectionsAvailable.removeElement(c);
	                       c.close();
	                    }
	                 
	                }
	                
	                System.out.println("available connections : " + getAvailableConnectionCount());
	                
	                
	                Thread.sleep(60000);
	            }    
	        }
	        catch(SQLException sqle)
	        {
	            sqle.printStackTrace();
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	    }
	}
 

	