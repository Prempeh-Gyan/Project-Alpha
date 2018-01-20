package org.isag_ghana.alpha.serviceImpl;

import java.util.Arrays;
import java.util.Calendar;

import org.isag_ghana.alpha.model.PasswordResetToken;
import org.isag_ghana.alpha.model.User;
import org.isag_ghana.alpha.repository.PasswordResetTokenRepository;
import org.isag_ghana.alpha.security.ISecurityUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ISecurityUserServiceImpl implements ISecurityUserService {

	@Autowired
	private PasswordResetTokenRepository passwordTokenRepository;

	// API

	@Override
	public String validatePasswordResetToken(long id, String token) {
		final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
		if ((passToken == null) || (passToken.getUser().getId() != id)) {
			if (passToken != null) {
				passwordTokenRepository.delete(passToken);
			}
			return "invalidToken";
		}

		final Calendar cal = Calendar.getInstance();
		if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			passwordTokenRepository.delete(passToken.getId());
			return "expired";
		}

		final User user = passToken.getUser();
		final Authentication auth = new UsernamePasswordAuthenticationToken(user, null,
				Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
		SecurityContextHolder.getContext().setAuthentication(auth);
		passwordTokenRepository.delete(passToken);
		return null;
	}

}