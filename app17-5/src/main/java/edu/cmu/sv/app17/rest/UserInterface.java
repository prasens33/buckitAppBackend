package edu.cmu.sv.app17.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.cmu.sv.app17.exceptions.APPBadRequestException;
import edu.cmu.sv.app17.exceptions.APPInternalServerException;
import edu.cmu.sv.app17.exceptions.APPNotFoundException;
import edu.cmu.sv.app17.helpers.PATCH;
import edu.cmu.sv.app17.models.*;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import com.mongodb.async.SingleResultCallback;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


@Path("users")
public class UserInterface {

    private MongoCollection<Document> collection;
    private MongoCollection<Document> challengeCollection;
    private MongoCollection<Document> savedChallengeCollection;
    private MongoCollection<Document> completedChallengeCollection;
    private MongoCollection<Document> notificationCollection;
    private MongoCollection<Document> receivedChallengeCollection;
    private MongoCollection<Document> friendRequestCollection;
    private MongoCollection<Document> friendCollection;
    private MongoCollection<Document> challengeRequestCollection;
    private MongoCollection<Document> challengeImageCollection;
    private MongoCollection<Document> profileImageCollection;

    private ObjectWriter ow;


    public UserInterface() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("buckitDB4");

        this.collection = database.getCollection("users");
        this.challengeCollection = database.getCollection("challenges");
        this.savedChallengeCollection = database.getCollection("savedChallengeList");
        this.completedChallengeCollection = database.getCollection("completedChallengeList");
        this.receivedChallengeCollection = database.getCollection("ReceivedChallengeList");
        this.notificationCollection = database.getCollection("notifications");
        this.friendRequestCollection = database.getCollection("friendRequests");
        this.friendCollection = database.getCollection("friends");
        this.challengeRequestCollection = database.getCollection("challengeRequests");
        this.challengeImageCollection = database.getCollection("challengeImages");
        this.profileImageCollection = database.getCollection("profileImages");

        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }
    //GET all uses

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public ArrayList<User> getAll(@DefaultValue("_id") @QueryParam("sort") String sortArg) {
        sortArg ="score";
        BasicDBObject sortParams = new BasicDBObject();
        List<String> sortList = Arrays.asList(sortArg.split(","));
        sortList.forEach(sortItem -> {
            sortParams.put(sortItem,1);
        });

        ArrayList<User> userList = new ArrayList<User>();

        FindIterable<Document> results = collection.find().sort(sortParams);

        if (results == null) {
            return  userList;
        }


        for (Document item : results) {
            User user = new User(

            item.getString("firstName"),
                    item.getString("lastName"),
                    item.getString("emailAddress"),
                    item.getInteger("score"),
                    item.getString("profilePictureLink")

            );
            user.setId(item.getObjectId("_id").toString());
            userList.add(user);
        }


        return userList;
    }
    //Get one user

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public User getOne(@PathParam("id") String id) {
        BasicDBObject query = new BasicDBObject();
        try {
            query.put("_id", new ObjectId(id));
            Document item = collection.find(query).first();
            if (item == null) {
                throw new APPNotFoundException(0, "No such challenge, my friend");
            }
            User user = new User(
                    item.getString("firstName"),
                    item.getString("lastName"),
                    item.getString("emailAddress"),
                    item.getInteger("score"),
                    item.getString("profilePictureLink")

            );
            user.setId(item.getObjectId("_id").toString());
            return user;

        } catch(APPNotFoundException e) {
            throw new APPNotFoundException(0,"No such challenge");
        } catch(IllegalArgumentException e) {
            throw new APPBadRequestException(45,"Doesn't look like MongoDB ID");
        }  catch(Exception e) {
            throw new APPInternalServerException(99,"Something happened, pinch me!");
        }


    }

    //GET challenges for a specific user

    @GET
    @Path("{id}/challenges")
    @Produces({MediaType.APPLICATION_JSON})
    public ArrayList<Challenge> getChallengesForUser(@PathParam("id") String id) {

        ArrayList<Challenge> challengeList = new ArrayList<Challenge>();

        try {
            BasicDBObject query = new BasicDBObject();
            query.put("userId", id);

            FindIterable<Document> results = challengeCollection.find(query);
            for (Document item : results) {
                String challengeName = item.getString("challengeName");
                Challenge challenge = new Challenge(
                        challengeName,
                        item.getString("challengeDescription"),
                        item.getString("challengeCreatedDate"),
                        item.getString("challengeType"),
                        item.getString("userId")


                );
                challenge.setId(item.getObjectId("_id").toString());
                challengeList.add(challenge);
            }
            return challengeList;

        } catch(Exception e) {
            System.out.println("EXCEPTION!!!!");
            e.printStackTrace();
            throw new APPInternalServerException(99,e.getMessage());
        }

    }


    //Post a challenge for a specific user
    @POST
    @Path("{id}/challenges")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Object create(@PathParam("id") String id, Object request) {
        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }
        if (!json.has("challengeName"))
            throw new APPBadRequestException(55,"missing challengeName");
        if (!json.has("challengeDescription"))
            throw new APPBadRequestException(55,"missing challengeDescription");
        if (!json.has("challengeCreatedDate"))
            throw new APPBadRequestException(55,"missing challengeCreatedDate");
        if (!json.has("challengeType"))
            throw new APPBadRequestException(55,"missing challengeType");

        Document doc = new Document("challengeName", json.getString("challengeName"))
                .append("challengeDescription", json.getString("challengeDescription"))
                .append("challengeCreatedDate", json.getString("challengeCreatedDate"))
                .append("challengeType", json.getString("challengeType"))
                .append("userId", id);
        addPoints(id);
        challengeCollection.insertOne(doc);
        return request;
    }


    //Post a user
    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Object create(Object request) {

        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }
            if (!json.has("firstName") ) {
                throw new APPBadRequestException(55,"missing firstName");
            }
        if (!json.has("lastName") ) {
            throw new APPBadRequestException(55,"missing lastName");
        }
        if (!json.has("emailAddress") ) {
            throw new APPBadRequestException(55,"missing emailAddress");
        }
        if (!json.has("score") ) {
            throw new APPBadRequestException(55,"missing score");
        }
        if (!json.has("profilePictureLink") ) {
            throw new APPBadRequestException(55,"missing profilePictureLink");
        }


            // You need to add all other fields
            Document doc = new Document("firstName", json.getString("firstName"))
                    .append("lastName", json.getString("lastName"))
                    .append("emailAddress", json.getString("emailAddress"))
                    .append("score", json.getInt("score"))
                    .append("profilePictureLink", json.getString("profilePictureLink"));
        //collection.insertOne(doc);
        //ObjectId id = (ObjectId)doc.get("_id");
        collection.insertOne(doc);



        return request;
    }

    //Patch a specific user
    @PATCH
    @Path("{id}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Object update(@PathParam("id") String id, Object request) {
        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }

        try {

            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document doc = new Document();
            if (json.has("firstName"))
                doc.append("firstName",json.getString("firstName"));
            if (json.has("lastName"))
                doc.append("lastName",json.getString("lastName"));
            if (json.has("emailAddress"))
                doc.append("emailAddress",json.getString("emailAddress"));
            if (json.has("score"))
                doc.append("score",json.getInt("score"));
            if (json.has("profilePictureLink"))
                doc.append("profilePictureLink",json.getInt("profilePictureLink"));
            Document set = new Document("$set", doc);
            collection.updateOne(query,set);

        } catch(JSONException e) {
            System.out.println("Failed to create a document");

        }
        return request;
    }

    
    
    

    //Patch a challenge
    @PATCH
    @Path("{id}/challenges/{challengeId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Object updateChallenges(@PathParam("challengeId") String id, Object request) {
        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }

        try {

            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document doc = new Document();
            if (json.has("challengeName"))
                doc.append("challengeName",json.getString("challengeName"));
            if (json.has("challengeDescription"))
                doc.append("challengeDescription",json.getString("challengeDescription"));
            if (json.has("challengeCreatedDate"))
                doc.append("challengeCreatedDate",json.getString("challengeCreatedDate"));
            if (json.has("challengeType"))
                doc.append("challengeType",json.getString("challengeType"));
            Document set = new Document("$set", doc);
            challengeCollection.updateOne(query,set);

        } catch(JSONException e) {
            System.out.println("Failed to create a document");

        }
        return request;
    }
    //Get One challenge
    @GET
    @Path("{id}/challenges/{challengeId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Challenge getOneChallenge(@PathParam("challengeId") String id) {
        BasicDBObject query = new BasicDBObject();
        try {
            query.put("_id", new ObjectId(id));
            Document item = challengeCollection.find(query).first();
            if (item == null) {
                throw new APPNotFoundException(0, "No such challenge, my friend");
            }
            Challenge challenge = new Challenge(
                    item.getString("challengeName"),
                    item.getString("challengeDescription"),
                    item.getString("challengeCreatedDate"),
                    item.getString("challengeType"),
                    item.getString("userId")
            );
            challenge.setId(item.getObjectId("_id").toString());
            return challenge;

        } catch(APPNotFoundException e) {
            throw new APPNotFoundException(0,"No such challenge");
        } catch(IllegalArgumentException e) {
            throw new APPBadRequestException(45,"Doesn't look like MongoDB ID");
        }  catch(Exception e) {
            throw new APPInternalServerException(99,"Something happened, pinch me!");
        }

    }


    //SAVE/POST a challenge in savedChallengeList
    @POST
    @Path("{id}/savedChallengeList")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Object createSavedChallengeList(@PathParam("id") String id, Object request) {
        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }
        if (!json.has("challengeId"))
            throw new APPBadRequestException(55,"missing challengeId");

        Document doc = new Document("challengeId", json.getString("challengeId"))
                .append("userId", id);
        savedChallengeCollection.insertOne(doc);
        return request;
    }


    //GET all saved challenges
    @GET
    @Path("{id}/savedChallengeList")
    @Produces({MediaType.APPLICATION_JSON})
    public ArrayList<SavedChallengeList> getSavedChallengeListForUser(@PathParam("id") String id) {

        ArrayList<SavedChallengeList> savedchallengeList = new ArrayList<SavedChallengeList>();

        try {
            BasicDBObject query = new BasicDBObject();
            query.put("userId", id);

            FindIterable<Document> results = savedChallengeCollection.find(query);
            for (Document item : results) {
                String challengeId = item.getString("challengeId");
                SavedChallengeList savedChallenge = new SavedChallengeList(
                        challengeId,
                        item.getString("userId")
                );
                savedChallenge.setId(item.getObjectId("_id").toString());
                savedchallengeList.add(savedChallenge);
            }
            return savedchallengeList;

        } catch(Exception e) {
            System.out.println("EXCEPTION!!!!");
            e.printStackTrace();
            throw new APPInternalServerException(99,e.getMessage());
        }

    }


    //COMPLETE a challenge
    @POST
    @Path("{id}/completedChallengeList")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Object createCompletedChallengeList(@PathParam("id") String id, Object request) {
        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }
        if (!json.has("challengeId"))
            throw new APPBadRequestException(55,"missing challengeId");

        Document doc = new Document("challengeId", json.getString("challengeId"))
                .append("userId", id);
        addPoints(id);
        completedChallengeCollection.insertOne(doc);
        return request;
    }
    //GET completed challenges
    @GET
    @Path("{id}/completedChallengeList")
    @Produces({MediaType.APPLICATION_JSON})
    public ArrayList<CompletedChallengeList> getCompletedChallengeListForUser(@PathParam("id") String id) {

        ArrayList<CompletedChallengeList> completedChallengeList = new ArrayList<CompletedChallengeList>();

        try {
            BasicDBObject query = new BasicDBObject();
            query.put("userId", id);

            FindIterable<Document> results = completedChallengeCollection.find(query);
            for (Document item : results) {
                String challengeId = item.getString("challengeId");
                CompletedChallengeList completedChallenge = new CompletedChallengeList(
                        challengeId,
                        item.getString("userId")
                );
                completedChallenge.setId(item.getObjectId("_id").toString());
                completedChallengeList.add(completedChallenge);
            }
            return completedChallengeList;

        } catch(Exception e) {
            System.out.println("EXCEPTION!!!!");
            e.printStackTrace();
            throw new APPInternalServerException(99,e.getMessage());
        }

    }


    //Add a notification
    @POST
    @Path("{id}/notifications")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Object createNotification(@PathParam("id") String id, Object request) {
        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }
        if (!json.has("notificationType"))
            throw new APPBadRequestException(55,"missing notification type");

        Document doc = new Document("notificationType", json.getString("notificationType"))
                .append("userId", id);
        notificationCollection.insertOne(doc);
        return request;
    }

    public void addNotification(String id,String notificationType) {
        JSONObject json = null;

        Document doc = new Document("notificationType", notificationType)
                .append("userId", id);
        notificationCollection.insertOne(doc);

    }



    @GET
    @Path("{id}/notifications")
    @Produces({MediaType.APPLICATION_JSON})
    public ArrayList<Notification> getNotificationsForUser(@PathParam("id") String id) {

        ArrayList<Notification> notificationList = new ArrayList<Notification>();

        try {
            BasicDBObject query = new BasicDBObject();
            query.put("userId", id);

            FindIterable<Document> results = notificationCollection.find(query);
            for (Document item : results) {
                String notificationType = item.getString("notificationType");
                Notification notification = new Notification(
                        notificationType,
                        item.getString("userId")
                );
                notification.setId(item.getObjectId("_id").toString());
                notificationList.add(notification);
            }
            return notificationList;

        } catch(Exception e) {
            System.out.println("EXCEPTION!!!!");
            e.printStackTrace();
            throw new APPInternalServerException(99,e.getMessage());
        }

    }
    //
    public void createReceivedChallenges(String challengeId, String userId) {

        Document doc = new Document("challengeId", challengeId)
                .append("userId", userId);
        receivedChallengeCollection.insertOne(doc);

    }

    @GET
    @Path("{id}/receivedChallengeList")
    @Produces({MediaType.APPLICATION_JSON})
    public ArrayList<ReceivedChallengeList> getReceivedChallengeListForUser(@PathParam("id") String id) {

        ArrayList<ReceivedChallengeList> receivedChallengeList = new ArrayList<ReceivedChallengeList>();

        try {
            BasicDBObject query = new BasicDBObject();
            query.put("userId", id);

            FindIterable<Document> results = receivedChallengeCollection.find(query);
            for (Document item : results) {
                String challengeId = item.getString("challengeId");
                ReceivedChallengeList receivedChallenge = new ReceivedChallengeList(
                        challengeId,
                        item.getString("userId")
                );
                receivedChallenge.setId(item.getObjectId("_id").toString());
                receivedChallengeList.add(receivedChallenge);
            }
            return receivedChallengeList;

        } catch(Exception e) {
            System.out.println("EXCEPTION!!!!");
            e.printStackTrace();
            throw new APPInternalServerException(99,e.getMessage());
        }

    }
    @POST
    @Path("{id}/friendRequest")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Object createFriendRequest(@PathParam("id") String id, Object request) {
        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }
        if (!json.has("receiverId"))
            throw new APPBadRequestException(55,"missing receiverId");
        if (!json.has("requestStatus"))
            throw new APPBadRequestException(55,"missing requestStatus");

        Document doc = new Document("receiverId", json.getString("receiverId"))
                .append("receiverFirstName", json.getString("receiverFirstName"))
                .append("receiverLastName", json.getString("receiverLastName"))
                .append("senderId", id)
                .append("senderFirstName", json.getString("senderFirstName"))
                .append("senderLastName", json.getString("senderLastName"))
                .append("requestStatus", json.getBoolean("requestStatus"));
        friendRequestCollection.insertOne(doc);
        addNotification(json.getString("receiverId"),"friendRequest");
        return request;
    }
    @GET
    @Path("{id}/friendRequest")
    @Produces({MediaType.APPLICATION_JSON})
    public ArrayList<FriendRequest> getReceivedFriendRequests(@PathParam("id") String id) {

        ArrayList<FriendRequest> receivedFriendRequestList = new ArrayList<FriendRequest>();

        try {
            BasicDBObject query = new BasicDBObject();
            query.put("receiverId", id);

            FindIterable<Document> results = friendRequestCollection.find(query);
            for (Document item : results) {
                String receiverId = item.getString("receiverId");
                FriendRequest friendRequest = new FriendRequest(
                        receiverId,
                        item.getString("receiverFirstName"),
                        item.getString("receiverLastName"),
                        item.getString("senderId"),
                        item.getString("senderFirstName"),
                        item.getString("senderLastName"),
                        item.getBoolean("requestStatus")

                );
                friendRequest.setId(item.getObjectId("_id").toString());
                receivedFriendRequestList.add(friendRequest);
            }
            return receivedFriendRequestList;

        } catch(Exception e) {
            System.out.println("EXCEPTION!!!!");
            e.printStackTrace();
            throw new APPInternalServerException(99,e.getMessage());
        }

    }

    @PATCH
    @Path("{id}/friendRequest/{requestId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Object updateFriendRequestStatus(@PathParam("requestId") String id, Object request) {
        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }
        try {

            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));

            Document doc = new Document();

            if (json.has("requestStatus"))
                doc.append("requestStatus",json.getBoolean("requestStatus"));
            Document set = new Document("$set", doc);
            Document item = friendRequestCollection.find(query).first();
            if (item == null) {
                throw new APPNotFoundException(0, "No such friendRequest, my friend");
            }
            String senderId = item.getString("senderId");
            String senderFirstName = item.getString("senderFirstName");
            String senderLastName = item.getString("senderLastName");

            String receiverFirstName = item.getString("receiverFirstName");
            String receiverLastName = item.getString("receiverLastName");
            String receiverId = item.getString("receiverId");

            addFriend(senderId,receiverId,senderFirstName,senderLastName,receiverFirstName,receiverLastName);
            addFriend(receiverId,senderId,receiverFirstName,receiverLastName,senderFirstName,senderLastName);
            friendRequestCollection.updateOne(query,set);




        } catch(JSONException e) {
            System.out.println("Failed to create a document");

        }
        return request;
    }

