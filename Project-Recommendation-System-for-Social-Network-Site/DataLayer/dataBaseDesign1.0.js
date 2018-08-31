
db.registeredUser.insert()
{
	userName:"SomeName",
	isOnline:true
}


db.userProfilee.insert()
{
	userName:"SomeName", // foreign key from registeredUser
	password:"*****",
	profilePic:"url",
	givenName:"Wall Name",
	DOB:"year",
	age:"age",
	gender:"m/f",
	address:"",
	profession:"",
	phoneNo:"",
	friendList:[
		friend1:{
			userName:"",
			likesCount:"",
			commentCount:"",
			//msgCount:"",			// To be appended more fields may b
			closenessFactor:"",		//calculated by a devised formula
			type:["",""]		
		},
		friend2:{

		}
	],
	chatBoxID : "",
	wallid:""	//denotes wall by foreign key from Wall Collection
}


db.userWall.insert()
{
	userName:"",
	wallid : "",
	statusList : [
		status n : {
			_id : "statusN"
			content:"",
			tags:["userName1","userName2"]
		}
		status n-1 : {}
	] ,
	postList : [
		post1:{
			_id:"",
			content:"",
			likes:["userName1","userName2"],
			comments:[
				commentor1:{
					userName:"",	//foreign Key
					content:""
				}
			],
			tags:["userName1","userName2"]
		}
		post2: {} 
	]
	
}

db.chatBox.insert()
{
	_id : "userName",
	chatBox:[
		chatThread1:{
			//isSessionActive:"True/False"			//shows if session going on or not...
			_id:"",		//friendName
			msgCount:"",
			recentSessionID:"",
			session:[
				session1:{
					_id:"sessionID"
					msgs:[
						msg1:{
							_id:"",
							msgContent:""
							time:"",
							type:"send/receive"
						},
						msg2:{

						}
					]
					startTime:"",
					endTime:"",
					moodList:["mood1","mood2"],
					avgMood : "mood",	//cumulative mood giving more weight to recent mood (formula)-required for optimization
					contextList:["contextOfMsg1","contextOfMsg2"],
					avgContext:"personal/professional/casual",//research
					msgCount:"",
					duration:""
				},
				session2:{

				}
			]
		},
		chatThread2:{

		}
	],
}
