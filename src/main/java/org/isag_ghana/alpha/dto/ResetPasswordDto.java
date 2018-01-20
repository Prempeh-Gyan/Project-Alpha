package org.isag_ghana.alpha.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ResetPasswordDto {

	@NotNull(message = "Password cannot be null")
	@NotEmpty(message = "Password cannot be Empty")
	@Size(min = 6, max = 25, message = "Password must be between 6 and 25 characters")
	private String password;

	@NotNull(message = "Confirm Password cannot be null")
	@NotEmpty(message = "Confirm Password cannot be Empty")
	private String confirmPassword;

	@AssertTrue(message = "Passwords do not match")
	public boolean isValid() {
		log.info("Password is : {}  and confirm password is : {}  isValid returns: {}", this.password,
				this.confirmPassword, this.password == null ? false : this.password.equals(this.confirmPassword));
		return this.password == null || this.password == "" || this.password.isEmpty() ? false
				: this.password.equals(this.confirmPassword);
	}

}
