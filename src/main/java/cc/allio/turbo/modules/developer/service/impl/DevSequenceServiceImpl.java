package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.extension.swift.*;
import cc.allio.turbo.modules.developer.entity.DevSequence;
import cc.allio.turbo.modules.developer.mapper.DevSequenceMapper;
import cc.allio.turbo.modules.developer.service.IDevSequenceService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class DevSequenceServiceImpl extends TurboCrudServiceImpl<DevSequenceMapper, DevSequence> implements IDevSequenceService {

    private final Swift swift;

    @Override
    public Boolean reset(Long id) {
        DevSequence sequence = getById(id);
        Sequential sequential = new Sequential(
                swift,
                sequence.getKey(),
                sequence.getInitialValue(),
                sequence.getLength(),
                sequence.getPrefix(),
                sequence.getSuffix(),
                sequence.getStep(),
                sequence.getGenType());
        return sequential.reset();
    }

    @Override
    public String generate(Long id) {
        DevSequence sequence = getById(id);
        Sequential sequential = new Sequential(
                swift,
                sequence.getKey(),
                sequence.getInitialValue(),
                sequence.getLength(),
                sequence.getPrefix(),
                sequence.getSuffix(),
                sequence.getStep(),
                sequence.getGenType());
        return sequential.nextNo();
    }

    @Override
    public List<String> generateList(Long id, int count) {
        if (count <= 0) {
            return Collections.emptyList();
        }
        List<String> sequenceList = Lists.newArrayList();
        for (int i = count; i > 0; i--) {
            String autoNumber = generate(id);
            sequenceList.add(autoNumber);
        }
        return sequenceList;
    }

}
