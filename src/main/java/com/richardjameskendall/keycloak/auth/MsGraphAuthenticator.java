/*
 * Copyright Richard Kendall 2022
 *
 */

package com.richardjameskendall.keycloak.auth;

import com.google.gson.Gson;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.*;

import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class MsGraphAuthenticator implements Authenticator {

    private static final Logger logger = Logger.getLogger("com.richardjameskendall.keycloak.auth.MsGraphAuthenticator");

    @Override
    public void action(AuthenticationFlowContext context) {
        logger.info("In MS graph authenticator - action method");
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        // get config details
        Map<String,String> config = context.getAuthenticatorConfig().getConfig();

        logger.info("In MS graph authenticator");
        UserModel user = context.getUser();
        RealmModel realm = context.getRealm();
        GroupProvider groups = context.getSession().groups();

        // get stored token
        FederatedIdentityModel idp = (FederatedIdentityModel)context.getSession().users().getFederatedIdentities(user, realm).toArray()[0];
        String token = idp.getToken();
        //logger.info("Got token from idp: " + token);

        // get access token for graph API
        Gson gson = new Gson();
        AzureADToken azureAdToken = (AzureADToken)gson.fromJson(token, AzureADToken.class);
        logger.info("Got access token of: " + azureAdToken.getAccess_token());
        String accessToken = azureAdToken.getAccess_token();

        // get list of groups we are checking for
        String groupsToCheck = config.get("allowed.groups");
        List<String> groupsToCheckList = Arrays.asList(groupsToCheck.split(","));
        Map<String,String> groupIdMap = MsGraphClient.GetMapOfGroupIds(groupsToCheckList, accessToken);

        // get group membership of user
        try {
            List<String> memberOf = MsGraphClient.GetCurrentUserGroupMembership(accessToken, false);
            if(memberOf != null) {
                memberOf.forEach((id) -> {
                    logger.info(String.format("User belongs to group with ID: %s", id));
                    // need to check if these groups are ones we care about
                    if(groupIdMap.containsKey(id)) {
                        logger.info(String.format("User is member of group %s", id));
                        // check if this group exists in keycloak already
                        GroupModel kcGroup = groups.getGroupById(realm, id);
                        if(kcGroup != null) {
                            logger.info("Group exists in keycloak already");
                        } else {
                            logger.info("Group does not exist in keycloak, so we need to create it");
                            GroupModel newGroup = groups.createGroup(realm, id, groupIdMap.get(id));
                            newGroup.setSingleAttribute("aad managed", "yes");
                        }
                        // get group again, just in case
                        kcGroup = groups.getGroupById(realm, id);
                        // join user to group
                        user.joinGroup(kcGroup);
                    }
                });

                // need to remove members from aad managed groups if they have now left
                user.getGroupsStream().forEach((group) -> {
                    logger.info(String.format("user is a member of group %s", group.getName()));
                    String aadManaged = group.getFirstAttribute("aad managed");
                    if(aadManaged != null) {
                        logger.info(String.format("Group %s is aad managed", group.getName()));
                        // need to check if user is still a member of this group
                        if(!memberOf.contains(group.getId())) {
                            logger.info(String.format("According to AAD, user is not a member of %s", group.getName()));
                            user.leaveGroup(group);
                            logger.info("User removed");
                        }
                    }
                });

            } else {
                logger.info("User does not belong to any groups");
            }
        } catch (IOException e) {
            logger.error("IOException while calling API to get groups the user belongs to", e);
        }

        context.success();

    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    //@Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
    }

    @Override
    public void close() {
    }

}