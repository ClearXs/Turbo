package cc.allio.turbo.modules.development.controller;

import cc.allio.turbo.common.web.CategoryTurboCrudController;
import cc.allio.turbo.modules.development.entity.DevDataset;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev/dataset")
@AllArgsConstructor
@Tag(name = "数据集管理")
public class DevDatasetController extends CategoryTurboCrudController<DevDataset> {
}
