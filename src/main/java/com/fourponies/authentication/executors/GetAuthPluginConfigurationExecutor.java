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

import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.HashMap;
import java.util.Map;

import static com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;

public class GetAuthPluginConfigurationExecutor {
    private static final Gson GSON = new Gson();

    public GoPluginApiResponse execute() {
        Map<String, Object> configuration = getConfiguration();

        DefaultGoPluginApiResponse defaultGoPluginApiResponse = new DefaultGoPluginApiResponse(SUCCESS_RESPONSE_CODE);
        defaultGoPluginApiResponse.setResponseBody(GSON.toJson(configuration));
        return defaultGoPluginApiResponse;
    }

    //TODO: change this to your needs
    Map<String, Object> getConfiguration() {
        Map<String, Object> configuration = new HashMap<String, Object>();
        configuration.put("display-name", "LDAP Authentication with Start TLS");
        configuration.put("display-image-url", "https://raw.githubusercontent.com/gocd-contrib/authentication-skeleton-plugin/master/src/main/resources/gocd_72_72_icon.png");
        configuration.put("supports-web-based-authentication", false);
        configuration.put("supports-password-based-authentication", true);
        configuration.put("supports-user-search", true);
        return configuration;
    }
}
