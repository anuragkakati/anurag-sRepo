import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Scanner;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;


public class ServerClass {
	static MongoClient server ;
	static DB db;
	Scanner scan = new Scanner(System.in);
	public ServerClass(){
		try {
			server = new MongoClient( "localhost" , 27017 );
			db = server.getDB( "FinalYearDB" );
			
			System.out.println("Server connected to Database");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	public boolean checkAuthentication(String uname, String passwd) {
		 DBCollection coll = db.getCollection("userProfile");
		 BasicDBObject query = new BasicDBObject("_id",uname);		//would be checked in registerUser Collection
		 DBCursor cursor = coll.find(query);
		 if(cursor.hasNext()){
			 BasicDBObject userLoggedIn = (BasicDBObject) cursor.next();
			 if(userLoggedIn.get("password").equals(passwd)){
				 System.out.println("Login Successful...Enjoy the Dive");
				 return true;
			 }
		 }
		 System.out.println("Wear Diving Suit Correctly");
		 return false;
	}
	public void createUserProfile() {
		DBCollection coll = db.getCollection("userProfile");
		BasicDBObject newUser = new BasicDBObject();
		System.out.print("Enter Username(ID) : ");
		Scanner scan = new Scanner(System.in);
		String uname = scan.next();
		newUser.put("_id", uname);
		System.out.print("\nWhat would be your key : ");
		String passwd = scan.next();
		newUser.put("password", passwd);
		System.out.print("\nWhat do you like to be called : ");
		String gname = scan.next();
		newUser.put("givenName", gname);
		System.out.print("\nAddress : ");
		String address= scan.next();
		newUser.put("address", address);
		//List<BasicDBObject> friendList = new ArrayList<>();
		BasicDBList friendList = new BasicDBList();
		newUser.put("friendList", friendList);
		newUser.put("wallID", uname.subSequence(0, uname.length()-2)+"wall");
		coll.save(newUser);
	}
	public void showProfile(String uname) {
		DBCollection coll = db.getCollection("userProfile");
		BasicDBObject query = new BasicDBObject("_id",uname);
		DBCursor cursor = coll.find(query);	
		BasicDBObject user = (BasicDBObject) cursor.next();
		System.out.println("Given Name :" + user.getString("givenName"));
		System.out.println("Address :" + user.getString("address"));
		System.out.println("Friends : ");
		BasicDBList friendList = (BasicDBList) user.get("friendList");
		Iterator it = friendList.iterator();
		while(it.hasNext()){
			BasicDBObject friend = (BasicDBObject) it.next();
			//System.out.println(friend);
			System.out.println("Friend : "+friend.getString("friendId"));
		}
	
	}
		
	
	
/*	{
		friendList:[{
					friendId:"SachinID"
				},
				{
					friendID:"AnuragID"
				}
			]
		
	}
	
	db.userProfile.update({_id:"sachinID"},{$addToSet:{"friendList":{"friendId":"unknownID"}});

	
*/
	public void sendFriendRequest(String uname) {
		System.out.print("\nEnter Friend Name ");
		String friendID = scan.next();
		BasicDBObject friendObject = new BasicDBObject();
		friendObject.put("friendId", friendID);
		DBCollection coll = db.getCollection("userProfile");
		BasicDBObject query = new BasicDBObject("_id",uname);
		DBCursor cursor = coll.find(query);	
		BasicDBObject query1 = new BasicDBObject();
		query1.put("friendList", friendObject);
		BasicDBObject query2 = new BasicDBObject();
		query2.put("$addToSet", query1);
		coll.update(query, query2 );
		
//		BasicDBObject user = (BasicDBObject) cursor.next();
//		
//		BasicDBList userFriends = (BasicDBList) user.get("friendList");
//		userFriends.add(friendObject);
		
	}

}
