/*
 * Copyright Richard Kendall 2022
 *
 */

package com.richardjameskendall.keycloak.auth;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class MsGraphAuthenticatorFactory implements AuthenticatorFactory {

    public static final String ID = "ms-graph-checker";

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    static {

        ProviderConfigProperty groupMembership;
        groupMembership = new ProviderConfigProperty();
        groupMembership.setName("allowed.groups");
        groupMembership.setLabel("Groups we check for membership in");
        groupMembership.setType(ProviderConfigProperty.STRING_TYPE);
        groupMembership.setHelpText("A comma separated list of groups display names");
        configProperties.add(groupMembership);

    }

    private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
        AuthenticationExecutionModel.Requirement.REQUIRED,
        AuthenticationExecutionModel.Requirement.ALTERNATIVE,
        AuthenticationExecutionModel.Requirement.DISABLED
    };

    //@Override
    public Authenticator create(KeycloakSession session) {
        return new MsGraphAuthenticator();
    }

    //@Override
    public String getId() {
        return ID;
    }

    //@Override
    public String getReferenceCategory() {
        return "msgraph";
    }

    //@Override
    public boolean isConfigurable() {
        return true;
    }

    //@Override
    public boolean isUserSetupAllowed() {
        return true;
    }

    //@Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    //@Override
    public String getDisplayType() {
        return "Microsoft Graph";
    }

    //@Override
    public String getHelpText() {
        return "Microsoft Graph";
    }

    //@Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    //@Override
    public void init(Config.Scope config) {

    }

    //@Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    //@Override
    public void close() {
    }

}