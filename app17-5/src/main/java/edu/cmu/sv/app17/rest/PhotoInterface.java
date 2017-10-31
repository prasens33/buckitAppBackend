package edu.cmu.sv.app17.rest;

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PhotoInterface {
    public static void main(String [] args) throws IOException {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "sem",
                "api_key", "445234287149499",
                "api_secret", "uem9FjHRaaRofdChZHKbtiLp04E"));


        File toUpload = new File("/Users/prasen/Desktop/_DSC5277.jpg");
        Map uploadResult = cloudinary.uploader().upload(toUpload, ObjectUtils.emptyMap());

        System.out.println("uploadResult : " + uploadResult);

    }

}
