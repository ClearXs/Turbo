package cc.allio.uno.turbo.modules.system.controller;

import cc.allio.uno.turbo.common.web.R;
import cc.allio.uno.turbo.common.web.TurboCrudController;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.modules.system.entity.SysMenu;
import cc.allio.uno.turbo.modules.system.param.SysMenuParam;
import cc.allio.uno.turbo.modules.system.service.ISysMenuService;
import cc.allio.uno.turbo.modules.system.vo.SysMenuTreeVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/menu")
@AllArgsConstructor
@Tag(name = "菜单")
public class SysMenuController extends TurboCrudController<SysMenu> {

    private final ISysMenuService menuService;

    @Override
    public R save(@Validated @RequestBody SysMenu sysMenu) {
        boolean save = menuService.save(sysMenu);
        return ok(save);
    }

    @Override
    public R edit(@Validated @RequestBody SysMenu sysMenu) {
        boolean edit =
                menuService.update(
                        sysMenu,
                        Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getId, sysMenu.getId()));
        return ok(edit);
    }

    @Override
    public R saveOrUpdate(@Validated @RequestBody SysMenu sysMenu) {
        boolean edit =
                menuService.saveOrUpdate(
                        sysMenu,
                        Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getId, sysMenu.getId()));
        return ok(edit);
    }

    @Override
    public R batchSave(List<SysMenu> entity) {
        return null;
    }

    @Override
    public R delete(List<Long> ids) throws BizException {
        boolean removed = menuService.deleteMenu(ids);
        return ok(removed);
    }

    @Override
    public R<SysMenu> details(long id) {
        SysMenu sysTenant = menuService.getById(id);
        return ok(sysTenant);
    }

    @Override
    public R<List<SysMenu>> list(SysMenu entity) {
        return null;
    }

    @Override
    public R<IPage<SysMenu>> page(Page page, SysMenu entity) {
        return null;
    }

    @Operation(summary = "菜单树")
    @GetMapping("/tree")
    public R<List<SysMenuTreeVO>> tree(SysMenuParam menuParam) {
        List<SysMenu> expandTree = menuService.tree(menuParam);
        List<SysMenuTreeVO> treeify = menuService.treeify(expandTree, SysMenuTreeVO.class);
        return ok(treeify);
    }
}
