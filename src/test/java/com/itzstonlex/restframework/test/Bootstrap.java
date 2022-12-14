package com.itzstonlex.restframework.test;

import com.itzstonlex.restframework.RestFrameworkBootstrap;
import com.itzstonlex.restframework.RestServicePublicManager;
import com.itzstonlex.restframework.api.context.RestBody;
import com.itzstonlex.restframework.api.context.response.RestResponse;

import java.util.ArrayList;

public final class Bootstrap {

    public static void main(String[] args) {
        System.setProperty("rest.auth.password", "password");
        System.setProperty("rest.auth.token", "TestToken123");

        RestServicePublicManager rest = RestFrameworkBootstrap.runServices(Bootstrap.class);

        // Bind a REST-server
        rest.bind(RestServerTest.class, new ArrayList<>());

        // Get initialized REST-client
        RestClientTest restClient = rest.get(RestClientTest.class);

        // Add user & print response.
        RestBody adduserBody = RestBody.fromConvertedObject(new Userdata("itzstonlex", 18, 3));
        System.out.println("[Test] " + restClient.addUserdata(adduserBody));

        // Get response-data at variables.
        Userdata itzstonlex = restClient.getUserdata("itzstonlex");
        RestResponse itzstonlexResponse = restClient.getUserdataAsResponse("itzstonlex");

        // Print responses.
        System.out.println("[Test] " + itzstonlex);
        System.out.println("[Test] " + itzstonlexResponse);
        System.out.println("[Test] " + restClient.getCachedUserdataList(2));
    }

}
