package com.example.algamoney.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//Essa classe tem a função de interceptar a geração de token e armazenar o refresh token em um cookie seguro
//Essa classe deverá ser executada quando ser chamado o endpoint de gerar token 
@ControllerAdvice
public class RefreshTokenPostProcessorIntercept implements ResponseBodyAdvice<OAuth2AccessToken> {
	/*
	 * Completando o comentario acima. A classe so vai executar caso o body
	 * retornado for do tipo >>>>OAuth2AccessToken<<<<
	 */

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		System.out.println(returnType.getMethod().getName());
		return returnType.getMethod().getName().equals("postAccessToken");
		// Caso o returnType acima for true, será executado o metodo abaixo
	}

	// Esse metodo é chamado apos o retorno do corpo da autenticação (O objeto do
	// token) e somente se o retorno do metodo acima for true
	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {

		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse res = ((ServletServerHttpResponse) response).getServletResponse();

		String refreshToken = body.getRefreshToken().getValue();
			
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
		
		AdicionarTokenECriarCookie(refreshToken, req, res);
		removerRefreshTonkeBody(token);
		
		return body;
	}

	private void removerRefreshTonkeBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null);
	}

	private void AdicionarTokenECriarCookie(String refreshToken, HttpServletRequest req, HttpServletResponse res) {
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		res.reset();

		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		cookie.setPath(req.getContextPath() + "/oauth/token");
		cookie.setMaxAge(2592000);
		res.addCookie(cookie);
	}

}
