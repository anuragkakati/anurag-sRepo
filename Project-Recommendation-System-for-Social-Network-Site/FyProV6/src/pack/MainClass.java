package pack;

public class MainClass {

	public static void main(String[] args) {
		MainClass mainObject = new MainClass();
		mainObject.connectToDB();
		mainObject.callMainPage();
	}
	public void connectToDB(){
		DataBaseConnector connect = new DataBaseConnector();
	}
	public void callMainPage(){
		MainPage page = new MainPage();
	}

}
