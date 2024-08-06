package cc.allio.turbo.modules.office.documentserver.command;

import cc.allio.uno.core.util.CollectionUtils;
import cc.allio.uno.core.util.JsonUtils;
import cc.allio.uno.core.util.StringUtils;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

/**
 * the request command interface
 *
 * @author j.x
 * @date 2024/5/13 22:51
 * @since 0.0.1
 * @param <P> the {@link P} params
 */
public abstract class Command<P> {

    protected final InternalCommandExecutor commandExecutor;

    protected Command(InternalCommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    /**
     * exec this {@link Command}
     *
     * @param docKey the doc key
     * @param params the params
     * @return the {@link Result} instance
     */
    public abstract Result execute(String docKey, P params);

    /**
     * get command method
     *
     * @return the {@link Method} instance
     */
    public abstract Method getMethod();


    /**
     * format args to {@link Map}. if {@code args} is empty, then return empty
     *
     * @param args the bean args
     * @return the args of {@link Map}
     */
    public Map<String, Object> formatParams(Object args) {
        if (args == null) {
            return Collections.emptyMap();
        }
        return JsonUtils.toMap(JsonUtils.toJson(args));
    }

    /**
     * the command service request info
     *
     * @author j.x
     * @date 2024/5/13 22:57
     * @since 0.0.1
     */
    @Data
    public static class Request {

        /**
         * the {@link Method}
         */
        private String c;

        /**
         * the doc key
         */
        private String key;

        /**
         * the token
         */
        private String token;

        private Map<String, Object> params;


        /**
         * return the map
         *
         * @return
         */
        public Map<String, Object> returnMap() {
            Map<String, Object> payload = Maps.newHashMap();
            if (StringUtils.isNotEmpty(c)) {
                payload.put("c", c);
            }
            if (StringUtils.isNotEmpty(key)) {
                payload.put("key", key);
            }
            if (StringUtils.isNotEmpty(token)) {
                payload.put("token", token);
            }
            if (CollectionUtils.isNotEmpty(params)) {
                payload.putAll(params);
            }
            return payload;
        }

        /**
         * return the json string
         *
         * @return
         */
        public String returnJson() {
            return JsonUtils.toJson(returnMap());
        }
    }
}