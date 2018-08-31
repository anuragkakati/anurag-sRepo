import java.net.UnknownHostException;
import java.util.Scanner;

import com.mongodb.DB;
import com.mongodb.MongoClient;


public class MongoClientClass {
	public static void main(String[] args) {
		try {
			ServerClass server = new ServerClass();
			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "FinalYearDB" );
			
			System.out.println("Connected to Server Database ");
			System.out.println("1. Login 2. Register");
			
			Scanner scan = new Scanner(System.in);
			int choice = scan.nextInt();
			switch(choice){
				case 1:	System.out.println("\t--Login Page--");
						System.out.print("\nLogin name :");
						String uname = scan.next();
						System.out.print("\nPassword :");
						String passwd = scan.next();
						if(server.checkAuthentication(uname,passwd)){
							System.out.print("\n---Functionalities of User----");
							System.out.print("\n1. Show Profile\n2. Send Friend Request \n3.Show Wall");
							int innerChoice =scan.nextInt();
							switch(innerChoice){
								case 1:	server.showProfile(uname);
										break;
								case 2:	server.sendFriendRequest(uname);
										break;
										
							}
						}
						break;
				case 2:	System.out.println("\t--Register Page--");
						server.createUserProfile();
						break;
			}
			
			
			
			
			
			
			
			
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
}
