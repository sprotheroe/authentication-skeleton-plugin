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

import com.example.authentication.domain.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class SearchUserExecutorTest {
    private Gson GSON = new Gson();
    private GoPluginApiRequest request;
    private User user;


    @Before
    public void setup() {
        request = mock(GoPluginApiRequest.class);
        user = new User("your-user-name", "GoCDAdmin", "go@go.com");
        when(request.requestBody()).thenReturn("{\"search-term\": \"your-user-name\"}");
    }

    @Test
    public void shouldAbleToSearchUser() throws Exception {
        SearchUserExecutor executor = spy(new SearchUserExecutor(request));
        when(executor.searchUsers(anyString())).thenReturn(Collections.singletonList(user));

        GoPluginApiResponse response = executor.execute();

        assertThat(response.responseCode(), is(200));

        Type listType = new TypeToken<List<User>>() {
        }.getType();

        List<User> users = GSON.fromJson(response.responseBody(), listType);
        assertThat(users, hasItem(user));
    }
}