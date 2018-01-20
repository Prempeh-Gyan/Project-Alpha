package org.isag_ghana.alpha.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(long id, String token);

}
