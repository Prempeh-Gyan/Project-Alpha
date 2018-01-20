package org.isag_ghana.alpha.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;
import org.isag_ghana.alpha.model.Business;

import lombok.Data;

@Data
public class AddBusinessFormDTO {

	@NotEmpty(message = "Business must have a name")
	@NotNull(message = "Business name cannot be null")
	private String companyName;

	@NotEmpty(message = "Address must be specified")
	@NotNull(message = "Please specify address")
	private String address;

	@NotEmpty(message = "Product Description cannot be empty")
	@NotNull(message = "Product Description is required")
	@Length(max = 2000, min = 3, message = "Business description is required and must be between 3 and 2000 characters")
	private String buisinessDescription;

	private String date;

	private LocalDate dateFounded;

	@URL(message = "The URL address to your company website must be in the form of http://www.myBusinessAddress.com")
	private String website;

	@Email(message = "Email format is incorrect", regexp = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
	private String email;

	@NotNull(message = "Phonenumber cannot be null")
	@NotEmpty(message = "Phonenumber cannot be Empty")
	@Size(min = 10, max = 20, message = "Phonenumber is expected to be between 10 and 20 digits")
	@Pattern(regexp = "\\d+", message = "Phonenumber must be digits only")
	private String phoneNumber;

	@AssertTrue
	public boolean isValidDate() {

		try {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

			this.dateFounded = LocalDate.parse(date == null ? "" : date, formatter);

		} catch (DateTimeParseException e) {

			return false;
		}

		return true;

	}

	public Business createBusiness() {

		Business business = new Business();
		business.setCompanyName(companyName);
		business.setAddress(address);
		business.setBuisinessDescription(buisinessDescription);

		if (email != null && !email.isEmpty()) {

			business.setEmail(email);
		}

		if (website != null && !website.isEmpty()) {
			business.setWebsite(website);
		}

		business.setPhoneNumber(phoneNumber);
		business.setDateFounded(dateFounded);

		return business;
	}

}
