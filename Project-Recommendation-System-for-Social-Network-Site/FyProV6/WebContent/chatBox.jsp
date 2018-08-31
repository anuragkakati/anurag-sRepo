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
            margin-left:10px;
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
            margin-right:275px;
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
            margin-left:275px;
        }
    </style>
</head>
<body background="green.jpg" style="font-family:Arial;overflow-x:hidden;" >
	<%! 
		BasicDBObject userFound,userWall,wallItem,userChatBox,friendChatBox,oneSession,msgItem;
		BasicDBList friendList,wallPostList,sessionList,msgList;
		Iterator<Object> it,msgIt;
		BasicDBObject findQuery;
		DBCollection coll;
		DB db;
		DBCursor cursor;
		DataBaseConnector dbConnector;
		String friendID,sessionID;
	%>
	<%
		userFound=(BasicDBObject)request.getAttribute("userFound");
		if(userFound==null)
			userFound=(BasicDBObject)session.getAttribute("userFound");
		session.setAttribute("userFound", userFound);
		
		friendID = request.getParameter("friend");
		System.out.println("Under ChatBox.jsp \tFriend : "+friendID);
		
		sessionList = (BasicDBList)session.getAttribute("sessionList");
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
            <li><a href="friendList.jsp" style="border-radius:3.5px;">FriendList</a></li>
            <li><a href="" style="border-radius:3.5px;"><span style="color:green;"><b>ChatBox</b></span></a></li>
        </ul>
        <div class="tabcontents" style="border-radius:20px;padding:0px;">
            <div id="view3" class="gradv3" style="border-radius:20px;padding-top:10px;padding-bottom:20px;background-color:#89B8E8;">
                <div class="container">
                    <br>
                    <div class="col-md-4 scroll" align="center" style="background:url(g4.jpg);height:478px;overflow-y:scroll;">
                            <font size="5" face="calibri"  style="padding-top:100px;"><br><b> Chat Threads</b></font>
                            <hr>
                            <%
										 try{	
											System.out.println("Finding Online/Offline");
											dbConnector=new DataBaseConnector();
											db=DataBaseConnector.getDatabase();
											friendList=(BasicDBList)userFound.get("friendList");
											it = friendList.iterator();
											
											while(it.hasNext()){
												BasicDBObject friend = (BasicDBObject) it.next();
												System.out.print("Friend got : "+friend.getString("userName"));
												findQuery = new BasicDBObject("_id",friend.getString("userName"));
												coll = db.getCollection("registeredUser");
												cursor = coll.find(findQuery);
												if(cursor.next().get("isOnline").equals("true")){%>
													<% System.out.println(" - is online "); %>
													<div class="row" style="padding:2%">
														<div class = "col-md-9" align="left">
							                                &nbsp &nbsp &nbsp &nbsp<img src="profile.png" alt="pic" width="10%" height="10%">
							                                &nbsp &nbsp &nbsp &nbsp<font size="5" face="calibri"><a href=""><%= friend.getString("userName") %></a></font>
						                                </div>
						                                <div class= "col-md-3" align="left">
						                                	<div class="circle" style="background-color:green;"></div>
						                            	</div>
						                            </div>
												<%}
												else{%>
													<% System.out.println(" - is offline "); %>
													<div class="row" style="padding:2%">
														<div class = "col-md-9" align="left">
							                                &nbsp &nbsp &nbsp &nbsp<img src="profile.png" alt="pic" width="10%" height="10%">
							                                &nbsp &nbsp &nbsp &nbsp<font size="5" face="calibri"><a href=""><%= friend.getString("userName") %></a></font>
						                                </div>
						                                <div class= "col-md-3" align="left">
						                                	<div class="circle" style="background-color:red;"></div>
						                            	</div>
						                            </div>
												<% }
											}
										}catch(Exception E){
												E.printStackTrace();	
										}
							%>
                            
                            <hr>
                        </div> 
                    <div class="col-md-1" align="center">
                    
                    </div>
                    <div class="col-md-6">
                        <div class="row scroll" align="center" style="height:350px;width:600px;overflow-y: scroll;text-align:center;">
                            <p><%= friendID %>'s Thread</p>
                            
                            <%
                            	db=DataBaseConnector.getDatabase();
                            	coll = db.getCollection("userTestSession");
                            	it = sessionList.iterator();
                            	System.out.println("All sessions : ");
                            	while(it.hasNext()){
                            		sessionID = it.next().toString();
                            		cursor = coll.find(new BasicDBObject("_id",sessionID));
                            		if(cursor.hasNext()){
                            			oneSession = (BasicDBObject)cursor.next();
                            			msgList = (BasicDBList)oneSession.get("msgs");
                            			msgIt = msgList.iterator();
                            			while(msgIt.hasNext()){
                            				msgItem = (BasicDBObject)msgIt.next();
                            				if(msgItem.getString("type").equals("s")){%>
                            					<div class="row send">
					                                <div class="chatSend" style="padding-left:10px;padding-right:10px;">
					                                    <%= msgItem.getString("msgContent") %> 
					                                </div>
					                            </div>
                            				<%}else{%>
                            					<div class="row receive" >
					                                <div class="chatReceive" style="padding-left:10px;padding-right:10px;">
					                                    <%= msgItem.getString("msgContent") %> 
					                                </div>
					                            </div>
                            				<%}
                            			}
                            		}
                            		
                            	}
                            
                            %>
                            
                        </div>
                        <div class="row" align="center" style="width:95%;height:100px;text-align:justify;">
                        <form action="chat" method="post">
               					<input type="hidden" name="service" value="sendMsg">
                                <br>
                                <b><textarea cols="64" name="postedMsg" placeholder="Type here..." rows="2"></textarea></b>
                                <br>
                                <input type="hidden" name="friend" value="<%= friendID  %>">
                                <div class="row" align="right" style="padding-top:10px;margin-right:-87px" >
                                    <b><input type="submit" value="Send" style="padding-right:15px ;align:right; background-color:#009900;padding-left:15px;border-radius:500px;color:white;"></b>
                                </div>
                        </form>    
                        </div>     
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
