package com.itzstonlex.restframework.test.service;

import com.itzstonlex.restframework.api.*;
import com.itzstonlex.restframework.api.method.Get;
import com.itzstonlex.restframework.api.method.Post;
import com.itzstonlex.restframework.api.RestBody;
import com.itzstonlex.restframework.api.request.RestRequestContext;
import com.itzstonlex.restframework.api.response.RestResponse;
import com.itzstonlex.restframework.test.Userdata;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.itzstonlex.restframework.api.response.RestResponse.CLIENT_ERROR;
import static com.itzstonlex.restframework.api.response.RestResponse.SUCCESS;

@RestService
@RestServer(host = "localhost", port = 8082, defaultContext = "/api")
@RestFlag(RestFlag.Type.ASYNC_REQUESTS)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class RestServerTest {

    private List<Userdata> userdataList;

    @RestExceptionHandler
    public void onExceptionThrow(Exception exception) {
        System.out.println("HANDLE EXCEPTION!");

        exception.printStackTrace();
    }

    @Get(context = "/users")
    public RestResponse onUsersGet() {
        return RestResponse.createOnlyBody(SUCCESS, userdataList);
    }

    @Get(context = "/users")
    public RestResponse onLimitedUsersGet(@RestParam("limit") long limit) {
        return RestResponse.createOnlyBody(SUCCESS, userdataList.stream().limit(limit).collect(Collectors.toList()));
    }

    @Get(context = "/user")
    public RestResponse onUserGet(@RestParam("name") String name) {
        Userdata userdata = userdataList.stream()
                .filter(cached -> cached.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if (userdata == null) {
            return RestResponse.createOnlyBody(CLIENT_ERROR + 4, RestBody.asText("Userdata is not found"));
        }

        return RestResponse.createOnlyBody(SUCCESS, userdata);
    }

    @Post(context = "/adduser")
    public RestResponse onUserAdd(@RestParam RestRequestContext context) {
        if (!context.getFirstHeader("Auth-Token").equals("testToken")) {
            throw new IllegalArgumentException("Auth-Token");
        }

        RestBody message = context.getBody();

        Userdata newUserdata = message.getBodyAsJsonObject(Userdata.class);

        userdataList.add(newUserdata);

        message.setValue("Successfully added");
        return RestResponse.createOnlyBody(SUCCESS, message);
    }

}
