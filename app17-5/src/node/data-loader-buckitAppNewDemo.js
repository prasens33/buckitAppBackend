var MongoClient = require('mongodb').MongoClient;

var dbConnection = null;

var lockCount = 0;



function getDbConnection(callback){
    MongoClient.connect("mongodb://localhost/buckitDB10", function(err, db){
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
        "profilePictureLink" : "https://pbs.twimg.com/profile_images/921025087251386369/11o7UGiT_400x400.jpg",
        "score" : 1000,
        "challengeIndex" : 0
    },
        {
            "firstName":    "Amol",
            "lastName":     "Patil",
            "emailAddress":        "Amol@Patil.com",
            "profilePictureLink" : "https://pbs.twimg.com/profile_images/501901162359312385/ZPKIPQhm.jpeg",
            "score" : 1000,
            "challengeIndex" : 0
        }];

    var users = dbConnection.collection('users');
    users.insertOne(d[0], function(err,doc){
        if (err){
            console.log("Could not add user 1");
        }
        else {
            addChallengestoUser(doc.ops[0]._id.toString(),10);
        }
    })
    users.insertOne(d[1], function(err,doc){
        if (err){
            console.log("Could not add user 1");
        }
        else {
            addChallengestoUser(doc.ops[0]._id.toString(),10);
        }
    })

}


ownerChallengeImages = ['https://media.licdn.com/mpr/mpr/shrinknp_200_200/AAEAAQAAAAAAAAewAAAAJGU1NjYxNjcwLTMzOGMtNGQwZS05NWYyLTQ5NDZmNzNiYjAwNg.jpg','http://www.slicktext.com/text-marketing-news/wp-content/uploads/2015/06/29906170001_3725658930001_Screen-Shot-2014-08-11-at-4-26-24-PM.jpg','http://www.nandc.org/wp-content/uploads/2016/12/NANDC-Clean-Streets.jpg','https://www.cityoflondon.gov.uk/business/environmental-health/environmental-protection/air-quality/PublishingImages/clean-air-day-large.jpg','http://cleanair.org/wp-content/uploads/Flyer-Image.jpg','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSlKJh4i8hSKk2qcB5McrkcXEyMqebI7SC9pY8my4ihlQY4chl9','https://gogreenyouthchallenge.files.wordpress.com/2011/11/dscf82061.jpg','https://2.bp.blogspot.com/-9InRQxGTzsY/V8BcnSoBLOI/AAAAAAAAAFU/iM2ZdRtqNOAlVzzjl6IwE1vXoccbKjX4wCEw/s1600/IMG-20160826-WA0010.jpg','https://i.ytimg.com/vi/6cBHExk1API/maxresdefault.jpg'];
description= ['CMU Event','Product school event','CMU PM Club Event','Social Innovation club','General social cause','Give it back','Social Innovation','Coding Gym','Hackathon','Street Clean','Steps Challenge','Clean Air'];
name = ['CMU Event','Product school event','CMU PM Club Event','Social Innovation club','General social cause','Give it back','Social Innovation','Coding Gym','Hackathon','Street Clean','Steps Challenge','Clean Air'];

function addChallengestoUser(userId,count) {
    var c = [];
    for (i=0;i<count;i++){
        console.log("Trying")
        var challengeName = name[Math.floor(Math.random() * name.length)];
        var challengeDescription = description[Math.floor(Math.random() * description.length)];
        var challengeCreatedDate = "6 Nov 2017";
        var challengeType = "Social";
        var ownerChallengeImageLink = ownerChallengeImages[Math.floor(Math.random() * ownerChallengeImages.length)];

           c. push({
               challengeName: challengeName,
               challengeDescription: challengeDescription,
               challengeCreatedDate: challengeCreatedDate,
               challengeType: challengeType,
               ownerChallengeImageLink: ownerChallengeImageLink,
               userId: userId
           });

    }

    c.forEach(function(challenge){
        var challenges = dbConnection.collection('challenges');
        challenges.insertOne(challenge);
    })

}

setTimeout(closeConnection,5000);