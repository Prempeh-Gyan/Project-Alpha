package org.isag_ghana.alpha.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String firstName;

	private String lastName;

	private String nationality;

	private String gender;

	private String partnersNationality;

	private String profession;

	private String countryOfResidence;

	private String city;

	private LocalDate dateOfBirth;

	private String phoneNumber;

	@Column(unique = true, nullable = false)
	private String email;

	@OneToOne(mappedBy = "profile", fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
	private User user;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "photo_id", referencedColumnName = "id")
	private Photo photo;

	public Profile(Profile userProfile) {
		id = userProfile.getId();
		firstName = userProfile.getFirstName();
		lastName = userProfile.getLastName();
		email = userProfile.getEmail();
	}

}
