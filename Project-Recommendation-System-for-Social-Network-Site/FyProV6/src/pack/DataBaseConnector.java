package pack;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;


public class DataBaseConnector {
	static MongoClient server ;
	static DB db;
	public DataBaseConnector(){
		try {
			server = new MongoClient( "localhost" , 27017 );
			db = server.getDB("FyProDB");
			
			//System.out.println("Server connected to Database");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	public static DB getDatabase(){
		return db;
	}
}
		
		
