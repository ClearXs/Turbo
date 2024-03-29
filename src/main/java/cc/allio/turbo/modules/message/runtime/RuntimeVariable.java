package cc.allio.turbo.modules.message.runtime;

import cc.allio.turbo.modules.message.template.Variable;
import cc.allio.uno.core.util.JsonUtils;
import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 运行时消息变量
 *
 * @author j.x
 * @date 2024/3/28 23:58
 * @since 0.1.1
 */
@Getter
public class RuntimeVariable {

    /**
     * 默认配置的消息配置列表
     * -- GETTER --
     *  获取运行时消息列表
     *
     * @return 变量

     */
    private final Map<String, Object> variables;

    public RuntimeVariable(List<Variable> variables) {
        this(variables, Maps.newHashMap());
    }

    public RuntimeVariable(Map<String, Object> runtimeVariables) {
        this(Collections.emptyList(), runtimeVariables);
    }

    public RuntimeVariable(List<Variable> variables, Map<String, Object> runtimeVariables) {
        variables.stream()
                .collect(Collectors.toMap(Variable::getKey, Variable::getDefaultValue))
                .forEach(runtimeVariables::putIfAbsent);
        this.variables = runtimeVariables;
    }

    /**
     * 获取运行时变量Json数据
     *
     * @return Json变量数据
     */
    public String getJsonVariables() {
        return JsonUtils.toJson(variables);
    }

    /**
     * 追加变量
     *
     * @param key key
     * @param val val
     * @return RuntimeVariable
     */
    public RuntimeVariable append(String key, Object val) {
        variables.put(key, val);
        return this;
    }

    /**
     * 追加变量
     *
     * @param variable variable
     * @return RuntimeVariable
     */
    public RuntimeVariable append(Variable variable) {
        return append(variable.getKey(), variable.getDefaultValue());
    }

    /**
     * 追加全部
     *
     * @param vals 变量
     * @return RuntimeVariable
     */
    public RuntimeVariable appendAll(Map<String, Object> vals) {
        variables.putAll(vals);
        return this;
    }

    /**
     * 获取运行时变量值
     *
     * @param key 变量Key
     * @return val
     */
    public Object get(String key) {
        return variables.get(key);
    }
}
