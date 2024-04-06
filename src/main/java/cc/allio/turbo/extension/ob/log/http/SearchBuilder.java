package cc.allio.turbo.extension.ob.log.http;

import cc.allio.turbo.extension.ob.log.OpenObserverProperties;
import cc.allio.uno.core.api.Self;
import cc.allio.uno.core.exception.Exceptions;
import cc.allio.uno.core.util.StringUtils;
import cc.allio.uno.data.orm.dsl.OperatorKey;
import cc.allio.uno.data.orm.dsl.SPIOperatorHelper;
import cc.allio.uno.data.orm.dsl.dml.QueryOperator;
import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * {@link Search} builder
 *
 * @author j.x
 * @date 2024/4/5 20:07
 * @since 0.1.1
 */
public class SearchBuilder implements Self<SearchBuilder> {

    OpenObserverProperties openObserverProperties;
    String sql;
    Long from;
    Long size;
    Date startTime;
    Date endTime;
    Boolean trackTotalHits;
    SQLModel sqlModel;

    List<String> aggs;

    public SearchBuilder sql(String sql) {
        this.sql = sql;
        return self();
    }

    /**
     * build the sql by {@link QueryOperator}.
     * <p>if {@link OpenObserverProperties} is not null, will be {@link QueryOperator#from(String)} from {@link OpenObserverProperties#getStream()} </p>
     *
     * @param func the unary operator
     * @return self
     */
    public SearchBuilder sql(UnaryOperator<QueryOperator> func) {
        QueryOperator queryOperator = func.apply(SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL));
        if (openObserverProperties != null) {
            queryOperator.from(openObserverProperties.getStream());
        }
        this.sql = queryOperator.getDSL();
        return self();
    }

    /**
     * search for page
     *
     * @param page     the page
     * @param pageSize the page size
     * @return self
     */
    public SearchBuilder page(Long page, Long pageSize) {
        this.from = (page - 1) * pageSize;
        this.size = pageSize;
        return self();
    }

    /**
     * search for between
     *
     * @param startTime the startTime
     * @param endTime   the  endTime
     * @return self
     */
    public SearchBuilder between(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        return self();
    }

    /**
     * the sql mode
     *
     * @param sqlModel the sql model
     * @return self
     */
    public SearchBuilder sqlModel(SQLModel sqlModel) {
        this.sqlModel = sqlModel;
        return self();
    }

    /**
     * the agg sql
     *
     * @param aggSql the aggSql
     * @return self
     */
    public SearchBuilder agg(String aggSql) {
        if (aggs == null) {
            aggs = Lists.newArrayList();
        }
        aggs.add(aggSql);
        return self();
    }

    /**
     * the agg sql by {@link QueryOperator}
     *
     * @param aggFunc the aggFunc
     * @return self
     */
    public SearchBuilder agg(UnaryOperator<QueryOperator> aggFunc) {
        QueryOperator queryOperator = aggFunc.apply(SPIOperatorHelper.lazyGet(QueryOperator.class, OperatorKey.SQL));
        String aggSql = queryOperator.getDSL();
        return agg(aggSql);
    }

    /**
     * the track to hits. default true
     *
     * @param trackTotalHits the trackTotalHits
     * @return self
     */
    public SearchBuilder trackTotalHits(boolean trackTotalHits) {
        this.trackTotalHits = trackTotalHits;
        return self();
    }

    /**
     * create {@link SearchBuilder} instance
     *
     * @param openObserverProperties the openObserverProperties
     * @return self
     */
    public static SearchBuilder builder(OpenObserverProperties openObserverProperties) {
        SearchBuilder searchBuilder = builder();
        searchBuilder.openObserverProperties = openObserverProperties;
        return searchBuilder;
    }

    /**
     * create {@link SearchBuilder} instance
     *
     * @return self
     */
    public static SearchBuilder builder() {
        return new SearchBuilder();
    }

    /**
     * build to {@link Search} instance
     *
     * @return a {@link Search} instance
     */
    public Search build() {
        if (StringUtils.isBlank(sql)) {
            throw Exceptions.unNull("sql is null");
        }
        Search search = new Search();
        search.setSql(sql);
        if (sqlModel == null) {
            sqlModel = SQLModel.CONTEXT;
        }
        search.setSqlModel(sqlModel);
        if (trackTotalHits == null) {
            trackTotalHits = true;
        }
        search.setTrackTotalHits(trackTotalHits);
        search.setFrom(from);
        search.setSize(size);
        search.setStartTime(startTime);
        search.setEndTime(endTime);
        search.setAggs(aggs);
        return search;
    }
}
