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
import com.fourponies.authentication.utils.Util;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import static com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse.REDIRECT_RESPONSE_CODE;
import static com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;

public class SetupLoginRequestExecutor {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String TEXT_HTML = "text/html; charset=UTF-8";
    private static final Gson GSON = new Gson();
    private PluginSettings pluginSettings;

    public SetupLoginRequestExecutor(PluginSettings pluginSettings) {
        this.pluginSettings = pluginSettings;
    }

    //TODO: change this to your needs, send redirect URL or send login page(check login-page.template.html)
    public GoPluginApiResponse execute() {
        return sendLoginPage();
//        return sendRedirect();
    }

    GoPluginApiResponse sendLoginPage() {
        DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(SUCCESS_RESPONSE_CODE, Util.readResource("/login-page.template.html"));
        response.addResponseHeader(CONTENT_TYPE, TEXT_HTML);
        return response;
    }

    GoPluginApiResponse sendRedirect() {
        DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(REDIRECT_RESPONSE_CODE);
        response.addResponseHeader("Location", "your redirect URL");
        return response;
    }
}
