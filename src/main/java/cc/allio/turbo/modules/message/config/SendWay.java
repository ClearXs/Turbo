package cc.allio.turbo.modules.message.config;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SendWay {

	SYSTEM("SYSTEM", "系统消息"),
	SMS("SMS", "短消息"),
	DINGDING("DINGTALK", "钉钉");

	@JsonValue
	private final String value;
	private final String label;
}
