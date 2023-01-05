package com.shopping.profileservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.profileservice.entity.JwtRes;
import com.shopping.profileservice.entity.LoginReq;
import com.shopping.profileservice.entity.MessageRes;
import com.shopping.profileservice.entity.SignupReq;
import com.shopping.profileservice.entity.UserProfile;
import com.shopping.profileservice.security.MyUserDetails;
import com.shopping.profileservice.security.jwt.JwtProvider;
import com.shopping.profileservice.service.UserProfileService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@AllArgsConstructor
public class ProfileController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final UserProfileService userProfileService;

    @PostMapping(value = "/createNewCustomer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewCustomer(@RequestBody UserProfile userProfile) {
       
    	if (userProfileService.existsByEmail(userProfile.getEmail())) {
            return new ResponseEntity<>(new MessageRes("User already exists!"),
                    HttpStatus.BAD_REQUEST);
        } 
    	userProfile.setRole("ROLE_USER");
        userProfile.setPassword(encoder.encode(userProfile.getPassword()));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userProfileService.addNewUserProfile(userProfile));
    }

    @PostMapping(value = "/createNewMerchant", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewMerchant(@RequestBody UserProfile userProfile) {
    	 if (userProfileService.existsByEmail(userProfile.getEmail())) {
             return new ResponseEntity<>(new MessageRes("User already exists!"),
                     HttpStatus.BAD_REQUEST);
         } 
    	userProfile.setRole("ROLES_ADMIN");
        userProfile.setPassword(encoder.encode(userProfile.getPassword()));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userProfileService.addNewUserProfile(userProfile));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody LoginReq loginReq) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateJwtToken(authentication);
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(
                new JwtRes(userDetails.getId(),
                        userDetails.getName(),
                        userDetails.getUsername(),
                        token,
                        userDetails.getIsAdmin()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@Valid @RequestBody SignupReq signupReq) {
        if (userProfileService.existsByEmail(signupReq.getEmail())) {
            return new ResponseEntity<>(new MessageRes("User already exists!"),
                    HttpStatus.BAD_REQUEST);
        }
        UserProfile user = UserProfile
                .builder()
                .fullName(signupReq.getName())
                .email(signupReq.getEmail())
                .isAdmin(false)
                .password(encoder.encode(signupReq.getPassword()))
                .build();
        if(signupReq.getRole().equalsIgnoreCase("Delivery")) {
        	user.setRole("ROLES_DELEVERY");
        }else if(signupReq.getRole().equalsIgnoreCase("Merchant")){
        	user.setRole("ROLES_ADMIN");
        	user.setIsAdmin(true);
        }else {
        	user.setRole("ROLES_USER");
        }
       
        userProfileService.addNewUserProfile(user);
        return login(new LoginReq(signupReq.getEmail(), signupReq.getPassword()));
    }

    @PostMapping(value = "/createNewDeliveryProfile", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserProfile> addNewDeliveryProfile(@RequestBody UserProfile userProfile) {
        userProfile.setRole("ROLE_DELEVERY");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userProfileService.addNewUserProfile(userProfile));
    }
    @GetMapping(value = "/allUserProfiles" )
    public ResponseEntity<List<UserProfile>> getAllUserProfiles() {
        return ResponseEntity.ok(userProfileService.getAllUserProfiles());
    }


    @GetMapping(value = "/{profileId}")
    public ResponseEntity<UserProfile> getAllProfileById(@PathVariable("profileId") int profileId ) {
        return ResponseEntity.ok(userProfileService.getByProfileId(profileId));
    }

    @GetMapping(value = "/mobile/{mobilePhone}")
    public ResponseEntity<UserProfile> getAllByMobilePhone(@RequestParam (value = "mobilePhone") long mobilePhone ) {
        return ResponseEntity.ok(userProfileService.getByMobileNumber(mobilePhone));
    }

    @GetMapping(value = "/name/{userName}" )
    public ResponseEntity<UserProfile> getByName(@RequestParam (value = "userName") String userName ) {
        return ResponseEntity.ok(userProfileService.getByUserName(userName));
    }

    @PutMapping(value = "/updateProfile" )
    public ResponseEntity<String> updateProduct(@RequestBody UserProfile userProfile) {
        userProfileService.updateUserProfile(userProfile);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/delete/{profileId}")
    public ResponseEntity<String> deleteProfile(@RequestParam(value = "profileId") int profileId) {	
    	userProfileService.deleteUserProfile(profileId);
        return ResponseEntity.ok("Profile Deleted successfully");
    }
}
