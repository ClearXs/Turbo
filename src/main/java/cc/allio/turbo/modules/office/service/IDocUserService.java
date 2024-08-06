package cc.allio.turbo.modules.office.service;

import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.office.dto.DocumentDTO;
import cc.allio.turbo.modules.office.dto.OnlineDocUser;
import cc.allio.turbo.modules.office.dto.page.DocPageDTO;
import cc.allio.turbo.modules.office.vo.DocUser;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * 与文档用户相关操作
 *
 * @author j.x
 * @date 2024/5/9 18:42
 * @since 0.0.1
 */
public interface IDocUserService {

    /**
     * collect specifies document
     *
     * @param docId the document id
     * @return success if true
     */
    Boolean favoriteOfDocument(Long docId) throws BizException;

    /**
     * cancel collect specifies document
     *
     * @param docId the document id
     * @return success if true
     */
    Boolean cancelFavoriteOfDocument(Long docId) throws BizException;

    /**
     * favor specifies document
     *
     * @param docId the document id
     * @return success if true
     */
    Boolean favorOfDocument(Long docId) throws BizException;

    /**
     * cancel favor specifies document
     *
     * @param docId the document id
     * @return success if true
     */
    Boolean cancelFavorOfDocument(Long docId) throws BizException;

    /**
     * 从请求中获取{@link DocUser}
     *
     * @return the {@link DocUser} instance
     */
    DocUser getDocUserByRequest(Long docId) throws BizException;

    /**
     * according document name or creator name
     *
     * @param pattern the match pattern
     * @return list of {@link DocumentDTO}
     */
    List<DocumentDTO> searchMineDocument(String pattern) throws BizException;

    /**
     * select user document list
     *
     * @param params the filter conditional
     * @return the {@link DocumentDTO}
     */
    IPage<DocumentDTO> selectUserDocument(DocPageDTO params) throws BizException;

    /**
     * select recently document list
     *
     * @param params the filter conditional
     * @return the {@link DocumentDTO}
     */
    IPage<DocumentDTO> selectRecentlyDocument(DocPageDTO params) throws BizException;

    /**
     * select share to me document list
     *
     * @param params the filter conditional
     * @return the {@link DocumentDTO}
     */
    IPage<DocumentDTO> selectShareToMeDocument(DocPageDTO params) throws BizException;

    /**
     * select mien favorite document list
     *
     * @param params the filter conditional
     * @return the {@link DocumentDTO}
     */
    IPage<DocumentDTO> selectMineFavoriteDocument(DocPageDTO params) throws BizException;

    /**
     * select mine create document list
     *
     * @param params the filter conditional
     * @return the {@link DocumentDTO}
     */
    IPage<DocumentDTO> selectMineCreateDocument(DocPageDTO params) throws BizException;

    /**
     * select mien favor document list
     *
     * @param params the filter conditional
     * @return the {@link DocumentDTO}
     */
    IPage<DocumentDTO> selectMineFavorDocument(DocPageDTO params) throws BizException;

    /**
     * get online doc user
     *
     * @param docId the doc user
     * @return {@link OnlineDocUser} list
     */
    List<OnlineDocUser> getOnlineDocUser(Long docId) throws BizException;

    /**
     * kickout specifies document from user ids list
     *
     * @param docId the doc id
     * @param userIds the user id list
     * @return success if true
     */
    Boolean kickout(Long docId, List<Long> userIds) throws BizException;

    /**
     * kickout other users and without creator
     *
     * @param docId the doc id
     * @return success if true
     */
    Boolean kickoutOthres(Long docId) throws BizException;

    /**
     * kickout specifies all user
     *
     * @param docId the doc id
     * @return
     */
    Boolean kickoutAll(Long docId) throws BizException;

    /**
     * force save specifies document
     *
     * @param docId the doc id
     * @return success if true
     */
    Boolean forceSave(Long docId) throws BizException;
}
