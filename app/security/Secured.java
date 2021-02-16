package security;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.user.Member;
import play.Logger;
import play.cache.AsyncCacheApi;
import play.cache.SyncCacheApi;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import utils.*;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Secured extends Security.Authenticator {

    Logger.ALogger logger = Logger.of(Secured.class);
    @Inject
    BizUtils businessUtils;
    @Inject
    AsyncCacheApi cache;
    @Inject
    EncodeUtils encodeUtils;

    /**
     * 认证接口，目前以token作为凭据，如果有在缓存中说明是合法用户，如果不在缓存中说明是非法用户
     *
     * @return
     */
    @Override
    public Optional<String> getUsername(Http.Request request) {
        String uidToken = businessUtils.getUIDFromRequest(request);
        if (ValidationUtil.isEmpty(uidToken)) return Optional.empty();
        try {
            String authToken = "";
            Optional<Object> tokenOptional = cache.getOptional(uidToken).toCompletableFuture().get(10, TimeUnit.SECONDS);
            if (!tokenOptional.isPresent()) return Optional.empty();
            authToken = (String) tokenOptional.get();//uid token对应的是用户uid
            if (ValidationUtil.isEmpty(authToken)) return Optional.empty();
            String timeStamp = "";
            String md5 = "";
            String nonce = "";
            Optional<String> tOptional = request.getHeaders().get("t");
            if (tOptional.isPresent()) {
                timeStamp = tOptional.get();
            }
            Optional<String> sOptional = request.getHeaders().get("s");
            if (sOptional.isPresent()) {
                md5 = sOptional.get();
            }
            Optional<String> nonceOptional = request.getHeaders().get("nonce");
            if (nonceOptional.isPresent()) {
                nonce = nonceOptional.get();
            }
            if (ValidationUtil.isEmpty(nonce) || ValidationUtil.isEmpty(timeStamp) || ValidationUtil.isEmpty(md5))
                return Optional.empty();
            long timeStampLong = Long.parseLong(timeStamp);
            long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime > timeStampLong + 30) return Optional.empty();//请求时间超出8秒置错
            String salt = authToken + EncodeUtils.API_SALT + timeStamp + nonce;
            String md5FirstTime = encodeUtils.getMd5(salt);

            String md5SecondTime = encodeUtils.getMd5(authToken + md5FirstTime);
            if (!md5.equals(md5SecondTime)) return Optional.empty();

            Optional<Object> platformKeyOptional = cache.getOptional(authToken).toCompletableFuture().get(10, TimeUnit.SECONDS);
            if (!platformKeyOptional.isPresent()) return Optional.empty();
            String platformKey = (String) platformKeyOptional.get();
            if (ValidationUtil.isEmpty(platformKey)) return Optional.empty();
            Optional<Object> optional = cache.getOptional(platformKey).toCompletableFuture().get(10, TimeUnit.SECONDS);
            if (!optional.isPresent()) return Optional.empty();
            Member member = (Member) optional.get();
            if (null == member) {
                return Optional.empty();
            }
            return Optional.of(member.id + "");
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Result onUnauthorized(Http.Request req) {
        ObjectNode node = Json.newObject();
        node.put("code", 403);
        node.put("reason", "亲，请先登录...");
        return ok(node);
    }

}
