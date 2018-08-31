import java.util.Scanner;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;


public class SignUpClass {
	String userName, password, givenName, dob, hometown, gender, phone, profession ,wallID ; // This will be "_id"
	int age;
	Scanner scan;
	DB db;
	public void getDetails(){
		scan = new Scanner(System.in);
		System.out.println("userName : ");
		userName = scan.next();
		System.out.println("password : ");
		password = scan.next();
		System.out.println("givenName : ");
		givenName = scan.next();
		System.out.println("dob : ");
		dob = scan.next();
		System.out.println("hometown : ");
		hometown = scan.next();
		System.out.println("age : ");
		age = scan.nextInt();
		System.out.println("gender : ");
		gender = scan.next();
		System.out.println("Phone no : ");
		phone = scan.next();
		System.out.println("profession : ");
		profession = scan.next();
		wallID = userName.substring(0, userName.length()-2)+"Wall";
	}
	public void insertDocument(){
		db = DBConnector.getDatabase();
		DBCollection userProfileColl = db.getCollection("userProfile");
		BasicDBObject newUser = new BasicDBObject("_id", userName)
			.append("password", password)
			.append("givenName",givenName)
			.append("DOB", dob)
			.append("age", age)
			.append("profession", profession)
			.append("phoneNo", phone)
			.append("wallID",wallID);
		userProfileColl.insert(newUser);
		
		DBCollection registeredUserColl = db.getCollection("registeredUser");
		BasicDBObject regUser = new BasicDBObject("_id", userName).append("isOnline","false");
		registeredUserColl.insert(regUser);
	}
		
		
		
		
		
		
		
		
	
}
