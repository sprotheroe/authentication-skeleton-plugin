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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * TODO: add any additional configuration fields here.
 */
public class GetPluginConfigurationExecutor implements RequestExecutor {
    public static final Field LDAP_URL = new NonBlankField("ldap_url", "LDAP Server URL", null, false, "0");
    public static final Field STARTTLS = new NonBlankField("starttls", "Start TLS", null, false, "1");
    public static final Field SEARCH_BASE = new NonBlankField("search_base", "Search Base", null, false, "2");
    public static final Field MANAGER_DN = new NonBlankField("manager_dn", "Manager DN", null, false, "3");
    public static final Field MANAGER_PASSWORD = new NonBlankField("manager_password", "Manager Password", null, false, "4");
    public static final Field SEARCH_FILTER = new NonBlankField("search_filter", "Search Filter", null, false, "5");
    public static final Field DISPLAY_NAME_ATTRIBUTE = new NonBlankField("display_name_attribute", "Display Name Attribute", null, false, "6");
    public static final Field GO_SERVER_URL = new NonBlankField("go_server_url", "Go Server URL", null, false, "7");


    public static final Map<String, Field> FIELDS = new LinkedHashMap<>();
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    static {
        FIELDS.put(LDAP_URL.key(), LDAP_URL);
        FIELDS.put(STARTTLS.key(), STARTTLS);
        FIELDS.put(SEARCH_BASE.key(), SEARCH_BASE);
        FIELDS.put(MANAGER_DN.key(), MANAGER_DN);
        FIELDS.put(MANAGER_PASSWORD.key(), MANAGER_PASSWORD);
        FIELDS.put(SEARCH_FILTER.key(), SEARCH_FILTER);
        FIELDS.put(DISPLAY_NAME_ATTRIBUTE.key(), DISPLAY_NAME_ATTRIBUTE);
        FIELDS.put(GO_SERVER_URL.key(), GO_SERVER_URL);
    }

    public GoPluginApiResponse execute() {
        return new DefaultGoPluginApiResponse(200, GSON.toJson(FIELDS));
    }
}
