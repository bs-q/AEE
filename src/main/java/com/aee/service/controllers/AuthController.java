package com.aee.service.controllers;

import com.aee.service.form.CheckAccountForm;
import com.aee.service.form.CreateAccountForm;
import com.aee.service.mapper.AuthMapper;
import com.aee.service.models.ERole;
import com.aee.service.models.Role;
import com.aee.service.models.User;
import com.aee.service.models.firebase.FbUserDetail;
import com.aee.service.payload.request.FirebaseLoginRequest;
import com.aee.service.payload.request.LoginRequest;
import com.aee.service.payload.request.LoginWithGoogleRequest;
import com.aee.service.payload.response.BaseResponse;
import com.aee.service.payload.response.LoginResponse;
import com.aee.service.repository.role.RoleRepository;
import com.aee.service.repository.user.UserRepository;
import com.aee.service.security.jwt.JwtUtils;
import com.aee.service.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.Console;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
    UserRepository userRepository;

	@Autowired
    RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RestTemplate restTemplate;

	@Value("${firebase.customer.api.key}")
	String accountKey;

	@Autowired
	AuthMapper authMapper;

	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = null;
		BaseResponse<LoginResponse> baseResponse = new BaseResponse<>();

		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		} catch (AuthenticationException e){
			baseResponse.setMessage("Wrong password");
			return baseResponse;
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		LoginResponse response = authMapper.fromUserToLoginResponse(userDetails);
		response.setToken(jwt);
		baseResponse.setData(response);
		baseResponse.setMessage("Signin success");
		baseResponse.setResult(true);
		return baseResponse;
	}
	@PostMapping(value = "/login-google", produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<LoginResponse> loginWithGoogle(@Valid @RequestBody LoginWithGoogleRequest loginRequest) {
		Authentication authentication = null;
		BaseResponse<LoginResponse> baseResponse = new BaseResponse<>();
		User user = userRepository.findByUid(loginRequest.getUid()).orElse(null);

		if (user == null){
			baseResponse.setMessage("Not register");
			return baseResponse;
		}
		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),"GOOGLE", AuthorityUtils.createAuthorityList("ROLE_USER")));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (AuthenticationException e){
			log.error(e.toString());
			baseResponse.setMessage("Wrong password");
			return baseResponse;
		}
		String jwt = jwtUtils.generateJwtToken(authentication);
		LoginResponse response = authMapper.fromUserToLoginResponse(user);
		response.setToken(jwt);
		baseResponse.setData(response);
		baseResponse.setMessage("Sign-in success");
		baseResponse.setResult(true);
		return baseResponse;
	}



	@PostMapping(value = "/check-register", produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> checkRegister(@Valid @RequestBody CheckAccountForm checkAccountForm, BindingResult bindingResult){
		//call firebase for check User
		BaseResponse<String> baseResponse = new BaseResponse<>();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		String url = "https://identitytoolkit.googleapis.com/v1/accounts:lookup?key="+accountKey;
		String data = "{\"idToken\":\""+checkAccountForm.getFirebaseToken()+"\"}";
		HttpEntity<String> entity = new HttpEntity<>(data, headers);
		ResponseEntity<FirebaseLoginRequest> response = restTemplate.exchange(url, HttpMethod.POST, entity, FirebaseLoginRequest.class);
		FirebaseLoginRequest firebaseLoginDto = response.getBody();
		if(firebaseLoginDto.getUsers().isEmpty() || !firebaseLoginDto.validationAccountData(checkAccountForm)){
			baseResponse.setMessage("firebase token invalid");
			return baseResponse;
		}
		User user = userRepository.findByUid(checkAccountForm.getFirebaseUserId()).orElse(null);
		if (user!=null){
			baseResponse.setMessage("email already used");
			return baseResponse;
		}
		baseResponse.setMessage("check success, this email can be used to register");
		baseResponse.setResult(true);
		return baseResponse;
	}
	@Transactional
	@PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<String> register(@Valid @RequestBody CreateAccountForm createCustomerForm, BindingResult bindingResult) {
		BaseResponse<String> baseResponse = new BaseResponse<>();

		//call firebase for check User
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		String url = "https://identitytoolkit.googleapis.com/v1/accounts:lookup?key="+accountKey;
		String data = "{\"idToken\":\""+createCustomerForm.getFirebaseToken()+"\"}";
		HttpEntity<String> entity = new HttpEntity<>(data, headers);
		ResponseEntity<FirebaseLoginRequest> response = restTemplate.exchange(url, HttpMethod.POST, entity, FirebaseLoginRequest.class);
		FirebaseLoginRequest firebaseLoginDto = response.getBody();
		if(firebaseLoginDto.getUsers().isEmpty() || !firebaseLoginDto.validationAccountData(createCustomerForm)){
			baseResponse.setMessage("firebase token invalid");
			return baseResponse;
		}
		User user = userRepository.findByUid(createCustomerForm.getFirebaseUserId()).orElse(null);
		if (user!=null){
			baseResponse.setMessage("email already used");
			return baseResponse;
		}
		Set<Role> roles = new HashSet<>();
		Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);
		FbUserDetail fbUserDetail = response.getBody().getUsers().get(0).getProviderUserInfo().get(0);
		user = authMapper.fromCreateFormToAccount(createCustomerForm);
		user.setRoles(roles);
		user.setUsername(fbUserDetail.getDisplayName());
		user.setPassword(encoder.encode(createCustomerForm.getPassword()));
		user.setAvatarPath(fbUserDetail.getPhotoUrl());
		userRepository.save(user);
		baseResponse.setMessage("check success, this email can be used to register");
		baseResponse.setResult(true);
		baseResponse.setMessage("Register customer success");
		return baseResponse;
	}
}
