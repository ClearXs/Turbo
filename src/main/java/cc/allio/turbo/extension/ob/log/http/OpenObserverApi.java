package cc.allio.turbo.extension.ob.log.http;

import cc.allio.turbo.extension.ob.log.OpenObserverProperties;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.http.metadata.HttpSwapper;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * open observer search
 *
 * @author j.x
 * @date 2024/4/5 22:25
 * @since 0.1.1
 */
public class OpenObserverApi {

    private final OpenObserverProperties openObserverProperties;
    public static final String LOG_PATH = "/api/#{organization}/_search?types=logs";
    public static final String METRIC_PATH = "/api/#{organization}/_search?types=logs";
    public static final String TRACE_PATH = "/api/#{organization}/_search?types=traces";

    public OpenObserverApi(OpenObserverProperties openObserverProperties) {
        this.openObserverProperties = openObserverProperties;
    }

    /**
     * search log
     *
     * @see #searchLog(Search)
     */
    public LogResponse searchLog(Function<SearchBuilder, Search> searchFunc) {
        Search search = searchFunc.apply(SearchBuilder.builder(openObserverProperties));
        return searchLog(search);
    }

    /**
     * search open observer log datas
     *
     * @param search the search
     * @return a {@link LogResponse} instance
     */
    public LogResponse searchLog(Search search) {
        String endpoint = openObserverProperties.getEndpoint();
        String url = endpoint + ExpressionTemplate.parse(LOG_PATH, "organization", openObserverProperties.getOrganization());
        HttpSwapper swapper = HttpSwapper.build(url, HttpMethod.POST);
        swapper.addBody(search);
        Map<String, String> headers = openObserverProperties.getHeaders();
        for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
            swapper.addHeader(headerEntry.getKey(), headerEntry.getValue());
        }
        AtomicReference<LogResponse> ref = new AtomicReference<>();
        swapper.swap()
                .flatMap(response -> response.toExpect(LogResponse.class))
                .subscribe(ref::set);
        return ref.get();
    }
}
