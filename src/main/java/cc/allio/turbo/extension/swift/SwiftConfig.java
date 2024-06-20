package cc.allio.turbo.extension.swift;

import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwiftConfig {

    /**
     * 流水号模版
     * 每个变量用 {...} 括起来。自带可用变量有：
     * {yyyy}：年，如2015
     * {yy}：短年，如15
     * {MM}：月：如12、01、02
     * {M}：短月：如1、2、12
     * {dd}：日：如01、02、30
     * {d}：短日：如1、2、30
     * {NO}：流水号，位数由swiftNumberLen决定，不足位数会补零
     * {no}：流水号，不补零
     * {RN}：随机数，位数由参数 randomNumberLen 决定。
     * 其它参数随意设置，对应参数值由传入的 params 设置。
     */
    @Getter
    @Setter
    private String tpl;
    //扩展的参数
    @Getter
    @Setter
    private Map<String, String> params = new HashMap<>();
    //本身的参数
    private final Map<String, String> selfParams = new HashMap<>();

    @Getter
    @Setter
    private SwiftGenType swiftGenType = SwiftGenType.ALWAYS;
    @Getter
    private int swiftNumberLen = 5;
    @Getter
    private int randomNumberLen = 5;
    //初始值
    @Getter
    private int initNumber = 1;
    //步长
    @Getter
    private int step = 1;
    //最大步长
    private static final int MAX_STEP = 99;
    //加步长或者减步长
    @Getter
    @Setter
    private ChangeType changeType = ChangeType.INCR;
    //别名
    @Getter
    @Setter
    private String alias = "";

    public boolean isDataPass() {
        if (StringUtils.isEmpty(tpl))
            return false;
        if (tpl.contains("{NO}") && tpl.contains("{no}")) {
            return false;
        } else if (!tpl.contains("{NO}") && !tpl.contains("{no}")) {
            return false;
        } else {
            return !tpl.contains("{RN}") || (randomNumberLen >= 0);
        }
    }

    public Map<String, String> getAllParams() {
        Map<String, String> allParams = new HashMap<>();
        allParams.putAll(params);
        allParams.putAll(getDefaultParams());
        allParams.putAll(selfParams);
        allParams.put("RN", getRandomNumber());
        return allParams;
    }

    public void setSwiftNumberLen(int swiftNumberLen) {
        if (swiftNumberLen > 0) {
            this.swiftNumberLen = swiftNumberLen;
        }
    }

    public void setRandomNumberLen(int randomNumberLen) {
        if (randomNumberLen > 0) {
            this.randomNumberLen = randomNumberLen;
        }
    }

    public String getCacheKey() {
        if (swiftGenType.equals(SwiftGenType.ALWAYS)) {
            return getAlwaysKey();
        } else if (swiftGenType.equals(SwiftGenType.YEAR)) {
            return getYearKey();
        } else if (swiftGenType.equals(SwiftGenType.MONTH)) {
            return getMonthKey();
        } else {
            return getDayKey();
        }
    }

    public Duration getCacheDays() {
        if (swiftGenType.equals(SwiftGenType.YEAR)) {
            return Duration.ofDays(367);
        } else if (swiftGenType.equals(SwiftGenType.MONTH)) {
            return Duration.ofDays(32);
        } else {
            return Duration.ofDays(2);
        }
    }

    public void setIdToParams(String id) {
        String idAppends = id;
        if (tpl.contains("{NO}")) {
            if (id.length() < swiftNumberLen) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < swiftNumberLen - id.length(); i++) {
                    sb.append("0");
                }
                idAppends = sb + id;
            }
            selfParams.put("NO", idAppends);
        } else {
            selfParams.put("no", idAppends);
        }
    }

    public void setInitNumber(int initNumber) {
        if (initNumber > 0) {
            this.initNumber = initNumber;
        } else {
            this.initNumber = 1;
        }
    }

    public void setStep(int step) {
        if (step > 0 && step <= MAX_STEP) {
            this.step = step;
        } else if (step > MAX_STEP) {
            this.step = MAX_STEP;
        } else {
            this.step = 1;
        }
    }

    private String getRandomNumber() {
        int baseNumber = Double.valueOf(Math.pow(10, randomNumberLen - 1)).intValue();
        int max = baseNumber * 9;
        Random random = new Random();
        int randomNumber = baseNumber + random.nextInt(max);
        return String.valueOf(randomNumber);
    }

    public String getAlwaysKey() {
        return replaceByMap(tpl, params);
    }

    private String getYearKey() {
        final String yyyy = DateUtil.getNowY();
        final String yy = DateUtil.getNowPart("yy");
        Map<String, String> defaultParams = new HashMap<>();
        defaultParams.put("yyyy", yyyy);
        defaultParams.put("yy", yy);
        defaultParams.putAll(params);
        return replaceByMap(tpl, defaultParams);
    }

    private String getMonthKey() {
        final String yyyy = DateUtil.getNowY();
        final String yy = DateUtil.getNowPart("yy");
        final String MM = DateUtil.getNowPart("MM");
        final String M = DateUtil.getNowPart("M");
        Map<String, String> defaultParams = new HashMap<>();
        defaultParams.put("yyyy", yyyy);
        defaultParams.put("yy", yy);
        defaultParams.put("MM", MM);
        defaultParams.put("M", M);
        defaultParams.putAll(params);
        return replaceByMap(tpl, defaultParams);
    }

    private String getDayKey() {
        Map<String, String> defaultParams = getDefaultParams();
        defaultParams.putAll(params);
        return replaceByMap(tpl, defaultParams);
    }

    public String getExtend() {
        String cacheKey = getCacheKey();
        return replaceByMap(cacheKey, selfParams);
    }

    private Map<String, String> getDefaultParams() {
        final String yyyy = DateUtil.getNowY();
        final String yy = DateUtil.getNowPart("yy");
        final String MM = DateUtil.getNowPart("MM");
        final String M = DateUtil.getNowPart("M");
        final String dd = DateUtil.getNowPart("dd");
        final String d = DateUtil.getNowPart("d");
        Map<String, String> defaultParams = new HashMap<>();
        defaultParams.put("yyyy", yyyy);
        defaultParams.put("yy", yy);
        defaultParams.put("MM", MM);
        defaultParams.put("M", M);
        defaultParams.put("dd", dd);
        defaultParams.put("d", d);
        return defaultParams;
    }

    public static String replaceByMap(String var0, Map<String, String> params) {
        return replaceByMap(var0, params, "\\{(.*?)\\}");
    }

    public static String replaceByMap(String tpl, Map<String, String> params, String pattern) {
        Matcher var5 = Pattern.compile(pattern).matcher(tpl);
        while (var5.find()) {
            String var3 = var5.group(1);
            String var4 = var5.group(0);
            if ((var3 = params.get(var3)) != null) {
                tpl = tpl.replace(var4, var3);
            }
        }
        return tpl;
    }
}
