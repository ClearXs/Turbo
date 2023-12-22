package cc.allio.turbo.extension.ob.opentelemetry;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.instrumentation.spring.autoconfigure.OpenTelemetryInjector;
import io.opentelemetry.instrumentation.spring.autoconfigure.resources.SpringResourceConfigProperties;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.OpenTelemetrySdkBuilder;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.ResourceProvider;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.SdkLoggerProviderBuilder;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.logs.export.LogRecordExporter;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.export.MetricProducer;
import io.opentelemetry.sdk.metrics.export.MetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SdkTracerProviderBuilder;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Collections;
import java.util.List;

@Configuration
@ConditionalOnMissingBean(OpenTelemetry.class)
public class OpenTelemetryAutoConfiguration {

    @Bean
    public Resource turboResource(
            Environment env, ObjectProvider<List<ResourceProvider>> resourceProviders) {
        ConfigProperties config = new SpringResourceConfigProperties(env, new SpelExpressionParser());
        Resource resource = Resource.getDefault();
        for (ResourceProvider resourceProvider :
                resourceProviders.getIfAvailable(Collections::emptyList)) {
            resource = resource.merge(resourceProvider.createResource(config));
        }
        return resource;
    }

    @Bean
    @ConditionalOnMissingBean
    public SdkLoggerProvider turboSdkLoggerProvider(ObjectProvider<List<LogRecordExporter>> logRecordExporterProviders, Resource turboResource) {
        SdkLoggerProviderBuilder builder = SdkLoggerProvider.builder();
        builder.setResource(turboResource);
        logRecordExporterProviders.ifAvailable(exporters ->
                exporters.forEach(exporter ->
                        builder.addLogRecordProcessor(BatchLogRecordProcessor.builder(exporter).setMaxQueueSize(1).build())));
        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public SdkMeterProvider turboSdkMeterProvider(ObjectProvider<List<MetricReader>> metricReaderProvider,
                                                  ObjectProvider<List<MetricProducer>> metricProducerProvider,
                                                  Resource turboResource) {
        SdkMeterProviderBuilder builder = SdkMeterProvider.builder();
        builder.setResource(turboResource);
        metricReaderProvider.ifAvailable(metricReaders ->
                metricReaders.forEach(builder::registerMetricReader));
        metricProducerProvider.ifAvailable(metricProducers ->
                metricProducers.forEach(builder::registerMetricProducer));

        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public SdkTracerProvider turboSdkTracerProvider(ObjectProvider<List<SpanExporter>> spanExporterProvider, Resource turboResource) {
        SdkTracerProviderBuilder builder = SdkTracerProvider.builder();
        builder.setResource(turboResource);
        spanExporterProvider.ifAvailable(spanExporters ->
                spanExporters.forEach(exporter ->
                        builder.addSpanProcessor(BatchSpanProcessor.builder(exporter).build())));

        return builder.build();
    }

    @Bean
    public OpenTelemetry turboOpenTelemetry(ObjectProvider<ContextPropagators> propagatorsProvider,
                                            SdkTracerProvider tracerProvider,
                                            SdkMeterProvider meterProvider,
                                            SdkLoggerProvider loggerProvider,
                                            ObjectProvider<List<OpenTelemetryInjector>> openTelemetryConsumerProvider) {
        OpenTelemetrySdkBuilder builder = OpenTelemetrySdk.builder();
        ContextPropagators contextPropagators = propagatorsProvider.getIfAvailable(() ->
                ContextPropagators.create(TextMapPropagator.composite(W3CTraceContextPropagator.getInstance(), W3CBaggagePropagator.getInstance())));
        builder.setPropagators(contextPropagators);
        builder.setLoggerProvider(loggerProvider);
        builder.setTracerProvider(tracerProvider);
        builder.setMeterProvider(meterProvider);
        OpenTelemetry openTelemetry = builder.build();
        openTelemetryConsumerProvider.ifAvailable(openTelemetryInjectors ->
                openTelemetryInjectors.forEach(openTelemetryInjector -> openTelemetryInjector.accept(openTelemetry)));
        return openTelemetry;
    }
}
