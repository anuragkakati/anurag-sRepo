package pack;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName, password, givenName, dob, hometown, gender, phone, profession;
		int age;
		userName = request.getParameter("username");
		password = request.getParameter("password");
		givenName = request.getParameter("givenname");
		dob = request.getParameter("dob");
		age = Integer.parseInt((request.getParameter("age")));
		hometown = request.getParameter("hometown");
		gender = request.getParameter("gender");
		phone = request.getParameter("phoneno");
		profession = request.getParameter("profession");
		PrintWriter wr = response.getWriter();
		wr.println("SEVLET age : "+age);
		System.out.println("Under Servlet UserName : "+userName+"\tphoneno : "+phone);
		SignUpClass signUp=new SignUpClass(userName,password,givenName,dob,age,hometown,gender,phone,profession);
		//signUp.insertDocument();
		response.sendRedirect("login.html");
	}

}
