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

import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.hamcrest.CoreMatchers;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.assertThat;

public class GetAuthPluginConfigurationExecutorTest {

    @Test
    public void shouldProvideValidConfiguration() throws JSONException {
        GoPluginApiResponse response = new GetAuthPluginConfigurationExecutor().execute();

        assertThat(response.responseCode(), CoreMatchers.is(200));
        String expectedJSON = "{" +
                "\"display-name\":\"LDAP Authentication with Start TLS\"," +
                "\"display-image-url\":\"https://raw.githubusercontent.com/gocd-contrib/authentication-skeleton-plugin/master/src/main/resources/gocd_72_72_icon.png\"," +
                "\"supports-password-based-authentication\":true," +
                "\"supports-user-search\":true," +
                "\"supports-web-based-authentication\":false" +
                "}";

        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);
    }
}