//    @POST
//    @Path("{id}/friend")
//    @Consumes({ MediaType.APPLICATION_JSON})
//    @Produces({ MediaType.APPLICATION_JSON})
    public void addFriend(String senderId, String receiverId, String senderFirstName, String senderLastName, String receiverFirstName, String receiverLastName ) {


        Document doc = new Document("userId", senderId)
                .append("friendId", receiverId)
                .append("userFirstName", senderFirstName)
                .append("userLastName", senderLastName)
                .append("friendFirstName", receiverFirstName)
                .append("friendLastName", receiverLastName);

        friendCollection.insertOne(doc);
    }
    @GET
    @Path("{id}/friends")
    @Produces({MediaType.APPLICATION_JSON})
    public ArrayList<Friend> getFriends(@PathParam("id") String id) {

        ArrayList<Friend> friendList = new ArrayList<Friend>();

        try {
            BasicDBObject query = new BasicDBObject();
            query.put("userId", id);

            FindIterable<Document> results = friendCollection.find(query);
            for (Document item : results) {
                String userId = item.getString("userId");
                Friend friend = new Friend(
                        userId,
                        item.getString("friendId"),
                        item.getString("userFirstName"),
                        item.getString("userLastName"),
                        item.getString("friendFirstName"),
                        item.getString("friendLastName")

                );
                friend.setId(item.getObjectId("_id").toString());
                friendList.add(friend);
            }
            return friendList;

        } catch(Exception e) {
            System.out.println("EXCEPTION!!!!");
            e.printStackTrace();
            throw new APPInternalServerException(99,e.getMessage());
        }

    }

    @POST
    @Path("{id}/challengeRequest")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Object createChallengeRequest(@PathParam("id") String id, Object request) {
        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }

        if (!json.has("challengeReceiverId"))
            throw new APPBadRequestException(55,"missing challengeReceiverId");
        if (!json.has("challengeId"))
            throw new APPBadRequestException(55,"missing challengeId");

        Document doc = new Document("challengerId", id)
                .append("challengeReceiverId", json.getString("challengeReceiverId"))
                .append("challengeId", json.getString("challengeId"));

        challengeRequestCollection.insertOne(doc);
        addNotification(json.getString("challengeReceiverId"),"challengeRequest");
        createReceivedChallenges(json.getString("challengeId"),json.getString("challengeReceiverId"));
        return request;
    }

    @POST
    @Path("{id}/challengeImage")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Object postChallengeImage(@PathParam("id") String id, Object request) {
        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }

        if (!json.has("challengeId"))
            throw new APPBadRequestException(55,"missing challengeId");
        if (!json.has("challengeImageLink"))
            throw new APPBadRequestException(55,"missing challengeImageLink");

        Document doc = new Document("challengeId", json.getString("challengeId"))
                .append("challengeImageLink", json.getString("challengeImageLink"))
                .append("userId", id);

        challengeImageCollection.insertOne(doc);

        return request;
    }


