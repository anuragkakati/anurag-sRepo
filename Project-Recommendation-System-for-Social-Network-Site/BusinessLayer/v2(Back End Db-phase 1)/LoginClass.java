import java.util.Scanner;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.WriteResult;


public class LoginClass {
	Scanner scan;
	DB db;
	public BasicDBObject authenticate(){
		db = DBConnector.getDatabase();
		scan = new Scanner(System.in);
		System.out.println("Username : ");
		String uName = scan.next();
		System.out.println("Password : ");
		String uPass = scan.next();
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
				/*db.registeredUser.update(
					{_id:uName},
					{$set:{isOnline:"true"}
					);
				*/
				BasicDBObject update = new BasicDBObject("$set",new BasicDBObject("isOnline","true"));
				accessedColl = db.getCollection("registeredUser");
				WriteResult wr  = accessedColl.update(query, update);
				return (BasicDBObject) cursor.next();
			}
		}
		
		return null;
	}
}
