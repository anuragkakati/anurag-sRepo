package pack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

import javax.servlet.http.HttpSession;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;


public class WallClass {

	DB db;

	public BasicDBList displayOnlineFriends(BasicDBObject userFound){
		db = DataBaseConnector.getDatabase();
		DBCollection coll = db.getCollection("userProfile");
		BasicDBObject findQuery = new BasicDBObject("_id",userFound.getString("_id"));
		DBCursor cursor = coll.find(findQuery);
		BasicDBList friendList = (BasicDBList) cursor.next().get("friendList");
		return friendList;
	}
	public void addFriend(String userID, String friendID){
		if(friendID.equals(""))
			return;
		db = DataBaseConnector.getDatabase();
		DBCollection coll = db.getCollection("userProfile");
		BasicDBObject findQuery = new BasicDBObject("_id",userID);
		DBCursor cur = coll.find(findQuery);
		BasicDBList exitingFriendList = (BasicDBList) cur.next().get("friendList");
		Iterator it = exitingFriendList.iterator();
		while(it.hasNext()){
			BasicDBObject friend = (BasicDBObject) it.next();
			if(friend.get("userName").equals(friendID))
				return;
		}
		
			BasicDBObject listItem = new BasicDBObject("friendList" ,new BasicDBObject("userName",friendID)
				.append("likesCount", 0)
				.append("commentCount",0)
				.append("CF", 0));
			BasicDBObject query = new BasicDBObject("$push",listItem);
			coll.update(findQuery,query );
			
			coll = db.getCollection("userTestChatBox");
			findQuery = new BasicDBObject("_id",userID);
			cur = coll.find(findQuery);
			if(cur.hasNext()){
				BasicDBObject user = (BasicDBObject) cur.next();
				BasicDBList chatBox = (BasicDBList) user.get("chatBox");
				BasicDBObject chatThread = new BasicDBObject("chatBox",new BasicDBObject("_id",friendID)
				.append("msgCount", 0)
				.append("recentSessionID", "")
				.append("session", new BasicDBList())
				.append("startTime", "")
				.append("endTime", "")
				.append("moodList", new BasicDBList())
				.append("avgMood", "")
				.append("contextList", new BasicDBList())
				.append("avgContext", "")
				.append("duration", ""));
				query = new BasicDBObject("$push",chatThread);
				coll.update(findQuery,query );
				
				
				coll=db.getCollection("userTestSession");
				BasicDBObject session = new BasicDBObject("_id",userID+friendID+"1")
				.append("msgs", new BasicDBList());
				coll.insert(session);
			}
			
	}
	public void addPost(BasicDBObject user,String postedText) {
		if(postedText.equals(""))
			return;
		db = DataBaseConnector.getDatabase();
		DBCollection coll = db.getCollection("userTestWall");
		System.out.println("Searching for "+user.getString("_id")+" in DB");
		BasicDBObject findQuery = new BasicDBObject("_id",user.getString("_id"));
		BasicDBObject updateQuery;
		DBCursor cur = coll.find(findQuery);
		BasicDBList postList,tagList,friendList;
		Stack<String> friendStack = new Stack<String>();
		Iterator it;
		BasicDBObject friendItem = new BasicDBObject();
		String friend;
		
		if(cur.hasNext()){
		
			tagList = new BasicDBList();
			String s,parts[] = new String[2];
			System.out.println("Exception here");
			if(postedText.contains("@tag ")){
				parts = postedText.split("@tag ");
				System.out.println("before getTagFriends");
				friendStack = getTagFriends(user,postedText);
				System.out.println("after getTagFriends");
				friendList = (BasicDBList) user.get("friendList");
				
				while(!friendStack.empty()){
					friend = friendStack.pop();
					System.out.println("picked "+friend);
					it = friendList.iterator();
					coll = db.getCollection("userProfile");
					while(it.hasNext()){
						friendItem = (BasicDBObject) it.next();
						System.out.println("matched with "+friendItem.getString("userName"));
						if(friendItem.getString("userName").equals(friend.trim())){
							tagList.add(friend.trim());
							findQuery = new BasicDBObject("_id",user.getString("_id"))
								.append("friendList.userName", friend);
							updateQuery = new BasicDBObject("$inc",new BasicDBObject("friendList.$.CF",60));
							coll.update(findQuery, updateQuery);
						}
					}
					if(tagList.contains(friend)==false){
						System.out.println(friend+" not added");
					}
				}
			}else{
				System.out.println("else part");
				parts[0]=new String(postedText);
				System.out.println("else part 2");
			}
			DBCursor curDec = db.getCollection("userProfile").find(new BasicDBObject("_id",user.getString("_id")));
			if(curDec.hasNext()){
				System.out.println("refined userProfile got");
			}else
				System.out.println("refiend userProfile not got");
			user = (BasicDBObject) curDec.next();
			friendList = (BasicDBList) user.get("friendList");
			System.out.println("cur exception");
			it = friendList.iterator();
			int friendCF=0,decCF=0;
			while(it.hasNext()){
				friendItem = (BasicDBObject) it.next();
				friendCF =  friendItem.getInt("CF");
				System.out.println("friendItem.get(CF) : "+friendItem.getInt("CF")+"\tfriendCF : "+friendCF+" of friend : "+friendItem.get("userName"));
				if(friendCF<5)
					decCF = friendCF;
				else
					decCF = 5;
				findQuery = new BasicDBObject("_id",user.getString("_id"))
					.append("friendList.userName", friendItem.getString("userName"));
				updateQuery = new BasicDBObject("$inc",new BasicDBObject("friendList.$.CF",decCF*-1));
				coll.update(findQuery, updateQuery);
			}
			
			
			BasicDBObject existingUser = (BasicDBObject) cur.next();
			postList = (BasicDBList) existingUser.get("postList");
			
			coll = db.getCollection("userTestWall");
			BasicDBObject listItem = new BasicDBObject("_id",postList.size()+1)
				.append("content",parts[0])
				.append("likes",new BasicDBList())
				.append("comments",new BasicDBList())
				.append("tags",tagList);
			findQuery = new BasicDBObject("_id",user.getString("_id"));
			BasicDBObject query = new BasicDBObject("$push",new BasicDBObject("postList",listItem));
			System.out.println(coll.update(findQuery,query ));
			System.out.println("--Data To be Written");

			
		}else{
			
			tagList = new BasicDBList();
			
			String s,parts[] = new String[2];
			System.out.println("Exception here");
			if(postedText.contains("@tag ")){
				parts = postedText.split("@tag ");
				System.out.println("before getTagFriends");
				friendStack = getTagFriends(user,postedText);
				System.out.println("after getTagFriends");
				friendList = (BasicDBList) user.get("friendList");
				
				while(!friendStack.empty()){
					friend = friendStack.pop();
					System.out.println("picked "+friend);
					it = friendList.iterator();
					while(it.hasNext()){
						friendItem = (BasicDBObject) it.next();
						System.out.println("matched with "+friendItem.getString("userName"));
						if(friendItem.getString("userName").equals(friend.trim())){
							tagList.add(friend.trim());
						}
					}
					if(tagList.contains(friend)==false){
						System.out.println(friend+" not added");
					}
				}
			}else{
				System.out.println("else part");
				parts[0]=new String(postedText);
				System.out.println("else part 2");
			}
			

			postList = new BasicDBList();
			BasicDBObject query = new BasicDBObject("_id",user.getString("_id"))
				.append("wallID", user.getString("wallID"));
			postList.add(new BasicDBObject("_id",1)
				.append("content",parts[0])
				.append("likes",new BasicDBList())
				.append("comments",new BasicDBList())
				.append("tags",tagList)				
			);
			query.append("postList",postList);
			System.out.println(coll.insert(query));
		}
		
	}
	public void addStatus(BasicDBObject user, String postedStatus) {
		if(postedStatus.equals(""))
			return;
		db = DataBaseConnector.getDatabase();
		DBCollection coll = db.getCollection("userTestWall");
		System.out.println("Searching for "+user.getString("_id")+" in DB");
		BasicDBObject findQuery = new BasicDBObject("_id",user.getString("_id"));
		DBCursor cur = coll.find(findQuery);
		BasicDBList statusList,tagList,friendList;
		Stack<String> friendStack = new Stack<String>();
		Iterator it;
		BasicDBObject friendItem = new BasicDBObject();
		BasicDBObject updateQuery;
		String friend;
		if(cur.hasNext()){
			
			tagList = new BasicDBList();
			String s,parts[] = new String[2];
			
			BasicDBObject existingUser = (BasicDBObject) cur.next();
			statusList = (BasicDBList) existingUser.get("statusList");
			
			/* Feeling blesses with @akashID @rituID   */
			
			if(postedStatus.contains("@tag ")){
				parts = postedStatus.split("@tag ");
				System.out.println("before getTagFriends");
				friendStack = getTagFriends(user,postedStatus);
				System.out.println("after getTagFriends");
				friendList = (BasicDBList) user.get("friendList");
				coll = db.getCollection("userProfile");
				while(!friendStack.empty()){
					friend = friendStack.pop();
					System.out.println("picked "+friend);
					it = friendList.iterator();
					while(it.hasNext()){
						friendItem = (BasicDBObject) it.next();
						System.out.println("matched with "+friendItem.getString("userName"));
						if(friendItem.getString("userName").equals(friend.trim())){
							tagList.add(friend.trim());
							findQuery = new BasicDBObject("_id",user.getString("_id"))
								.append("friendList.userName", friend);
							updateQuery = new BasicDBObject("$inc",new BasicDBObject("friendList.$.CF",70));
							coll.update(findQuery, updateQuery);
						}
					}
					if(tagList.contains(friend)==false){
						System.out.println(friend+" not added");
					}
				}
				System.out.println("updated CF in database");
				cur = db.getCollection("userProfile").find(new BasicDBObject("_id",user.getString("_id")));
				if(cur.hasNext()){
					System.out.println("refined userProfile got");
				}else
					System.out.println("refiend userProfile not got");
				user = (BasicDBObject) cur.next();
				friendList = (BasicDBList) user.get("friendList");
				System.out.println("cur exception");
				it = friendList.iterator();
				int friendCF=0,decCF=0;
				while(it.hasNext()){
					friendItem = (BasicDBObject) it.next();
					friendCF =  friendItem.getInt("CF");
					System.out.println("friendItem.get(CF) : "+friendItem.getInt("CF")+"\tfriendCF : "+friendCF+" of friend : "+friendItem.get("userName"));
					if(friendCF<5)
						decCF = friendCF;
					else
						decCF = 5;
					findQuery = new BasicDBObject("_id",user.getString("_id"))
						.append("friendList.userName", friendItem.getString("userName"));
					updateQuery = new BasicDBObject("$inc",new BasicDBObject("friendList.$.CF",decCF*-1));
					coll.update(findQuery, updateQuery);
				}
			}else{
				System.out.println("else part");
				parts[0]=new String(postedStatus);
				System.out.println("else part 2");
			}
			findQuery = new BasicDBObject("_id",user.getString("_id"));
			coll = db.getCollection("userTestWall");
			BasicDBObject listItem = new BasicDBObject("_id",statusList.size()+1)
				.append("content",parts[0])
				.append("tags",tagList);
			BasicDBObject query = new BasicDBObject("$push",new BasicDBObject("statusList",listItem));
			System.out.println(coll.update(findQuery,query ));			
		}else{
			tagList = new BasicDBList();
			String s,parts[] = new String[2];
			System.out.println("Entering first time");
			if(postedStatus.contains("@tag ")){
				parts = postedStatus.split("@tag ");
				System.out.println("before getTagFriends");
				friendStack = getTagFriends(user,postedStatus);
				System.out.println("after getTagFriends");
				friendList = (BasicDBList) user.get("friendList");
				
				while(!friendStack.empty()){
					friend = friendStack.pop();
					System.out.println("picked "+friend);
					it = friendList.iterator();
					while(it.hasNext()){
						friendItem = (BasicDBObject) it.next();
						System.out.println("matched with "+friendItem.getString("userName"));
						if(friendItem.getString("userName").equals(friend.trim())){
							tagList.add(friend.trim());
//							findQuery = new BasicDBObject("_id",user.getString("_id"))
//								.append("friendList.userName", friend);
//							updateQuery = new BasicDBObject("$inc",new BasicDBObject("friendList.$.CF",70));
//							coll.update(findQuery, updateQuery);
						}
					}
					if(tagList.contains(friend)==false){
						System.out.println(friend+" not added");
					}
				}
			}else{
				System.out.println("else part");
				parts[0]=new String(postedStatus);
				System.out.println("else part 2");
			}
			
			statusList = new BasicDBList();
			BasicDBObject query = new BasicDBObject("_id",user.getString("_id"))
				.append("wallID", user.getString("wallID"));
			statusList.add(new BasicDBObject("_id",1)
				.append("content",parts[0])
				.append("tags",tagList)				
			);
			query.append("statusList",statusList);
			System.out.println(coll.insert(query));
		}
		

	}
	private Stack<String> getTagFriends(BasicDBObject user, String postedStatus) {
		String parts[] = postedStatus.split("@tag ");
		int i,j=0,k=0;
		String friendID;
		Stack<String> friends = new Stack<String>();
		
		if(parts[1].indexOf('@')==-1)
			return friends;
		
		while(j!=-1){
			i = parts[1].indexOf('@');
			j = parts[1].indexOf('@',i+1);
			if(j==-1){
				System.out.println(parts[1].substring(i).trim());
				friendID = parts[1].substring(i+1).trim();
				friends.push(friendID);
			}
			else{
				System.out.println(parts[1].substring(i,j-1));
				
				friendID = parts[1].substring(i+1,j-1);
				friends.push(friendID);
				parts[1]=parts[1].substring(j);
			}
			
		}
		System.out.println("Friends obtained : "+friends);
		return friends;
		
	}
	
