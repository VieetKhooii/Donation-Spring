package org.example.userroleservice.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

    public CustomAuthorizationRequestResolver(DefaultOAuth2AuthorizationRequestResolver defaultResolver) {
        this.defaultResolver = defaultResolver;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest requestFromResolver = defaultResolver.resolve(request);
        return customizeAuthorizationRequest(requestFromResolver);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest requestFromResolver = defaultResolver.resolve(request, clientRegistrationId);
        return customizeAuthorizationRequest(requestFromResolver);
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest) {
        if (authorizationRequest == null) {
            return null;
        }

        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(params -> params.put("prompt", "select_account"))
                .build();
    }
}
