package org.isag_ghana.alpha.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	private float price;

	private String currency;

	private String location;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private Set<ProductType> productTypes;

	private String shortDescription;

	private Boolean isAvailable;

	private LocalDateTime date = LocalDateTime.now();

	@OneToMany(fetch = FetchType.EAGER)
	private Set<Photo> photos;

	@OneToMany
	private Set<Video> videos;

	@ManyToOne
	private User owner;
}
