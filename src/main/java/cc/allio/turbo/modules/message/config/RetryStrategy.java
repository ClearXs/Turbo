package cc.allio.turbo.modules.message.config;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RetryStrategy {

	IGNORE("IGNORE", "忽略"),
	EXECUTE_IMMEDIATELY("EXECUTE_IMMEDIATELY", "立即执行"),
	DELAYED_EXECUTION("DELAYED_EXECUTION", "延迟执行");

	@JsonValue
	private final String value;
	private final String label;
}


