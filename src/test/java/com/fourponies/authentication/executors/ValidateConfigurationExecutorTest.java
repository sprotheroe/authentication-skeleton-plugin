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

import com.fourponies.authentication.requests.ValidatePluginSettings;
import com.fourponies.authentication.PluginSettings;
import com.fourponies.authentication.SettingsPrimer;

import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ValidateConfigurationExecutorTest {

    private GoPluginApiRequest request;
    private PluginSettings pluginSettings;
    private SettingsPrimer settingsPrimer;

    @Before
    public void setup() {
        request = mock(GoPluginApiRequest.class);
        pluginSettings = new PluginSettings();
        settingsPrimer = mock(SettingsPrimer.class);
        when(settingsPrimer.load()).thenReturn(pluginSettings);
    }

    @Test
    public void shouldValidateABadConfiguration() throws Exception {
        String requestBody = "";
        when(request.requestBody()).thenReturn(requestBody);
        GoPluginApiResponse response = new ValidateConfigurationExecutor(request, settingsPrimer).execute();

        assertThat(response.responseCode(), is(200));
        JSONAssert.assertEquals("[\n" +
                "  {\n" +
                "    \"message\": \"LDAP Server URL must not be blank.\",\n" +
                "    \"key\": \"ldap_url\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"message\": \"Start TLS must not be blank.\",\n" +
                "    \"key\": \"starttls\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"message\": \"Search Base must not be blank.\",\n" +
                "    \"key\": \"search_base\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"message\": \"Manager DN must not be blank.\",\n" +
                "    \"key\": \"manager_dn\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"message\": \"Manager Password must not be blank.\",\n" +
                "    \"key\": \"manager_password\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"message\": \"Search Filter must not be blank.\",\n" +
                "    \"key\": \"search_filter\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"message\": \"Display Name Attribute must not be blank.\",\n" +
                "    \"key\": \"display_name_attribute\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"message\": \"Go Server URL must not be blank.\",\n" +
                "    \"key\": \"go_server_url\"\n" +
                "  }\n" +
                "]", response.responseBody(), true);
    }

    @Test
    public void shouldValidateAGoodConfiguration() throws Exception {
        String requestBody = "{ \"plugin-settings\": {" +
            "\"ldap_url\": { \"value\": \"ldap://ldap.example.com\" }, " + 
            "\"starttls\": { \"value\": \"yes\" }, " + 
            "\"search_base\": { \"value\": \"ldap://ldap.example.com\" }, " + 
            "\"manager_dn\": { \"value\": \"ou=people,dc=example,dc=com\" }, " + 
            "\"manager_password\": { \"value\": \"secretPassw0rd\" }, " + 
            "\"search_filter\": { \"value\": \"(ou=people)\" }, " + 
            "\"display_name_attribute\": { \"value\": \"cn\" }, " + 
            "\"go_server_url\": { \"value\": \"https://go.example.com\" } " + 
            "} }";

        when(request.requestBody()).thenReturn(requestBody);
        GoPluginApiResponse response = new ValidateConfigurationExecutor(request, settingsPrimer).execute();

        assertThat(response.responseCode(), is(200));
        JSONAssert.assertEquals("[]", response.responseBody(), true);
    }
}
