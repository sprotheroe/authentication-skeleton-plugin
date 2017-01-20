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
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ValidateConfigurationExecutorTest {
    @Test
    public void shouldValidateABadConfiguration() throws Exception {
        ValidatePluginSettings settings = new ValidatePluginSettings();
        GoPluginApiResponse response = new ValidateConfigurationExecutor(settings).execute();

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
        ValidatePluginSettings settings = new ValidatePluginSettings();
        settings.put("ldap_url", "ldap://ldap.example.com");
        settings.put("starttls", "1");
        settings.put("search_base", "https://ci.example.com");
        settings.put("manager_dn", "cn=admin,dc=example,dc=com");
        settings.put("manager_password", "secretPassw0rd");
        settings.put("search_filter", "https://ci.example.com");
        settings.put("display_name_attribute", "fred");
        settings.put("go_server_url", "https://ci.example.com");
        GoPluginApiResponse response = new ValidateConfigurationExecutor(settings).execute();

        assertThat(response.responseCode(), is(200));
        JSONAssert.assertEquals("[]", response.responseBody(), true);
    }
}
