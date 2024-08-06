package cc.allio.turbo.modules.office.configuration.properties;

import lombok.Data;

@Data
public class Docservice {

    /**
     * 请求onlyoffice密钥
     */
    private String secret = "ClearXs";

    /**
     * 验证头部
     */
    private String header = "Authorization";

    private Boolean verifyPeerOff = true;

    /**
     * 支持口语语言
     */
    private String language = "en:English|hy:Armenian|az:Azerbaijani|eu:Basque|be:Belarusian|bg:Bulgarian|ca:Catalan|zh:Chinese (People's Republic of China)|zh-TW:Chinese (Traditional, Taiwan)|cs:Czech|da:Danish|nl:Dutch|fi:Finnish|fr:French|gl:Galego|de:German|el:Greek|hu:Hungarian|id:Indonesian|it:Italian|ja:Japanese|ko:Korean|lv:Latvian|lo:Lao|ms:Malay (Malaysia)|nb:Norwegian|pl:Polish|pt:Portuguese (Brazil)|pt-PT:Portuguese (Portugal)|ro:Romanian|ru:Russian|sk:Slovak|sl:Slovenian|es:Spanish|sv:Swedish|tr:Turkish|uk:Ukrainian|vi:Vietnamese|aa-AA:Test Language";

    /**
     * 支持fillform的文件格式
     */
    private String fillformsDocs = ".oform|.docx";

    /**
     * 支持预览的文件格式
     */
    private String viewedDocs = ".pdf|.djvu|.xps|.oxps";

    /**
     * 支持编辑的文件格式
     */
    private String editedDocs = ".docx|.xlsx|.csv|.pptx|.txt|.docxf";

    /**
     * 支持转换的文件格式
     */
    private String convertDocs = ".docm|.dotx|.dotm|.dot|.doc|.odt|.fodt|.ott|.xlsm|.xlsb|.xltx|.xltm|.xlt|.xls|.ods|.fods|.ots|.pptm|.ppt|.ppsx|.ppsm|.pps|.potx|.potm|.pot|.odp|.fodp|.otp|.rtf|.mht|.html|.htm|.xml|.epub|.fb2";

    /**
     * 连接超时时间
     */
    private Long timeout = 120000L;

    private Url url;

    @Data
    public static class Url {

        /**
         * onlyoffice url
         */
        private String site = "http://localhost:8890/";

        private String converter = "ConvertService.ashx";

        private String command = "coauthoring/CommandService.ashx";

        /**
         * onlyoffice api address
         */
        private String api = "/web-apps/apps/api/documents/api.js";

        private String preloader = "/web-apps/apps/api/documents/cache-scripts.html";

        /**
         * 当前服务地址或者能够访问当前服务的地址
         */
        private String example = "http://localhost:8228";
    }

    @Data
    public static class History {

        private String postfix = "hist";
    }
}
