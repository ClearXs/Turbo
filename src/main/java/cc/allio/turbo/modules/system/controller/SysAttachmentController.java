package cc.allio.turbo.modules.system.controller;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.modules.system.entity.SysAttachment;
import cc.allio.turbo.modules.system.service.ISysAttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sys/attachment")
@Tag(name = "附件")
public class SysAttachmentController extends TurboCrudController<SysAttachment, ISysAttachmentService, SysAttachment> {

    @PostMapping("/upload")
    @Operation(summary = "上传")
    public R<SysAttachment> upload(HttpServletRequest request, @RequestBody MultipartFile file) throws BizException {
        return R.ok(getService().upload(request, file));
    }

    @GetMapping("/download/{object}")
    @Operation(summary = "下载")
    public void download(@Parameter(description = "参数object可以为文件名称...") @PathVariable("object") String object, HttpServletRequest request, HttpServletResponse response) throws BizException {
        getService().download(object, request, response);
    }

}
