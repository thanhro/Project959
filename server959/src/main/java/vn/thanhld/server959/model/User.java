package vn.thanhld.server959.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User extends AbstractModel {
	private String email;
	private String displayName;
	private String firstName;
	private String lastName;
	private String photoUrl;
	private String dateOfBirth;

	public User() {
		super();
	}

	public User(String email, String displayName, String firstName, String lastName, String photoUrl,
			String dateOfBirth) {
		super();
		this.email = email;
		this.displayName = displayName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.photoUrl = photoUrl;
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

}
