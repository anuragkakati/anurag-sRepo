package pack;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class ChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public RequestDispatcher rd;
	String service;
	ChatClass chat = new ChatClass();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("under ChatServlet");
		service = request.getParameter("service");
		System.out.println("service : "+service);
		HttpSession session = request.getSession();
		BasicDBObject userFound = (BasicDBObject) session.getAttribute("userFound");
		String friendName = request.getParameter("friend");
		BasicDBObject friendFound = null;
		DB db = DataBaseConnector.getDatabase();
		DBCollection coll = db.getCollection("userProfile");
		BasicDBObject findQuery = new BasicDBObject("_id",friendName);
		DBCursor cur = coll.find(findQuery);
		if(cur.hasNext()){
			friendFound = (BasicDBObject) cur.next();
		}
		session.setAttribute("friendFound", friendFound);
		session.setAttribute("userFound", userFound);
		//BasicDBObject friend = (BasicDBObject) session.getAttribute("friend");
		if(service.equals("showFriendProfile")){
			System.out.println("Reached else if. Forwarding request to friendProfile.jsp");
			RequestDispatcher rd = request.getRequestDispatcher("friendProfile.jsp");
			rd.forward(request, response);
		}
		else if(service.equals("showChatBox")){
			db = DataBaseConnector.getDatabase();
			coll = db.getCollection("userTestChatBox");
			findQuery = new BasicDBObject("_id",userFound.getString("_id"))
				.append("chatBox._id",friendName);
			BasicDBObject projectQuery = new BasicDBObject("chatBox.$",1); 
			cur = coll.find(findQuery,projectQuery);
			BasicDBObject userChatBox,friendChatBox,chatThread;
			BasicDBList chatLists,sessionList;
			
			if(cur.hasNext()){
				userChatBox = (BasicDBObject) cur.next();
				chatLists = (BasicDBList) userChatBox.get("chatBox");
				chatThread = (BasicDBObject) chatLists.get(0);
				
				sessionList = (BasicDBList) chatThread.get("session");
				System.out.println(sessionList);
				session.setAttribute("sessionList", sessionList);

				RequestDispatcher rd = request.getRequestDispatcher("chatBox.jsp");
				rd.forward(request, response);
		
			}
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("under ChatServlet");
		String service = request.getParameter("service");
		HttpSession session = request.getSession();
		BasicDBObject userFound = (BasicDBObject) session.getAttribute("userFound");
		session.setAttribute("userFound", userFound);
		String friend = request.getParameter("friend");
		if(service.equals("sendMsg")){
			String msg = request.getParameter("postedMsg");
			LocalDateTime msgTime = LocalDateTime.now() ;
			System.out.println("Sender Object :");
			chat.updateUserInbox(msg,msgTime.toString(),userFound.getString("_id"),friend,"s");
			chat.updateUserInbox(msg, msgTime.toString(), friend, userFound.getString("_id"),"r");
			
			//service="showChatBox";
			
			DB db = DataBaseConnector.getDatabase();
			DBCollection coll = db.getCollection("userTestChatBox");
			BasicDBObject findQuery = new BasicDBObject("_id",userFound.getString("_id"))
				.append("chatBox._id",friend);
			BasicDBObject projectQuery = new BasicDBObject("chatBox.$",1); 
			DBCursor cur = coll.find(findQuery,projectQuery);
			BasicDBObject userChatBox,friendChatBox,chatThread;
			BasicDBList chatLists,sessionList;
			
			if(cur.hasNext()){
				userChatBox = (BasicDBObject) cur.next();
				chatLists = (BasicDBList) userChatBox.get("chatBox");
				chatThread = (BasicDBObject) chatLists.get(0);
				
				sessionList = (BasicDBList) chatThread.get("session");
				System.out.println(sessionList);
				session.setAttribute("sessionList", sessionList);

				RequestDispatcher rd = request.getRequestDispatcher("chatBox.jsp");
				rd.forward(request, response);
		
			}
			
			//doGet(request, response);	
				
				/*
				 * For storing msgs into another array within another array
				 * db.userTestChatBox.update({_id:"sonyID","chatBox._id":3},{$push:{"chatBox.$.session":{_id:1,"msgContent":"I sent msg to other friend through mongoShell","time":ISODate(),"type":"s"}}});
				 */
				
				
				
		}
	}
}
