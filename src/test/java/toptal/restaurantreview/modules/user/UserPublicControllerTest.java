package dev.restaurantreview.modules.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import javax.persistence.EntityManager;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.google.gson.Gson;

import dev.restaurantreview.config.accesspolicy.RoleEnum;
import dev.restaurantreview.modules.restaurant.model.RestaurantInput;
import dev.restaurantreview.modules.restaurant.model.TypeOfFood;
import dev.restaurantreview.modules.restaurant.model.TypeOfFoodEnum;
import dev.restaurantreview.modules.user.model.OwnerInput;
import dev.restaurantreview.modules.user.model.User;
import dev.restaurantreview.modules.user.model.UserInput;
import dev.restaurantreview.modules.user.service.UserRepository;
import dev.restaurantreview.modules.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserPublicControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private EntityManager em;

	@Autowired
	private UserRepository userRepository;

	@Captor
	private ArgumentCaptor<RoleEnum> roleEnumArgumentCaptor;

	private UserInput userInput;
	private RestaurantInput restaurantInput;

	@BeforeEach
	public void setup() {
		this.userInput = new UserInput("name", "email@email.com", "password1234");
		this.restaurantInput = new RestaurantInput("name", "description", TypeOfFoodEnum.JAPANESE, "address", 1.0, 1.0);
	}

	private String replaceEnumJson(String json) {
		return json.replace("\"JAPANESE\"", "{\"id\":1,\"description\":\"Japanese\"}");
	}

	@Test
	public void invalidNullUserName() throws Exception {
		this.userInput.setName(null);
		Gson gson = new Gson();
		String json = gson.toJson(this.userInput);
		this.mockMvc.perform(post("/api/public/users")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("must not be blank")));
	}

	@Test
	public void invalidBlankUserName() throws Exception {
		this.userInput.setName("");
		Gson gson = new Gson();
		String json = gson.toJson(this.userInput);
		this.mockMvc.perform(post("/api/public/users")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is("must not be blank")));
	}

	@Test
	public void invalidUserEmail() throws Exception {
		this.userInput.setEmail("email");
		Gson gson = new Gson();
		String json = gson.toJson(this.userInput);
		this.mockMvc.perform(post("/api/public/users")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is("must be a well-formed email address")));
	}

	@Test
	public void invalidNullUserEmail() throws Exception {
		this.userInput.setEmail(null);
		Gson gson = new Gson();
		String json = gson.toJson(this.userInput);
		this.mockMvc.perform(post("/api/public/users")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
	}

	@Test
	public void invalidBlankUserEmail() throws Exception {
		this.userInput.setEmail("");
		Gson gson = new Gson();
		String json = gson.toJson(this.userInput);
		this.mockMvc.perform(post("/api/public/users")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
	}

	@Test
	public void invalidPasswordWithouNumber() throws Exception {
		this.userInput.setPassword("password");
		Gson gson = new Gson();
		String json = gson.toJson(this.userInput);
		this.mockMvc.perform(post("/api/public/users")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.password", Is.is("password must contain number and letters")));
	}

	@Test
	public void invalidPasswordWithouLetter() throws Exception {
		this.userInput.setPassword("1234");
		Gson gson = new Gson();
		String json = gson.toJson(this.userInput);
		this.mockMvc.perform(post("/api/public/users")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.password", Is.is("password must contain number and letters")));
	}

	@Test
	public void invalidNullUserPassword() throws Exception {
		this.userInput.setPassword(null);
		Gson gson = new Gson();
		String json = gson.toJson(this.userInput);
		this.mockMvc.perform(post("/api/public/users")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
	}

	@Test
	public void invalidBlankUserPassword() throws Exception {
		this.userInput.setPassword("");
		Gson gson = new Gson();
		String json = gson.toJson(this.userInput);
		this.mockMvc.perform(post("/api/public/users")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
	}

	@Test
	public void validUserInput() throws Exception {
		Mockito.when(this.userService.createRegularUser(Mockito.any(), Mockito.any())).thenReturn(new User());
		Gson gson = new Gson();
		String json = gson.toJson(this.userInput);
		this.mockMvc.perform(post("/api/public/users")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.userService).createRegularUser(Mockito.any(), this.roleEnumArgumentCaptor.capture());
		RoleEnum roleEnum = this.roleEnumArgumentCaptor.getValue();
		Assertions.assertEquals(RoleEnum.ROLE_USER, roleEnum);
	}

	@Test
	public void validUserInputWithEmailAlreadyInUse() throws Exception {
		Mockito.when(this.userService.createRegularUser(Mockito.any(), Mockito.any())).thenReturn(null);
		Gson gson = new Gson();
		String json = gson.toJson(this.userInput);
		this.mockMvc.perform(post("/api/public/users")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is("email already in use")));
		Mockito.verify(this.userService).createRegularUser(Mockito.any(), this.roleEnumArgumentCaptor.capture());
		RoleEnum roleEnum = this.roleEnumArgumentCaptor.getValue();
		Assertions.assertEquals(RoleEnum.ROLE_USER, roleEnum);
	}

	@Test
	public void invalidNullOwner() throws Exception {
		OwnerInput ownerInput = new OwnerInput();
		Gson gson = new Gson();
		String json = gson.toJson(ownerInput);
		this.mockMvc.perform(post("/api/public/users/restaurant")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.user", Is.is("must not be null")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.restaurant", Is.is("must not be null")));
	}

	@Test
	public void invalidNullRestaurantName() throws Exception {
		this.restaurantInput.setName(null);
		OwnerInput ownerInput = new OwnerInput(this.userInput, this.restaurantInput);
		Gson gson = new Gson();
		String json = this.replaceEnumJson(gson.toJson(ownerInput));
		this.mockMvc.perform(post("/api/public/users/restaurant")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.['restaurant.name']", Is.is("must not be blank")));
	}

	@Test
	public void invalidBlankRestaurantName() throws Exception {
		this.restaurantInput.setName("");
		OwnerInput ownerInput = new OwnerInput(this.userInput, this.restaurantInput);
		Gson gson = new Gson();
		String json = this.replaceEnumJson(gson.toJson(ownerInput));
		this.mockMvc.perform(post("/api/public/users/restaurant")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.['restaurant.name']", Is.is("must not be blank")));
	}

	@Test
	public void invalidNullRestaurantDescription() throws Exception {
		this.restaurantInput.setDescription(null);
		OwnerInput ownerInput = new OwnerInput(this.userInput, this.restaurantInput);
		Gson gson = new Gson();
		String json = this.replaceEnumJson(gson.toJson(ownerInput));
		this.mockMvc.perform(post("/api/public/users/restaurant")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.['restaurant.description']", Is.is("must not be blank")));
	}

	@Test
	public void invalidBlankRestaurantDescription() throws Exception {
		this.restaurantInput.setDescription("");
		OwnerInput ownerInput = new OwnerInput(this.userInput, this.restaurantInput);
		Gson gson = new Gson();
		String json = this.replaceEnumJson(gson.toJson(ownerInput));
		this.mockMvc.perform(post("/api/public/users/restaurant")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.['restaurant.description']", Is.is("must not be blank")));
	}

	@Test
	public void invalidNullRestaurantTypeOfFood() throws Exception {
		this.restaurantInput.setTypeOfFood(null);
		OwnerInput ownerInput = new OwnerInput(this.userInput, this.restaurantInput);
		Gson gson = new Gson();
		String json = this.replaceEnumJson(gson.toJson(ownerInput));
		this.mockMvc.perform(post("/api/public/users/restaurant")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.['restaurant.typeOfFood']", Is.is("must not be null")));
	}

	@Test
	public void invalidNullRestaurantAddress() throws Exception {
		this.restaurantInput.setAddress(null);
		OwnerInput ownerInput = new OwnerInput(this.userInput, this.restaurantInput);
		Gson gson = new Gson();
		String json = this.replaceEnumJson(gson.toJson(ownerInput));
		this.mockMvc.perform(post("/api/public/users/restaurant")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.['restaurant.address']", Is.is("must not be blank")));
	}

	@Test
	public void invalidBlankRestaurantAddress() throws Exception {
		this.restaurantInput.setAddress("");
		OwnerInput ownerInput = new OwnerInput(this.userInput, this.restaurantInput);
		Gson gson = new Gson();
		String json = this.replaceEnumJson(gson.toJson(ownerInput));
		this.mockMvc.perform(post("/api/public/users/restaurant")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.['restaurant.address']", Is.is("must not be blank")));
	}

	@Test
	public void validRestaurantInput() throws Exception {
		TypeOfFood typeOfFood = new TypeOfFood();
		this.em.persist(typeOfFood);
		User user = new User();
		this.userRepository.save(user);
		Mockito.when(this.userService.createRegularUser(Mockito.any(), Mockito.any())).thenReturn(user);
		OwnerInput ownerInput = new OwnerInput(this.userInput, this.restaurantInput);
		Gson gson = new Gson();
		String json = this.replaceEnumJson(gson.toJson(ownerInput));
		this.mockMvc.perform(post("/api/public/users/restaurant")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
		Mockito.verify(this.userService).createRegularUser(Mockito.any(), this.roleEnumArgumentCaptor.capture());
		RoleEnum roleEnum = this.roleEnumArgumentCaptor.getValue();
		Assertions.assertEquals(RoleEnum.ROLE_OWNER, roleEnum);
	}

	@Test
	public void validRestaurantInputWithEmailAlreadyInUse() throws Exception {
		Mockito.when(this.userService.createRegularUser(Mockito.any(), Mockito.any())).thenReturn(null);
		OwnerInput ownerInput = new OwnerInput(this.userInput, this.restaurantInput);
		Gson gson = new Gson();
		String json = this.replaceEnumJson(gson.toJson(ownerInput));
		this.mockMvc.perform(post("/api/public/users/restaurant")
				.content(json)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is("email already in use")));
		Mockito.verify(this.userService).createRegularUser(Mockito.any(), this.roleEnumArgumentCaptor.capture());
		RoleEnum roleEnum = this.roleEnumArgumentCaptor.getValue();
		Assertions.assertEquals(RoleEnum.ROLE_OWNER, roleEnum);
	}

}