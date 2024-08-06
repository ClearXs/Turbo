package cc.allio.turbo.modules.office.controller.v1;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.extension.oss.OssExecutorFactory;
import cc.allio.turbo.extension.oss.OssProperties;
import cc.allio.turbo.extension.oss.OssResponse;
import cc.allio.turbo.extension.oss.Path;
import cc.allio.turbo.extension.oss.request.OssGetRequest;
import cc.allio.turbo.modules.office.documentserver.util.DocumentDescriptor;
import cc.allio.turbo.modules.office.documentserver.vo.Rename;
import cc.allio.turbo.modules.office.dto.DocumentCreateDTO;
import cc.allio.turbo.modules.office.entity.Doc;
import cc.allio.turbo.modules.office.service.IDocService;
import cc.allio.turbo.modules.office.service.IDocUserService;
import cc.allio.turbo.modules.system.entity.SysAttachment;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jodd.io.FileUtil;
import jodd.io.IOUtil;
import jodd.io.ZipBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/office/v1/doc")
@AllArgsConstructor
@Tag(name = "文档")
@Valid
public class DocumentController {

    private final IDocService docService;
    private final IDocUserService docUserService;

    @PostMapping("/saves")
    @Operation(summary = "保存文档")
    public R<Doc> saves(@RequestBody MultipartFile file) throws BizException, IOException {
        Doc doc = this.docService.saves(file);
        return R.ok(doc);
    }

    @GetMapping("/forceSave/{docId}")
    @Operation(summary = "强制保存")
    public R<Boolean> forceSave(@PathVariable("docId") @NotNull Long docId) throws BizException {
        Boolean success = docUserService.forceSave(docId);
        return R.ok(success);
    }

    @PostMapping("/create")
    @Operation(summary = "根据空白模板创建文档")
    public R<Doc> create(@Validated @RequestBody DocumentCreateDTO documentCreate) throws BizException {
        Doc doc = this.docService.createDocumentFromTemplate(documentCreate);
        return R.ok(doc);
    }

    @PutMapping("/rename/{docId}")
    @Operation(summary = "修改文档名称")
    public R<Boolean> rename(@PathVariable("docId") @NotNull Long docId, @Valid @RequestBody Rename rename) throws BizException {
        Boolean success = docService.rename(docId, rename);
        return R.ok(success);
    }

    @GetMapping(value = "/shard/{docId}")
    @Operation(summary = "分享文档")
    public R<String> share(@PathVariable("docId") @NotNull Long docId) {
        return R.ok();
    }

    @GetMapping("/getDocumentById/{docId}")
    @Operation(summary = "根据id获取文档")
    public R<Doc> getDocumentById(@PathVariable("docId") @NotNull Long docId) {
        Doc doc = this.docService.getById(docId);
        return R.ok(doc);
    }

    @GetMapping("/download/{docId}")
    @Operation(summary = "根据文档Id下载文档")
    public void download(@PathVariable("docId") @NotNull Long docId, HttpServletResponse response) throws BizException {
        Doc doc = docService.getById(docId);
        if (doc == null) {
            throw new BizException("not found document");
        }
        DocumentDescriptor documentDescriptor = new DocumentDescriptor(doc);
        SysAttachment documentAttachment = documentDescriptor.obtainAttachment();
        if (documentAttachment == null) {
            throw new BizException("not found document");
        }
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(documentDescriptor.getFullname(), StandardCharsets.UTF_8));
        response.setContentType("application/force-download");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String filepath = documentAttachment.getFilepath();
        OssGetRequest request = OssGetRequest.builder().path(Path.from(filepath)).build();
        OssResponse ossResponse = OssExecutorFactory.getCurrent().download(request, new OssProperties());
        try (var documentStream = ossResponse.getInputStream()) {
            IOUtil.copy(documentStream, response.getOutputStream());
        } catch (Throwable ex) {
            throw new BizException("Copy string literal text to the clipboard");
        }
    }

    @GetMapping("/downloads")
    @Operation(summary = "批量下载文档")
    public void downloads(@RequestParam("docIds") List<Long> docIds, HttpServletResponse response) throws IOException {
        List<Doc> docs = docService.list(Wrappers.<Doc>lambdaQuery().in(Doc::getId, docIds));
        String zipPath = "output.zip";
        ZipBuilder zipBuilder = ZipBuilder.createZipFile(new File(zipPath));
        for (Doc doc : docs) {
            DocumentDescriptor documentDescriptor = new DocumentDescriptor(doc);
            SysAttachment documentAttachment = documentDescriptor.obtainAttachment();
            if (documentAttachment == null) {
                continue;
            }
            String fullname = documentDescriptor.getFullname();
            String filepath = documentAttachment.getFilepath();
            OssGetRequest request = OssGetRequest.builder().path(Path.from(filepath)).build();
            OssResponse ossResponse = OssExecutorFactory.getCurrent().download(request, new OssProperties());
            InputStream documentStream = ossResponse.getInputStream();
            File file = new File(fullname);
            FileUtil.writeStream(file, documentStream);
            try {
                zipBuilder = zipBuilder.add(file).save();
            } finally {
                FileUtil.delete(file);
            }
        }

        File zipFile = zipBuilder.toZipFile();
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(zipPath, StandardCharsets.UTF_8));
        response.setContentType("application/force-download");
        response.setCharacterEncoding("UTF-8");
        try (var fis = new FileInputStream(zipFile)) {
            IOUtil.copy(fis, response.getOutputStream());
        } catch (Throwable ex) {
            log.error("Failed to download document file", ex);
        }
    }

    @DeleteMapping("/remove")
    @Operation(summary = "移除文档")
    public R<Boolean> remove(@RequestBody List<Long> docIdList) {
        Boolean remove = docService.remove(docIdList);
        return R.ok(remove);
    }
}
