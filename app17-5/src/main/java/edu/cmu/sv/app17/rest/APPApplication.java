package edu.cmu.sv.app17.rest;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

//@ApplicationPath("/")

@Path("")

public class APPApplication extends ResourceConfig {
    public APPApplication() {
        // Define the package which contains the service classes.
        packages("SEM Project");
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public Object getAll() {
        Version ver = new Version("1.1.2", "2017-11-05");
        return ver;
    }

    public class Version {
        String version, date;
        public Version(String version,String date) {
            this.version = version;
            this.date = date;
        }

    }


}