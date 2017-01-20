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
import com.fourponies.authentication.SettingsPrimer;

import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class GetPluginConfigurationExecutorTest {

    private SettingsPrimer settingsPrimer;
    private PluginSettings pluginSettings;

    @Before
    public void setup() {
        pluginSettings = new PluginSettings();
        settingsPrimer = mock(SettingsPrimer.class);
        when(settingsPrimer.load()).thenReturn(pluginSettings);
    }

    @Test
    public void shouldSerializeAllFields() throws Exception {
        GetPluginConfigurationExecutor executor = new GetPluginConfigurationExecutor(settingsPrimer);
        GoPluginApiResponse response = executor.execute();
        HashMap hashMap = new Gson().fromJson(response.responseBody(), HashMap.class);
        assertEquals("Are you using anonymous inner classes — see https://github.com/google/gson/issues/298",
                hashMap.size(),
                executor.fieldMap().size()
        );
    }

    @Test
    public void assertJsonStructure() throws Exception {
        GoPluginApiResponse response = new GetPluginConfigurationExecutor(settingsPrimer).execute();
        assertThat(response.responseCode(), CoreMatchers.is(200));
        String expectedJSON = "{\n" +
                "  \"ldap_url\": {\n" +
                "    \"display-name\": \"LDAP Server URL\",\n" +
                "    \"required\": true,\n" +
                "    \"secure\": false,\n" +
                "    \"display-order\": \"0\"\n" +
                "  },\n" +
                "  \"starttls\": {\n" +
                "    \"display-name\": \"Start TLS\",\n" +
                "    \"required\": true,\n" +
                "    \"secure\": false,\n" +
                "    \"display-order\": \"1\"\n" +
                "  },\n" +
                "  \"search_base\": {\n" +
                "    \"display-name\": \"Search Base\",\n" +
                "    \"required\": true,\n" +
                "    \"secure\": false,\n" +
                "    \"display-order\": \"2\"\n" +
                "  },\n" +
                "  \"manager_dn\": {\n" +
                "    \"display-name\": \"Manager DN\",\n" +
                "    \"required\": true,\n" +
                "    \"secure\": false,\n" +
                "    \"display-order\": \"3\"\n" +
                "  },\n" +
                "  \"manager_password\": {\n" +
                "    \"display-name\": \"Manager Password\",\n" +
                "    \"required\": true,\n" +
                "    \"secure\": false,\n" +
                "    \"display-order\": \"4\"\n" +
                "  },\n" +
                "  \"search_filter\": {\n" +
                "    \"display-name\": \"Search Filter\",\n" +
                "    \"required\": true,\n" +
                "    \"secure\": false,\n" +
                "    \"display-order\": \"5\"\n" +
                "  },\n" +
                "  \"display_name_attribute\": {\n" +
                "    \"display-name\": \"Display Name Attribute\",\n" +
                "    \"required\": true,\n" +
                "    \"secure\": false,\n" +
                "    \"display-order\": \"6\"\n" +
                "  },\n" +
                "  \"go_server_url\": {\n" +
                "    \"display-name\": \"Go Server URL\",\n" +
                "    \"required\": true,\n" +
                "    \"secure\": false,\n" +
                "    \"display-order\": \"7\"\n" +
                "  }\n" +
                "}";
        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);
    }
}
