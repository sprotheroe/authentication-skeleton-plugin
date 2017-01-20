/*
 * Copyright 2017 Simon Protheroe
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

import com.fourponies.authentication.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.*;
import javax.net.ssl.SSLSession;

import static com.fourponies.authentication.LdapStartTlsPlugin.LOG;

public class LdapClient {

    private String providerUrl;
    private String securityPrincipal;
    private String securityCredentials;
    private String subtreeScope;
    private String searchFilter;

    private boolean startTls;

    public LdapClient(PluginSettings settings) {
        this.providerUrl = settings.getLdapURL();
        this.securityPrincipal = settings.getManagerDN();
        this.securityCredentials = settings.getManagerPassword();
        this.subtreeScope = settings.getSearchBase();
        this.searchFilter = settings.getSearchFilter();
    }

    public User authenticate(String username, String password) {
        User user = null;

        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, providerUrl);

        try {
            User tempUser = null;
            InitialLdapContext ctx = new InitialLdapContext(props, null);
            StartTlsResponse tls = (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());
            SSLSession sess = tls.negotiate();

            ctx.addToEnvironment(Context.SECURITY_AUTHENTICATION, "simple");
            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, securityPrincipal);
            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, securityCredentials);

            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            searchControls.setReturningAttributes(new String[] {"uniqueIdentifier", "givenName", "mail"});

            // login-specific piece from here
            String searchPattern = "(uniqueIdentifier=" + username + ")";
      LOG.debug("search patter is " + searchPattern);
            NamingEnumeration<SearchResult> results = ctx.search(subtreeScope, searchPattern, searchControls);

            String dn = null;
            if (results.hasMoreElements()) {
                SearchResult result = results.next();
                dn = result.getNameInNamespace();
                LOG.debug("Found user " + dn);

                String uid = (String)result.getAttributes().get("uniqueIdentifier").get();
                String email = (String)result.getAttributes().get("mail").get();
                String displayName = (String)result.getAttributes().get("givenName").get();

                tempUser = new User(uid, displayName, email);
            }
            else
                LOG.debug("user not found");

            tls.close();
            ctx.close();
            if (dn != null) {
                try {
                    props = new Properties();
                    props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                    props.put(Context.PROVIDER_URL, providerUrl);

                    ctx = new InitialLdapContext(props, null);
    		    tls = (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());
		    sess = tls.negotiate();

                    ctx.addToEnvironment(Context.SECURITY_AUTHENTICATION, "simple");
		    ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, dn);
		    ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);

                    ctx.reconnect(null);

                    user = tempUser;
                    LOG.debug("password authentication succeeded");
                    tls.close();
                    ctx.close();
                }
                catch (Exception e) {
                    LOG.debug("password authentication failed");
                    LOG.debug(e.getMessage());
                }
            }

        }
        catch(NamingException nex) {
            LOG.error("Got a naming exception");
        }
        catch(Exception ex) {
            LOG.error("Got some sort of LDAP exception");
        }

        return user;
    }

    public List<User> search(String pattern) {
        List<User> users = new ArrayList<User>();
        LOG.debug("searching with " + pattern);
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, providerUrl);

        try {
            InitialLdapContext ctx = new InitialLdapContext(props, null);
            StartTlsResponse tls = (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());
            SSLSession sess = tls.negotiate();

            ctx.addToEnvironment(Context.SECURITY_AUTHENTICATION, "simple");
            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, securityPrincipal);
            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, securityCredentials);

            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            searchControls.setReturningAttributes(new String[] {"uniqueIdentifier", "givenName", "mail"});
            NamingEnumeration<SearchResult> results = ctx.search(subtreeScope, searchFilter, searchControls);

            while (results.hasMoreElements()) {
                SearchResult result = (SearchResult)results.nextElement();
                String username = (String)result.getAttributes().get("uniqueIdentifier").get();
                String email = (String)result.getAttributes().get("mail").get();
                String displayName = (String)result.getAttributes().get("givenName").get();

                User user = new User(username, displayName, email);
                users.add(user);
            }

            tls.close();
            ctx.close();
        }
        catch(NamingException nex) {
            LOG.error("Got a naming exception");
        }
        catch(Exception ex) {
            LOG.error("Got some sort of LDAP exception");
        }

        return users;
    }
}
