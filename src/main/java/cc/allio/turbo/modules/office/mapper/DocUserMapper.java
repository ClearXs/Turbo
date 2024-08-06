package cc.allio.turbo.modules.office.mapper;

import cc.allio.turbo.modules.office.dto.page.DocPageDTO;
import cc.allio.turbo.modules.office.vo.DocVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DocUserMapper {

    /**
     * search user document list
     *
     * @return list of {@link DocVO}
     */
    List<DocVO> searchUserDocList(@Param("params") DocPageDTO params);

    /**
     * select user {@link DocVO}
     *
     * @param params the filter conditional and page
     * @return the {@link DocVO} list
     */
    IPage<DocVO> selectUserDocList(@Param("params") DocPageDTO params);

    /**
     * select creator doc user list
     *
     * @param params the filter conditional and page
     * @return the {@link DocVO} list
     */
    IPage<DocVO> selectCreatorDocList(@Param("params") DocPageDTO params);

    /**
     * select collaborator doc list
     *
     * @param params the filter conditional and page
     * @return the {@link DocVO} list
     */
    IPage<DocVO> selectCollaboratorDocList(@Param("params") DocPageDTO params);
}