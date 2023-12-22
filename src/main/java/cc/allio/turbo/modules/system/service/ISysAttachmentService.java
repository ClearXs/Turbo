package cc.allio.turbo.modules.system.service;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.mybatis.service.ITurboCrudService;
import cc.allio.turbo.modules.system.entity.SysAttachment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ISysAttachmentService extends ITurboCrudService<SysAttachment> {

    /**
     * 附件上传，通过系统云存储配置进行上传，不存储于本地
     *
     * @param request request
     * @param file    file
     * @return SysAttachment 上传成功并保存数据库后返回
     * @throws BizException 上传失败抛出
     */
    SysAttachment upload(HttpServletRequest request, MultipartFile file) throws BizException;

    /**
     * 附件下载
     *
     * @param object 附件名称
     */
    void download(String object, HttpServletRequest request, HttpServletResponse response) throws BizException;
}
