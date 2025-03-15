package cc.allio.turbo.modules.message.controller;

import cc.allio.turbo.common.db.mybatis.helper.Conditions;
import cc.allio.turbo.common.util.AuthUtil;
import cc.allio.turbo.common.web.R;
import cc.allio.turbo.common.web.TurboCrudController;
import cc.allio.turbo.common.web.params.QueryParam;
import cc.allio.turbo.modules.message.enums.Status;
import cc.allio.turbo.modules.message.dto.ReceiveVariables;
import cc.allio.turbo.modules.message.entity.SysMessage;
import cc.allio.turbo.modules.message.service.ISysMessageService;
import cc.allio.uno.core.exception.Trys;
import cc.allio.uno.core.metadata.endpoint.source.SinkSource;
import cc.allio.uno.core.util.JsonUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/message")
@Tag(name = "消息")
public class SysMessageController extends TurboCrudController<SysMessage, SysMessage, ISysMessageService> {

    @Autowired
    private SinkSource sinkSource;

    @Operation(summary = "当前用户消息")
    @PostMapping("/current-user/all")
    public R<IPage<SysMessage>> currentUser(@RequestBody QueryParam<SysMessage> params) {
        String currentUserId = AuthUtil.getUserId();
        SysMessage sysMessage = new SysMessage();
        params.addTerm(sysMessage::getReceiver, Trys.onContinue(() -> Long.valueOf(currentUserId)));
        QueryWrapper<SysMessage> queryWrapper = Conditions.entityQuery(params, getEntityType());
        Page<SysMessage> entityPage = service.page(params.getPage(), queryWrapper);
        return R.ok(entityPage);
    }

    @Operation(summary = "当前用户消息数量消息")
    @PostMapping("/current-user/count")
    public R<Long> currentUserCount(@RequestBody QueryParam<SysMessage> params) {
        String currentUserId = AuthUtil.getUserId();
        SysMessage sysMessage = new SysMessage();
        params.addTerm(sysMessage::getReceiver, Trys.onContinue(() -> Long.valueOf(currentUserId)));
        QueryWrapper<SysMessage> queryWrapper = Conditions.entityQuery(params, getEntityType());
        long count = service.count(queryWrapper);
        return R.ok(count);
    }

    @Operation(summary = "更新消息未已读")
    @PutMapping("/read")
    public R<Boolean> currentUserCount(@RequestBody List<Long> ids) {
        UpdateWrapper<SysMessage> updateWrapper = Wrappers.update();
        updateWrapper.setEntityClass(SysMessage.class);
        updateWrapper.in("id", ids);
        updateWrapper.set("messageStatus", Status.READ);
        boolean update = service.update(updateWrapper);
        return R.ok(update);
    }

    @Operation(summary = "发送消息")
    @PostMapping("/publish")
    public R currentUser(@RequestBody ReceiveVariables receiveVariables) {
        sinkSource.publish(JsonUtils.toJson(receiveVariables));
        return R.ok();
    }
}
