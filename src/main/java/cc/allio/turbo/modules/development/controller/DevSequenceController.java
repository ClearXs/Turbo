package cc.allio.turbo.modules.development.controller;

import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.development.entity.DevSequence;
import cc.allio.turbo.modules.development.service.IDevSequenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dev/sequence")
@AllArgsConstructor
@Tag(name = "流水号")
public class DevSequenceController extends TurboCrudController<DevSequence, DevSequence, IDevSequenceService> {

    @GetMapping("/rest/{id}")
    @Operation(summary = "重置")
    public R<Boolean> rest(@Validated @PathVariable("id") @NotNull Long id) {
        boolean rested = getService().reset(id);
        return R.ok(rested);
    }

    @GetMapping("/generate/{id}")
    @Operation(summary = "生成流水号")
    public R<String> generate(@Validated @PathVariable("id") @NotNull Long id) {
        String autoNumber = getService().generate(id);
        return R.ok(autoNumber);
    }

    @GetMapping("/batch-generate/{id}")
    @Operation(summary = "批量生成流水号")
    public R<List<String>> batchGenerate(@Validated @PathVariable("id") @NotNull Long id, @NotNull Integer count) {
        List<String> generateList = getService().generateList(id, count);
        return R.ok(generateList);
    }
}
