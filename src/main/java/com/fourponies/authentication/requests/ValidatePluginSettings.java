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

package com.fourponies.authentication.requests;

import com.fourponies.authentication.RequestExecutor;
import com.fourponies.authentication.executors.ValidateConfigurationExecutor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class ValidatePluginSettings extends HashMap<String, String> {
    private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    @SuppressWarnings("unchecked")
    public static ValidatePluginSettings fromJSON(String json) {
        ValidatePluginSettings result = new ValidatePluginSettings();


System.out.println("****************************************************************************************");
System.out.println("****************************************************************************************");
System.out.println("****************************************************************************************");
System.out.println(json);
System.out.println("****************************************************************************************");
System.out.println("****************************************************************************************");
System.out.println("****************************************************************************************");
HashMap hm = GSON.fromJson(json, HashMap.class);
System.out.println(hm);
System.out.println("****************************************************************************************");
System.out.println("****************************************************************************************");
System.out.println("****************************************************************************************");

        Map<String, Map<String, String>> settings = (Map<String, Map<String, String>>) GSON.fromJson(json, HashMap.class).get("plugin-settings");

        for (Map.Entry<String, Map<String, String>> entry : settings.entrySet()) {
            result.put(entry.getKey(), entry.getValue().get("value"));
        }

        return result;
    }
}
