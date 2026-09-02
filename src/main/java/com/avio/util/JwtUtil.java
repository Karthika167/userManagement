package com.avio.util;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String secretKeyBase64;
	@Value("${jwt.expiration.ms}")
	private long expirationMs;

	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(UUID userId, String email, UUID orgId, List<String> roles) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + expirationMs);
		return Jwts.builder().subject(userId.toString()).claim("email", email)
				.claim("orgId", orgId != null ? orgId.toString() : null).claim("roles", roles).issuedAt(now)
				.expiration(expiry).signWith(getSigningKey()).compact();
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
	}

	public UUID extractUserId(String token) {
		return UUID.fromString(extractAllClaims(token).getSubject());
	}

	public String extractEmail(String token) {
		return extractAllClaims(token).get("email", String.class);
	}

	public UUID extractOrgId(String token) {
		String orgId = extractAllClaims(token).get("orgId", String.class);
		return orgId != null ? UUID.fromString(orgId) : null;
	}

	@SuppressWarnings("unchecked")
	public List<String> extractRoles(String token) {
		return extractAllClaims(token).get("roles", List.class);
	}

	public boolean isTokenValid(String token) {
		try {
			Claims claims = extractAllClaims(token);
			return claims.getExpiration().after(new Date());
		} catch (Exception e) {
			return false;
		}
	}
}