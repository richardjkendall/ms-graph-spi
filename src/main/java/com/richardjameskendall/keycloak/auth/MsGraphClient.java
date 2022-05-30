/*
 * Copyright Richard Kendall 2022
 *
 */

package com.richardjameskendall.keycloak.auth;

import org.jboss.logging.Logger;

import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class MsGraphClient {

    private static final Logger logger = Logger.getLogger("com.richardjameskendall.keycloak.auth.MsGraphClient");

    public static String GetGroupIdByDisplayName(String displayName, String token) throws IOException {
        String url = String.format("https://graph.microsoft.com/v1.0/groups?$filter=displayName eq '%s'", displayName);
        Map r = (Map)ApiClient.GetToApi(url, token);
        if(r.containsKey("value")) {
            List<Map> groups = (List<Map>)r.get("value");
            if(groups.size() == 1) {
                Map group = groups.get(0);
                String group_id = (String)group.get("id");
                logger.info(String.format("Got ID %s for group with name %s", group_id, displayName));
                return group_id;
            } else {
                logger.info(String.format("Could not find a group with name %s", displayName));
                return null;
            }
        } else {
            logger.error("Something went wrong in API call to get group ID from group display name");
            return null;
        }
    }

    public static Map<String,String> GetMapOfGroupIds(List<String> groupDisplayNames, String token) {
        HashMap<String,String> groups = new HashMap<>();
        groupDisplayNames.forEach((name) -> {
            try {
                String groupId = GetGroupIdByDisplayName(name, token);
                if(groupId != null) {
                    groups.put(groupId, name);
                }
            } catch (IOException e) {
                logger.error("IOException while attempting to get group ID", e);
            }
        });
        return groups;
    }

    public static List<String> GetCurrentUserGroupMembership(String token, boolean securityGroupsOnly) throws IOException {
        String url = "https://graph.microsoft.com/v1.0/me/getMemberGroups";
        HashMap<String,Object> body = new HashMap<>();
        body.put("securityEnabledOnly", securityGroupsOnly);
        Map r = (Map)ApiClient.PostJsonToApi(url, token, body);
        if(r.containsKey("value")) {
            return (List<String>)r.get("value");
        } else {
            return null;
        }
    }

}
