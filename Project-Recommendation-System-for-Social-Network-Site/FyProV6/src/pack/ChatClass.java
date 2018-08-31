package pack;

import java.time.LocalDateTime;
import java.util.Iterator;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class ChatClass {
	
	public boolean isSameSession(String endTime, String msgTime){
		
		LocalDateTime et = LocalDateTime.parse(endTime);
		LocalDateTime mt = LocalDateTime.parse(msgTime);
		if(et.plusSeconds(60).compareTo(mt)==1){
			return true;
		}
		return false;
	}

	public void updateUserInbox(String msg, String msgTime,
			String userID,String friendID,String type) {
		
		DB db = DataBaseConnector.getDatabase();
		DBCollection coll = db.getCollection("userTestChatBox");
		DBCursor cur = coll.find(new BasicDBObject("_id",userID));
		if(cur.hasNext()){
			BasicDBObject userChatBox = (BasicDBObject) cur.next();
			BasicDBList chatBox = (BasicDBList) userChatBox.get("chatBox");
			Iterator it = chatBox.iterator();
			BasicDBObject chatThread ,findQuery ,updateQuery , setQuery;
			BasicDBList sessionList;
			while(it.hasNext()){
				chatThread = (BasicDBObject) it.next();
				if(chatThread.getString("_id").equals(friendID)){
					sessionList = (BasicDBList) chatThread.get("session");
					String sessionID = userID+friendID+(sessionList.size()+1);
					String recentSessionID = chatThread.getString("recentSessionID");
					System.out.println(userID+"\t"+friendID+"\tsession size :"+sessionList.size());
					if(sessionList.size()==0){
						
						/* db.userTestChatBox.update({_id:"maddyID","chatBox._id":"pavanID"},{$inc:{"chatBox.$.msgCount":1},$set:{"chatBox.$.recentSessionID":"maddyIDpavanID1","chatBox.$.startTime":ISODate(),"chatBox.$.endTime":ISODate()},$push:{"chatBox.$.session":"maddyIDpavanID1"}}); */
						findQuery = new BasicDBObject("_id",userID).append("chatBox._id", friendID);
						setQuery = new BasicDBObject("chatBox.$.recentSessionID",sessionID)
							.append("chatBox.$.startTime", msgTime)
							.append("chatBox.$.endTime", msgTime);
						updateQuery = new BasicDBObject("$inc",new BasicDBObject("chatBox.$.msgCount",1))
							.append("$set", setQuery)
							.append("$push", new BasicDBObject("chatBox.$.session",sessionID));
						coll.update(findQuery, updateQuery);
						updateUserSession(sessionID,msg,msgTime,type);
					}else if(isSameSession(chatThread.getString("endTime"),msgTime)){
					/* db.userTestChatBox.update({"_id:userID","chatBox._id":"friendID"},{$set:{"endTime":"msgTime"},$inc:{"msgCount":1}}) */
						findQuery = new BasicDBObject("_id",userID).append("chatBox._id", friendID);
						setQuery = new BasicDBObject("chatBox.$.endTime", msgTime);
						updateQuery = new BasicDBObject("$set",setQuery)
							.append("$inc", new BasicDBObject("chatBox.$.msgCount",1));
						coll.update(findQuery,updateQuery);
						updateUserSession(recentSessionID,msg,msgTime,type);
					}else{
						findQuery = new BasicDBObject("_id",userID).append("chatBox._id", friendID);
						setQuery = new BasicDBObject("chatBox.$.recentSessionID",sessionID)
							.append("chatBox.$.startTime", msgTime)
							.append("chatBox.$.endTime", msgTime);
						updateQuery = new BasicDBObject("$inc",new BasicDBObject("chatBox.$.msgCount",1))
							.append("$set", setQuery)
							.append("$push", new BasicDBObject("chatBox.$.session",sessionID));
						coll.update(findQuery, updateQuery);
						coll=db.getCollection("userTestSession");
						BasicDBObject session = new BasicDBObject("_id",sessionID)
						.append("msgs", new BasicDBList());
						coll.insert(session);
						updateUserSession(sessionID,msg,msgTime,type);

					}
				}
			}
		}
			
			
			/* QUERIES THAT WUD BE HELPFULL	*/
			/* db.userTestChatBox.update({_id:"sonyID","chatBox._id":3},{$push:{"chatBox.$.session":{_id:1,"startTime":ISODate(),"endTime":ISODate(),"msgCount":1,moodList:[],"avgMood":"",contextList:[],avgContext:"",duration:"",msgs:[{_id:1,msgContent:"Hope This goes right",time:ISODate(),type:"s"}]}}}); */
			
			/* db.userTestChatBox.update({_id:"maddyID","chatBox._id":"pavanID"},{$inc:{"chatBox.$.msgCount":1},$set:{"chatBox.$.recentSessionID":"maddyIDpavanID1","chatBox.$.startTime":ISODate(),"chatBox.$.endTime":ISODate()},$push:{"chatBox.$.session":"maddyIDpavanID1"}}); */
			/* db.userTestSession.update({_id:"sureshIDrameshID1"},{$push:{msgs:{"_id":1,"msgContent":"Hello msg","time":ISODate(),"type":"s"}}}); */
			/* For storing msgs into another array within another array
			 * db.userTestChatBox.update({_id:"sonyID","chatBox._id":3},{$push:{"chatBox.$.session":{_id:1,"msgContent":"I sent msg to other friend through mongoShell","time":ISODate(),"type":"s"}}});
			 */
			
			
		
	}

	private void updateUserSession(String sessionID, String msg, String msgTime, String type) {
		DB db = DataBaseConnector.getDatabase();
		DBCollection coll = db.getCollection("userTestSession");
		BasicDBObject findQuery = new BasicDBObject("_id",sessionID);
		DBCursor cur = coll.find(findQuery);
		System.out.println("Taggu has "+sessionID);
		BasicDBObject sessionItem,setQuery;
		if(cur.hasNext())
			sessionItem = (BasicDBObject)cur.next();
		else{
			System.out.println("return");
			return;
		}
		System.out.println("jaggu");
		BasicDBList msgs = (BasicDBList) sessionItem.get("msgs");
		int sessionMsgLength = msgs.size();
		if(sessionMsgLength==0){
			setQuery = new BasicDBObject("$set",new BasicDBObject("startTime",msgTime).append("endTime", msgTime));
		}
		else{
			setQuery = new BasicDBObject("$set",new BasicDBObject("endTime", msgTime));
		}
		/* db.userTestSession.update({_id:"sureshIDrameshID1"},{$push:{msgs:{"_id":1,"msgContent":"Hello msg","time":ISODate(),"type":"s"}}}); */
		
		BasicDBObject pushItem = new BasicDBObject("_id",sessionMsgLength+1)
			.append("msgContent",msg)
			.append("time", msgTime)
			.append("type", type);
		
		BasicDBObject pushQuery = new BasicDBObject("msgs",pushItem);
		BasicDBObject updateQuery = new BasicDBObject("$push",pushQuery);
		
		coll.update(findQuery,updateQuery);
		coll.update(findQuery, setQuery);
	}

}
