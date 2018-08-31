<%@page import="java.util.ArrayList"%>
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
          background-color: #CCFF66;
          opacity:0.9;
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
           height: 320px;
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
		BasicDBObject userFound,friendFound,owner,userWall,wallItem;
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
		friendFound = (BasicDBObject)session.getAttribute("friendFound");
		owner = (BasicDBObject)session.getAttribute("userFound");
		if(userFound==null)
			userFound=(BasicDBObject)session.getAttribute("userFound");
		session.setAttribute("userFound", userFound);
		session.setAttribute("friendFound", friendFound);
		session.setAttribute("owner", userFound);
	%>
    <div class="row">
        <div class="col-md-7" style="margin-top:8px;">
            <b><font size="6" face="Monaco" color="#009900">&nbsp&nbsp&nbspfriend<I><span style="color:black">Space</span></I></font></b>
        </div>
        <div class="col-md-3" align="right">
            <a href="userProfile.jsp"><b><button type="button" style="padding-left:40px;padding-right:40px;padding-top:5px;padding-bottom:5px;border-radius:500px;color:black;background-color:transparent;margin-top:20px;"><%= userFound.getString("_id") %>'s<b>&nbsp&nbsp<font size="3" face="Monaco"><span style="color:#009900">f</span>S</b></font></button></b></a>
            <% userFound = friendFound; %>
        </div>
        <div class="col-md-2" align="center">
        	<form action="login" method="get">
				<input type="hidden" name="username" value="<%= userFound.get("_id")%>">
            	<b><input type="submit" value="logout" style="padding-left:40px;padding-right:40px;padding-top:5px;padding-bottom:5px;border-radius:500px;color:black;background-color:transparent;margin-top:20px;margin-right:35px;"></b>
			</form>
        </div>
    </div>
    <div style="width: 1300px ; margin: 0 auto; padding-left:20px; padding-right:20px;padding-bottom:0px;">
        <ul class="tabs" style="padding-left:30px;">
            <li><a href="" style="border-radius:3.5px;"><span style="color:green;"><b>Profile</b></span></a></li>
            <li><a href="friendList.jsp" style="border-radius:3.5px;">FriendList</a></li>
            <li><a href="chatBox.jsp" style="border-radius:3.5px;">ChatBox</a></li>
        </ul>
        <div class="tabcontents" style="border-radius:20px;padding:0px;">
            <div id="view1" style="padding-top:10px;border-radius:20px;">
                <div class="container">
                    <div class="col-md-3 profile" style="padding-right:50px;">
                        <img src="profile.png" alt="profile-pic" width="100%" height="100%" >
                        <br>
                        <br>
                        <b><font face="monospace" size="6" color="#009900" ><%= userFound.getString("givenName") %></font></b>
                        <br>
                        <br>
                        <div class="row" style="text-align:justify;padding-left:25px;">
                            <div class="row" style="width:225px;height:auto;padding:10px;background-color:#009900">
                               <b><font color="white" face="monospace" size="4">
                               		<%-- db.userTestWall.aggregate({$match:{_id:"kiranID"}},{$unwind: "$statusList"},{$sort:{'statusList._id':-1}}).result[0].statusList.content; --%>
										<%
											try{%>
											<% 	dbConnector=new DataBaseConnector();
												db=DataBaseConnector.getDatabase();
												coll = db.getCollection("userTestWall");
												BasicDBObject match = new BasicDBObject("$match",new BasicDBObject("_id",userFound.get("_id")));
												BasicDBObject unwind = new BasicDBObject("$unwind","$statusList");
												BasicDBObject sort = new BasicDBObject("$sort",new BasicDBObject("statusList._id",-1));
												Iterator itt = coll.aggregate(match, unwind,sort).results().iterator();
												if(itt.hasNext()==false){%>
													<p> No Status </p>
												<%}%>
												<%if(itt.hasNext()){%>
													<%
														BasicDBObject st = (BasicDBObject)itt.next();
														BasicDBObject status = (BasicDBObject)st.get("statusList");%>
														<%= status.get("content") %>
														<%
															ArrayList<String> tagList = new ArrayList<String>();
															tagList =(ArrayList<String>) status.get("tags");
															if(tagList.size()!=0){%>
																<span style="color:yellow"> tagged with
																<%for(String friend : tagList){%>
																		<%= friend %>
																<%}%>
																</span>
														<%}%>
												<%}%>
												
													
											<% }catch(Exception e){
												e.printStackTrace();
											}
										%>
                              
                               
                               
                               </font></b> 
                            </div>
                            <br>
                            <div class="row" align="left" style="padding-right:20px;">
                               <!-- <form name="myform" action="wall" method="get">
                               	 	<input type="hidden" name="service" value="addStatus">
                                    <div text-align="justify">
                                        <textarea align="center" cols="25" rows="3" color="#290029" name="postedText" style="background:white" style="font-family:monospace" style="color:#009900" style="border style:solid" >
                                        </textarea>
                                        <%-- 
											session.setAttribute("userWhoPosted",userFound);
										--%>
                                        <br><br>
                                        <b><input type="submit" value="Edit" align="right" style="padding-right:15px ; padding-left:15px;margin-left:160px;border-radius:500px;background-color:#009900;color:white;"></b>
                                    </div>
                                </form> --> 
                            </div>
                        </div>  
                    </div>
                    <div class="col-md-6 background" align="center" style="padding-bottom:-20px;">
                        <div class="scroll" style="height: 553px; overflow-y: scroll;">
                        	<%
								try{%>
									<% 	dbConnector=new DataBaseConnector();
										db=DataBaseConnector.getDatabase();
										coll = db.getCollection("userTestWall");
										BasicDBObject match = new BasicDBObject("$match",new BasicDBObject("_id",userFound.get("_id")));
										BasicDBObject unwind = new BasicDBObject("$unwind","$postList");
										BasicDBObject sort = new BasicDBObject("$sort",new BasicDBObject("postList._id",-1));
										Iterator itt = coll.aggregate(match, unwind,sort).results().iterator();%>
										<% while(itt.hasNext()){
												BasicDBObject st = (BasicDBObject)itt.next();
												BasicDBObject post = (BasicDBObject)st.get("postList");
												BasicDBList comments = (BasicDBList) post.get("comments"); 
												BasicDBList likes = (BasicDBList)post.get("likes");%>
					                            <div class="transbox" align="center" style="width:95%;height:auto;text-align:justify;padding-left:2%;padding-right:2%;padding-top:2%;padding-bottom:2%;">
														<div style="padding-left:2%;padding-top:2%;padding-bottom:2%;background-image:radial-gradient(#009900,#009900);">
															<font color="white" face="calibri" size="3">
																<p>
																	<%= post.getString("content") %>
																	<b>
																	<%
																		ArrayList<String> tagList = new ArrayList<String>();
																		tagList =(ArrayList<String>) post.get("tags");
																		if(tagList.size()!=0){%>
																			<span style="color:yellow"> tagged with </span>
																			<%for(String friend : tagList){%>
																				<span style="color:yellow"> 
																				<%= friend %>
																				</span>
																			<%}%>
																		<%}%>
																	</b>	
																</p>
															</font>					                                	
														</div>
														<br>
														<div class="row">
															<div class="col-md-2">
															<form action="wall" method="post">
																<input type="hidden" name="service" value="addLike">
																<input type="hidden" name="postContent" value="<%= post.getString("content") %>">
																<%
																	session.setAttribute("userWhoLiked",owner);
																	session.setAttribute("friendFound", friendFound);
																%>
																<b>&nbsp&nbsp<font color="#006600"><input type="submit" value="like"></font></b>
															</form>
															</div>
															<div class="col-md-10">
																<font color="#006600"><%= likes.size()%> people like this</font>
															</div>
														</div>
														<br>
														<div class="scroll" style="height:125px;overflow-y:auto;padding-left:2%">
															<div>
															<%
																Iterator it = comments.iterator();
																while(it.hasNext()){%>
																<% BasicDBObject nextItem;%>
																<div>
																	<img src="profile.png" height=7% width=7%>
																	<span style="font-size:18px"><a href="">
																	<%
																		nextItem=(BasicDBObject)it.next(); 
																	%>
																	<%=
																		nextItem.getString("userName")
																	%>
																	</a></span>
																	<p><%= nextItem.getString("content") %></p>
																	<span style="margin-top:0px">
																	<!--<form action="wall" method="post">
																		<input type="hidden" name="service" value="addLike">
																		<%
																			session.setAttribute("userWhoLiked",owner);
																			session.setAttribute("friendFound", friendFound);
																		%>
																		<input type="submit" value="like">
																	</form> -->
																</div>
																<br>
																<%}%> 
															</div>
														</div>
														<form action="wall" method="post">
															<div>
																<br>
																<p style="color:black">Comment...</p>
																<textarea align="center" cols="56" rows="2" name="newComment" placeholder="Your comment here. . ."></textarea>
															</div>
															<input type="hidden" name="postContent" value="<%= post.getString("content") %>">
															<input type="hidden" name="service" value="addComment">
															<% 
																session.setAttribute("userWhoCommented",owner);
															%>
															<font size="2"><input type="submit" value="Comment" align="right" style="padding-right:5px ; padding-left:5px;margin-left:385px;border-radius:500px;background-color:#009900;color:white;"></font>
															<!--<button align="right" type="button" style="padding-right:15px ; padding-left:15px;margin-left:385px;border-radius:500px;background-color:#009900;color:white;"><b>Comment</b></button>-->
														</form>	
							                    	</div> 
							                    	<br>
											<%}
									 }catch(Exception e){
												e.printStackTrace();
									}
							 	 %>                      
                        </div>  
                    </div>
                    <div class="col-md-3">
                        <div class="row" style="padding-left:30px">
                            <p>
                                <h3>Profile Details</h3><hr>
                                People call me <b><%= userFound.getString("givenName") %></b><br><br>
                                Born on <b><%= userFound.getString("DOB") %></b><br><br>
                                Lives in <b><%= userFound.getString("hometown") %></b><br><br>
                                Experienced <b><%= userFound.getString("age") %></b> years of life<br><br>
                                Can call me on <b><%= userFound.getString("phoneNo") %></b><br><br>
                                Working as a <b><%= userFound.getString("profession") %></b><br><br>
                            </p>
                        </div>
                        <br>
                        <br>
                        <div class="row" style="padding-left:10px">
                            <!--<form action="wall" method="get">
								<input type="hidden" name="service" value="addFriend">
								<input type="hidden" name="username" value="<%= userFound.get("_id")%>">
								<div class="col-md-2">
                                    <input type="text" name="friendName">
                                </div>
                                <div class="col-md-1">
                                    <input type="submit" value="friend++" style="align:right; padding-left:15px;margin-left:140px;border-radius:500px;background-color:#009900;color:white;">
                                </div>
                            </form> -->
                        </div>
                    </div>
                </div>
                <br>
            </div>
        </div>
    </div>
</body>
</html>
