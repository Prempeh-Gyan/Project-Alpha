package org.isag_ghana.alpha.controller.member;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.dto.AddProductFormDTO;
import org.isag_ghana.alpha.model.Photo;
import org.isag_ghana.alpha.model.Product;
import org.isag_ghana.alpha.model.ProductType;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.PhotoService;
import org.isag_ghana.alpha.service.ProductService;
import org.isag_ghana.alpha.service.ProductTypeService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class AddProductController {

	private final @NotNull UserService userService;
	private final @NotNull ProductService productService;
	private final @NotNull PhotoService photoService;
	private final @NotNull ProductTypeService productTypeService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/member/add-product-to-marketplace" }, method = RequestMethod.GET)
	public String getUpdateIndexPage(@CurrentUser User currentUser, Model model) {
		model.addAttribute("addProductFormDTO", new AddProductFormDTO());
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		return "add-product-to-marketplace";
	}

	@RequestMapping(value = { "/member/add-product-to-marketplace" }, method = RequestMethod.POST)
	public String setUpdateIndexPage(@Valid @ModelAttribute("addProductFormDTO") AddProductFormDTO addProductFormDTO,
			BindingResult bindingResult, Model model, @CurrentUser User currentUser,
			@RequestParam(name = "files") List<MultipartFile> files,
			@RequestParam(name = "coverPicture") MultipartFile coverPicture, @RequestParam(name = "tags") String tags,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("addProductFormDTO", addProductFormDTO);
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			return "add-product-to-marketplace";
		} else {

			Set<Photo> photos = new HashSet<>();

			log.info("Total number of files = {}", files.size());

			if (coverPicture != null && !coverPicture.isEmpty()) {
				if ((coverPicture.getOriginalFilename().contains(".")) && (coverPicture.getOriginalFilename()
						.substring(coverPicture.getOriginalFilename().lastIndexOf("."))
						.matches("((\\.(?i)(jpg||png||gif||bmp))$)"))) {
					log.info("Cover Photo file format is a match");
					try {
						// Save Cover Picture
						Photo coverPhoto = new Photo();
						coverPhoto.setName("coverPicture");
						coverPhoto.setImage(coverPicture.getBytes());
						photoService.save(coverPhoto);
						photos.add(coverPhoto);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					model.addAttribute("addProductFormDTO", addProductFormDTO);
					Notification.getNotifications(model, currentUser, userService, messageRecipientService,
							committeeAndCommitteeMembersService);
					model.addAttribute("fileFormatError", "File Format not accepted");
					return "add-product-to-marketplace";
				}
			} else {
				model.addAttribute("addProductFormDTO", addProductFormDTO);
				model.addAttribute("fileFormatError",
						"Please add at least one picture that relates to the product you're posting | Upload a cover picture");
				Notification.getNotifications(model, currentUser, userService, messageRecipientService,
						committeeAndCommitteeMembersService);
				return "add-product-to-marketplace";
			}

			if (files != null || !files.isEmpty()) {

				for (MultipartFile file : files) {
					if (file.isEmpty()) {
						log.info("file is empty");
						continue; // go to the next file
					} else {
						log.info("file name is : {}", file.getOriginalFilename());
						log.info("ext is : {}",
								file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));

						if ((file.getOriginalFilename().contains("."))
								&& (file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."))
										.matches("((\\.(?i)(jpg||png||gif||bmp))$)"))) {
							log.info("file format is a match");

							try {
								Photo photo = new Photo();
								photo.setImage(file.getBytes());
								photo.setName(file.getOriginalFilename()
										.substring(file.getOriginalFilename().lastIndexOf(".")));
								photoService.save(photo);
								photos.add(photo);
								log.info("Photo added : {}", file.getOriginalFilename());
							} catch (IOException e) {
								e.printStackTrace();
							}

						} else {
							model.addAttribute("addProductFormDTO", addProductFormDTO);
							Notification.getNotifications(model, currentUser, userService, messageRecipientService,
									committeeAndCommitteeMembersService);
							model.addAttribute("fileFormatError", "File Format not accepted");
							return "add-product-to-marketplace";
						}
					}
				}
			}

			Product product = addProductFormDTO.createProduct();
			Set<String> productTypeStrings = new HashSet<>();
			Set<ProductType> productTypes = new HashSet<>();
			ProductType productType;
			String productNameTag = product.getName().replaceAll("\\s+", "").toLowerCase();
			productTypeStrings.add(productNameTag);

			if (tags != null && tags != "" && tags.contains("#")) {

				tags = tags.replaceAll("\\s+", "");
				StringTokenizer stringTokenizer = new StringTokenizer(tags, "#");

				while (stringTokenizer.hasMoreTokens()) {
					String currentToken = stringTokenizer.nextToken().toLowerCase();
					productTypeStrings.add(currentToken);
				}

			}

			for (String tag : productTypeStrings) {
				log.info("HashTag:  #{}", tag);
				ProductType dbProductType = productTypeService.findOne(tag);
				if (dbProductType == null) {
					log.info("#{} does not exist in the database, creating new hashtag", tag);
					productType = new ProductType();
					productType.setName(tag);
					productTypeService.save(productType);
					productTypes.add(productType);
				} else {
					log.info("#{} already exist in the database, using already existing hastag : #{}", tag,
							dbProductType.getName());
					productTypes.add(dbProductType);
				}
			}

			product.setOwner(currentUser);
			product.setProductTypes(productTypes);

			product.setPhotos(photos);
			productService.save(product);
			redirectAttributes.addFlashAttribute("AddedSuccessfully", "You Have Successfully added your product!");
			return "redirect:/member/add-product-to-marketplace";
		}

	}
}
