import java.util.Scanner;

import com.mongodb.BasicDBObject;


public class MainPage {
	public MainPage(){
		Scanner scan = new Scanner(System.in);
		System.out.println("PAGE : \n1.SignUP\n2.LoginIn");
		int choice = scan.nextInt();
		switch(choice){
			case 1: System.out.println("\n-- Main Page --");
					SignUpClass signUp = new SignUpClass();
					signUp.getDetails();
					signUp.insertDocument();
			case 2:	System.out.println("\n-- Login Page -- ");
					LoginClass login = new LoginClass();
					BasicDBObject userFound;
					if((userFound=login.authenticate())!=null){
						WallClass wall = new WallClass();
						wall.displayWall(userFound);
					}else{
						System.out.println("Login Unsuccessful");
					}
					break;
			default:
					System.out.println("Baba ji ka thullu");
		}
	}
}
