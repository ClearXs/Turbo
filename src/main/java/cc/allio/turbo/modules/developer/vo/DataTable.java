package cc.allio.turbo.modules.developer.vo;

import lombok.Data;

/**
 * the DataTable contains datasource
 *
 * @author j.x
 * @date 2024/6/20 22:27
 * @since 0.1.1
 */
@Data
public class DataTable {

    /**
     * the datasource id
     */
    private Long dataSource;

    /**
     * select table name
     */
    private String table;
}
