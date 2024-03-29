package cc.allio.turbo.modules.message.runtime;

import cc.allio.uno.core.metadata.endpoint.DefaultEndpoint;
import cc.allio.uno.core.metadata.endpoint.source.SourceCollector;
import cc.allio.uno.core.metadata.endpoint.source.SourceConverter;

/**
 * 消息接入端点
 *
 * @author j.x
 * @date 2024/3/29 00:25
 * @since 0.1.1
 */
public class MessageEndpoint extends DefaultEndpoint<ReceiveMetadata> {

	public MessageEndpoint(SourceConverter<ReceiveMetadata> converter, SourceCollector<ReceiveMetadata> collector) {
		setConverter(converter);
		setCollector(collector);
	}
}
