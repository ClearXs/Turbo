package cc.allio.turbo.modules.developer.service.impl;

import cc.allio.turbo.common.cache.CacheName;
import cc.allio.turbo.common.exception.BizException;
import cc.allio.turbo.common.i18n.DevCodes;
import cc.allio.turbo.common.db.mybatis.service.impl.TurboCrudServiceImpl;
import cc.allio.turbo.modules.developer.domain.BoAttributeTree;
import cc.allio.turbo.modules.developer.domain.BoSchema;
import cc.allio.turbo.modules.developer.entity.DevBo;
import cc.allio.turbo.modules.developer.entity.DevBoAttribute;
import cc.allio.turbo.modules.developer.mapper.DevBoMapper;
import cc.allio.turbo.modules.developer.service.IDevBoAttributeService;
import cc.allio.turbo.modules.developer.service.IDevBoService;
import cc.allio.uno.core.datastructure.tree.TreeSupport;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DevBoServiceImpl extends TurboCrudServiceImpl<DevBoMapper, DevBo> implements IDevBoService, CacheName {

    private final IDevBoAttributeService boAttributeService;

    @Override
    public BoSchema toSchema(Long boId) throws BizException {
        DevBo bo = getById(boId);
        if (bo == null) {
            throw new BizException(DevCodes.BO_ERROR);
        }
        List<DevBoAttribute> tree = boAttributeService.tree(Wrappers.<DevBoAttribute>lambdaQuery().eq(DevBoAttribute::getBoId, boId));
        List<BoAttributeTree> treeify = tree.stream().map(boAttributeService::entityToDomain).toList();
        return BoSchema.from(bo, TreeSupport.adjust(treeify));
    }


    @Override
    public String getCacheName() {
        return "bo";
    }
}
