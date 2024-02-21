package cc.allio.turbo.modules.developer.domain.view;

import lombok.Data;

@Data
public class Card {

    // 卡片点击
    private Object onClick;
    // 渲染卡片标题，如果没有则为空
    private String renderTitle;
    // 渲染卡片内容，如果没有则为空
    private String renderContent;
    // 渲染卡片页脚，如果没有则为空
    private String renderFooter;
}
