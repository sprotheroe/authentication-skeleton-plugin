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

package com.fourponies.authentication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Implement any settings that your plugin needs
public class PluginSettings {
    private static final Gson GSON = new GsonBuilder().
            excludeFieldsWithoutExposeAnnotation().
            create();

    @Expose
    @SerializedName("go_server_url")
    private String goServerUrl;

    @Expose
    @SerializedName("api_user")
    private String apiUser;

    @Expose
    @SerializedName("api_key")
    private String apiKey;

    @Expose
    @SerializedName("api_url")
    private String apiUrl;

    @Expose
    @SerializedName("api_version")
    private String apiVersion;

    // Parameters
    @Expose
    @SerializedName("ldap_url")
    private String ldapURL;

    @Expose
    @SerializedName("starttls")
    private String startTLS;

    @Expose
    @SerializedName("search_base")
    private String searchBase;

    @Expose
    @SerializedName("manager_dn")
    private String managerDN;

    @Expose
    @SerializedName("manager_password")
    private String managerPassword;

    @Expose
    @SerializedName("search_filter")
    private String searchFilter;

    @Expose
    @SerializedName("display_name_attribute")
    private String displayNameAttribute;


    public static PluginSettings fromJSON(String json) {
        return GSON.fromJson(json, PluginSettings.class);
    }

    public String getApiUser() {
        return apiUser;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getGoServerUrl() {
        return goServerUrl;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getLdapURL() {
        return ldapURL;
    }

    public String getStartTLS() {
        return startTLS;
    }

    public String getSearchBase() {
        return searchBase;
    }

    public String getManagerDN() {
        return managerDN;
    }

    public String getManagerPassword() {
        return managerPassword;
    }

    public String getSearchFilter() {
        return searchFilter;
    }

    public String getDisplayNameAttribute() {
        return displayNameAttribute;
    }
}
