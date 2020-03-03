package com.gcb.learning.mybatisplus.security.utils;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtTokenUtil {

    /**
     * 这些都是负载内部定义好的一些key
     * iss (issuer)签发者, exp (expiration time)过期时间, sub (subject)用户名, aud (audience)接受者
     * 还可以自定义key
     */
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    //@Value("${jwt.secret}")
    //生成签名的时候使用的秘钥secret，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。
    //一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
    private String secret = "mySecret";

    //@Value("${jwt.expiration}")
    private Long expiration = 604800L;

   // @Value("${jwt.tokenHead}")
    private String tokenHead = "Bearer";


    /**
     * 从token中获取claims，即负载
     * @param token
     * @return
     */
    public Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.info("JWT格式验证失败:{}",token);
        }
        return claims;
    }

    /**
     * 从token中获取用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token){
        String username;
        Claims claims = getClaimsFromToken(token);
        try {
            username = claims.getSubject();
        }catch (Exception e){
            username = null;
        }
        return username;
    }


    /**
     * 判断token是否时间过期
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        Date dateFromToken = getClaimsFromToken(token).getExpiration();
        return dateFromToken.before(new Date());
    }


    /**
     * 判断token是否有效
     * @param token
     * @param userDetails  从数据库中查询出来的用户信息
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails){
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 根据负载生成jwt的Token
     * @param claims claims中包含用户名和登录时间，即是token的载荷信息
     * @return
     */
    private String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                // 指定加密方式和签名
                .signWith(SignatureAlgorithm.HS512, secret)
                // 返回一个字符串
                .compact();
    }


    /**
     * 根据用户信息生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }



    /**
     * 判断token在指定时间内是否刚刚刷新过
     * @param token 原token
     * @param time 指定时间（秒）
     */
    private boolean tokenRefreshJustBefore(String token, int time){
        Claims claims = getClaimsFromToken(token);
        Date created = claims.get(CLAIM_KEY_CREATED, Date.class);
        Date refreshDate = new Date();
        if (refreshDate.after(created) && refreshDate.before(DateUtil.offsetSecond(created,time))){
            return true;
        }
        return false;
    }

    /**
     * 刷新token
     * @param oldToken
     * @return
     */
    public String refreshHeadToken(String oldToken){
        if (StrUtil.isEmpty(oldToken)){
            return null;
        }
        String token = oldToken.substring(tokenHead.length());
        if (StrUtil.isEmpty(token)){
            return null;
        }
        //token校验不通过
        Claims claims = getClaimsFromToken(token);
        if(claims==null){
            return null;
        }
        //如果token已经过期，不支持刷新
        if(isTokenExpired(token)){
            return null;
        }
        //如果token在30分钟之内刚刷新过，返回原token
        if(tokenRefreshJustBefore(token,30*60)){
            return token;
        }else{
            claims.put(CLAIM_KEY_CREATED, new Date());
            return generateToken(claims);
        }
    }

    /**
     * 从token中得到过期时间
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token){
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

}
