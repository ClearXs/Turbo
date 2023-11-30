package cc.allio.uno.turbo.common.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.Date;

/**
 * 时间转换，未转换前时间格式为2023-11-22T09:26:08.702+00:00
 *
 * @author j.x
 * @date 2023/11/29 18:27
 * @since 1.0.0
 */
public class DateModule extends SimpleModule {

    public DateModule() {
        addSerializer(Date.class, new DateSerializer());
        addDeserializer(Date.class, new DateDeserializer());
    }

}
