package cc.allio.turbo.modules.office.controller.v1;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.modules.office.dto.DocumentDTO;
import cc.allio.turbo.modules.office.dto.OnlineDocUser;
import cc.allio.turbo.modules.office.dto.page.DocPageDTO;
import cc.allio.turbo.modules.office.service.IDocUserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.xml.core.Validate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/office/v1/doc/user")
@AllArgsConstructor
@Tag(name = "用户文档", description = "用户文档")
public class DocumentUserController {

    private final IDocUserService docUserService;

    @PutMapping("/favor/{docId}")
    @Operation(summary = "固定常用的文档")
    public R<Boolean> favorOfDocument(@Validate @NotNull @PathVariable("docId") Long docId) throws BizException {
        Boolean success = docUserService.favorOfDocument(docId);
        return R.ok(success);
    }

    @PutMapping("/favor/cancel/{docId}")
    @Operation(summary = "取消固定常用的文档")
    public R<Boolean> cancelFavorOfDocument(@Validate @NotNull @PathVariable("docId") Long docId) throws BizException {
        Boolean success = docUserService.cancelFavorOfDocument(docId);
        return R.ok(success);
    }

    @PutMapping("/favorite/{docId}")
    @Operation(summary = "收藏指定的文档")
    public R<Boolean> favoriteOfDocument(@Validate @NotNull @PathVariable("docId") Long docId) throws BizException {
        Boolean success = docUserService.favoriteOfDocument(docId);
        return R.ok(success);
    }

    @PutMapping("/favorite/cancel/{docId}")
    @Operation(summary = "取消收藏指定的文档")
    public R<Boolean> cancelFavoriteDocument(@Validate @NotNull @PathVariable("docId") Long docId) throws BizException {
        Boolean success = docUserService.cancelFavoriteOfDocument(docId);
        return R.ok(success);
    }

    @GetMapping("/searchMine")
    @Operation(summary = "搜索我的文档")
    public R<List<DocumentDTO>> searchMineDocument(@Valid @NotNull String pattern) throws BizException {
        List<DocumentDTO> documentList = docUserService.searchMineDocument(pattern);
        return R.ok(documentList);
    }

    @PostMapping("/getMine")
    @Operation(summary = "获取我的文档列表")
    public R<IPage<DocumentDTO>> getMineDocument(@RequestBody DocPageDTO params) throws BizException {
        IPage<DocumentDTO> documentList = docUserService.selectUserDocument(params);
        return R.ok(documentList);
    }

    @PostMapping("/getMineRecently")
    @Operation(summary = "获取最近我的文档列表")
    public R<IPage<DocumentDTO>> getMineRecentlyDocument(@RequestBody DocPageDTO params) throws BizException {
        IPage<DocumentDTO> documentList = docUserService.selectRecentlyDocument(params);
        return R.ok(documentList);
    }

    @PostMapping("/getShareToMe")
    @Operation(summary = "获取分享给我的文档列表")
    public R<IPage<DocumentDTO>> getShareToMeDocument(@RequestBody DocPageDTO params) throws BizException {
        IPage<DocumentDTO> documentList = docUserService.selectShareToMeDocument(params);
        return R.ok(documentList);
    }

    @PostMapping("/getMineFavorite")
    @Operation(summary = "获取我喜爱文档列表")
    public R<IPage<DocumentDTO>> getMineFavoriteDocument(@RequestBody DocPageDTO params) throws BizException {
        IPage<DocumentDTO> documentList = docUserService.selectMineFavoriteDocument(params);
        return R.ok(documentList);
    }

    @PostMapping("/getMineCreate")
    @Operation(summary = "获取我创建文档列表")
    public R<IPage<DocumentDTO>> getMineCreateDocument(@RequestBody DocPageDTO params) throws BizException {
        IPage<DocumentDTO> documentList = docUserService.selectMineCreateDocument(params);
        return R.ok(documentList);
    }

    @PostMapping("/getMineFavor")
    @Operation(summary = "获取我常用文档列表")
    public R<IPage<DocumentDTO>> getMineFavorDocument(@RequestBody DocPageDTO params) throws BizException {
        IPage<DocumentDTO> documentList = docUserService.selectMineFavorDocument(params);
        return R.ok(documentList);
    }

    @PostMapping("/kickout/{docId}")
    @Operation(summary = "踢出指定人")
    public R<Boolean> kickout(@PathVariable("docId") @NotNull Long docId, @RequestBody List<Long> userId) throws BizException {
        Boolean success = docUserService.kickout(docId, userId);
        return R.ok(success);
    }

    @PostMapping("/kickoutOthers/{docId}")
    @Operation(summary = "提出非创建者其他人")
    public R<Boolean> kickoutOthers(@PathVariable("docId") @NotNull Long docId) throws BizException {
        Boolean success = docUserService.kickoutOthres(docId);
        return R.ok(success);
    }

    @PostMapping("/kickoutAll/{docId}")
    @Operation(summary = "踢出所有人")
    public R<Boolean> kickoutAll(@PathVariable("docId") @NotNull Long docId) throws BizException {
        Boolean success = docUserService.kickoutAll(docId);
        return R.ok(success);
    }

    @GetMapping("/getOnlineDocUser/{docId}")
    @Operation(summary = "获取文档在线编辑人列表")
    public R<List<OnlineDocUser>> getOnlineDocUser(@PathVariable("docId") @NotNull Long docId) throws BizException {
        List<OnlineDocUser> onlineDocUser = docUserService.getOnlineDocUser(docId);
        return R.ok(onlineDocUser);
    }
}
