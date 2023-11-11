package cc.allio.uno.turbo.system.controller;

import cc.allio.uno.turbo.common.R;
import cc.allio.uno.turbo.common.TurboController;
import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.system.entity.SysMenu;
import cc.allio.uno.turbo.system.param.SysMenuParam;
import cc.allio.uno.turbo.system.service.ISysMenuService;
import cc.allio.uno.turbo.system.vo.SysMenuTreeVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/menu")
@AllArgsConstructor
@Tag(name = "菜单模块")
public class SysMenuController extends TurboController {

    private final ISysMenuService menuService;

    @PostMapping("/save")
    @Operation(summary = "保存")
    public R save(@Validated @RequestBody SysMenu sysMenu) {
        boolean save = menuService.save(sysMenu);
        return ok(save);
    }

    @Operation(summary = "修改")
    @PutMapping("/edit")
    public R edit(@Validated @RequestBody SysMenu sysMenu) {
        boolean edit =
                menuService.update(
                        sysMenu,
                        Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getId, sysMenu.getId()));
        return ok(edit);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete")
    public R delete(long id) throws BizException {
        boolean removed = menuService.deleteMenu(id);
        return ok(removed);
    }

    @Operation(summary = "详情")
    @GetMapping("/details")
    public R<SysMenu> details(long id) {
        SysMenu sysTenant = menuService.getById(id);
        return ok(sysTenant);
    }

    @Operation(summary = "菜单树")
    @GetMapping("/tree")
    public R<List<SysMenuTreeVO>> tree(SysMenuParam menuParam) {
        List<SysMenu> expandTree = menuService.tree(menuParam);
        List<SysMenuTreeVO> treeify = menuService.treeify(expandTree, SysMenuTreeVO.class);
        return ok(treeify);
    }
}
