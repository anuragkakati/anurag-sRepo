package pack;
import java.util.Scanner;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;


public class SignUpClass {
	String userName, password, givenName, dob, hometown, gender, phone, profession ,wallID ; // This will be "_id"
	int age;
	Scanner scan;
	DataBaseConnector connect;// = new DataBaseConnector();
	DB db;
	public SignUpClass(String userName2, String password2, String givenName2, String dob2, int age2, String hometown2, String gender2, String phone2, String profession2) {
		connect = new DataBaseConnector();
		userName = userName2;
		System.out.println("Class me USERNAME : "+userName);
		password = password2;
		givenName = givenName2;
		dob = dob2;
		age = age2;
		hometown = hometown2;
		gender = gender2;
		phone = phone2;
		profession = profession2;
		wallID = userName.substring(0, userName.length()-2)+"Wall";
		insertDocument();
	}

	public void insertDocument(){
		
		db = DataBaseConnector.getDatabase();
		DBCollection userProfileColl = db.getCollection("userProfile");
		BasicDBObject newUser = new BasicDBObject("_id", userName)
			.append("password", password)
			.append("givenName",givenName)
			.append("DOB", dob)
			.append("age", age)
			.append("friendList", new BasicDBList())
			.append("hometown", hometown)
			.append("profession", profession)
			.append("phoneNo", phone)
			.append("wallID",wallID);
		System.out.println("Under class UserName : "+userName+"\tphoneno : "+phone);
		userProfileColl.insert(newUser);
		
		DBCollection registeredUserColl = db.getCollection("registeredUser");
		BasicDBObject regUser = new BasicDBObject("_id", userName).append("isOnline","false");
		registeredUserColl.insert(regUser);
		
		DBCollection userTestWall = db.getCollection("userTestWall");
		BasicDBObject userBlankWall = new BasicDBObject("_id",userName)
			.append("wallID", wallID)
			.append("postList", new BasicDBList())
			.append("statusList", new BasicDBList());
		userTestWall.insert(userBlankWall);
		
		DBCollection userTestChatBox = db.getCollection("userTestChatBox");
		BasicDBObject userBlankChatBox = new BasicDBObject("_id",userName)
			.append("chatBox", new BasicDBList());
		userTestChatBox.insert(userBlankChatBox);
	}
		
		
		
		
		
		
		
		
	
}
