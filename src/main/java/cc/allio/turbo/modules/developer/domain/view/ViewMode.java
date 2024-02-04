package cc.allio.turbo.modules.developer.domain.view;

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
    LEFT_TREE_RIGHT_LIST("left_tree_right_list", "左树右列表"),
    LEFT_TREE_RIGHT_PAGE("left_tree_right_page", "左树右分页列表"),
    LEFT_TREE_RIGHT_TREE("left_tree_right_tree", "左树右树"),
    LEFT_TREE_RIGHT_CARD_PAGE("left_tree_right_card_page", "左树右分页卡片列表"),
    LEFT_TREE_RIGHT_SCROLLING_CARD("left_tree_right_card_page", "左树右滚动卡片列表"),
    LEFT_TREE_RIGHT_SCROLLING_LIST("left_tree_right_scrolling_list", "左树右滚动列表");

    @JsonValue
    @EnumValue
    private final String value;
    private final String label;
}
