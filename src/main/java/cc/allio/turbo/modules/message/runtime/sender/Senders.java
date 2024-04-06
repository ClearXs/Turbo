package cc.allio.turbo.modules.message.runtime.sender;

import cc.allio.turbo.modules.message.config.Protocol;
import cc.allio.turbo.modules.message.config.SendWay;
import cc.allio.turbo.modules.message.config.Template;
import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 组合发送
 *
 * @author j.x
 * @date 2024/4/5 04:56
 * @since 1.1.8
 */
public class Senders implements ApplicationContextAware {

    private static Map<String, Network> networks = Maps.newHashMap();

    /**
     * create {@link Sender} instance base on {@link Template}. it will be use {@link Protocol} build {@link Network} instance,
     * decide how to send publish message in network, like as TCP, UDP, MQTT etc...
     *
     * @param template the template
     * @return the {@link Sender} instance or null
     */
    public static Sender create(Template template) {
        SendWay sendWay = template.getSendWay();
        Protocol[] protocols = template.getProtocols();
        Network[] executableNetworks =
                Optional.ofNullable(protocols)
                        .map(pros -> Arrays.stream(pros).map(p -> networks.get(p.getValue())).toArray(Network[]::new))
                        .orElse(new Network[]{});
        if (SendWay.SYSTEM == sendWay) {
            return new SystemSender(executableNetworks);
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Senders.networks =
                applicationContext.getBeansOfType(Network.class)
                        .values()
                        .stream()
                        .collect(Collectors.toMap(Network::getProtocol, network -> network));
    }
}
