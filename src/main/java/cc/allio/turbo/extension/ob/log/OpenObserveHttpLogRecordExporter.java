package cc.allio.turbo.extension.ob.log;

import cc.allio.turbo.extension.ob.common.OpenObserveHttpExporterBuilder;
import cc.allio.turbo.extension.ob.common.OpenObverseHttpExporter;
import io.opentelemetry.exporter.internal.otlp.logs.LogsRequestMarshaler;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.logs.data.LogRecordData;
import io.opentelemetry.sdk.logs.export.LogRecordExporter;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;

/**
 * Exports logs using OTLP via HTTP, using OpenTelemetry's protobuf model.
 *
 * @since 1.27.0
 */
@ThreadSafe
public final class OpenObserveHttpLogRecordExporter implements LogRecordExporter {

    private final OpenObserveHttpExporterBuilder<LogsRequestMarshaler> builder;
    private final OpenObverseHttpExporter<LogsRequestMarshaler> delegate;

    OpenObserveHttpLogRecordExporter(
            OpenObserveHttpExporterBuilder<LogsRequestMarshaler> builder,
            OpenObverseHttpExporter<LogsRequestMarshaler> delegate) {
        this.builder = builder;
        this.delegate = delegate;
    }

    /**
     * Submits all the given logs in a single batch to the OpenTelemetry collector.
     *
     * @param logs the list of sampled Logs to be exported.
     * @return the result of the operation
     */
    @Override
    public CompletableResultCode export(Collection<LogRecordData> logs) {
        LogsRequestMarshaler exportRequest = LogsRequestMarshaler.create(logs);
        return delegate.export(exportRequest, logs.size());
    }

    @Override
    public CompletableResultCode flush() {
        return CompletableResultCode.ofSuccess();
    }

    /**
     * Shutdown the exporter.
     */
    @Override
    public CompletableResultCode shutdown() {
        return delegate.shutdown();
    }

    @Override
    public String toString() {
        return "OtlpHttpLogRecordExporter{" + builder.toString(false) + "}";
    }
}
