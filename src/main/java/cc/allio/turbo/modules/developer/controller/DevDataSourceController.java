package cc.allio.turbo.modules.developer.controller;

import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.ServiceTurboCrudController;
import cc.allio.turbo.modules.developer.domain.TableColumns;
import cc.allio.turbo.modules.developer.entity.DevDataSource;
import cc.allio.turbo.modules.developer.service.IDevDataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dev/datasource")
@AllArgsConstructor
@Tag(name = "数据源管理")
public class DevDataSourceController extends ServiceTurboCrudController<DevDataSource, IDevDataSourceService> {

    @PostMapping("/test-connection")
    @Operation(summary = "测试连接")
    public R<Boolean> testConnection(@Validated @RequestBody DevDataSource dataSource) {
        boolean connected = getService().testConnection(dataSource);
        return R.ok(connected);
    }

    @GetMapping("/show-tables/{dataSourceId}")
    @Operation(summary = "显示所有表信息")
    public R<List<TableColumns>> showTables(@PathVariable("dataSourceId") Long dataSourceId) {
        List<TableColumns> tables = getService().showTables(dataSourceId, null);
        return R.ok(tables);
    }
}
