package cc.allio.turbo.modules.developer.controller;

import cc.allio.turbo.common.domain.TreeDomain;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.params.QueryParam;
import cc.allio.turbo.modules.developer.entity.DomainEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/dev/bo/domain")
@AllArgsConstructor
@Tag(name = "领域对象")
public class DomainController {

    @PostMapping("/save/{boId}")
    @Operation(summary = "保存")
    public R<Boolean> save(@PathVariable("boId") Serializable boId, @RequestBody DomainEntity domain) {
        return null;
    }

    @Operation(summary = "修改")
    @PutMapping("/edit/{pageId}")
    public R<Boolean> edit(@PathVariable("boId") Serializable boId, @RequestBody DomainEntity domain) {
        return null;
    }

    @Operation(summary = "保存或修改")
    @PostMapping("/save-or-update/{boId}")
    public R<Boolean> saveOrUpdate(@PathVariable("boId") Serializable boId, @RequestBody DomainEntity domain) {
        return null;
    }

    @PostMapping("/batch-save-or-update/{boId}")
    @Operation(summary = "批量保存或更新")
    public R<Boolean> batchSave(@PathVariable("boId") Serializable boId, @RequestBody List<DomainEntity> domain) {
        return null;
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete/{boId}")
    public R<Boolean> delete(@PathVariable("boId") Serializable boId, @RequestBody List<Serializable> ids) {
        return null;
    }

    @Operation(summary = "详情")
    @GetMapping("/details/{boId}")
    public R<DomainEntity> details(@PathVariable("boId") Serializable boId, Serializable id) {
        return null;
    }

    @Operation(summary = "列表")
    @PostMapping("/list/{boId}")
    public R<List<DomainEntity>> list(@PathVariable("boId") Serializable boId, @RequestBody QueryParam<DomainEntity> params) {
        return null;
    }

    @Operation(summary = "分页")
    @PostMapping("/page/{boId}")
    public R<IPage<DomainEntity>> page(@PathVariable("boId") Serializable boId, @RequestBody QueryParam<DomainEntity> params) {
        return null;
    }

    @Operation(summary = "导出")
    @PostMapping("/export/{boId}")
    public void export(@PathVariable("boId") Serializable boId, @RequestBody QueryParam<DomainEntity> params, HttpServletResponse response) {

    }

    @Operation(summary = "导入")
    @PostMapping("/import/{boId}")
    public R<Boolean> importFile(@PathVariable("boId") Serializable boId, @RequestBody MultipartFile file) {
        return null;
    }

    @Operation(summary = "树查询")
    @PostMapping("/tree/{boId}")
    public <Z extends TreeDomain<DomainEntity, Z>> R<List<Z>> tree(@PathVariable("boId") Serializable boId, @RequestBody QueryParam<DomainEntity> params) throws BizException {
        return null;
    }


}
