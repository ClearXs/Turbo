package cc.allio.turbo.modules.message.controller;

import cc.allio.turbo.common.db.mybatis.helper.Conditions;
import cc.allio.turbo.common.util.AuthUtil;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.common.web.params.QueryParam;
import cc.allio.turbo.modules.message.dto.ReceiveVariables;
import cc.allio.turbo.modules.message.entity.SysMessage;
import cc.allio.turbo.modules.message.runtime.ReceiveMetadata;
import cc.allio.turbo.modules.message.service.ISysMessageService;
import cc.allio.uno.core.metadata.endpoint.source.SinkSource;
import cc.allio.uno.core.util.JsonUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/message")
@Tag(name = "消息")
public class SysMessageController extends TurboCrudController<SysMessage, SysMessage, ISysMessageService> {

    @Autowired
    private SinkSource sinkSource;

    @Operation(summary = "当前用户未读消息")
    @PostMapping("/current-user")
    public R<IPage<SysMessage>> currentUser(@RequestBody QueryParam<SysMessage> params) {
        Long currentUserId = AuthUtil.getCurrentUserId();
        SysMessage sysMessage = new SysMessage();
        params.addTerm(sysMessage::getReceiver, currentUserId);
        QueryWrapper<SysMessage> queryWrapper = Conditions.entityQuery(params, getEntityType());
        Page<SysMessage> entityPage = service.page(params.getPage(), queryWrapper);
        return R.ok(entityPage);
    }

    @Operation(summary = "发送消息")
    @PostMapping("/publish")
    public R currentUser(@RequestBody ReceiveVariables receiveVariables) {
        sinkSource.publish(JsonUtils.toJson(receiveVariables));
        return R.ok();
    }
}
