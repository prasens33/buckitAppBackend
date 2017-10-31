package edu.cmu.sv.app17.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import edu.cmu.sv.app17.exceptions.APPBadRequestException;
import edu.cmu.sv.app17.exceptions.APPInternalServerException;
import edu.cmu.sv.app17.exceptions.APPNotFoundException;
import edu.cmu.sv.app17.models.Image;
import edu.cmu.sv.app17.helpers.PATCH;

import edu.cmu.sv.app17.models.Challenge;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;



@Path("challenges")
public class ChallengeInterface {

    private MongoCollection<Document> collection = null;
    private MongoCollection<Document> challengeImageCollection;
    private ObjectWriter ow;

    public ChallengeInterface() {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("buckitDB4");
        collection = database.getCollection("challenges");
        this.challengeImageCollection = database.getCollection("challengeImages");
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public ArrayList<Challenge> getAll() {

        ArrayList<Challenge> challengeList = new ArrayList<Challenge>();

        try {
            FindIterable<Document> results = collection.find();
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

    @GET
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Challenge getOne(@PathParam("id") String id) {


        BasicDBObject query = new BasicDBObject();

        try {
            query.put("_id", new ObjectId(id));
            Document item = collection.find(query).first();
            if (item == null) {
                throw new APPNotFoundException(0, "No such challenge, my friend");
            }
            Challenge challenge  = new Challenge(
                    item.getString("challengeName"),
                    item.getString("challengeDescription"),
                    item.getString("challengeCreatedDate"),
                    item.getString("challengeType"),
                    item.getString("userId")
            );
            challenge.setId(item.getObjectId("_id").toString());
            return challenge;

        } catch(IllegalArgumentException e) {
            throw new APPBadRequestException(45,"Doesn't look like MongoDB ID");
        }  catch(Exception e) {
            throw new APPInternalServerException(99,"Something happened, pinch me!");
        }


    }

    @GET
    @Path("{id}/challengeImage")
    @Produces({MediaType.APPLICATION_JSON})
    public ArrayList<Image> getChallengeImageForUser(@PathParam("id") String id) {

        ArrayList<Image> challengeImageList = new ArrayList<Image>();

        try {
            BasicDBObject query = new BasicDBObject();
            query.put("challengeId", id);

            FindIterable<Document> results = challengeImageCollection.find(query);
            for (Document item : results) {
                String challengeId = item.getString("challengeId");
                Image image = new Image(
                        challengeId,
                        item.getString("challengeImageLink"),
                        item.getString("userId")

                );
                image.setId(item.getObjectId("_id").toString());
                challengeImageList.add(image);
            }
            return challengeImageList;

        } catch(Exception e) {
            System.out.println("EXCEPTION!!!!");
            e.printStackTrace();
            throw new APPInternalServerException(99,e.getMessage());
        }

    }

    @GET
    @Path("{id}/challengeImage/{userId}")
    @Produces({MediaType.APPLICATION_JSON})
    public ArrayList<Image> getOneChallengeImageForUser(@PathParam("id") String id, @PathParam("userId") String userId) {

        ArrayList<Image> challengeImageList = new ArrayList<Image>();

        try {
            BasicDBObject query = new BasicDBObject();
            query.put("challengeId", id);
            query.append("userId",userId);

            FindIterable<Document> results = challengeImageCollection.find(query);
            for (Document item : results) {
                String challengeId = item.getString("challengeId");
                Image image = new Image(
                        challengeId,
                        item.getString("challengeImageLink"),
                        item.getString("userId")

                );
                image.setId(item.getObjectId("_id").toString());
                challengeImageList.add(image);
            }
            return challengeImageList;

        } catch(Exception e) {
            System.out.println("EXCEPTION!!!!");
            e.printStackTrace();
            throw new APPInternalServerException(99,e.getMessage());
        }

    }



/*
    @DELETE
    @Path("{id}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Object delete(@PathParam("id") String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));

        DeleteResult deleteResult = collection.deleteOne(query);
        if (deleteResult.getDeletedCount() < 1)
            throw new APPNotFoundException(66,"Could not delete");

        return new JSONObject();
    }*/
}
