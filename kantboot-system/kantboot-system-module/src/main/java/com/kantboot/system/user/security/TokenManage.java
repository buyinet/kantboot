package com.kantboot.system.user.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kantboot.project.util.common.util.RedisUtil;
import com.kantboot.util.core.exception.NotLoginException;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * token的管理类
 */
@Slf4j
@Component
@Data
public class TokenManage {
    @Resource
    private HttpServletRequest request;

    @Resource
    private RedisUtil redisUtil;


    // token 有效时长
    private int tokenExpiration = 24*60*60*1000;

    @SneakyThrows
    public String createToken(String username){

        // 生成秘钥
        String secret = TokenManage.createSecret();

        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");
        String scene = request.getHeader("scene");
        if(scene!=null){
            scene=scene.toUpperCase();
        }

        // 指定token过期时间为20秒
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, tokenExpiration);

        String token = JWT.create()
                .withHeader(new HashMap<>())  // Header
                .withClaim("username", username)
                .withClaim("ip", ip) // 登录时ip
                .withClaim("ua", ua) // 登录时ua
                .withClaim("scene",scene) //登录时场景
                .withExpiresAt(calendar.getTime())  // 过期时间
                .sign(Algorithm.HMAC256(secret));  // 签名用的secret

        // 在进行一次加密
        token = TripleDES.desEncript(token, secret);

        //将token为key，秘钥为值，传入redis
        this.saveSecret(token,secret);

        return token;
    }

    /**
     * 保存token的秘钥到redis中
     * 格式 TOKEN:{{token}}:secret
     * @param token
     * @param secret 秘钥
     */
    private void saveSecret(String token,String secret){
        redisUtil.setEx("token:"+token+":secret",secret,7l, TimeUnit.DAYS);
    }

    /**
     * 通过token得到秘钥
     * @param token
     */
    private String findSecretByToken(String token){
        return redisUtil.get("token:" + token + ":secret");
    }


    /**
     * 通过token得到用户名
     * @return 用户名
     */
    public String getUserName(){
        String token = request.getHeader("token");

        //从redis中得到秘钥，对token进行解密
        String secret = findSecretByToken(token);

        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");

        JWTVerifier jwtVerifier = null;
        try{
            jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            token = TripleDES.desDecript(token,secret);
            DecodedJWT verify = jwtVerifier.verify(token);

            String claimIp = verify.getClaim("ip").asString();
            String claimUa = verify.getClaim("ua").asString();
            String claimUsername = verify.getClaim("username").asString();

            if(!claimIp.equals(ip)){
                // 如果ip不对应，直接返回为空
                log.info("["+claimUsername+"]用户更换了IP:"+claimIp+" -> " + ip);
            }

            if(!claimUa.equals(ua)){
                // 如果ua不对应，返回"不在登录状态"异常
                log.info("["+claimUsername+"]用户ua不对应:"+claimUa+" -> " + ua);
                throw new NotLoginException();
            }

            return claimUsername;
        }catch (Exception e){
            throw new NotLoginException();
        }

    }

    /**
     * 得到登录场景
     * @return
     */
    public String getScene(){
        String token = request.getHeader("token");

        //从redis中得到秘钥，对token进行解密
        String secret = findSecretByToken(token);

        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");
        String scene = request.getHeader("scene");

        JWTVerifier jwtVerifier = null;
        try{
            jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            token = TripleDES.desDecript(token,secret);
            DecodedJWT verify = jwtVerifier.verify(token);

            String claimIp = verify.getClaim("ip").asString();
            String claimUa = verify.getClaim("ua").asString();
            String claimUsername = verify.getClaim("username").asString();

            if(!claimIp.equals(ip)){
                // 如果ip不对应，直接返回为空
                log.info("["+claimUsername+"]用户更换了IP:"+claimIp+" -> " + ip);
            }

            if(!claimUa.equals(ua)){
                // 如果ua不对应，返回"不在登录状态"异常
                log.info("["+claimUsername+"]用户ua不对应:"+claimUa+" -> " + ua);
                throw new NotLoginException();
            }

            return scene;
        }catch (Exception e){
            throw new NotLoginException();
        }

    }

    /**
     * 删除token
     * 主要逻辑：删除用来解析对应秘钥的键值对
     */
    public void removeToken(){
        String token = request.getHeader("token");
        redisUtil.delete("token:" + token + ":secret");
    }

    //生成秘钥
    public static String createSecret(){
        String secret = UUID.randomUUID().toString();
        return secret.replace("-","").substring(0,24);
    }

}
