Spring Security OAuth2 implementation for machine-to-machine authentication process on WebFlux endpoints.

AUTHORISATION SERVER
====================

We are using `Keycloak` as authorization server into a Docker image :

- Start the Keycloak authorization server using the following docker command :
```
docker run -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -p 8080:8080 jboss/keycloak:4.6.0.Final
```
- Configure the client to use by importing the JSON file `keycloak-master-clients.json` in the Keycloak import menu, using a *Skip* policy if a resource already exists
- Verify that the client `security-client` is well added
- Get the `security-client` secret by :
  - Going into the *Clients* menu
  - Click on the `security-client` list entry
  - In the *Credentials* tab, click on the "Regenerate Secret" button
  - Copy the secret generated and store it for later

RESOURCE SERVER
===============

The resource server `security-server` project is composed of the following packages :

- the main package "server" containing :
  - _ServerApplication.java_ : the main app
- the configuration package "server.config" containing
  - _CustomAuthenticationConverter.java_ : the JWT token analyzer
  - _ServerSecurityConfig.java_ : the configuration implementing the security web filter chain
  - _ServerSecurityProperties.java_ : the properties listed in the application.yml file (with prefix : "com.security.oauth2.jwt")
- the controller package "server.controller" :
  - _ServerController.java_ : exposing one endpoint `/principal`

To start the security server application :

- Import the `sercurity-server`project as a "Maven project" into your IDE
- Start the Java application using your IDE command

CLIENT APPLICATION
==================

The client application `security-client` project is composed of the following packages :

- the main package "client" containing :
  - _ClientApplication.java_ : the main app sending a request to the `/principal` endpoint
- the configuration package "client.config" containing :
  - _ApiConfig.java_ : properties linked for data fetching (with prefix : "com.api")
  - _WebClientSecurityConfig.java_ : the configuration implementing the WebClient customizer
  - _WebClientSecurityCustomizer.java_ : add automatically an exchange function as filter

To start the security client application :

- Import the `sercurity-client`project as a "Maven project" into your IDE
- In the `src/main/resources`, add an `application-my.properties` file with the following property :
  ```
  security.client.secret=SECRET_FROM_KEYCLOAK
  ```
  Replace the `SECRET_FROM_KEYCLOAK` by the secret generated in the `security-client` credentials tab.
- Start the Java application using your IDE command

EXECUTION
=========

**Principle**

Once started, the `security-client` is connecting to the `security-server` on the `/principal` endpoint resulting in an identification of the calling application by the server.

**Server application**

On one side, the `security-server` hence displays the content of the `clientId` of the JWT token :
```
c.s.server.controller.ServerController   : App authentified : 'security-client'
```

**Client application**

On the other side, the `security-client` application logs the result from the request to the server which is is application name authenticated:
```
com.security.client.ClientApplication    : Server response : 'security-client'
```
