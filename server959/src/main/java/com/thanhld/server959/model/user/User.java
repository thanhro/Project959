package com.thanhld.server959.model.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import com.thanhld.server959.model.AbstractModel;
import com.thanhld.server959.model.security.AuthProvider;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "user")
public class User extends AbstractModel {

	private String id;

	private String name;

	private String email;

	private String imageUrl;

	private Boolean emailVerified = false;

	@JsonIgnore
	private String password;

	@NotNull
	@JsonEnumDefaultValue //FIXME
	private AuthProvider authProvider;

	private String providerId;

	public User() {
	}

	public User(String id, String name, @Email String email, String imageUrl, Boolean emailVerified, String password,
			@NotNull AuthProvider authProvider, String providerId) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.imageUrl = imageUrl;
		this.emailVerified = emailVerified;
		this.password = password;
		this.authProvider = authProvider;
		this.providerId = providerId;
	}

	public String getId() {
		return id;
	}

	public void setUserId(String userId) {
		this.id = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Boolean getEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(Boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AuthProvider getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(AuthProvider authProvider) {
		this.authProvider = authProvider;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

}