/*
    @POST
    @Path("{id}/profileImage")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public Object postProfileImage(@PathParam("id") String id, Object request) {
        JSONObject json = null;
        try {
            json = new JSONObject(ow.writeValueAsString(request));
        }
        catch (JsonProcessingException e) {
            throw new APPBadRequestException(33, e.getMessage());
        }

        if (!json.has("challengeImageLink"))
            throw new APPBadRequestException(55,"missing challengeImageLink");

        Document doc = new Document("challengeImageLink", json.getString("challengeImageLink"))
                .append("userId", id);

        profileImageCollection.insertOne(doc);

        return request;
    }

*/

    public void addPoints(String id) {
        try {
            Integer score;

            BasicDBObject query = new BasicDBObject();
            try {
                query.put("_id", new ObjectId(id));
                Document item = collection.find(query).first();
                if (item == null) {
                    throw new APPNotFoundException(0, "No such user, my friend");
                }
                        score = item.getInteger("score");
                        score = score + 100;

            } catch(APPNotFoundException e) {
                throw new APPNotFoundException(0,"No such User");
            } catch(IllegalArgumentException e) {
                throw new APPBadRequestException(45,"Doesn't look like MongoDB ID");
            }  catch(Exception e) {
                throw new APPInternalServerException(99,"Something happened, pinch me!");
            }

            Document doc = new Document();

            doc.append("score",score);
            Document set = new Document("$set", doc);
            collection.updateOne(query,set);

        } catch(JSONException e) {
            System.out.println("Failed to create a document");

        }
    }


}
