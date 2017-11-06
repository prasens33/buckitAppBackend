var MongoClient = require('mongodb').MongoClient;

var dbConnection = null;

var lockCount = 0;



function getDbConnection(callback){
    MongoClient.connect("mongodb://localhost/buckitDB8", function(err, db){
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
            addChallengestoUser(doc.ops[0]._id.toString(),50);
        }
    })
    users.insertOne(d[1], function(err,doc){
        if (err){
            console.log("Could not add user 1");
        }
        else {
            addChallengestoUser(doc.ops[0]._id.toString(),70);
        }
    })
}


ownerChallengeImages = ['http://www.memorymakersevents.com/my_uploads/2017/10/22046021_145141362759102_8650660159182733863_n.jpg','http://www.memorymakersevents.com/my_uploads/2017/10/22049802_145141372759101_736423111511193071_n.jpg',
'https://www.windycityfieldhouse.com/wp-content/uploads/2016/03/eventforacause2-1038x564.jpg','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRiAloVee439CfmXOAmJTlxgluRJxAOYtZKDW6FhMrgk9Xa2fGj',
'https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F36148470%2F201626737127%2F1%2Foriginal.jpg?h=230&w=460&rect=0%2C112%2C300%2C150&s=d0c6ff0ed00da2d64e4d9be64f528601','http://www.burnthillstables.com/uploads/7/8/4/6/7846873/edited/2-1932245.jpeg',
'http://stonebridge.carsonwealth.com/wp-content/uploads/sites/37/2017/03/ad-image-2-700x335.jpg','https://cdn.udou.ph/wp-content/uploads/2017/07/bookishtakish.jpg', 'https://www.eiseverywhere.com/file_uploads/2719165a041a253634f38484af24c5bc_SISDC16FrontPagePhotoBlock.jpg','https://nyc.socialinnovation.org/sites/default/files/TonyaCheerCrowd.jpg',
    'https://socialinnovation.org/wp-content/uploads/2016/06/192-lounge.jpg','http://social-innovation.hitachi/my/event/images/youtube1.jpg',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYfYHxx8PFWVSeC29ddtiHM_cbMWS57-63cYc1RUYjlsHMBSB50w','http://i1.wp.com/www.un.org/youthenvoy/wp-content/uploads/2016/01/UNDP-Egypt-1.jpeg?resize=540%2C265','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHcKcuMzgnIvpWSfjuolcdhu5oKtR0lmuf6i6-qFAjQReiOM63','http://www.causeartist.com/wp-content/uploads/2017/07/Collaborative-Conference-758x463.jpg',
    'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ15ufeedHUI-BLLF7BPDmQuckgmUYM7ag9taGqPIXVrwPXZIfH','https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTk7Rv4wx2J2up4eWSkmXTu2j2fPj62LyOpjiBp78AMkjJQuPNf',
    'https://www.techstars.com/uploads/care-society.png','https://static1.squarespace.com/static/58fd2ad159cc68d77606bfab/t/59066a789f7456663ec31f57/1493592705571/','http://www.babson.edu/news-events/babson-news-old/PublishingImages/2017-SIITLewis.jpg',
    'https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F30089803%2F62970121765%2F1%2Foriginal.jpg?w=1000&rect=0%2C323%2C678%2C339&s=a068b2bbf7369534d6f94396fa800913','https://lawyersagainstpoverty.org/wp-content/uploads/Calendar-Nepal2.jpg',
    'https://tours.allthingstreasurecoast.com/187380/events/trot_lg.jpg','https://cdn.talkpoverty.org/content/uploads/2014/05/joel-e1461333972241-938x625.jpg','http://www.cohsf.org/wp-content/uploads/2017/04/header_image.jpg','http://ancopcanada.org/wp-content/uploads/2014/10/websiteWalkThanks.jpg'];



function addChallengestoUser(userId,count) {
    var c = [];
    for (i=0;i<count;i++){
        console.log("Trying")
        var challengeName = "Social Challenge"+ userId+ i;
        var challengeDescription = "Social Event";
        var challengeCreatedDate = "5 Nov 2017";
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