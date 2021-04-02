package dev.restaurantreview.modules.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserPrivateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void tryGetCurrentUserWithoutLogin() throws Exception {
		this.mockMvc.perform(get("/api/users/current")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	@WithMockUser()
	@Test
	public void getCurrentUser() throws Exception {
		this.mockMvc.perform(get("/api/users/current")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "owner", roles = { "OWNER" })
	@Test
	public void getCurrentUserOwner() throws Exception {
		this.mockMvc.perform(get("/api/users/current")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "admin", roles = { "ADMIN" })
	@Test
	public void getCurrentUserAdmin() throws Exception {
		this.mockMvc.perform(get("/api/users/current")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}