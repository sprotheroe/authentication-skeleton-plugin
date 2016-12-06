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

package com.example.authentication.executors;

import com.example.authentication.PluginSettings;
import com.example.authentication.utils.Util;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Before;
import org.junit.Test;

import static com.example.authentication.executors.SetupLoginRequestExecutor.CONTENT_TYPE;
import static com.example.authentication.executors.SetupLoginRequestExecutor.TEXT_HTML;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SetupLoginRequestExecutorTest {

    private Gson GSON = new Gson();
    private PluginSettings pluginSettings;

    @Before
    public void setup() {
        pluginSettings = new PluginSettings();
    }

    @Test
    public void shouldAbleToSendLoginPage() {

        GoPluginApiResponse response = new SetupLoginRequestExecutor(pluginSettings).execute();

        assertThat(response.responseCode(), is(200));
        assertThat(response.responseBody(), is(Util.readResource("/login-page.template.html")));
        assertThat(response.responseHeaders().get(CONTENT_TYPE), is(TEXT_HTML));
    }

    @Test
    public void shouldAbleToSendRedirectURL() {

        GoPluginApiResponse response = new SetupLoginRequestExecutor(pluginSettings) {
            @Override
            public GoPluginApiResponse execute() {
                return sendRedirect();
            }
        }.execute();

        assertThat(response.responseCode(), is(302));
        assertThat(response.responseHeaders(), hasEntry("Location", "your redirect URL"));
    }
}