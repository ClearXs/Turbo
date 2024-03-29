package cc.allio.turbo.modules.message.runtime.sender;

import cc.allio.turbo.modules.message.config.SendWay;
import cc.allio.turbo.modules.message.config.Template;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 组合发送
 *
 * @author jiangwei
 * @date 2022/12/6 11:37
 * @since 2.9.0-RELEASE
 */
public class Senders implements ApplicationContextAware {

    private static Map<String, Network> networks;

    public static Sender create(Template template) {
        SendWay sendWay = template.getSendWay();
        if (Objects.requireNonNull(sendWay) == SendWay.SYSTEM) {
            return new SystemSender(Arrays.stream(template.getProtocols()).map(protocol -> networks.get(protocol.getValue())).toArray(Network[]::new));
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
