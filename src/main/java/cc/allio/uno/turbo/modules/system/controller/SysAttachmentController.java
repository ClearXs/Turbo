package cc.allio.uno.turbo.modules.system.controller;

import cc.allio.uno.turbo.common.exception.BizException;
import cc.allio.uno.turbo.common.web.R;
import cc.allio.uno.turbo.common.web.TurboCrudController;
import cc.allio.uno.turbo.common.mybatis.entity.BaseEntity;
import cc.allio.uno.turbo.modules.system.entity.SysAttachment;
import cc.allio.uno.turbo.modules.system.service.ISysAttachmentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/sys/attachment")
@AllArgsConstructor
@Tag(name = "附件")
public class SysAttachmentController extends TurboCrudController<SysAttachment> {

    private final ISysAttachmentService attachmentService;

    @Override
    public R save(@Validated @RequestBody SysAttachment sysAttachment) {
        boolean save = attachmentService.save(sysAttachment);
        return ok(save);
    }

    @Override
    public R edit(@Validated @RequestBody SysAttachment sysAttachment) {
        boolean edit = attachmentService.update(sysAttachment, Wrappers.<SysAttachment>lambdaQuery().eq(SysAttachment::getId, sysAttachment.getId()));
        return ok(edit);
    }

    @Override
    public R saveOrUpdate(@Validated @RequestBody SysAttachment sysAttachment) {
        boolean edit = attachmentService.saveOrUpdate(sysAttachment, Wrappers.<SysAttachment>lambdaQuery().eq(SysAttachment::getId, sysAttachment.getId()));
        return ok(edit);
    }

    @Override
    public R batchSave(List<SysAttachment> entity) {
        boolean batch = attachmentService.saveBatch(entity);
        return ok(batch);
    }

    @Override
    public R delete(@RequestBody List<Long> ids) {
        boolean removed = attachmentService.removeByIds(ids);
        return ok(removed);
    }

    @Override
    public R<SysAttachment> details(long id) {
        SysAttachment sysAttachment = attachmentService.getById(id);
        return ok(sysAttachment);
    }

    @Override
    public R<List<SysAttachment>> list(SysAttachment sysAttachment) {
        List<SysAttachment> list = attachmentService.list(Wrappers.lambdaQuery(sysAttachment).orderByDesc(BaseEntity::getUpdatedTime));
        return ok(list);
    }

    @Override
    public R<IPage<SysAttachment>> page(Page page, SysAttachment sysAttachment) {
        IPage<SysAttachment> sysAttachmentPage = attachmentService.page(page, Wrappers.lambdaQuery(sysAttachment).orderByDesc(BaseEntity::getUpdatedTime));
        return ok(sysAttachmentPage);
    }

    @PostMapping("/upload")
    @Operation(summary = "上传")
    public R<SysAttachment> upload(HttpServletRequest request, MultipartFile file) throws BizException {
        return R.ok(attachmentService.upload(request, file));
    }

    @GetMapping("/download/{object}")
    @Operation(summary = "下载")
    public void download(@Parameter(description = "参数object可以为文件名称...") @PathVariable("object") String object, HttpServletRequest request, HttpServletResponse response) throws BizException {
        attachmentService.download(object, request, response);
    }
}
