package cc.allio.turbo.modules.developer.constant;

import cc.allio.uno.data.orm.dsl.type.DBType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 存储类型
 *
 * @author jiangwei
 * @date 2024/1/25 00:21
 * @since 0.1.0
 */
@Getter
@AllArgsConstructor
public enum StorageType {

    MYSQL(DBType.MYSQL.getName(), DBType.MYSQL.getName(), DBType.MYSQL.getCategory()),
    POSTGRESQL(DBType.POSTGRESQL.getName(), DBType.POSTGRESQL.getName(), DBType.POSTGRESQL.getCategory()),
    SQLSERVER(DBType.SQLSERVER.getName(), DBType.SQLSERVER.getName(), DBType.SQLSERVER.getCategory()),
    ORACLE(DBType.ORACLE.getName(), DBType.ORACLE.getName(), DBType.ORACLE.getCategory()),
    SQLITE(DBType.SQLITE.getName(), DBType.SQLITE.getName(), DBType.SQLITE.getCategory()),
    H2(DBType.H2.getName(), DBType.H2.getName(), DBType.H2.getCategory()),
    OPEN_GAUSS(DBType.OPEN_GAUSS.getName(), DBType.OPEN_GAUSS.getName(), DBType.OPEN_GAUSS.getCategory()),
    DB2(DBType.DB2.getName(), DBType.DB2.getName(), DBType.DB2.getCategory()),
    MARIADB(DBType.MARIADB.getName(), DBType.MARIADB.getName(), DBType.MARIADB.getCategory()),
    MONGODB(DBType.MONGODB.getName(), DBType.MONGODB.getName(), DBType.MONGODB.getCategory()),
    REDIS(DBType.REDIS.getName(), DBType.REDIS.getName(), DBType.REDIS.getCategory()),
    INFLUXDB(DBType.INFLUXDB.getName(), DBType.INFLUXDB.getName(), DBType.INFLUXDB.getCategory()),
    NEO4J(DBType.NEO4J.getName(), DBType.NEO4J.getName(), DBType.NEO4J.getCategory()),
    ELASTIC_SEARCH(DBType.ELASTIC_SEARCH.getName(), DBType.ELASTIC_SEARCH.getName(), DBType.ELASTIC_SEARCH.getCategory()),
    TD_ENGINE(DBType.TD_ENGINE.getName(), DBType.TD_ENGINE.getName(), DBType.TD_ENGINE.getCategory());

    @EnumValue
    @JsonValue
    private final String value;
    private final String label;

    private final DBType.DBCategory dbCategory;

    public DBType toDbType() {
        return DBType.getDbType(value);
    }

    public static StorageType getByDbType(DBType dbType) {
        for (StorageType value : values()) {
            if (value.getValue().equals(dbType.getName())) {
                return value;
            }
        }
        return null;
    }
}
