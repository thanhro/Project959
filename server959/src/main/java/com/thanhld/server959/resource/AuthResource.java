package com.thanhld.server959.resource;

import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;

import javax.validation.Valid;

import com.thanhld.server959.model.security.ResponseObjectFactory;
import com.thanhld.server959.service.googledrive.GoogleDriveAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.thanhld.server959.exception.BadRequestException;
import com.thanhld.server959.model.security.AuthProvider;
import com.thanhld.server959.model.user.User;
import com.thanhld.server959.payload.ApiResponse;
import com.thanhld.server959.payload.AuthResponse;
import com.thanhld.server959.payload.LoginRequest;
import com.thanhld.server959.payload.SignUpRequest;
import com.thanhld.server959.repository.UserRepository;
import com.thanhld.server959.security.TokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthResource {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private GoogleDriveAuthService googleDriveAuthService;

	private static final String HOST = "http://localhost:8080/auth/oauth2/callback";

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = tokenProvider.createToken(authentication);
		return ResponseEntity.ok(new AuthResponse(token));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new BadRequestException("Email address already in use.");
		}

		// Creating user's account
		User user = new User();
		user.setName(signUpRequest.getName());
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(signUpRequest.getPassword());
		user.setAuthProvider(AuthProvider.local);

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		User result = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/me")
				.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully@"));
	}

	@GetMapping("/oauth2")
	public String login() throws IOException, GeneralSecurityException {
		return googleDriveAuthService.getGoogleAuthorizationCodeFlow().newAuthorizationUrl().setRedirectUri(HOST).build();
	}

	@GetMapping("/oauth2/callback")
	public ResponseEntity<String> loginCallback(@RequestParam("code") String code) throws Exception {
		if (code != null){
			googleDriveAuthService.saveToken(code);
			return ResponseObjectFactory.toResult("OK", HttpStatus.OK);
		}
		return ResponseObjectFactory.toResult("ERROR", HttpStatus.BAD_REQUEST);
	}
}