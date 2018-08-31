
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
			msgCount:"",			// To be appended more fields may b
			closenessFactor:"",		//calculated by a devised formula
			type:["",""]		
		},
		friend2:{

		}
	],
	chatBox:[
		chatThread1:{
			isSessionActive:"True/False"			//shows if session going on or not...
			userName:"",		//foreign Key
			msgCount:"",
			msgs:[
				msg1:{
					msgContent:""
					time:"",
					type:"send/receive"
				},
				msg2:{

				}
			],
			session:[
				session1:{
					startTime:"",
					endTime:"",
					mood:"",
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
	wallid:""	//denotes wall by foreign key from Wall Collection
}


db.wall.insert()
{
	userName:"",
	status:{
		content:"",
		likes:["userName1","userName2"],
		comments:[
			commentor1:{
				userName:"",	//foreign Key
				content:""
			}
		],
		tags:["userName1","userName2"]
	},
	post1:{
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
}

