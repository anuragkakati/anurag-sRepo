
public class MainClass {

	public static void main(String[] args) {
		MainClass mainObject = new MainClass();
		mainObject.connectToDB();
		mainObject.callMainPage();
	}
	public void connectToDB(){
		DBConnector connect = new DBConnector();
	}
	public void callMainPage(){
		MainPage page = new MainPage();
	}

}
