package cc.allio.turbo.modules.message.runtime.targeter;

import cc.allio.turbo.common.db.entity.IdEntity;
import cc.allio.turbo.modules.message.config.SendTarget;
import cc.allio.turbo.modules.message.config.Target;
import cc.allio.turbo.modules.message.runtime.RuntimeVariable;
import cc.allio.turbo.modules.system.entity.SysPost;
import cc.allio.turbo.modules.system.service.ISysPostService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * post target
 *
 * @author j.x
 * @date 2024/3/29 00:50
 * @since 0.1.1
 */
@Component
@AllArgsConstructor
public class PostTargeter implements Targeter {

    private final ISysPostService postService;

    @Override
    public List<Long> getUserList(SendTarget sendTarget, RuntimeVariable runtimeVariable) {
        List<SysPost> sysPosts = postService.list(Wrappers.<SysPost>lambdaQuery().eq(SysPost::getCode, sendTarget.getKey()));
        return sysPosts.stream().map(IdEntity::getId).toList();
    }

    @Override
    public Target getTarget() {
        return Target.POST;
    }
}
