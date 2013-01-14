/**
 * 
 */
package com.chinarewards.metro.core.security;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

@SuppressWarnings("unchecked")
public class AccessManager extends AbstractAccessDecisionManager {

	@SuppressWarnings("rawtypes")
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> list) throws AccessDeniedException, InsufficientAuthenticationException {
		int deny = 0;
        for ( AccessDecisionVoter voter : getDecisionVoters()) {
			int result = voter.vote(authentication, object, list);
            if (logger.isDebugEnabled()) {
                logger.debug("Voter: " + voter + ", returned: " + result);
            }
            switch (result) {
            case AccessDecisionVoter.ACCESS_GRANTED:
                return;
            case AccessDecisionVoter.ACCESS_DENIED:
                deny++;
                break;
            default:
                break;
            }
        }
        if (deny > 0) {
            throw new AccessDeniedException(messages.getMessage("AbstractAccessDecisionManager.accessDenied","Access is denied"));
        }
        
		Iterator<ConfigAttribute> iterator = list.iterator();  
        while(iterator.hasNext()) {  
            ConfigAttribute configAttribute = iterator.next();
            String result = configAttribute.getAttribute();
            if(!AuthenticatedVoter.IS_AUTHENTICATED_FULLY.equals(result)){
            	throw new AccessDeniedException("no permission to access " + ((FilterInvocation) object).getRequestUrl());
            }
        }
        
	}
	
}