package org.isag_ghana.alpha.controller.member;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.isag_ghana.alpha.dto.QuestionsFormDTO;
import org.isag_ghana.alpha.model.Question;
import org.isag_ghana.alpha.model.QuestionType;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.service.BusinessAndPhotosService;
import org.isag_ghana.alpha.service.BusinessAndVideosService;
import org.isag_ghana.alpha.service.BusinessService;
import org.isag_ghana.alpha.service.CommitteeAndCommitteeMembersService;
import org.isag_ghana.alpha.service.MessageRecipientService;
import org.isag_ghana.alpha.service.QuestionService;
import org.isag_ghana.alpha.service.QuestionTypeService;
import org.isag_ghana.alpha.service.UserService;
import org.isag_ghana.alpha.util.CurrentUser;
import org.isag_ghana.alpha.util.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
public class FAQController {

	private final @NotNull UserService userService;
	private final @NotNull QuestionService questionService;
	private final @NotNull QuestionTypeService questionTypeService;
	private final @NotNull BusinessService businessService;
	private final @NotNull BusinessAndPhotosService businessAndPhotosService;
	private final @NotNull BusinessAndVideosService businessAndVideosService;
	private final @NotNull MessageRecipientService messageRecipientService;
	private final @NotNull CommitteeAndCommitteeMembersService committeeAndCommitteeMembersService;

	@RequestMapping(value = { "/member/frequently-asked-questions" }, method = RequestMethod.GET)
	public String getFAQs(@CurrentUser User currentUser, Model model) {

		log.info("Contact-Us page fired...");
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		model.addAttribute("questionsFormDTO", new QuestionsFormDTO());
		return "frequently-asked-questions";
	}

	@RequestMapping(value = "/member/frequently-asked-questions", method = RequestMethod.POST)
	public String postQuestion(@Valid @ModelAttribute("questionsFormDTO") QuestionsFormDTO questionsFormDTO,
			BindingResult bindingResult, Model model, @CurrentUser User currentUser,
			@RequestParam(name = "tags") String tags, RedirectAttributes redirectAttributes) {

		log.info("Join us post fired...");

		if (bindingResult.hasErrors()) {
			log.info("Errors in signup forms...");
			model.addAttribute("questionsFormDTO", questionsFormDTO);
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			return "frequently-asked-questions";
		}

		Question question = questionsFormDTO.creatQuestion();
		Set<String> questionTypeStrings = new HashSet<>();
		Set<QuestionType> questionTypes = new HashSet<>();
		QuestionType questionType;

		if (tags != null && tags != "" && tags.contains("#")) {

			tags = tags.replaceAll("\\s+", "");
			StringTokenizer stringTokenizer = new StringTokenizer(tags, "#");

			while (stringTokenizer.hasMoreTokens()) {
				String currentToken = stringTokenizer.nextToken().toLowerCase();
				questionTypeStrings.add(currentToken);
			}

		} else {
			log.info("Errors in signup forms...");
			model.addAttribute("questionsFormDTO", questionsFormDTO);
			model.addAttribute("error", "Error! There are issues with your hashtags, please review them and try again");
			Notification.getNotifications(model, currentUser, userService, messageRecipientService,
					committeeAndCommitteeMembersService);
			return "frequently-asked-questions";
		}

		for (String tag : questionTypeStrings) {
			log.info("HashTag:  #{}", tag);
			QuestionType dbQuestionType = questionTypeService.findOne(tag);
			if (dbQuestionType == null) {
				log.info("#{} does not exist in the database, creating new hashtag", tag);
				questionType = new QuestionType();
				questionType.setName(tag);
				questionTypeService.save(questionType);
				questionTypes.add(questionType);
			} else {
				log.info("#{} already exist in the database, using already existing hastag : #{}", tag,
						dbQuestionType.getName());
				questionTypes.add(dbQuestionType);
			}
		}

		question.setOwner(currentUser);
		question.setQuestionTypes(questionTypes);
		questionService.save(question);

		redirectAttributes.addFlashAttribute("success",
				"Success! Your Question has been posted, wait patiently as you get responses from members");
		return "redirect:/member/frequently-asked-questions";
	}

	@RequestMapping(value = { "/member/questions" }, method = RequestMethod.GET)
	public String getQuestions(@CurrentUser User currentUser, Model model,
			@SortDefault(sort = "date", direction = Direction.DESC) Pageable pageable) {

		log.info("Contact-Us page fired...");
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		model.addAttribute("questionsFormDTO", new QuestionsFormDTO());
		model.addAttribute("page", questionService.findAll(pageable));
		return "questions";
	}

	@RequestMapping(value = { "/member/question/{questionId}" }, method = RequestMethod.GET)
	public String getQuestionAndAnswers(@CurrentUser User currentUser, Model model,
			@PathVariable("questionId") Long questionId) {
		log.info("questionId is : {}", questionId);
		Notification.getNotifications(model, currentUser, userService, messageRecipientService,
				committeeAndCommitteeMembersService);
		model.addAttribute("question", questionService.findOne(questionId));
		return "question-and-answers"; 
	}
}
