package com.summerlockin.GenNoteApp.security

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
}