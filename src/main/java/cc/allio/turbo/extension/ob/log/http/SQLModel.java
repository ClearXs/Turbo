package cc.allio.turbo.extension.ob.log.http;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * the sql mode.
 * <p>
 * context / full, default is context mode, you cann't use limit group by in query.sql, and will use the SQL result as a context of aggregations.
 * aggregation will get result from context. You you set it to full, in full mode, you can write a full SQL in query.sql,it supports limit group by and keywords, but it doesn't support aggregation.
 * </p>
 *
 * @author j.x
 * @date 2024/4/5 20:02
 * @since 0.1.1
 */
@Getter
@AllArgsConstructor
public enum SQLModel {
    CONTEXT("context"),
    FULL("full");

    @JsonValue
    private final String value;
}
