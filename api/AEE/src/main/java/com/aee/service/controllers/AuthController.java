package com.aee.service.controllers;

import com.aee.service.form.CheckAccountForm;
import com.aee.service.form.CreateAccountForm;
import com.aee.service.mapper.AuthMapper;
import com.aee.service.models.ERole;
import com.aee.service.models.Role;
import com.aee.service.models.User;
import com.aee.service.payload.request.FirebaseLoginRequest;
import com.aee.service.payload.request.LoginRequest;
import com.aee.service.payload.response.BaseResponse;
import com.aee.service.payload.response.JwtResponse;
import com.aee.service.repository.RoleRepository;
import com.aee.service.repository.UserRepository;
import com.aee.service.security.jwt.JwtUtils;
import com.aee.service.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
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

	@PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResponse<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		BaseResponse<JwtResponse> baseResponse = new BaseResponse<>();
		baseResponse.setData(new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail(),
				roles));
		baseResponse.setMessage("Signin success");
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
		if (user==null){
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
		if (user==null){
			baseResponse.setMessage("email already used");
			return baseResponse;
		}
		Set<Role> roles = new HashSet<>();
		Role userRole = roleRepository.findByName(ERole.ROLE_USER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(userRole);
		user = authMapper.fromCreateFormToAccount(createCustomerForm);
		user.setRoles(roles);
		user.setUsername(response.getBody().getUsers().get(0).getProviderUserInfo().get(0).getDisplayName());
		baseResponse.setMessage("check success, this email can be used to register");
		baseResponse.setResult(true);
		baseResponse.setMessage("Register customer success");
		return baseResponse;
	}
}
