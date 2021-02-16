package security;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.cache.AsyncCacheApi;
import play.cache.SyncCacheApi;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import utils.BizUtils;
import utils.EncodeUtils;
import utils.ValidationUtil;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static constants.BusinessConstant.KEY_AUTH_TOKEN_UID;

public class ApiSecured extends Security.Authenticator {

    @Inject
    EncodeUtils encodeUtils;
    @Inject
    BizUtils businessUtils;
    @Inject
    AsyncCacheApi cache;
    Logger.ALogger logger = Logger.of(ApiSecured.class);
    public static final String API_NONE_KEY = "API_NONE_KEY:";

    /**
     * api认证接口
     *
     * @return
     */
    @Override
    public Optional<String> getUsername(Http.Request request) {
        String uri = request.uri();
        String authToken = "";
        String uidToken = businessUtils.getUIDFromRequest(request);
        if (!ValidationUtil.isEmpty(uidToken)) {
            try {
                Optional<Object> tokenOptional = cache.getOptional(uidToken).toCompletableFuture().get(10, TimeUnit.SECONDS);
                if (tokenOptional.isPresent()) {
                    authToken = (String) tokenOptional.get();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (uri.contains("/v1/")) {
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

            if (ValidationUtil.isEmpty(nonce) || ValidationUtil.isEmpty(md5) || ValidationUtil.isEmpty(timeStamp))
                return Optional.empty();
            long timeStampLong = Long.parseLong(timeStamp);
            long currentTime = System.currentTimeMillis() / 1000;
            if (currentTime > timeStampLong + 30) return Optional.empty();
//            if (nonce.length() != 18) return Optional.empty();
            String key = API_NONE_KEY + nonce;
            try {
                Optional<Object> nonceKeyOptional = cache.getOptional(key).toCompletableFuture().get(10, TimeUnit.SECONDS);
                if (nonceKeyOptional.isPresent()) return Optional.empty();
                String salt = authToken + EncodeUtils.API_SALT + timeStamp + nonce;
                String md5FirstTime = encodeUtils.getMd5(salt);
                String md5SecondTime = encodeUtils.getMd5(authToken + md5FirstTime);
                if (!md5.equals(md5SecondTime)) return Optional.empty();
                cache.set(key, key, 24 * 3600);
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }
        return Optional.of("YES");
    }


    @Override
    public Result onUnauthorized(Http.Request req) {
        ObjectNode node = Json.newObject();
        node.put("code", -1);
        node.put("reason", "服务器暂时开小差了,请稍后再试");
        return ok(node);
    }

}
