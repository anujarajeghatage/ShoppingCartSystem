package com.shopping.profileservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.shopping.profileservice.dao.UserProfileRepository;
import com.shopping.profileservice.entity.Address;
import com.shopping.profileservice.entity.UserProfile;
import com.shopping.profileservice.service.UserProfileService;

@SpringBootTest 
class ProfileServiceApplicationTests {
 
	

	@InjectMocks
	private UserProfileService service;
	
	@Mock
	private UserProfileRepository repository;



	@Test
	public void Test_addNewUserProfile() {
		Address address = new Address(12,"dandekar birdge","sdashiv peth","Pune","Maharastra","India", 411030); 
		UserProfile user =  new UserProfile("1",1, "Yuvraj kaulage", "yuvi.jpg", "yuvi@gmail.com", 7758876, "Tech", null, "Male", "Admin", "password", true, address);
	
		when(repository.save(user)).thenReturn(user);
		assertEquals(user, service.addNewUserProfile(user));
	}
	
	@Test
	public void Test_Login() {
		Address address = new Address(12,"sadashiv peth","Pimpri","Pune","Maharastra","India", 411018); 
		UserProfile user =  new UserProfile("1",1, "Yuvi kaulge", "yuvi.jpg", "yuvi@gmail.com", 7758876, "Tech", null, "Male", "Admin", "password", true, address);

		Optional<UserProfile> profile = Optional.of(user);
		
		Map<String, String> userProfile = new HashMap<>();
		userProfile.put("email", "yuvi@gmail.com");
		userProfile.put("password", "yuvi");
		
		when(repository.findByEmailAndPassword(userProfile.get("email"),userProfile.get("password")))
		.thenReturn(profile);
		
		assertEquals(user.getFullName(), service.login(userProfile).getFullName());
	}
	
	@Test
	public void Test_getAllUserProfiles() {
		Address address = new Address(12,"dandekar bridge","sadashiv peth","Pune","Maharastra","India", 411030); 
	 	UserProfile user =  new UserProfile("1",1, "yuvraj kaulage", "yuvi.jpg", "yuvi@gmail.com", 775887, "Tech", null, "Male", "Admin", "password", true, address);
		UserProfile user1 =  new UserProfile("1",2, "Thor", "thor.jpg", "thor@gmail.com", 6370455, "Tech", null, "Male", "Admin", "password", true, address);
		
		List<UserProfile> list =  new ArrayList<>();
		list.add(user);
		list.add(user1);
		
		when(repository.findAll()).thenReturn(list);
		assertEquals(list.size(), service.getAllUserProfiles().size());
	
	}
	@Test
	public void Test_getByProfileId() {
		Address address = new Address(12,"Ajmera","Pimpri","Pune","Maharastra","India", 411018); 
	 	UserProfile user =  new UserProfile("1",1, "Saurabh Raut", "saurabh.jpg", "saurabh@gmail.com", 6370455, "Tech", null, "Male", "Admin", "password", true, address);
		 
		Optional<UserProfile> profile = Optional.of(user);
		
	 	when(repository.findByProfileId(1)).thenReturn(profile);
		assertEquals(user, service.getByProfileId(1)); 
	}
	 
	@Test
	public void Test_getByMobileNumber() {
		Address address = new Address(12,"Ajmera","Pimpri","Pune","Maharastra","India", 411018); 
	 	UserProfile user =  new UserProfile("1",1, "Saurabh Raut", "saurabh.jpg", "saurabh@gmail.com", 986370455, "Tech", null, "Male", "Admin", "password", true, address);
		 
		Optional<UserProfile> profile = Optional.of(user);
		
	 	when(repository.findByMobileNumber(986370455)).thenReturn(profile);
		assertEquals(user, service.getByMobileNumber(986370455)); 
	}
	
	@Test
	public void Test_getByUserName() {
		Address address = new Address(12,"Ajmera","Pimpri","Pune","Maharastra","India", 411018); 
	 	UserProfile user =  new UserProfile("1",1, "Saurabh Raut", "saurabh.jpg", "saurabh@gmail.com", 986370455, "Tech", null, "Male", "Admin", "password", true, address);
		 
		Optional<UserProfile> profile = Optional.of(user);
		
	 	when(repository.findByFullName("Saurabh Raut")).thenReturn(profile);
		assertEquals(user, service.getByUserName("Saurabh Raut")); 
	}
	
//	@Test
//	public void Test_updateUserProfile() {
//		Address address = new Address(12,"Ajmera","Pimpri","Pune","Maharastra","India", 411018); 
//		UserProfile user =  new UserProfile("1",1, "Saurabh Raut", "saurabh.jpg", "saurabh@gmail.com", 906370455, "Tech", null, "Male", "Admin", "password", true, address);
//		service.updateUserProfile(user);
//		verify(repository,times(1).save(user));
//	}
	
//	@Test
//	public void Test_deleteUserProfile() {
//		Address address = new Address(12,"Ajmera","Pimpri","Pune","Maharastra","India", 411018); 
//		UserProfile user =  new UserProfile("1",1, "Saurabh Raut", "saurabh.jpg", "saurabh@gmail.com", 906370455, "Tech", null, "Male", "Admin", "password", true, address);
//		service.deleteUserProfile(1);
//		verify(repository,times(1).deleteByProfileId(1));
//	}

	
//	@Autowired
//	private MockMvc mockMvc;
	
//	@Test
//	public void test_getAllUserProfiles() throws Exception {
//		Address address = new Address(12,"BTM","BTM","Bangalore","Karnatak","India", 754001); 
//
//		when(service.getAllUserProfiles())
//			.thenReturn(Stream
//					.of(new UserProfile("1",1, "Saurabh Raut", "saurabh.jpg", "saurabh@gmail.com", 6370455, "Tech", null, "Male", "Admin", "password", true, address),
//						new UserProfile("1",2, "Iron Man", "iron.jpg", "iron@gmail.com", 6370455, "Tech", null, "Male", "Admin", "password", true, address))
//				.collect(Collectors.toList())); 
//	}
	
	
//	@Test
//	public void testAddNewCustomer() throws Exception {	
//		Address address = new Address(12,"BTM","BTM","Bangalore","Karnatak","India", 754001); 
//		UserProfile user =  new UserProfile("1",1, "Saurabh Raut", "saurabh.jpg", "saurabh@gmail.com", 6370455, "Tech", null, "Male", "Admin", "password", true, address);
//		
//		ObjectMapper mapper = new ObjectMapper();
//		String jsonUser = mapper.writeValueAsString(user);
//		
//		when(service.addNewUserProfile(user)).thenReturn(user);
//		
//		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createNewCustomer")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(jsonUser);
//		ResultActions perform = mockMvc.perform(requestBuilder);
//		
//		MvcResult mvcResult = perform.andReturn();
//		MockHttpServletResponse response = mvcResult.getResponse();  
//		int status = response.getStatus();
//		assertEquals(201, status);   
//	}
 
 
	 
	
}

