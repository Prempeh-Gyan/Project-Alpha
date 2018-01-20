package org.isag_ghana.alpha.dto;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.isag_ghana.alpha.model.Product;
import org.isag_ghana.alpha.model.ProductType;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class AddProductFormDTO {

	@NotEmpty(message = "Product must have a name")
	@NotNull(message = "Product name cannot be null")
	private String name;

	@NotEmpty(message = "Enter a valid product price")
	@NotNull(message = "Product price cannot be null")
	@Length(max = 9, min = 0, message = "Amount is too large")
	private String price;

	@NotEmpty(message = "Currency must be specified")
	@NotNull(message = "Please select currency")
	private String currency;

	@NotEmpty(message = "A location must be provided")
	@NotNull(message = "Location of product is required")
	private String location;

	@NotEmpty(message = "Product Description cannot be empty")
	@NotNull(message = "Product Description is required")
	@Length(max = 2000, min = 3, message = "Product description is required and must be at most 2000 characters")
	private String shortDescription;

	Set<ProductType> productTypes = new HashSet<>();

	Number productPrice = 0;

	@AssertTrue(message = "Invalid amount entered for price")
	public boolean isPriceValid() {
		try {
			productPrice = NumberFormat.getCurrencyInstance(Locale.US).parse("$" + price);
			log.info("Product price is : {}", productPrice);
		} catch (ParseException e) {
			this.price = "";
			return false;
		}
		return true;
	}

	public Product createProduct() {

		ProductType productType = new ProductType();
		productType.setName(name.toLowerCase());
		productTypes.add(productType);
		log.info("Default product type is name of product : {}", productType.getName());

		Product product = new Product();
		product.setName(name);
		product.setPrice(productPrice.floatValue());
		product.setCurrency(currency);
		product.setLocation(location);
		product.setIsAvailable(true);
		product.setShortDescription(shortDescription);
		product.setProductTypes(productTypes);
		return product;
	}

}
