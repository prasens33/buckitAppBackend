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
        "firstName":    "System",
        "lastName":     "Admin",
        "emailAddress":  "sytem@admin.com",
        "profilePictureLink" : "",
        "score" : 0,
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


ownerChallengeImages = ['https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F34361394%2F40199627302%2F1%2Foriginal.jpg?w=800&rect=0%2C0%2C4320%2C2160&s=9bd097fbf5f32048a0abef78a6aeeecc','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F37606177%2F12985183433%2F1%2Foriginal.jpg?w=800&rect=0%2C0%2C1878%2C939&s=62ef456e5a63e2b6f108be0c97ebf2bf','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F36388254%2F63491087173%2F1%2Foriginal.jpg?w=800&rect=0%2C13%2C1200%2C600&s=229460facb3e784a4e8147f69cf902cf','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F37523292%2F32854382%2F1%2Foriginal.jpg?w=800&rect=0%2C54%2C2548%2C1274&s=f9ceb05d85f9ad0e0588a26ec8565c39','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F37448322%2F8936028377%2F1%2Foriginal.jpg?w=800&rect=0%2C0%2C2160%2C1080&s=b235e383693dc85264de95d6300242eb','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F32185291%2F11280211593%2F1%2Foriginal.jpg?w=800&rect=0%2C3%2C2296%2C1148&s=7b11b48a9c9aa7636d97481a93f7477c','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F37873327%2F18284253071%2F1%2Foriginal.jpg?w=800&rect=0%2C0%2C2160%2C1080&s=e499cdd77ef7da218f70d8938f1a490e','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F36152395%2F2015048287%2F1%2Foriginal.jpg?w=800&rect=0%2C0%2C2160%2C1080&s=6cff070a3868666bb228882acf71ac36','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F37429480%2F20578739830%2F1%2Foriginal.jpg?w=800&rect=0%2C0%2C2160%2C1080&s=a048ad8cfe94d2e39b7c6096e2c36efd','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F37257451%2F740130902%2F1%2Foriginal.jpg?w=800&rect=2%2C0%2C642%2C321&s=cf64fe98aab065712ee964d6d9d78cf7','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F37030364%2F102530191283%2F1%2Foriginal.jpg?w=800&rect=0%2C384%2C536%2C268&s=d7f5b88994abb8afb24b31d0867168b3','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F37683062%2F215213838698%2F1%2Foriginal.jpg?w=800&rect=18%2C0%2C2362%2C1181&s=e7e5458b626adbfabd7109994edbfb30','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F36017844%2F203908347377%2F1%2Foriginal.jpg?w=800&rect=0%2C52%2C1588%2C794&s=ebe21279d2387c035a1a8cf61909bf02','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F37438973%2F217518585780%2F1%2Foriginal.jpg?w=800&rect=0%2C82%2C1650%2C825&s=0d7e21a7daef6349a2d95f990a117c22','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F37738143%2F217668632808%2F1%2Foriginal.jpg?w=800&rect=0%2C65%2C1800%2C900&s=59a6af6c35e8f1ea3786f6dda9e92234','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F37662035%2F53968926390%2F1%2Foriginal.jpg?w=800&rect=0%2C0%2C750%2C375&s=08aa7545e4c8fb769bae4ceecbf23d93','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F37217034%2F206975673405%2F1%2Foriginal.jpg?w=800&rect=0%2C2%2C384%2C192&s=bf54334b0eba2412e0deb3dca84f7397','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F36268153%2F68106480343%2F1%2Foriginal.jpg?w=800&rect=0%2C558%2C3300%2C1650&s=242a0a66ebca78cfab89c7ae47bb57b4','https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F36651695%2F231395602651%2F1%2Foriginal.jpg?w=800&rect=0%2C0%2C1064%2C532&s=c14ae181ade0866ada99a46bf6f51578',''];
description= ['National AIDS Memorial donation camp','Futures Without Violence x Argent x Phenomenal Woman Action Campaign','Join us for our first-ever "A Night in Bold," a celebration of the most powerful stories in 2017 at the historic Veteran\'s Building in San Francisco.','Join us to celebrate all the work our grantee partners are doing to make this world better for women and girls.','Join us for this unique opportunity to #Resist the status quo and #Persist towards gender equality.','Enjoy wine, hors d\'oeuvres, and fantastic views aboard FDRs presidential yacht, the U.S.S. Potomac--also known as the Floating White House.','You are invited to join us as a participant, sponsor or donor to our upcoming Men’s Conference','Please join the Sustainable Economies Law Center and friends on December 5th, 2017 for our 8th Annual Fall Celebration and Showcase!','AN EXCLUSIVE HOLIDAY EVENT FEATURING LOCAL TASTEMAKERS IN FOOD, WINE, LIBATIONS AND ART','Kick off the holiday season and party with a purpose with the ladies of ​Miller, Harper, Williams!','City Embrace Counseling and Resource Center would like to invite you to our fundraising Brunch.','Join us for an evening of film and conversation around water, the mind and culture - and delicious Bengali food!','Ars Nova ( Latin for " New Art" ) is an eclectic musical series featuring Oakland musicians, bands ,rappers and singers.','Join us for Contra Costa NOW”s 7th Annual Holiday Party!Celebrate the season with delicious food and drink, stimulating conversation, holiday-themed games, and donation drive.','A project of the African People\'s Education and Defense Fund Dr. Martin Luther King Jr. Event and Day of Service','Palava H.U.T. (Hearing, Understanding and Teaching) is an open forum which allows communities to discuss various problems within the community.','All are welcome to attend, however we will have some garden activities planned for these days for the special needs individuals and their families, Activities include: planting, harvesting, weeding, and more.','First Annual, Shattering Barriers Symposium','Acknowledge our partners; supporters; and unsung heroes in the Fairfield, Suisun City, and Vacaville communities.',''];
name = ['Light in the Grove 2017','Phenomenal Woman Conversation & Benefit Reception','A Night in Bold: Celebrating a Year in Social Justice Storytelling','Holiday Party SparkSF','The #Resist Party','2018 Pride Law Fund Annual Bay Cruise','Men\'s Conference Empowering Lives, Building Resiliency','2017 Sustainable Economies Law Center Fall Celebration & Showcase!','Tis\' The Season','Party for a Purpose','City Embrace Fundraiser & Kat\'s Birthday Party','Where the Waves Bloom: an evening of film, food and conversation','Ars Nova ,Benefit Concert','7th Annual Holiday Party','Uhuru MLK Day','H.E.A.L\'s Palava H.U.T presents Small Small Things','Family Workday-Holiday in the Gardens','Shattering Barriers Symposium: A Human Trafficking Gathering','16 Dec 2017',''];
date = ['30 Nov 2017','5 Dec 2017','29 Nov 2017','5 Dec 2017','7 Dec 2017','9 Jun 2018','16 Dec 2017','5 Dec 2017','7 Dec 2017','2 Dec 2017','3 Dec 2017','28 Dec 2017','3 Dec 2017','9 Dec 2017','15 Jan 2018','10 Dec 2017','9 Dec 2017','8 Dec 2017',''];

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