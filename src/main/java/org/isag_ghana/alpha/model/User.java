package org.isag_ghana.alpha.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.jboss.aerogear.security.otp.api.Base32;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(unique = true, nullable = false)
	private String email;

	private String password;

	private Boolean isEnabled = false;

	private Boolean isAccountNonExpired = false;

	private Boolean isAccountNonLocked = false;

	private Boolean isCredentialsNonExpired = false;

	private Boolean isUsing2FA = false;
	
	private Boolean isEmailConfirmed = false;

	private String secret = Base32.random();

	private LocalDateTime userCreationDate = LocalDateTime.now();

	@ManyToOne
	private Role role;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private Profile profile;

	public User(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.profile = user.getProfile();
		this.isEnabled = user.getIsEnabled();
		this.isAccountNonExpired = user.getIsAccountNonExpired();
		this.isAccountNonLocked = user.getIsAccountNonLocked();
		this.isCredentialsNonExpired = user.getIsCredentialsNonExpired();
		this.role = user.getRole();
	}
}
