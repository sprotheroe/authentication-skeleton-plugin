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
import com.fourponies.authentication.SettingsPrimer;
import com.fourponies.authentication.RequestExecutor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.fourponies.authentication.LdapStartTlsPlugin.LOG;

/*
 * TODO: add any additional configuration fields here.
 */
public class GetPluginConfigurationExecutor implements RequestExecutor {

    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private final SettingsPrimer settingsPrimer;

    public GetPluginConfigurationExecutor(SettingsPrimer settingsPrimer) {
        this.settingsPrimer = settingsPrimer;
    }

    public Map<String, Field> fieldMap() {
        PluginSettings settings = settingsPrimer.load();

	ArrayList<Field> fields = new ArrayList<Field>();
        fields.add(new NonBlankField("ldap_url", "LDAP Server URL", settings.getLdapURL(), false, "0"));
        fields.add(new NonBlankField("starttls", "Start TLS", settings.getStartTLS(), false, "1"));
        fields.add(new NonBlankField("search_base", "Search Base", settings.getSearchBase(), false, "2"));
        fields.add(new NonBlankField("manager_dn", "Manager DN", settings.getManagerDN(), false, "3"));
        fields.add(new NonBlankField("manager_password", "Manager Password", settings.getManagerPassword(), false, "4"));
        fields.add(new NonBlankField("search_filter", "Search Filter", settings.getSearchFilter(), false, "5"));
        fields.add(new NonBlankField("display_name_attribute", "Display Name Attribute", settings.getDisplayNameAttribute(), false, "6"));
        fields.add(new NonBlankField("go_server_url", "Go Server URL", settings.getGoServerUrl(), false, "7"));

	Map<String, Field> fieldMap = new LinkedHashMap<>();
	for (Field field : fields) {
            fieldMap.put(field.key(), field);
        }
        return fieldMap;
    }

    public GoPluginApiResponse execute() {
        return new DefaultGoPluginApiResponse(200, GSON.toJson(fieldMap()));
    }
}
