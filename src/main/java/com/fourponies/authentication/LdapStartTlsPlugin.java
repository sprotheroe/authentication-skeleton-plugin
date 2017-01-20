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

import com.fourponies.authentication.executors.*;
import com.fourponies.authentication.requests.ValidatePluginSettings;
import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.logging.Logger;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import static com.fourponies.authentication.Constants.PLUGIN_IDENTIFIER;

@Extension
public class LdapStartTlsPlugin implements GoPlugin {

    public static final Logger LOG = Logger.getLoggerFor(LdapStartTlsPlugin.class);

    private GoApplicationAccessor accessor;
    private PluginRequest pluginRequest;

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor accessor) {
        this.accessor = accessor;
        this.pluginRequest = new PluginRequest(accessor);
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
	LOG.debug("request '" + request.requestName() + "'");
        try {
            switch (Request.fromString(request.requestName())) {
                case PLUGIN_SETTINGS_GET_CONFIGURATION:
                    return new GetPluginConfigurationExecutor().execute();
                case PLUGIN_SETTINGS_VALIDATE_CONFIGURATION:
                    return ValidatePluginSettings.fromJSON(request.requestBody()).executor().execute();
                case PLUGIN_SETTINGS_GET_VIEW:
                    return new GetViewRequestExecutor().execute();
                case REQUEST_AUTH_PLUGIN_CONFIGURATION:
                    return new GetAuthPluginConfigurationExecutor().execute();
                case REQUEST_INDEX:
                    return new SetupLoginRequestExecutor(pluginRequest.getPluginSettings()).execute();
                case REQUEST_AUTHENTICATE:
                    return new AuthenticationExecutor(accessor, request, pluginRequest.getPluginSettings()).execute();
                case REQUEST_SEARCH_USER:
                    return new SearchUserExecutor(request).execute();
                default:
                    throw new UnhandledRequestTypeException(request.requestName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return PLUGIN_IDENTIFIER;
    }
}
