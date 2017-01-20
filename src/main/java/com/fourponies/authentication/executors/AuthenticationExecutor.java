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

package com.fourponies.authentication.executors;

import com.fourponies.authentication.RequestExecutor;
import com.fourponies.authentication.domain.User;
import com.fourponies.authentication.LdapClient;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.request.DefaultGoApiRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.HashMap;
import java.util.Map;

import static com.fourponies.authentication.Constants.PLUGIN_IDENTIFIER;
import static com.fourponies.authentication.GoRequest.GO_REQUEST_AUTHENTICATE_USER;
import static com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse.REDIRECT_RESPONSE_CODE;
import static com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse.VALIDATION_ERROR;

import static com.fourponies.authentication.LdapStartTlsPlugin.LOG;

public class AuthenticationExecutor implements RequestExecutor {
    public static final String LOCATION = "Location";
    private static final Gson GSON = new Gson();
    private final GoPluginApiRequest request;
    private final LdapClient ldapClient;
    private GoApplicationAccessor accessor;

    public AuthenticationExecutor(GoApplicationAccessor accessor, GoPluginApiRequest request, LdapClient ldapClient) {
        this.accessor = accessor;
        this.request = request;
        this.ldapClient = ldapClient;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        DefaultGoPluginApiResponse response = null;

        User user = validateAndGetUser();
        if (user != null) {
            final Map<String, Object> userMap = new HashMap<String, Object>();
            userMap.put("user", user);
            response = new DefaultGoPluginApiResponse(200, GSON.toJson(userMap));
        } else {
            response = new DefaultGoPluginApiResponse(VALIDATION_ERROR);
        }
        return response;
    }

    //TODO: Validate and return user object
    @SuppressWarnings("unchecked")
    private User validateAndGetUser() {
        Map<String, String> credentialMap = GSON.fromJson(request.requestBody(), Map.class);
        String username = credentialMap.get("username");
        String password = credentialMap.get("password");

        return ldapClient.authenticate(username, password);
    }

    public void authenticateUser(User user) {
        final Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("user", user);
        DefaultGoApiRequest authenticateUserRequest = new DefaultGoApiRequest(GO_REQUEST_AUTHENTICATE_USER.requestName(), "1.0", PLUGIN_IDENTIFIER);
        authenticateUserRequest.setRequestBody(GSON.toJson(userMap));
        GoApiResponse authenticateUserResponse = accessor.submit(authenticateUserRequest);
    }
}
