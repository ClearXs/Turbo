package cc.allio.uno.turbo.modules.system.controller;

import cc.allio.uno.turbo.common.web.R;
import cc.allio.uno.turbo.common.web.TurboCrudController;
import cc.allio.uno.turbo.modules.system.entity.SysTenant;
import cc.allio.uno.turbo.modules.system.service.ISysTenantService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/tenant")
@AllArgsConstructor
@Tag(name = "租户")
public class SysTenantController extends TurboCrudController<SysTenant> {

    private final ISysTenantService tenantService;

    @Override
    public R save(@Validated @RequestBody SysTenant sysTenant) {
        boolean save = tenantService.save(sysTenant);
        return ok(save);
    }

    @Override
    public R batchSave(@Validated @RequestBody List<SysTenant> tenants) {
        boolean save = tenantService.saveBatch(tenants);
        return ok(save);
    }

    @Override
    public R edit(@Validated @RequestBody SysTenant sysTenant) {
        boolean edit =
                tenantService.update(
                        sysTenant,
                        Wrappers.<SysTenant>lambdaQuery().ge(SysTenant::getTenantId, sysTenant.getTenantId()));
        return ok(edit);
    }

    @Override
    public R saveOrUpdate(@Validated @RequestBody SysTenant sysTenant) {
        boolean edit =
                tenantService.saveOrUpdate(
                        sysTenant,
                        Wrappers.<SysTenant>lambdaQuery().ge(SysTenant::getTenantId, sysTenant.getTenantId()));
        return ok(edit);
    }

    @Override
    public R delete(@RequestBody List<Long> ids) {
        boolean removed = tenantService.removeByIds(ids);
        return ok(removed);
    }

    @Override
    public R<SysTenant> details(long id) {
        SysTenant sysTenant = tenantService.getById(id);
        return ok(sysTenant);
    }

    @Override
    public R<List<SysTenant>> list(SysTenant sysTenant) {
        List<SysTenant> list = tenantService.list(new QueryWrapper<>(sysTenant));
        return ok(list);
    }

    @Override
    public R<IPage<SysTenant>> page(Page page, SysTenant sysTenant) {
        IPage<SysTenant> sysUserPage = tenantService.page(page, new QueryWrapper<>(sysTenant));
        return ok(sysUserPage);
    }

}
