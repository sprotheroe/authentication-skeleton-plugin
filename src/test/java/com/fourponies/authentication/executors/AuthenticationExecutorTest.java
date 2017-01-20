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

import com.fourponies.authentication.LdapClient;
import com.fourponies.authentication.domain.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.request.GoApiRequest;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AuthenticationExecutorTest {
    private Gson GSON = new Gson();
    private GoApplicationAccessor accessor;
    private GoPluginApiRequest request;
    private LdapClient ldapClient;
    private User user;

    @Before
    public void setup() {
        accessor = mock(GoApplicationAccessor.class);
        ldapClient = mock(LdapClient.class);
        request = mock(GoPluginApiRequest.class);
        user = new User("your-user-name", "GoCDAdmin", "go@go.com");

        when(ldapClient.authenticate("gocd","password")).thenReturn(user);
        when(ldapClient.authenticate("unkown","password")).thenReturn(null);
    }

    @Test
    public void shouldAuthenticateUser() throws Exception {
        String requestBody = "{ 'username':'gocd', 'password':'password'}";
        when(request.requestBody()).thenReturn(requestBody);

        AuthenticationExecutor executor = spy(new AuthenticationExecutor(accessor, request, ldapClient));

        GoPluginApiResponse response = executor.execute();

        assertThat(response.responseCode(), is(200));

        Type mapType = new TypeToken<Map<String,User>>() {
        }.getType();

        Map<String, User> map = GSON.fromJson(response.responseBody(), mapType);

        User testUser = map.get("user");
        assertEquals(testUser, user);
    }

    @Test
    public void shouldAuthenticateInvalidUser() throws Exception {
        String requestBody = "{ 'username':'unkown', 'password':'password'}";
        when(request.requestBody()).thenReturn(requestBody);

        AuthenticationExecutor executor = spy(new AuthenticationExecutor(accessor, request, ldapClient));
        GoPluginApiResponse response = executor.execute();

        assertThat(response.responseCode(), is(412)); // Validation error
    }
}
