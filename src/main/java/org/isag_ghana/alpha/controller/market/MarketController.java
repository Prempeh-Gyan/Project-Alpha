package org.isag_ghana.alpha.controller.market;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.dto.SendMessageFormDTO;
import org.isag_ghana.alpha.model.Photo;
import org.isag_ghana.alpha.model.Product;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.MessageService;
import org.isag_ghana.alpha.service.PhotoService;
import org.isag_ghana.alpha.service.ProductService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class MarketController {
	private final @NotNull ProductService productService;
	private final @NotNull UserService userService;
	private final @NotNull PhotoService photoService;
	private final @NotNull MessageService messageService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/market/isag-market" }, method = RequestMethod.GET)
	public String marketProductList(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {
		log.info("market place fired...");
		model.addAttribute("page", productService.findAll(pageable));
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "isag-market";
	}

	@RequestMapping(value = { "/market/market-product" }, method = RequestMethod.GET)
	public String marketProducts(RedirectAttributes redirectAttributes) {
		return "redirect:/market/isag-market";
	}

	@RequestMapping(value = { "/market/delete-product-from-market" }, method = RequestMethod.POST)
	public String deleteProduct(@RequestParam("productId") Long productId, RedirectAttributes redirectAttributes) {
		Product product = productService.findOne(productId);
		product.setIsAvailable(false);
		productService.save(product);
		redirectAttributes.addFlashAttribute("manageAccount", "true");
		redirectAttributes.addFlashAttribute("profileTab", 0);
		redirectAttributes.addFlashAttribute("marketTab", "true");
		redirectAttributes.addFlashAttribute("marketSuccess", "You have successfully deleted your product!");
		return "redirect:/member/my-profile";
	}

	@RequestMapping(value = { "/market/market-product/{productId}" }, method = RequestMethod.GET)
	public String marketProducts(@CurrentUser User currentUser, Model model,
			@PathVariable("productId") Long productId) {
		log.info("market place fired...");
		log.info("product id is ... : {}", productId);
		Product product = productService.findOne(productId);
		model.addAttribute("product", product);
		model.addAttribute("photos", new ArrayList<>(product.getPhotos()));
		model.addAttribute("sendMessageFormDTO", new SendMessageFormDTO());
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "market-product";
	}

	@RequestMapping(value = "/market/getCoverPicture/{parentId}", method = RequestMethod.GET, produces = MediaType.ALL_VALUE)
	public ResponseEntity<byte[]> getImage(@PathVariable("parentId") Long parentId) throws IOException {
		log.info("Parent id is : {}", parentId);
		Product product = productService.findOne(parentId);
		List<Photo> photos = new ArrayList<>(product.getPhotos());
		byte[] imageContent = null;
		for (Photo photo : photos) {
			if (photo.getName() != null && photo.getName().equalsIgnoreCase("coverPicture")) {
				imageContent = photo.getImage();
			}
		}
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		log.info("returning image");
		return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	}
}
