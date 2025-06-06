package cc.allio.turbo.extension.oss;

import cc.allio.uno.core.StringPool;
import cc.allio.uno.core.util.DateUtil;
import cc.allio.uno.core.util.StringUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * describe filepath
 *
 * @author j.x
 * @date 2024/6/23 16:54
 * @since 0.1.1
 */
@ToString
public class Path {

    @Getter
    private int depth;
    @Getter
    private final String fullPath;
    private List<String> nameList;
    private final String separator;
    private final AppendStrategy strategy;

    public Path(String fullPath, String separator, AppendStrategy strategy) {
        this.depth = 0;
        this.fullPath = fullPath;
        this.nameList = Lists.newArrayList();
        this.separator = separator;
        this.strategy = strategy;
    }

    /**
     * append new name to path
     *
     * @param name the name
     * @return {@link Path} instance
     */
    public Path append(String name) {
        return append(from(fullPath, name, AppendStrategy.None));
    }

    /**
     * batch append name to path
     *
     * @param nameList the name list
     * @return current {@link Path}
     */
    public Path append(List<String> nameList) {
        this.depth += nameList.size();
        for (String name : nameList) {
            this.append(from(fullPath, name, AppendStrategy.None));
        }
        return this;
    }

    /**
     * append other path to current
     *
     * @param path other path
     * @return current {@link Path}
     */
    public Path append(Path path) {
        this.depth += path.depth;
        this.nameList.addAll(path.nameList);
        return this;
    }

    /**
     * append other path to first
     *
     * @param path the other {@link Path}
     * @return current {@link Path}
     */
    public Path appendFirst(Path path) {
        this.depth += path.depth;
        List<String> nameList = Lists.newArrayList();
        nameList.addAll(path.nameList);
        nameList.addAll(this.nameList);
        this.nameList = nameList;
        return this;
    }

    /**
     * append other path to last
     *
     * @param path the other path
     * @return current {@link Path}
     */
    public Path appendLast(Path path) {
        this.depth += path.depth;
        List<String> nameList = Lists.newArrayList();
        nameList.addAll(this.nameList);
        nameList.addAll(path.nameList);
        this.nameList = nameList;
        return this;
    }

    /**
     * hybrid {@link AppendStrategy} return newly path of string
     *
     * @return combine path
     */
    public Path withStrategy() {
        if (strategy == AppendStrategy.Date) {
            String nowPart = DateUtil.getNowPart("/yyyy/MM/dd");
            this.appendFirst(Path.from(fullPath, nowPart, strategy));
        }
        return this;
    }

    public void doAppend(String name) {
        if (StringUtils.isNotBlank(name)) {
            this.nameList.add(name);
        }
    }

    /**
     * base on {@link #separator} combine all append name
     *
     * @return combine path
     */
    public String compose() {
        return String.join(separator, this.nameList);
    }

    /**
     * determine the parameter name is {@link Path}
     *
     * @param name the name
     * @return true if {@link Path}
     */
    public boolean isPath(String name) {
        return name.contains(separator);
    }

    /**
     * @see #from(String, String, String, AppendStrategy)
     */
    public static Path from(String name) {
        return from(name, name, StringPool.SLASH, AppendStrategy.None);
    }

    /**
     * @see #from(String, String, String, AppendStrategy)
     */
    public static Path from(String name, AppendStrategy strategy) {
        return from(name, name, StringPool.SLASH, strategy);
    }

    /**
     * @see #from(String, String, String, AppendStrategy)
     */
    public static Path from(String fullPath, String name) {
        return from(fullPath, name, StringPool.SLASH, AppendStrategy.None);
    }

    /**
     * @see #from(String, String, String, AppendStrategy)
     */
    public static Path from(String fullPath, String name, AppendStrategy strategy) {
        return from(fullPath, name, StringPool.SLASH, strategy);
    }

    /**
     * from the parameter name and separator build new {@link Path} instance
     *
     * @param name      the name
     * @param separator the separator name
     * @return the {@link Path} instance
     */
    public static Path from(String fullPath, String name, String separator, AppendStrategy strategy) {
        Path path = new Path(fullPath, separator, strategy);
        if (path.isPath(name)) {
            String[] nameList = name.split(path.separator);
            path.depth += nameList.length;
            for (String nameOfSeparator : nameList) {
                path.doAppend(nameOfSeparator);
            }
        } else {
            path.doAppend(name);
            path.depth++;
        }
        return path;
    }

    @AllArgsConstructor
    @Getter
    public enum AppendStrategy {
        None("none", "none"),
        Date("date", "date");

        private final String value;
        private final String label;
    }
}
