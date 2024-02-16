package com.example.demo.OAuth2config;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {
	private OAuth2User oauthuser;
	
	
	public CustomOAuth2User(OAuth2User oauthuser) {
		super();
		this.oauthuser = oauthuser;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oauthuser.getAttributes(); 
	
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oauthuser.getAuthorities();
	}

	@Override
	public String getName() {
		oauthuser.getAttribute("name");
		return null;
	}

}
