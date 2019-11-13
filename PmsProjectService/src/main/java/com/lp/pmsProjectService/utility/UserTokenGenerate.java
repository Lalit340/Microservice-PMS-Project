package com.lp.pmsProjectService.utility;

import java.io.UnsupportedEncodingException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.lp.pmsProjectService.exception.InternalException;

@Component
public class UserTokenGenerate {
	
	private static String tokenKey;
	
	public  String generateToken(long id) {
		tokenKey = "Lalit";
		Algorithm algorithm = null;
		String token = null;
		
		try {
			algorithm = algorithm.HMAC256(tokenKey);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		token =JWT.create().withClaim("ID", id).sign(algorithm);
	
		return token;
	}
	
	public  long varifyToken(String token) {
		tokenKey = "Lalit";
		long userid;
		//here verify the given token's algorithm
		Verification verification = null;
		try {
		verification = JWT.require(Algorithm.HMAC256(UserTokenGenerate.tokenKey));
		
		JWTVerifier jwtverifier=verification.build();
		DecodedJWT decodedjwt=jwtverifier.verify(token);
		Claim claim=decodedjwt.getClaim("ID");
		userid=claim.asLong();	
		System.out.println(userid);
		return userid;
		}catch (Exception e) {
			throw new InternalException("Token is not valid ", HttpStatus.UNAUTHORIZED.value());
		}
		
	}

}
