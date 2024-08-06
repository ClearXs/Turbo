package cc.allio.turbo.modules.office.configuration.properties;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("turbo.office.document")
public class DocumentProperties {

    /**
     * document server url
     */
    private String documentServerUrl = "http://localhost:8228";

    /**
     * onlyoffice document server url
     */
    private String onlyofficeServerUrl = "http://localhost:8890";

    /**
     * onlyoffice 相关设置
     */
    private Docservice docservice;

    /**
     * onlyoffice 编辑器个性化配置
     */
    private Customization customization;

    /**
     * logo information
     */
    private Logo logo;

    /**
     * document mention
     */
    private List<Mention> mentions = Lists.newArrayList();

    /**
     * storage
     */
    private Storage storage;

    @Data
    public static class Custom {

        /**
         * index url
         */
        private String index = "/index";

        /**
         * 创建地址
         */
        private String create = "/file/create";

        /**
         * 转换地址
         */
        private String converter = "/file/converter";

        /**
         * 编辑器地址
         */
        private String editor = "/editor";

        /**
         * 回调地址
         */
        private String track = "/callback/track";

        /**
         * 文档下载地址
         */
        private String download = "/file/download";
    }


    @Data
    public static class Logo {

        /**
         * image地址
         */
        private String image = "";

        /**
         * image内嵌地址
         */
        private String imageEmbedded = "";

        /**
         * logo url
         */
        private String url = "";
    }

    @Data
    public static class Customization {

        /**
         *  if the Autosave menu option is enabled or disabled
         */
        private Boolean autosave = true;

        /**
         * if the Comments menu button is displayed or hidden
         */
        private Boolean comments = true;

        /**
         * if the additional action buttons are displayed
         *     in the upper part of the editor window header next to the logo (false) or in the toolbar (true)
         */
        private Boolean compactHeader = false;

        /**
         * if the top toolbar type displayed is full (false) or compact (true)
         */
        private Boolean compactToolbar = false;

        /**
         * the use of functionality only compatible with the OOXML format
         */
        private Boolean compatibleFeatures = false;

        /**
         * add the request for the forced file saving to the callback handler,when saving the document within the document editing service
         *
         */
        private Boolean forcesave = false;

        /**
         * if the Help menu button is displayed or hidden
         */
        private Boolean help = false;

        /**
         * if the right menu is displayed or hidden on first loading
         */
        private Boolean hideRightMenu = false;

        /**
         * if the editor rulers are displayed or hidden
         */
        private Boolean hideRulers = false;

        /**
         * if the Submit form button is displayed or hidden
         */
        private Boolean submitForm = false;
        private Boolean about = false;
        private Boolean feedback = false;
    }
}
