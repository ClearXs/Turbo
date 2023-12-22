/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package cc.allio.turbo.extension.ob.common;

import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.exporter.internal.ExporterMetrics;
import io.opentelemetry.exporter.internal.grpc.GrpcExporterUtil;
import io.opentelemetry.exporter.internal.http.HttpSender;
import io.opentelemetry.exporter.internal.marshal.Marshaler;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.internal.ThrottlingLogger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An exporter for http/protobuf or http/json using a signal-specific Marshaler.
 *
 * <p>This class is internal and is hence not for public use. Its APIs are unstable and can change
 * at any time.
 */
@SuppressWarnings("checkstyle:JavadocMethod")
public final class OpenObverseHttpExporter<T extends Marshaler> {

    private static final Logger internalLogger = Logger.getLogger(OpenObverseHttpExporter.class.getName());

    private final ThrottlingLogger logger = new ThrottlingLogger(internalLogger);
    private final AtomicBoolean isShutdown = new AtomicBoolean();

    private final String type;
    private final HttpSender httpSender;
    private final ExporterMetrics exporterMetrics;
    private final boolean exportAsJson;

    public OpenObverseHttpExporter(
            String exporterName,
            String type,
            HttpSender httpSender,
            Supplier<MeterProvider> meterProviderSupplier,
            boolean exportAsJson) {
        this.type = type;
        this.httpSender = httpSender;
        this.exporterMetrics =
                exportAsJson
                        ? ExporterMetrics.createHttpJson(exporterName, type, meterProviderSupplier)
                        : ExporterMetrics.createHttpProtobuf(exporterName, type, meterProviderSupplier);
        this.exportAsJson = exportAsJson;
    }

    public CompletableResultCode export(T exportRequest, int numItems) {
        if (isShutdown.get()) {
            return CompletableResultCode.ofFailure();
        }

        exporterMetrics.addSeen(numItems);

        CompletableResultCode result = new CompletableResultCode();

        Consumer<OutputStream> marshaler =
                os -> {
                    try {
                        if (exportAsJson) {
                            exportRequest.writeJsonTo(os);
                        } else {
                            exportRequest.writeBinaryTo(os);
                        }
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                };

        // 当启动export to json模式，导致原先log marshal size将小于序列化的json数据，故先采取倍数增长
        int binarySerializedSize = exportRequest.getBinarySerializedSize();
        if (exportAsJson) {
            binarySerializedSize = -1;
        }
        httpSender.send(
                marshaler,
                binarySerializedSize,
                httpResponse -> {
                    int statusCode = httpResponse.statusCode();

                    if (statusCode >= 200 && statusCode < 300) {
                        exporterMetrics.addSuccess(numItems);
                        result.succeed();
                        return;
                    }

                    exporterMetrics.addFailed(numItems);

                    byte[] body;
                    try {
                        body = httpResponse.responseBody();
                    } catch (IOException ex) {
                        throw new IllegalStateException(ex);
                    }

                    String status = extractErrorStatus(httpResponse.statusMessage(), body);

                    logger.log(
                            Level.WARNING,
                            "Failed to export "
                                    + type
                                    + "s. Server responded with HTTP status code "
                                    + statusCode
                                    + ". Error message: "
                                    + status);
                    result.fail();
                },
                e -> {
                    exporterMetrics.addFailed(numItems);
                    logger.log(
                            Level.SEVERE,
                            "Failed to export "
                                    + type
                                    + "s. The request could not be executed. Full error message: "
                                    + e.getMessage(),
                            e);
                    result.fail();
                });

        return result;
    }

    public CompletableResultCode shutdown() {
        if (!isShutdown.compareAndSet(false, true)) {
            logger.log(Level.INFO, "Calling shutdown() multiple times.");
            return CompletableResultCode.ofSuccess();
        }
        return httpSender.shutdown();
    }

    private static String extractErrorStatus(String statusMessage, @Nullable byte[] responseBody) {
        if (responseBody == null) {
            return "Response body missing, HTTP status message: " + statusMessage;
        }
        try {
            return GrpcExporterUtil.getStatusMessage(responseBody);
        } catch (IOException e) {
            return "Unable to parse response body, HTTP status message: " + statusMessage;
        }
    }
}
