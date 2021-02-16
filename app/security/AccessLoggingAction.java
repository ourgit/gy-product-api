package security;

import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * access logging action
 */
public class AccessLoggingAction extends Action.Simple {
    private Logger.ALogger accessLogger = Logger.of("access");

    public CompletionStage<Result> call(Http.Context ctx) {
        final Http.Request request = ctx.request();
        accessLogger.info("method={} uri={} remote-address={}", request.method(), request.uri(), request.remoteAddress());
        return delegate.call(ctx);
    }
}
