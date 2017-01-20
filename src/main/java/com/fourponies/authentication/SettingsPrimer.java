/*
 * Copyright 2017 Simon Protheroe
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

package com.fourponies.authentication;

import com.fourponies.authentication.PluginSettings;
import com.google.gson.Gson;

import java.io.File;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static com.fourponies.authentication.LdapStartTlsPlugin.LOG;

public class SettingsPrimer {
    private static final Gson GSON = new Gson();

    public PluginSettings load() {
        PluginSettings settings = new PluginSettings(); // default with null properties
        try {
            Path configPath = getConfigPath();
            LOG.debug("Reading config file from " + configPath);
            String json = new String(Files.readAllBytes(configPath), StandardCharsets.UTF_8);
            settings = PluginSettings.fromJSON(json);
        }
        catch (Exception ex) {
            LOG.error("Failed to read config file: " + ex.getMessage());
        }
        return settings;
    }

    public void save(HashMap<String, String> settings) {
        try {
            Path configPath = getConfigPath();
            LOG.debug("Writing config file to " + configPath);
            Files.write(configPath, GSON.toJson(settings).getBytes(StandardCharsets.UTF_8));
        }
        catch (Exception ex) {
            LOG.error("Failed to write config file: " + ex.getMessage());
        }
    }

    private Path getConfigPath() {
        String userHome = System.getProperty("user.home");
        String configPath = userHome + File.separator + "ldap-starttls-config.json";
        return Paths.get(configPath);
    }
}
