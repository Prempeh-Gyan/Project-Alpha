package org.isag_ghana.alpha.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Business {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String companyName;

	private String website;

	private String address;

	private String email;

	private String phoneNumber;

	private String buisinessDescription;

	private LocalDateTime date = LocalDateTime.now();

	private LocalDate dateFounded;

	@ManyToMany(fetch = FetchType.LAZY)
	private Set<BusinessType> businessTypes;

	@ManyToOne
	private User owner;

}