	public void addComment(BasicDBObject ownerOfPost, BasicDBObject userWhoCommented,String postedComment,String postContent) {
		if(postedComment=="")
			return;
		
		/*db.userTestWall.update({_id:"anuragID","postList._id":1},{$push:{"postList.$.likes":"akashID"}});*/
		/*db.userTestWall.update({_id:"anuragID","postList._id":1},{$push:{"postList.$.tags":"akashID"}});*/
		/*db.userTestWall.update({_id:"anuragID","postList._id":1},{$push:{"postList.$.comments":{"userName":"akashID","content":"Kya baat hai bhai :P"}}});*/
		
		db = DataBaseConnector.getDatabase();
		DBCollection coll = db.getCollection("userTestWall");
		BasicDBList postList = null;
		BasicDBList likeList = null;
		BasicDBObject commentQuery = null;
		BasicDBObject updateQuery = null;
		BasicDBObject pushQuery = null;
		BasicDBObject findQuery = new BasicDBObject("_id",ownerOfPost.getString("_id"));
		DBCursor cur = coll.find(findQuery);
		if(cur.hasNext()){
			BasicDBObject userWall = (BasicDBObject) cur.next();
			postList = (BasicDBList) userWall.get("postList");
			//System.out.println("Owner = "+owner.getString("_id") +"\nUser = "+user.getString("_id") + "\nSize of postList : "+postList.size());
		}
		findQuery = new BasicDBObject("_id",ownerOfPost.getString("_id"))
			.append("postList.content",postContent);
		commentQuery = new BasicDBObject("userName",userWhoCommented.getString("_id"))
			.append("content", postedComment);
		pushQuery = new BasicDBObject("$push",new BasicDBObject("postList.$.comments",commentQuery));
		
		coll.update(findQuery,pushQuery);
		
		coll = db.getCollection("userProfile");
		findQuery = new BasicDBObject("_id",ownerOfPost.getString("_id"))
			.append("friendList.userName",userWhoCommented.getString("_id"));
		updateQuery = new BasicDBObject("$inc",new BasicDBObject("friendList.$.commentCount",1));
		coll.update(findQuery, updateQuery);
		
		DBCollection incColl = db.getCollection("userProfile");
		BasicDBObject incfindQuery = new BasicDBObject("_id",ownerOfPost.getString("_id"))
			.append("friendList.userName", userWhoCommented.getString("_id"));
		BasicDBObject incupdateQuery = new BasicDBObject("$inc",new BasicDBObject("friendList.$.CF",3));
		coll.update(incfindQuery, incupdateQuery);
		
		
	}
	public void addLike(BasicDBObject ownerOfPost, BasicDBObject userWhoLiked,String postContent) {
		db = DataBaseConnector.getDatabase();
		DBCollection coll = db.getCollection("userTestWall");
		BasicDBList postList = null;
		BasicDBObject pushQuery = null;
		BasicDBObject projectQuery = null;
		BasicDBObject findQuery = new BasicDBObject("_id",ownerOfPost.getString("_id"));
		BasicDBObject updateQuery ;
		//DBCursor cur = coll.find(findQuery);
//		if(cur.hasNext()){
//			BasicDBObject userWall = (BasicDBObject) cur.next();
//			postList = (BasicDBList) userWall.get("postList");
//			System.out.println("OwnerOfPost = "+ownerOfPost.getString("_id") +"\nUserWhoLiked = "+userWhoLiked.getString("_id") + "\nSize of postList : "+postList.size());
//		}
		
		findQuery = new BasicDBObject("_id",ownerOfPost.getString("_id"))
			.append("postList.content", postContent);
		projectQuery = new BasicDBObject("postList.$.likes",1);
		DBCursor cur = coll.find(findQuery,projectQuery);
		if(cur.hasNext()){
			BasicDBObject userWall = (BasicDBObject) cur.next();
			postList = (BasicDBList) userWall.get("postList");
			Iterator it = postList.iterator();
			if(it.hasNext()){
				BasicDBObject post = (BasicDBObject) it.next();
				BasicDBList likesList = (BasicDBList) post.get("likes");
				it = likesList.iterator();
				while(it.hasNext()){
					if(userWhoLiked.getString("_id").equals(it.next().toString()))
						return;
				}
			}
		}
		pushQuery = new BasicDBObject("$push",new BasicDBObject("postList.$.likes",userWhoLiked.getString("_id")));
		coll.update(findQuery,pushQuery);
		
		coll = db.getCollection("userProfile");
		findQuery = new BasicDBObject("_id",ownerOfPost.getString("_id"))
			.append("friendList.userName",userWhoLiked.getString("_id"));
		updateQuery = new BasicDBObject("$inc",new BasicDBObject("friendList.$.likesCount",1));
		coll.update(findQuery, updateQuery);
		
		DBCollection incColl = db.getCollection("userProfile");
		BasicDBObject incfindQuery = new BasicDBObject("_id",ownerOfPost.getString("_id"))
			.append("friendList.userName", userWhoLiked.getString("_id"));
		BasicDBObject incupdateQuery = new BasicDBObject("$inc",new BasicDBObject("friendList.$.CF",2));
		coll.update(incfindQuery, incupdateQuery);
	}
	
}
