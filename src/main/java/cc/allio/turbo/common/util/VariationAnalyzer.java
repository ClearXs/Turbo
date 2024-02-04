package cc.allio.turbo.common.util;

import cc.allio.uno.core.bean.ObjectWrapper;
import cc.allio.uno.core.bean.ValueWrapper;
import cc.allio.uno.core.function.lambda.MethodFunction;
import cc.allio.uno.core.type.Types;
import cc.allio.uno.core.util.CollectionUtils;
import com.google.common.collect.Lists;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 两个集合的差异分析
 * <ul>
 *     <li>新增：如果源中的元素不在benchmark中，就是新增的</li>
 *     <li>减少：如果benchmark中的元素不在sources里，就是减少的</li>
 *     <li>变化：当结束完上述两者操作，则剩下的元素进行比较</li>
 * </ul>
 *
 * @param <T>   用于分析的元素类型
 * @param <Key> 元素唯一标识类型
 * @author jiangwei
 * @date 2024/1/27 16:40
 * @since 0.1.0
 */
public class VariationAnalyzer<T, Key> {

    private final Collection<T> sources;
    // 作为分析的基准集合，作用是与sources进行比较
    private final Collection<T> benchmark;
    // 用于标识key的name
    private final String keyName;

    private final BiPredicate<T, T> comparator;

    /**
     * @param sources    源集合
     * @param benchmark  基准集合
     * @param getter     获取元素唯一标识的函数
     * @param comparator 比较两个元素是否变化的函数 如果为true则认为变化，否则认为不变化
     */
    public VariationAnalyzer(@NotNull Collection<T> sources, @NotNull Collection<T> benchmark, @NotNull MethodFunction<T, Key> getter, @NotNull BiPredicate<T, T> comparator) {
        this.sources = sources;
        this.benchmark = benchmark;
        this.keyName = getter.getFieldName();
        this.comparator = comparator;
    }

    /**
     * @param sources    源集合
     * @param benchmark  基准集合
     * @param keyName    keyName
     * @param comparator 比较两个元素是否变化的函数 如果为true则认为变化，否则认为不变化
     */
    public VariationAnalyzer(@NotNull Collection<T> sources, @NotNull Collection<T> benchmark, @NotNull String keyName, @NotNull BiPredicate<T, T> comparator) {
        this.sources = sources;
        this.benchmark = benchmark;
        this.keyName = keyName;
        this.comparator = comparator;
    }

    /**
     * 执行差异分析，算法以空间换时间
     * <ul>
     *     <li>基于{@link #keyName}获取{@link #sources}与{@link #benchmark}的key map</li>
     *     <li>新增、减少基于差集获取</li>
     *     <li>变化求交集通过{@link #comparator}比较获的，新增的元素以{@link #sources}进行放入</li>
     * </ul>
     *
     * @throws NullPointerException 如果给定的Key值为null则抛出
     */
    public AnalyzeResultSet<T, Key> analyze() {

        AnalyzeResultSet<T, Key> resultSet = new AnalyzeResultSet<>();

        Function<T, Key> toKey = k -> {
            if (Types.isBean(k.getClass())) {
                Object key = ObjectWrapper.getValue(k, keyName);
                if (key == ValueWrapper.EMPTY_VALUE) {
                    throw new NullPointerException("specific key value is null");
                }
                return (Key) key;
            }
            return (Key) k;
        };
        Map<Key, T> keySourceMap = sources.stream()
                .collect(Collectors.toMap(
                        toKey,
                        v -> v));
        Map<Key, T> keyBenchmarkMap = benchmark.stream()
                .collect(Collectors.toMap(
                        toKey,
                        v -> v));

        Set<Key> keysSource = keySourceMap.keySet();
        Set<Key> keyBenchmark = keyBenchmarkMap.keySet();

        // 新增
        Collection<Key> addKeys = CollectionUtils.complement(keysSource, keyBenchmark);
        for (Key addKey : addKeys) {
            Result<T, Key> result = new Result<>(addKey, keySourceMap.get(addKey), null);
            resultSet.addAddition(result);
        }

        // 删除
        Collection<Key> removeKeys = CollectionUtils.complement(keyBenchmark, keysSource);
        for (Key removeKey : removeKeys) {
            Result<T, Key> result = new Result<>(removeKey, null, keyBenchmarkMap.get(removeKey));
            resultSet.addReduced(result);
        }

        // 变化
        Collection<Key> mutualKeys = CollectionUtils.intersection(keysSource, keyBenchmark);
        for (Key mutualKey : mutualKeys) {
            T source = keySourceMap.get(mutualKey);
            T bench = keyBenchmarkMap.get(mutualKey);
            Boolean mutable = comparator.test(source, bench);
            if (Boolean.TRUE.equals(mutable)) {
                Result<T, Key> result = new Result<>(mutualKey, source, bench);
                resultSet.addMutable(result);
            }
        }

        return resultSet;
    }

    @Getter
    public static class AnalyzeResultSet<T, Key> {
        // 新增的
        private final List<Result<T, Key>> addition = Lists.newArrayList();
        // 减少的
        private final List<Result<T, Key>> reduction = Lists.newArrayList();
        // 变化的
        private final List<Result<T, Key>> mutative = Lists.newArrayList();

        void addAddition(Result<T, Key> added) {
            addition.add(added);
        }

        void addReduced(Result<T, Key> reduced) {
            reduction.add(reduced);
        }

        void addMutable(Result<T, Key> mutable) {
            mutative.add(mutable);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Result<T, Key> {
        Key key;
        T source;
        T bench;
    }
}
