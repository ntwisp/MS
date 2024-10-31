package com.itm.space.backendresources;

import com.itm.space.backendresources.api.request.UserRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserServiceIntegratedTest extends BaseIntegrationTest {

    private final Keycloak keycloak;
    private String userId;

    @Autowired
    public UserServiceIntegratedTest(final Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testCreateUser() throws Exception {
        UserRequest userRequest = new UserRequest
                ("durak", "durak@lol.com", "durak999", "Lol","Lol");
        mvc.perform(requestWithContent(post("/api/users"),userRequest))
                .andExpect(status().isOk());
        userId = keycloak.realm("ITM").users().search(userRequest.getUsername()).get(0).getId();
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void testGetUserById() throws Exception {
        String id = "7c51ccf8-06cf-4388-813b-7dedb4f2ed29";
        mvc.perform(get("/api/users/" + id))
                .andExpect(status().isOk());
    }

    @AfterEach
    public void tearDown() {
        if (userId != null) {
            keycloak.realm("ITM").users().get(userId).remove();
        }
    }
}
