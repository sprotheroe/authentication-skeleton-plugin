/*
 * Copyright 2016 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.authentication.executors;

import com.example.authentication.PluginSettings;
import com.example.authentication.RequestExecutor;
import com.example.authentication.domain.User;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.request.DefaultGoApiRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.HashMap;
import java.util.Map;

import static com.example.authentication.Constants.PLUGIN_IDENTIFIER;
import static com.example.authentication.ExamplePlugin.LOG;
import static com.example.authentication.GoRequest.GO_REQUEST_AUTHENTICATE_USER;
import static com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse.REDIRECT_RESPONSE_CODE;
import static com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse.VALIDATION_ERROR;

public class AuthenticationExecutor implements RequestExecutor {
    public static final String LOCATION = "Location";
    private static final Gson GSON = new Gson();
    private final GoPluginApiRequest request;
    private final PluginSettings settings;
    private GoApplicationAccessor accessor;

    public AuthenticationExecutor(GoApplicationAccessor accessor, GoPluginApiRequest request, PluginSettings settings) {
        this.accessor = accessor;
        this.request = request;
        this.settings = settings;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {

        User user = validateAndGetUser();

        int responseCode = REDIRECT_RESPONSE_CODE;
        if (user != null) {
            authenticateUser(user);
        } else {
            responseCode = VALIDATION_ERROR;
        }

        DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(responseCode);
        response.addResponseHeader(LOCATION, settings.getGoServerUrl());
        return response;
    }

    //TODO: Validate and return user object
    private User validateAndGetUser() {
        String username = request.requestParameters().get("username");
        String password = request.requestParameters().get("password");

        if ("gocd".equals(username) && "password".equals(password))
            return new User(username, "GoCDAdmin", "admin@go");
        return null;
    }

    public void authenticateUser(User user) {
        final Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("user", user);
        DefaultGoApiRequest authenticateUserRequest = new DefaultGoApiRequest(GO_REQUEST_AUTHENTICATE_USER.requestName(), "1.0", PLUGIN_IDENTIFIER);
        authenticateUserRequest.setRequestBody(GSON.toJson(userMap));
        GoApiResponse authenticateUserResponse = accessor.submit(authenticateUserRequest);
        LOG.error("Authenticate user: " + authenticateUserResponse.responseCode());
    }
}
