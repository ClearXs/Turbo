package cc.allio.turbo.extension.ob.log.http;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * open observe search entity
 *
 * @author j.x
 * @date 2024/4/5 20:03
 * @see <a href="https://openobserve.ai/docs/api/search/search/">openobserve search docs</a>
 * @since 0.1.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Search {

    /**
     * use SQL query data, and filter data by start_time and end_time, and default order by _timestamp,
     * you can use order by override order, and fetch offset limit by form and size
     */
    private String sql;

    /**
     * unit: microseconds, filter data by time range, without it, will scan data from first record to end_time
     */
    private Date startTime;

    /**
     * unit: microseconds, filter data by time range, without it, will scan data from start_time to NOW
     */
    private Date EndTime;

    /**
     * offset in SQL
     */
    private Long from;

    /**
     * limit in SQL
     */
    private Long size;

    /**
     * response real total of the query SQL, you can set it to true for response total. when you have aggs, this value will auto set to true.
     */
    private Boolean trackTotalHits;

    /**
     * @see SQLModel
     */
    private SQLModel sqlModel;

    /**
     * aggregation params, you can ignore it if you have no aggregations.
     */
    private List<String> aggs;
}
