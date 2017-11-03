var MongoClient = require('mongodb').MongoClient;

var dbConnection = null;

var lockCount = 0;



function getDbConnection(callback){
    MongoClient.connect("mongodb://localhost/buckitDB6", function(err, db){
        if(err){
            console.log("Unable to connect to Mongodb");
        }else{
            dbConnection = db;
            callback();
        }
    });
};

function closeConnection() {
    if (dbConnection)
        dbConnection.close();

}

getDbConnection(function(){
    dbConnection.dropDatabase(function(err,doc){
        if (err)
            console.log("Could not drop database");
        else
            addUser();
    });
});

function addUser() {
    d = [{
        "firstName":    "Gary",
        "lastName":     "Peters",
        "emailAddress":  "Gary@Peters.com",
        "score" : 0
    },
        {
            "firstName":    "Amol",
            "lastName":     "Patil",
            "emailAddress":        "Amol@Patil.com",
            "score" : 0
        }];

    var users = dbConnection.collection('users');
    users.insertOne(d[0], function(err,doc){
        if (err){
            console.log("Could not add user 1");
        }
        else {
            addChallengestoUser0(doc.ops[0]._id.toString());
        }
    })
    users.insertOne(d[1], function(err,doc){
        if (err){
            console.log("Could not add user 1");
        }
        else {
            addChallengestoUser1(doc.ops[0]._id.toString());
        }
    })
}

function addChallengestoUser0(userId) {
    c = [{
        "challengeName" : "SelfieChallenge",
        "challengeDescription" : "Take selfie.",
        "challengeCreatedDate" : "20 Oct 2017",
        "challengeType" : "funny",
        "userId" : userId
    },{
        "challengeName" : "FlyHigh",
        "challengeDescription" : "Sky diving challenge.",
        "challengeCreatedDate" : "21 Oct 2017",
        "challengeType" : "funny",
        "userId" : userId
    },{
        "challengeName" : "CodeChallenge",
        "challengeDescription" : "Write a code",
        "challengeCreatedDate" : "31 Oct 2015",
        "challengeType" : "social",
        "userId" : userId
    }];
    c.forEach(function(challenge){
        var challenges = dbConnection.collection('challenges');
        challenges.insertOne(challenge);
    })

}

function addChallengestoUser1(userId) {
    c = [{
        "challengeName" : "Fun challenge",
        "challengeDescription" : "Play ps4",
        "challengeCreatedDate" : "11 Oct 2015",
        "challengeType" : "social",
        "userId" : userId
    },{
        "challengeName" : "Scary Challenge",
        "challengeDescription" : "Watch a horror movie.",
        "challengeCreatedDate" : "1 Oct 2015",
        "challengeType" : "social",
        "userId" : userId
    }];
    c.forEach(function(challenge){
        var challenges = dbConnection.collection('challenges');
        challenges.insertOne(challenge);
    })

}

setTimeout(closeConnection,5000);