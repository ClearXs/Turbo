package cc.allio.turbo.modules.developer.controller;

import cc.allio.turbo.common.web.GenericTurboCrudController;
import cc.allio.turbo.modules.developer.entity.DevDataSource;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev/datasource")
@AllArgsConstructor
@Tag(name = "数据源管理")
public class DevDataSourceController extends GenericTurboCrudController<DevDataSource> {
}
