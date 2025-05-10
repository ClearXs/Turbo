package cc.allio.turbo.modules.development.domain.view;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ViewMode {

    LIST("list", "无分页列表"),
    PAGE("page", "分页列表"),
    TREE("tree", "树"),
    CARD_PAGE("cardPage", "卡片列表"),
    SCROLLING_LIST("scrollingList", "滚动列表"),
    SCROLLING_CARD("scrollingCard", "滚动卡片"),
    LEFT_TREE_RIGHT_LIST("leftTreeRightList", "左树右列表"),
    LEFT_TREE_RIGHT_PAGE("leftTreeRightPage", "左树右分页列表"),
    LEFT_TREE_RIGHT_TREE("leftTreeRightTree", "左树右树"),
    LEFT_TREE_RIGHT_CARD_PAGE("leftTreeRightCardPage", "左树右分页卡片列表"),
    LEFT_TREE_RIGHT_SCROLLING_CARD("leftTreeRightScrollingCard", "左树右滚动卡片列表"),
    LEFT_TREE_RIGHT_SCROLLING_LIST("leftTreeRightScrollingList", "左树右滚动列表");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
