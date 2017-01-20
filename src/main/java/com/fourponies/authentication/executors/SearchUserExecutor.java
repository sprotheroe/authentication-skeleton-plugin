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

import com.fourponies.authentication.PluginSettings;
import com.fourponies.authentication.RequestExecutor;
import com.fourponies.authentication.domain.User;
import com.fourponies.authentication.LdapClient;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.List;
import java.util.Map;

import static com.fourponies.authentication.LdapStartTlsPlugin.LOG;

public class SearchUserExecutor implements RequestExecutor {
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    private final GoPluginApiRequest request;
    private final PluginSettings settings;

    public SearchUserExecutor(GoPluginApiRequest request, PluginSettings settings) {
        this.request = request;
        this.settings = settings;
    }

    @Override
    @SuppressWarnings("unchecked")
    public GoPluginApiResponse execute() throws Exception {
        Map<String, String> requestParam = GSON.fromJson(request.requestBody(), Map.class);
        String searchTerm = requestParam.get("search-term");

        return new DefaultGoPluginApiResponse(200, GSON.toJson(searchUsers(searchTerm)));
    }

    //TODO: change this to your needs
    List<User> searchUsers(String searchTerm) {
	LdapClient ldap = new LdapClient(settings);
        return ldap.search(searchTerm);
    }
}
