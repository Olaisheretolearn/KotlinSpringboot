package com.summerlockin.GenNoteApp.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*


@Service
class JwtService(
    @Value("JWT_SECRET_BASE64") private val jwtsecret :String
) {
    private val secretkey  = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtsecret))
    private val accessTokenValidityMS = 15L * 60L * 100L
    val refreshTokenValidityMs = 30L * 24 * 60 * 60 * 100L

    private fun  generateToken(
        userId: String,
        type:String,
        expiry : Long
    ) :String {
        val now = Date()
        val expiryDate = Date(now.time + expiry)
        return Jwts.builder()
            .subject(userId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretkey, Jwts.SIG.HS256)
            .compact()
    }


    fun generateAccessToken(userId:String):String{
        return generateToken(userId, type = "access", accessTokenValidityMS);
    }

    fun generateRefreshToken(userId: String) :String{
        return generateToken(userId, type="refresh", refreshTokenValidityMs);
    }

    fun validateAccessToken (token:String ) : Boolean {
        val claims = parseAllClaims(token) ?: return false;
        val tokenType = claims["type"] as? String?: false;
        return tokenType == "access"
    }

    fun validateRefreshToken (token:String ) : Boolean {
        val claims = parseAllClaims(token) ?: return false;
        val tokenType = claims["type"] as? String?: false;
        return tokenType == "refresh"
    }

    //Authorization : Bearer <token>
    fun getUserIdFromToken(token : String) :String {
        val rawToken = if(token.startsWith("Bearer ")){
            token.removePrefix("Bearer ")
        } else token
        val claims = parseAllClaims(rawToken) ?: throw IllegalArgumentException("invalid token")
        return claims.subject
    }

    //all data that is saved in the token is passed by this method here
    private fun parseAllClaims(token :String) : Claims? {
        return try {
            Jwts.parser()
                .verifyWith(secretkey)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch(e :Exception){
            null
        }
    }


}