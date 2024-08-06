package cc.allio.turbo.modules.office.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.office.entity.DocCustomization;
import cc.allio.turbo.modules.office.mapper.DocCustomizationMapper;
import cc.allio.turbo.modules.office.service.IDocCustomizationService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class DocCustomizationServiceImpl extends TurboCrudServiceImpl<DocCustomizationMapper, DocCustomization> implements IDocCustomizationService {

    @Override
    public DocCustomization settingToShared(Long docId, Long userId, boolean shared) throws BizException {
        return settingsCustomization(docId, userId, d -> d.setShared(shared));
    }

    @Override
    public DocCustomization settingToFavorite(Long docId, Long userId, boolean favorite) throws BizException {
        return settingsCustomization(docId, userId, d -> d.setFavorite(favorite));
    }

    /**
     * setting customization document
     *
     * @param docId the doc id
     * @param userId the user id
     * @param consumer the {@link DocCustomization} callback
     */
    DocCustomization settingsCustomization(Long docId, Long userId, Consumer<DocCustomization> consumer) throws BizException {
        if (docId == null || userId == null) {
            throw new BizException("doc id or user id is not empty");
        }
        DocCustomization docCustomization = selectOneByDocIdAndUserId(docId, userId);
        if (docCustomization == null) {
            docCustomization = new DocCustomization();
            docCustomization.setDocId(docId);
            docCustomization.setUserId(userId);
        }
        consumer.accept(docCustomization);
        // save or update to database
        this.saveOrUpdate(docCustomization);
        return docCustomization;
    }

    @Override
    public DocCustomization selectOneByDocIdAndUserId(Long docId, Long userId) {
        return getOne(Wrappers.<DocCustomization>lambdaQuery().eq(DocCustomization::getDocId, docId).eq(DocCustomization::getUserId, userId));
    }
}
