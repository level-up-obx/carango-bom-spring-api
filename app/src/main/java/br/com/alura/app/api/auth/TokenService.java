package br.com.alura.app.api.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.app.api.usuario.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${app.jwt.expiration}")
	private String expiration;
	
	@Value("${app.jwt.secret}")
	private String secret;

	public String gerarToken(Authentication authentication) {
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date expiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		return Jwts.builder()
				.setIssuer("API <nome>")
				.setSubject(usuarioLogado.getId().toString())
				.setIssuedAt(new Date())
				.setExpiration(expiracao)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}
	
	public Boolean isTokenValido(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}	
	}

	public Long getIdUsuario(String token) {
		Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		return Long.parseLong(body.getSubject());
	}

}
