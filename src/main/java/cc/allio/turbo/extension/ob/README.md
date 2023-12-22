ob = observability 即可观测性

通过把可观测性相关的内容提取出来，是为了能够有更好的拓展，适配更多的APM系统。目前该工程下面包含以下包：

- common: 通用依赖内容，如远程调用提供
- log: 日志有关的内容，有集成于openobserve
- metrics: application measurement collector for turbo，有集成于prometheus
- traces: application track，有集成于zipkin
- opentelemetry: 集成opentelemetry