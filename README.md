# ms-graph-spi

A simple Keycloak SPI which pulls group information for a user from the Microsoft Graph.

It is designed to be used as a post login flow for an Identity Provider which uses Azure Active Directory.

It will check to see if the user who has logged in is a member of one or more groups and if so reflect that group membership in Keycloak.

It creates groups with the same name and manages the membership of those groups.  When they leave the group in Azure AD they are removed from the associated group in Keycloak the next time they log in.
