package pack;
import java.util.Scanner;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteResult;


public class LoginClass {
	String uName, uPass;
	Scanner scan;
	DB db;
	public LoginClass(){
		
	}
	public LoginClass(String userName, String password) {
		DataBaseConnector connect = new DataBaseConnector();
		uName = userName;
		uPass = password;
	}
	public BasicDBObject authenticate(){
		db = DataBaseConnector.getDatabase();

		BasicDBObject query = new BasicDBObject("_id",uName);
		DBCollection accessedColl = db.getCollection("registeredUser");
		DBCursor cursor = accessedColl.find(query);
		if(cursor.hasNext()){
			accessedColl = db.getCollection("userProfile");
			query = new BasicDBObject("_id",uName).append("password", uPass);
			cursor = accessedColl.find(query);
			if(cursor.hasNext()){
				System.out.println("Login Succesful");
				query = new BasicDBObject("_id",uName);
				BasicDBObject update = new BasicDBObject("$set",new BasicDBObject("isOnline","true"));
				accessedColl = db.getCollection("registeredUser");
				WriteResult wr  = accessedColl.update(query, update);
				return (BasicDBObject) cursor.next();
			}
		}
		return null;
	}
	public void logOut(String loggedInUser){
		db = DataBaseConnector.getDatabase();
		BasicDBObject findQuery = new BasicDBObject("_id",loggedInUser);
		BasicDBObject updateQuery = new BasicDBObject("$set",new BasicDBObject("isOnline","false"));
		DBCollection registeredUser = db.getCollection("registeredUser");
		registeredUser.update(findQuery, updateQuery);
	}
}
