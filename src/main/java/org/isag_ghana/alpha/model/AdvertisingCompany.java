package org.isag_ghana.alpha.model;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
public class AdvertisingCompany {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String companyName;

	private Boolean isNonExpired;

	private Calendar effectiveDate;

	private Calendar expiryDate;

	private String advertCode;

	private LocalDateTime date = LocalDateTime.now();

	@OneToOne
	private User companyUser;

	@OneToMany
	private Set<Photo> bannerAds;

	@OneToMany
	private Set<Photo> squareAds;

	@OneToMany
	private Set<Video> videos;

}
