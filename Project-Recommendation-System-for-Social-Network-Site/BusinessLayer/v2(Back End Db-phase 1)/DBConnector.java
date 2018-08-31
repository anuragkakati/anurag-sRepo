import java.net.UnknownHostException;
import java.util.Scanner;

import com.mongodb.DB;
import com.mongodb.MongoClient;


public class DBConnector {
	static MongoClient server ;
	static DB db;
	public DBConnector(){
		try {
			server = new MongoClient( "localhost" , 27017 );
			db = server.getDB("FyProDB");
			
			System.out.println("Server connected to Database");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	public static DB getDatabase(){
		return db;
	}
}
		
		
