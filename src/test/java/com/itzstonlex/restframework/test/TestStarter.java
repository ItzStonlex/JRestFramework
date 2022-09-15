package com.itzstonlex.restframework.test;

import com.itzstonlex.restframework.RestFrameworkBootstrap;
import com.itzstonlex.restframework.RestFrameworkStorage;
import com.itzstonlex.restframework.api.request.RestRequestMessage;
import com.itzstonlex.restframework.api.response.RestResponse;
import com.itzstonlex.restframework.test.service.RestClientTest;
import com.itzstonlex.restframework.test.service.RestServerTest;

import java.util.ArrayList;

public class TestStarter {

    public static void main(String[] args)
    throws Throwable {

        RestFrameworkStorage restStorage = RestFrameworkBootstrap.runServices(TestStarter.class);
        restStorage.initServer(RestServerTest.class, new ArrayList<>());

        // await for server bind.
        Thread.sleep(3000);

        // test client connection.
        RestClientTest restClient = restStorage.get(RestClientTest.class);

        restClient.addUserdata(RestRequestMessage.asJsonObject(new Userdata("itzstonlex", 18, 3)));

        Userdata itzstonlex = restClient.getUserdata("itzstonlex");
        RestResponse itzstonlexResponse = restClient.getUserdataResponse("itzstonlex");

        System.out.println("[Test] " + itzstonlex);
        System.out.println("[Test] " + itzstonlexResponse);
    }

}
