package cc.allio.turbo.modules.developer.service;

import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import cc.allio.turbo.modules.developer.entity.DevSequence;

import java.util.List;

public interface IDevSequenceService extends ITurboCrudService<DevSequence> {

    /**
     * rest sequence to initial value
     *
     * @param id the sequence id
     * @return true if success
     */
    Boolean reset(Long id);

    /**
     * generate auto number
     *
     * @param id the {@link DevSequence} id
     * @return generation auto number
     */
    String generate(Long id);

    /**
     * generate list of count to auto number
     *
     * @param id the {@link DevSequence} id
     * @param count the generate count
     *
     * @return list of generate auto number
     */
    List<String> generateList(Long id, int count);
}
