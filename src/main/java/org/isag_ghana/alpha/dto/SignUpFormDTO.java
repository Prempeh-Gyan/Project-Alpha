package org.isag_ghana.alpha.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.isag_ghana.alpha.model.Profile;
import org.isag_ghana.alpha.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class SignUpFormDTO {

	@NotNull(message = "Firstname cannot be null")
	@NotEmpty(message = "Firstname cannot be Empty")
	@Size(min = 2, max = 30, message = "Firstname is expected to be at least 2 characters")
	private String firstName;

	@NotNull(message = "Lastname cannot be null")
	@NotEmpty(message = "Lastname cannot be Empty")
	@Size(min = 2, max = 30, message = "Lastname is expected to be at least 2 characters")
	private String lastName;

	@NotNull(message = "Gender cannot be null")
	@NotEmpty(message = "Gender cannot be Empty")
	private String gender;

	@Size(max = 12, message = "Error with Date of Birth")
	private String dateOfBirth1;

	private LocalDate dateOfBirth;

	@NotNull(message = "Email cannot be null")
	@NotEmpty(message = "Email cannot be Empty")
	@Email(message = "Email format is incorrect", regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
	private String email;

	@NotNull(message = "Country must be selected")
	@NotEmpty(message = "Country cannot be Empty")
	private String nationality;

	@NotNull(message = "Partner's Country must be selected")
	@NotEmpty(message = "Partner's Country cannot be Empty")
	private String partnersNationality;

	@NotNull(message = "Partner's Country must be selected")
	@NotEmpty(message = "Partner's Country cannot be Empty")
	private String countryOfResidence;

	@NotNull(message = "Country must be selected")
	@NotEmpty(message = "Country cannot be Empty")
	private String city;

	@NotNull(message = "Profession cannot be null")
	@NotEmpty(message = "Profession cannot be Empty")
	@Size(min = 2, max = 40, message = "Profession must be between 2 and 60 characters")
	private String profession;

	@NotNull(message = "Phonenumber cannot be null")
	@NotEmpty(message = "Phonenumber cannot be Empty")
	@Size(min = 10, max = 20, message = "Phonenumber is expected to be between 10 and 20 digits")
	@Pattern(regexp = "\\d+", message = "Phonenumber must be digits only")
	private String phoneNumber;

	@NotNull(message = "Password cannot be null")
	@NotEmpty(message = "Password cannot be Empty")
	@Size(min = 1, max = 25, message = "Password must be between 6 and 25 characters")
	private String password;

	@NotNull(message = "Confirm Password cannot be null")
	@NotEmpty(message = "Confirm Password cannot be Empty")
	private String confirmPassword;

	@AssertTrue(message = "Passwords do not match")
	public boolean isValid() {
		log.info("Password is : {}  and confirm password is : {}  isValid returns: {}", this.password,
				this.confirmPassword, this.password == null ? false : this.password.equals(this.confirmPassword));
		return this.password == null ? false : this.password.equals(this.confirmPassword);
	}

	@AssertTrue(message = "Incorrect Date format!")
	public boolean isEighteen() {

		if (dateOfBirth1 == null || dateOfBirth1 == "" || dateOfBirth1.isEmpty()) {

			return true;

		} else {

			try {

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

				this.dateOfBirth = LocalDate.parse(dateOfBirth1 == null ? "" : dateOfBirth1, formatter);

			} catch (DateTimeParseException e) {

				return false;
			}
		}
		return true;

	}

	/*
	 * @AssertTrue(message = "One Spouse must be Ghanaian") public boolean
	 * isSpouseGhanaian() {
	 * 
	 * String nationality2 = this.nationality == null ||
	 * this.nationality.isEmpty() ? "no country" : this.nationality; String
	 * partnersNationality2 = this.partnersNationality == null ||
	 * this.partnersNationality.isEmpty() ? "no country" :
	 * this.partnersNationality; return (nationality2.equalsIgnoreCase("Ghana")
	 * && !partnersNationality2.equalsIgnoreCase("Ghana")) ||
	 * (!nationality2.equalsIgnoreCase("Ghana") &&
	 * partnersNationality2.equalsIgnoreCase("Ghana")) ? true : false; }
	 */

	public User createUser(User user) {

		user.setEmail(email);
		user.setPassword(new BCryptPasswordEncoder().encode(password));
		user.setIsAccountNonExpired(false);
		user.setIsAccountNonLocked(false);
		user.setIsCredentialsNonExpired(false);
		user.setIsEnabled(false);

		user.setProfile(new Profile());
		user.getProfile().setFirstName(firstName.trim());
		user.getProfile().setLastName(lastName.trim());
		user.getProfile().setGender(gender);
		user.getProfile().setEmail(email.trim());
		user.getProfile().setNationality(nationality != null ? nationality.trim() : null);
		user.getProfile().setPartnersNationality(partnersNationality != null ? partnersNationality.trim() : null);
		user.getProfile().setPhoneNumber(phoneNumber.trim());
		user.getProfile().setDateOfBirth(dateOfBirth);
		user.getProfile().setCountryOfResidence(countryOfResidence);
		user.getProfile().setCity(city);
		user.getProfile().setProfession(profession);

		return user;
	}
}
