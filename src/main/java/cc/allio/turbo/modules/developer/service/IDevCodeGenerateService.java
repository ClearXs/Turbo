package cc.allio.turbo.modules.developer.service;

import cc.allio.turbo.common.db.uno.repository.ITurboCrudRepositoryService;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.developer.dto.CodeFile;
import cc.allio.turbo.modules.developer.entity.DevCodeGenerate;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface IDevCodeGenerateService extends ITurboCrudRepositoryService<DevCodeGenerate> {

    /**
     * preview generative result
     *
     * @param id the {@link DevCodeGenerate} id
     * @return list of {@link CodeFile}
     */
    List<CodeFile> preview(Long id) throws BizException;

    /**
     * generate zip code file, and user download it
     *
     * @param id the {@link DevCodeGenerate} id
     * @param response the {@link HttpServletResponse} instance
     */
    void generate(Long id, HttpServletResponse response);
}
