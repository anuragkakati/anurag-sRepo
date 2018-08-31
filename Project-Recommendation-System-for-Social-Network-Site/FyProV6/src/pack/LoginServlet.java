package pack;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.BasicDBObject;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		RequestDispatcher rd;
		PrintWriter wr=response.getWriter();
		LoginClass login = new LoginClass(userName,password);
		BasicDBObject userFound;
		
		if((userFound=login.authenticate())!=null){
			System.out.println("User Name logged in is "+userFound.getString("_id"));
			request.setAttribute("userFound", userFound);
			rd=request.getRequestDispatcher("userProfile.jsp");  
	        rd.forward(request, response); 
//			WallClass wall = new WallClass();
//			wall.displayWall(userFound);
			
		}else{
			response.sendRedirect("login.html");
		}
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getParameter("username");
		LoginClass logout = new LoginClass();
		logout.logOut(userName);
		response.sendRedirect("login.html");
		
	}

}
