package pack;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

@WebServlet("/WallServlet")
public class WallServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String service = request.getParameter("service");
		WallClass wall = new WallClass();
		System.out.println("In doGet of WallServlet");
		if(service.equals("addFriend")){
			String friendID = request.getParameter("friendName");
			String userID = request.getParameter("username");
			wall.addFriend(userID,friendID);
			
			
			BasicDBObject findQuery = new BasicDBObject("_id",userID);
			DataBaseConnector connect = new DataBaseConnector();
			DB db = DataBaseConnector.getDatabase();
			DBCollection userProfile = db.getCollection("userProfile");
			DBCursor cur = userProfile.find(findQuery);
			BasicDBObject userFound = (BasicDBObject) cur.next();
			request.setAttribute("userFound", userFound);
			RequestDispatcher rd =request.getRequestDispatcher("userProfile.jsp");  
	        rd.forward(request, response);
		}else if(service.equals("addPost")){
			BasicDBObject user = new BasicDBObject();
			//user = (BasicDBObject) request.getAttribute("userWhoPosted");
			HttpSession ses = request.getSession();
			user = (BasicDBObject) ses.getAttribute("userWhoPosted");
			if(user==null)
				System.out.println("User who POsted could not be transferred");
			else{
				String postedText = request.getParameter("postedText");
				wall.addPost(user,postedText);
			}
			request.setAttribute("userFound", user);
			RequestDispatcher rd =request.getRequestDispatcher("userProfile.jsp");  
	        rd.forward(request, response);	
	        
		}else if(service.equals("addStatus")){
			BasicDBObject user = new BasicDBObject();
			//user = (BasicDBObject) request.getAttribute("userWhoPosted");
			HttpSession ses = request.getSession();
			user = (BasicDBObject) ses.getAttribute("userWhoPosted");
			if(user==null)
				System.out.println("User who Updated Status could not be transferred");
			else{
				String postedText = request.getParameter("postedText");
				wall.addStatus(user,postedText);
			}
			request.setAttribute("userFound", user);
			RequestDispatcher rd =request.getRequestDispatcher("userProfile.jsp");  
	        rd.forward(request, response);	
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String service = request.getParameter("service");
		WallClass wall = new WallClass();
		System.out.println("In doGet of WallServlet");
		
		if(service.equals("addComment")){
			String postContent = request.getParameter("postContent");
			BasicDBObject userWhoCommented = new BasicDBObject();
			BasicDBObject ownerOfPost = new BasicDBObject();
			HttpSession ses = request.getSession();
			userWhoCommented = (BasicDBObject) ses.getAttribute("userWhoCommented");
			ownerOfPost = (BasicDBObject) ses.getAttribute("friendFound");
			ses.setAttribute("userFound", userWhoCommented);
			ses.setAttribute("friendFound", ownerOfPost);
			if(userWhoCommented==null)
				System.out.println("User who commented could not be transferred");
			else{
				String postedComment = request.getParameter("newComment");
				wall.addComment(ownerOfPost,userWhoCommented,postedComment,postContent);
			}
		}else if(service.equals("addLike")){
			String postContent = request.getParameter("postContent");
			BasicDBObject userWhoLiked = new BasicDBObject();
			BasicDBObject ownerOfPost = new BasicDBObject();
			HttpSession ses = request.getSession();
			userWhoLiked = (BasicDBObject) ses.getAttribute("userWhoLiked");
			ownerOfPost = (BasicDBObject) ses.getAttribute("friendFound");
			ses.setAttribute("userFound", userWhoLiked);
			ses.setAttribute("friendFound", ownerOfPost);
			System.out.println("liker : "+userWhoLiked.getString("_id"));
			System.out.println("commenter : "+ownerOfPost.getString("_id"));
			
			if(userWhoLiked==null)
				System.out.println("User who liked could not be transferred");
			else
				wall.addLike(ownerOfPost,userWhoLiked,postContent);
		}
		response.sendRedirect("friendProfile.jsp");
		
	}

}
