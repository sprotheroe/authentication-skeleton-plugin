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
import com.fourponies.authentication.SettingsPrimer;
import com.fourponies.authentication.requests.ValidatePluginSettings;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.ArrayList;
import java.util.Map;

import static com.fourponies.authentication.LdapStartTlsPlugin.LOG;

public class ValidateConfigurationExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();

    private final ValidatePluginSettings settings;
    private final SettingsPrimer settingsPrimer;

    public ValidateConfigurationExecutor(GoPluginApiRequest request, SettingsPrimer settingsPrimer) {
        String json = request.requestBody();
        if (json == null || json.isEmpty()) {
            this.settings = new ValidatePluginSettings();
        } else {
            this.settings = ValidatePluginSettings.fromJSON(json);
        }
        this.settingsPrimer = settingsPrimer;
    }

    public GoPluginApiResponse execute() {
        ArrayList<Map<String, String>> result = new ArrayList<>();

	GetPluginConfigurationExecutor pluginConfig = new GetPluginConfigurationExecutor(settingsPrimer);
        Map<String, Field> fieldMap = pluginConfig.fieldMap();
        for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
            Field field = entry.getValue();
            Map<String, String> validationError = field.validate(settings.get(entry.getKey()));

            if (!validationError.isEmpty()) {
                result.add(validationError);
            }
        }

        // If no errors then save config file
        if (result.isEmpty()) {
            settingsPrimer.save(settings);
        }

        return DefaultGoPluginApiResponse.success(GSON.toJson(result));
    }
}
