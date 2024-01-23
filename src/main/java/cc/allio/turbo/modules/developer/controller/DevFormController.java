package cc.allio.turbo.modules.developer.controller;

import cc.allio.turbo.common.web.CategoryServiceTurboCrudController;
import cc.allio.turbo.common.web.CategoryTurboCrudController;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.WebCrudInterceptor;
import cc.allio.turbo.modules.developer.entity.DevForm;
import cc.allio.turbo.modules.developer.service.IDevFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dev/form")
@AllArgsConstructor
@Tag(name = "表单管理")
public class DevFormController extends CategoryServiceTurboCrudController<DevForm, IDevFormService> {

    @PostMapping("/publish")
    @Operation(summary = "表单数据发布")
    public R<Boolean> publish(@Validated @RequestBody DevForm form) {
        boolean publish = getService().publish(form);
        return ok(publish);
    }
}
