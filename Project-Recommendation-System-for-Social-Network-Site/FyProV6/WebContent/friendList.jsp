<%@page import="com.mongodb.DBCollection"%>
<%@page import="com.mongodb.BasicDBList"%>
<%@page import="org.omg.CORBA.Request"%>
<%@page import="com.mongodb.BasicDBObject"%>
<%@page import="java.util.Iterator" %>
<%@page import="com.mongodb.DB" %>
<%@page import="com.mongodb.DBCursor" %>
<%@page import="pack.DataBaseConnector" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>friendSpace</title>
    <script src="tabcontent.js" type="text/javascript"></script>
    <link href="template1/tabcontent.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="bootstrap/css/bootstrap.css" type="text/css">
    <link rel="stylesheet" href="style.css" type="text/css">
    <style>
        hr { 
            display: block;
            margin-top: 1.5em;
            margin-bottom: 1.5em;
            margin-left: auto;
            margin-right: auto;
            border-style: inset;
            border-width: 1px;
            border-color: #009900;
        } 
        a:link {
            text-decoration: none;
        }

        a:visited {
            text-decoration: none;
        }

        a:hover {
            text-decoration: none;
        }

        a:active {
            text-decoration: none;
        }
        .circle {
           background-color: red;
           padding: 6px;
           display: inline-block;
           -moz-border-radius:5%;
           -webkit-border-radius: 50%;
           border-radius:50%; 
        }
        div.transbox
        {
          background-color: #ffffff;
          opacity:0.6;
          filter:alpha(opacity=60); /* For IE8 and earlier */
        }
        .div.transbox p
        {
            font-weight: bold;
            color: #00FF00;
        }
        div.background
        {
          background: url(g.jpg);
          border: 0px;
        }
        .scroll {
           height: 400px;
           overflow-y: scroll;
           overflow-x:hidden;
        }
        .scroll::-webkit-scrollbar {
            width: 5px;
        }

        .scroll::-webkit-scrollbar-track {
            -webkit-box-shadow: inset 0 0 9px rgba(5,0,0,0.3); 
            border-radius: 10px;
        }

        .scroll::-webkit-scrollbar-thumb {
            border-radius: 10px;
            -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.5); 
        }
        .chatSend{
            background-image:
            radial-gradient(
              #CCFF66,
              #CCFF66
            );
            border-radius: 20px;
        }
        .chatReceive{
            background-image:
            radial-gradient(
              #FFFF66,
              #FFFF66
            );
            border-radius: 20px;
        }
        .friendList{
              background: -webkit-linear-gradient(left top, #CCFF66, white); /* For Safari 5.1 to 6.0 */
              background: -o-linear-gradient(bottom right, white,#CCFF66); /* For Opera 11.1 to 12.0 */
              background: -moz-linear-gradient(bottom right, #CCFF66, white); /* For Firefox 3.6 to 15 */
              background: linear-gradient(to bottom right,  white,#CCFF66); /* Standard syntax */
        }
        .gradv3{
            background-image:
            radial-gradient(
                white,
                white
            );
            opacity:0.9;
            filter:alpha(opacity=90);
        }
        .send{
            align:left;
            font-size:120%;
            padding-left:5px;
            padding-right:25px;
            margin-top:10px;
            border-radius:30px;
            color:#003300;
            width:auto;
            text-align:justify;
            width:auto;
            margin-right:250px;
        }
        .receive{
            font-size:120%;
            padding-left:25px;
            padding-right:25px;
            margin-top:10px;
            border-radius:30px;
            color:black;
            width:auto;
            text-align:justify;
            width:auto;
            margin-left:250px;
        }
    </style>
</head>
<body background="green.jpg" style="font-family:Arial;overflow-x:hidden;" >

	<%! 
		BasicDBObject userFound,userWall,wallItem;
		BasicDBList friendList,wallPostList;
		Iterator<Object> it;
		BasicDBObject findQuery;
		DBCollection coll;
		DB db;
		DBCursor cursor;
		DataBaseConnector dbConnector;
	%>
	<%
		userFound=(BasicDBObject)request.getAttribute("userFound");
		if(userFound==null)
			userFound=(BasicDBObject)session.getAttribute("userFound");
		session.setAttribute("userFound", userFound);
		System.out.println("friendList.jsp");
		System.out.println("*********************************");
		System.out.println("userFound is : "+userFound.get("_id"));
	%>
    <div class="row">
        <div class="col-md-7" style="margin-top:8px;">
            <b><font size="6" face="Monaco" color="#009900">&nbsp&nbsp&nbspfriend<I><span style="color:black">Space</span></I></font></b>
        </div>
        <div class="col-md-3" align="right">
            <a href="userProfile.jsp"><b><button type="button" style="padding-left:40px;padding-right:40px;padding-top:5px;padding-bottom:5px;border-radius:500px;color:black;background-color:transparent;margin-top:20px;"><%= userFound.getString("_id") %>'s<b>&nbsp&nbsp<font size="3" face="Monaco"><span style="color:#009900">f</span>S</b></font></button></b></a>
        </div>
        <div class="col-md-2" align="center">
        	<form action="login" method="get">
				<input type="hidden" name="username" value="<%= userFound.get("_id")%>">
            	<b><input type="submit" value="logout" style="padding-left:40px;padding-right:40px;padding-top:5px;padding-bottom:5px;border-radius:500px;color:black;background-color:transparent;margin-top:20px;margin-right:35px;"></b>
			</form>
        </div>
    </div>
    <div style="width: 1300px ; margin: 0 auto; padding-left:20px; padding-right:20px;padding-bottom:0px;">
        <ul class="tabs" data-persist="true" style="padding-left:30px;">
            <li><a href="userProfile.jsp" style="border-radius:3.5px;">Profile</a></li>
            <li><a href="" style="border-radius:3.5px;"><span style="color:green;"><b>FriendList</b></span></a></li>
            <li><a href="chatBox.jsp" style="border-radius:3.5px;">ChatBox</a></li>
        </ul>
        <div class="tabcontents" style="border-radius:20px;padding:0px;">
            <div id="view2">
                <div class="container">
                    <div class="col-md-3"  style="padding-top:20px;margin-bottom:10px;padding-bottom:20px">
                        <div class="row" style="padding-top:10px;padding-left:30px">
                            <form>
                                <input type="text" name="friendSelected" >
                                <input type="submit" value="Search">
                            </form>
                        </div>
                        <br>
                        <div class="scroll" style="height: 422px; overflow-y: scroll;">
                            <div class="row friendList" style="padding-left:20px;padding-right:20px;">
                            		<%
										 try{	
											System.out.println("start");
											dbConnector=new DataBaseConnector();
											db=DataBaseConnector.getDatabase();
											friendList=(BasicDBList)userFound.get("friendList");
											it = friendList.iterator();
											System.out.println("friendList.jsp 2");
											while(it.hasNext()){
												BasicDBObject friend = (BasicDBObject)it.next();%>
												<div class="col-md-6" align="center">
			                                        <img src="profile.png" height="100%" width="100%" style="padding-left:5px;padding-right:5px;padding-top:5px">
			                                        <form action="chat" method="get">
											   			<input type="hidden" name="friend" value="<%=friend.getString("userName")%>">
			                                        	<input type="hidden" name="service" value="showFriendProfile">
			                                        	<input type="submit" value="<%= friend.getString("userName") %>">
			                                        </form>
			                                        
			                                    </div>  
											<%}
										}catch(Exception E){
												E.printStackTrace();	
										}
										%>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-9" align="center" style="padding-left:20px;padding-right:20px;">
                        <div class="row"style="padding-top:10px;padding-left:10px;" >
                            <h3><b>Currently </b>Online</h3>
                        </div>
                        <div class="scroll" style="height: 125px; width:700px;">
                            <div class="row friendList" style="padding-left:20px;padding-right:10px;padding-top:10px;">
                                    <%
										 try{	
											System.out.println("start");
											dbConnector=new DataBaseConnector();
											db=DataBaseConnector.getDatabase();
											friendList=(BasicDBList)userFound.get("friendList");
											System.out.println("friendList.jsp 3");
											it = friendList.iterator();
										
											while(it.hasNext()){
												BasicDBObject friend = (BasicDBObject) it.next();
												findQuery = new BasicDBObject("_id",friend.getString("userName"));
												coll = db.getCollection("registeredUser");
												cursor = coll.find(findQuery);
												System.out.println(friend.getString("userName"));
												if(cursor.next().get("isOnline").equals("true")){%>
													<div align="center" style="width:95%;height:20%;">
														<form action="chat" method ="get">
															<input type="hidden" name="hostUser" value=<%= userFound.get("_id") %>>
															<input type="hidden" name="service" value="showChatBox">
															<input type="hidden" name="friend" value="<%=friend.getString("userName")%>">
															<div class="col-md-2">
						                                        <img src="profile.png" height="100%" width="100%" style="padding-left:5px;padding-right:5px;padding-top:5px">
						                                        <input type="submit" value="<%=friend.getString("userName")%>" style="width:100%;	height:60%"> 
						                                    </div>
															
														</form>
														<%-- <a href=""><button style="width:100%;	height:70%"><%=friend.getString("userName")%></button></a>  --%>
													</div>
												<%}
											}
										}catch(Exception E){
												E.printStackTrace();	
										}
									%>
                            </div>
                        </div>
                        <hr>
                        <div class="row"style="padding-left:10px;" >
                            <h3><b>Online</b> Close Friends</h3>
            <%-- db.userProfile.aggregate({$match:{_id:"dID"}},{$unwind:"$friendList"},{$sort:{"friendList.CF":-1}}).result; --%>
                        </div>
                        <div class="scroll" style="height: 125px; width:700px;">
                            <div class="row friendList" style="padding-left:20px;padding-right:10px;padding-top:10px;">
                                    <%
	                                	dbConnector=new DataBaseConnector();
										db=DataBaseConnector.getDatabase();
										coll = db.getCollection("userProfile");
										BasicDBObject match = new BasicDBObject("$match",new BasicDBObject("_id",userFound.get("_id")));
										BasicDBObject unwind = new BasicDBObject("$unwind","$friendList");
										BasicDBObject sort = new BasicDBObject("$sort",new BasicDBObject("friendList.CF",-1));
										Iterator itt = coll.aggregate(match, unwind,sort).results().iterator();
										BasicDBObject CFObject,CFFriendObj,regUser;
										DBCursor CFCur;
										coll = db.getCollection("registeredUser");
										String CFFriendID;
										while(itt.hasNext()){
											CFObject = (BasicDBObject)itt.next();
											CFFriendObj = (BasicDBObject)CFObject.get("friendList");
											CFFriendID = CFFriendObj.getString("userName");
											System.out.println("For close got "+CFFriendID);
											CFCur = coll.find(new BasicDBObject("_id",CFFriendID));
											if(CFCur.hasNext()){
												regUser = (BasicDBObject)CFCur.next();%>
												<% System.out.println("\tclose "+CFFriendID); %>
												<% if(regUser.getString("isOnline").equals("true")){%>
													<% System.out.println("\t\tclose "+CFFriendID); %>
													<div align="center" style="width:95%;height:20%;">
													<form action="chat" method ="get">
														<input type="hidden" name="hostUser" value=<%= userFound.get("_id") %>>
														<input type="hidden" name="service" value="showChatBox">
														<input type="hidden" name="friend" value="<%=CFFriendID%>">
														<div class="col-md-2">
					                                        <img src="profile.png" height="100%" width="100%" style="padding-left:5px;padding-right:5px;padding-top:5px">
					                                        <input type="submit" value="<%=CFFriendID%>" style="width:100%;	height:60%"> 
					                                    </div>
														
													</form>
													<%-- <a href=""><button style="width:100%;	height:70%"><%=friend.getString("userName")%></button></a>  --%>
												</div>
												<%}
											}
										}
									%>
                            </div>
                        </div>
                        <hr>
                        <div class="row"style="padding-left:10px;" >
                            <h3><b>Current Mood</b> based friends</h3>
                        </div>
                    </div>
                </div>
            </div><!-- end of view2-->
        </div>
    </div>
</body>
</html>
