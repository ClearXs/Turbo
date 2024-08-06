package cc.allio.turbo.modules.office.controller;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.office.documentserver.vo.HistoryData;
import cc.allio.turbo.modules.office.documentserver.vo.HistoryList;
import cc.allio.turbo.modules.office.documentserver.vo.Rename;
import cc.allio.turbo.modules.office.dto.OnlineDocUser;
import cc.allio.turbo.modules.office.entity.Doc;
import cc.allio.turbo.modules.office.service.IDocService;
import cc.allio.turbo.modules.office.service.IDocUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/office/doc")
@AllArgsConstructor
@Tag(name = "文档")
public class DocController extends TurboCrudController<Doc, Doc, IDocService> {

    private final IDocUserService docUserService;

    @PostMapping("/saves")
    @Operation(summary = "保存文档")
    public R<Doc> save(@RequestBody MultipartFile file) throws BizException, IOException {
        Doc doc = this.service.saves(file);
        return R.ok(doc);
    }

    @GetMapping("/forceSave/{docId}")
    @Operation(summary = "强制保存")
    public R<Boolean> forceSave(@PathVariable("docId") Long docId) throws BizException {
        Boolean success = this.docUserService.forceSave(docId);
        return R.ok(success);
    }

    @PutMapping("/rename/{docId}")
    @Operation(summary = "修改文档名称")
    public R<Boolean> rename(@PathVariable("docId") Long docId, @Valid @RequestBody Rename rename) throws BizException {
        Boolean success = this.service.rename(docId, rename);
        return R.ok(success);
    }

    @PutMapping("/restore/{docId}/{version}")
    @Operation(summary = "还原指定版本版本")
    public R<Boolean> restore(@PathVariable("docId") Long docId, @PathVariable("version") Integer version) {
        Boolean success = this.service.restore(docId, version);
        return R.ok(success);
    }

    @GetMapping("/history/{docId}")
    @Operation(summary = "获取用户的历史版本")
    public R<HistoryList> history(@PathVariable("docId") Long docId) throws BizException {
        HistoryList history = this.service.getHistoryList(docId);
        return R.ok(history);
    }

    @GetMapping("/historyData/{docId}/{version}")
    @Operation(summary = "获取用户的历史版本数据")
    public R<HistoryData> historyData(@PathVariable("docId") Long docId, @PathVariable("version") Integer version) throws BizException {
        HistoryData historyData = this.service.getHistoryData(docId, version);
        return R.ok(historyData);
    }

    @PostMapping("/kickout/{docId}")
    @Operation(summary = "踢出指定人")
    public R<Boolean> kickout(@PathVariable("docId") Long docId, @RequestBody List<Long> userId) throws BizException {
        Boolean success = docUserService.kickout(docId, userId);
        return R.ok(success);
    }

    @PostMapping("/kickoutOthers/{docId}")
    @Operation(summary = "提出非创建者其他人")
    public R<Boolean> kickoutOthers(@PathVariable("docId") Long docId) throws BizException {
        Boolean success = docUserService.kickoutOthres(docId);
        return R.ok(success);
    }

    @PostMapping("/kickoutAll/{docId}")
    @Operation(summary = "踢出所有人")
    public R<Boolean> kickoutAll(@PathVariable("docId") Long docId) throws BizException {
        Boolean success = docUserService.kickoutAll(docId);
        return R.ok(success);
    }

    @GetMapping("/getOnlineDocUser/{docId}")
    @Operation(summary = "获取文档在线编辑人列表")
    public R<List<OnlineDocUser>> getOnlineDocUser(@PathVariable("docId") Long docId) throws BizException {
        List<OnlineDocUser> onlineDocUser = docUserService.getOnlineDocUser(docId);
        return R.ok(onlineDocUser);
    }
}