package cc.allio.turbo.modules.office.service;

import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.modules.office.documentserver.util.DocumentDescriptor;
import cc.allio.turbo.modules.office.documentserver.vo.HistoryData;
import cc.allio.turbo.modules.office.documentserver.vo.HistoryList;
import cc.allio.turbo.modules.office.documentserver.vo.Rename;
import cc.allio.turbo.modules.office.documentserver.vo.Track;
import cc.allio.turbo.modules.office.dto.DocumentCreateDTO;
import cc.allio.turbo.modules.office.entity.Doc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * about document handle operation. like as
 * <ol>
 *     <li>create new version document</li>
 *     <li>restore document</li>
 *     <li>rename document</li>
 *     <li>...</li>
 * </ol>
 *
 * @author j.x
 * @date 2024/5/18 18:06
 * @since 0.0.1
 */
public interface IDocService extends ITurboCrudService<Doc> {

    /**
     * from template create document
     *
     * @return the {@link Doc} instance
     */
    Doc createDocumentFromTemplate(DocumentCreateDTO documentCreate) throws BizException;

    /**
     * from the {@link MultipartFile} file saves to new doc
     *
     * @param file the file
     * @return
     */
    Doc saves(MultipartFile file) throws IOException, BizException;

    /**
     * force save document
     *
     * @param docId    the doc id
     * @param filename the filename
     * @param track    the  onlyoffice callback data
     */
    void forceSave(Long docId, String filename, Track track);

    /**
     * create new version document by specifies doc id and download url
     *
     * @param docId    the doc id
     * @param filename the filename
     * @param track    the  onlyoffice callback data
     * @return
     */
    DocumentDescriptor newVersion(Long docId, String filename, Track track);

    /**
     * modify document name
     *
     * @param docId  the doc id
     * @param rename the doc new name
     * @return
     */
    Boolean rename(Long docId, Rename rename) throws BizException;

    /**
     * restore specifies version document
     *
     * @param docId  the document id
     * @param version the document version
     * @return true if success
     */
    Boolean restore(Long docId, Integer version);

    /**
     * 获取当前用户的历史版本
     *
     * @param docId the doc id
     * @return the {@link HistoryList} instance
     */
    HistoryList getHistoryList(Long docId) throws BizException;

    /**
     * get history data by doc id
     *
     * @param docId   the doc id
     * @param version
     * @return the {@link HistoryData} instance
     */
    HistoryData getHistoryData(Long docId, Integer version) throws BizException;

    /**
     * remove document
     *
     * @return success if true
     */
    Boolean remove(List<Long> docIdList);
}
