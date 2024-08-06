package cc.allio.turbo.modules.office.service;

import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.office.entity.DocCustomization;

public interface IDocCustomizationService extends ITurboCrudService<DocCustomization> {

    /**
     * setting user doc is shared. if not exist then create one data
     *
     * @param docId  the doc id
     * @param userId the user id
     * @param shared the shared
     */
    DocCustomization settingToShared(Long docId, Long userId, boolean shared) throws BizException;

    /**
     * setting user doc is favorite. if not exits then create one data
     *
     * @param docId    the doc id
     * @param userId   the user id
     * @param favorite the favorite
     *d
     */
    DocCustomization settingToFavorite(Long docId, Long userId, boolean favorite) throws BizException;

    /**
     * select one {@link DocCustomization} by doc id and user id
     *
     * @param docId the doc id
     * @param userId the user id
     * @return {@link DocCustomization} instance or null
     */
    DocCustomization selectOneByDocIdAndUserId(Long docId, Long userId);
}
