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
import com.example.authentication.domain.User;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.request.GoApiRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class AuthenticationExecutorTest {
    private Gson GSON = new Gson();
    private GoApplicationAccessor accessor;
    private GoPluginApiRequest request;
    private PluginSettings settings;

    @Before
    public void setup() {
        accessor = mock(GoApplicationAccessor.class);
        settings = mock(PluginSettings.class);
        request = mock(GoPluginApiRequest.class);

        when(settings.getGoServerUrl()).thenReturn("https://your.go.server.url");
    }

    @Test
    public void shouldAuthenticateUser() throws Exception {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("username", "gocd");
        requestParams.put("password", "password");
        when(request.requestParameters()).thenReturn(requestParams);

        AuthenticationExecutor executor = spy(new AuthenticationExecutor(accessor, request, settings));

        GoPluginApiResponse response = executor.execute();

        assertThat(response.responseCode(), is(302));

        verify(accessor, times(1)).submit(any(GoApiRequest.class));
        verify(executor, times(1)).authenticateUser(new User("gocd", "GoCDAdmin", "admin@go"));
        Map<String, String> responseData = response.responseHeaders();
        assertThat(responseData, hasEntry("Location", settings.getGoServerUrl()));
    }

    @Test
    public void shouldAuthenticateInvalidUser() throws Exception {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("username", "unkown");
        requestParams.put("password", "password");
        when(request.requestParameters()).thenReturn(requestParams);

        AuthenticationExecutor executor = spy(new AuthenticationExecutor(accessor, request, settings));
        GoPluginApiResponse response = executor.execute();

        assertThat(response.responseCode(), is(412));
        verify(accessor, times(0)).submit(any(GoApiRequest.class));
        verify(executor, times(0)).authenticateUser(new User("gocd", "GoCDAdmin", "admin@go"));
    }
}
