/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package cc.allio.turbo.extension.ob.log;

import cc.allio.turbo.extension.ob.common.OpenObserveHttpExporterBuilder;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.exporter.internal.http.HttpExporterBuilder;
import io.opentelemetry.exporter.internal.otlp.logs.LogsRequestMarshaler;
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter;
import io.opentelemetry.exporter.otlp.internal.OtlpUserAgent;
import io.opentelemetry.sdk.common.export.RetryPolicy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static io.opentelemetry.api.internal.Utils.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Builder utility for {@link OtlpHttpLogRecordExporter}.
 *
 * @since 1.27.0
 */
public final class OpenObverseHttpLogRecordExporterBuilder {

    private static final String DEFAULT_ENDPOINT = "http://localhost:4318/v1/logs";

    private final OpenObserveHttpExporterBuilder<LogsRequestMarshaler> delegate;

    OpenObverseHttpLogRecordExporterBuilder(OpenObserveHttpExporterBuilder<LogsRequestMarshaler> delegate) {
        this.delegate = delegate;
        OtlpUserAgent.addUserAgentHeader(delegate::addConstantHeaders);
    }

    public OpenObverseHttpLogRecordExporterBuilder() {
        this(new OpenObserveHttpExporterBuilder<>("otlp", "log", DEFAULT_ENDPOINT));
    }

    /**
     * Sets the maximum time to wait for the collector to process an exported batch of logs. If unset,
     * defaults to {@value HttpExporterBuilder#DEFAULT_TIMEOUT_SECS}s.
     */
    public OpenObverseHttpLogRecordExporterBuilder setTimeout(long timeout, TimeUnit unit) {
        requireNonNull(unit, "unit");
        checkArgument(timeout >= 0, "timeout must be non-negative");
        delegate.setTimeout(timeout, unit);
        return this;
    }

    /**
     * Sets the maximum time to wait for the collector to process an exported batch of logs. If unset,
     * defaults to {@value HttpExporterBuilder#DEFAULT_TIMEOUT_SECS}s.
     */
    public OpenObverseHttpLogRecordExporterBuilder setTimeout(Duration timeout) {
        requireNonNull(timeout, "timeout");
        return setTimeout(timeout.toNanos(), TimeUnit.NANOSECONDS);
    }

    /**
     * Sets the maximum time to wait for new connections to be established. If unset, defaults to
     * {@value HttpExporterBuilder#DEFAULT_CONNECT_TIMEOUT_SECS}s.
     *
     * @since 1.33.0
     */
    public OpenObverseHttpLogRecordExporterBuilder setConnectTimeout(long timeout, TimeUnit unit) {
        requireNonNull(unit, "unit");
        checkArgument(timeout >= 0, "timeout must be non-negative");
        delegate.setConnectTimeout(timeout, unit);
        return this;
    }

    /**
     * Sets the maximum time to wait for new connections to be established. If unset, defaults to
     * {@value HttpExporterBuilder#DEFAULT_CONNECT_TIMEOUT_SECS}s.
     *
     * @since 1.33.0
     */
    public OpenObverseHttpLogRecordExporterBuilder setConnectTimeout(Duration timeout) {
        requireNonNull(timeout, "timeout");
        return setConnectTimeout(timeout.toNanos(), TimeUnit.NANOSECONDS);
    }

    /**
     * Sets the OTLP endpoint to connect to. If unset, defaults to {@value DEFAULT_ENDPOINT}. The
     * endpoint must start with either http:// or https://, and include the full HTTP path.
     */
    public OpenObverseHttpLogRecordExporterBuilder setEndpoint(String endpoint) {
        requireNonNull(endpoint, "endpoint");
        delegate.setEndpoint(endpoint);
        return this;
    }

    /**
     * Sets the method used to compress payloads. If unset, compression is disabled. Currently
     * supported compression methods include "gzip" and "none".
     */
    public OpenObverseHttpLogRecordExporterBuilder setCompression(String compressionMethod) {
        requireNonNull(compressionMethod, "compressionMethod");
        checkArgument(
                compressionMethod.equals("gzip") || compressionMethod.equals("none"),
                "Unsupported compression method. Supported compression methods include: gzip, none.");
        delegate.setCompression(compressionMethod);
        return this;
    }

    /**
     * Add a constant header to requests. If the {@code key} collides with another constant header
     * name or a one from {@link #setHeaders(Supplier)}, the values from both are included.
     */
    public OpenObverseHttpLogRecordExporterBuilder addHeader(String key, String value) {
        delegate.addConstantHeaders(key, value);
        return this;
    }

    /**
     * Set the supplier of headers to add to requests. If a key from the map collides with a constant
     * from {@link #addHeader(String, String)}, the values from both are included.
     *
     * @since 1.33.0
     */
    public OpenObverseHttpLogRecordExporterBuilder setHeaders(Supplier<Map<String, String>> headerSupplier) {
        delegate.setHeadersSupplier(headerSupplier);
        return this;
    }

    /**
     * Sets the certificate chain to use for verifying servers when TLS is enabled. The {@code byte[]}
     * should contain an X.509 certificate collection in PEM format. If not set, TLS connections will
     * use the system default trusted certificates.
     */
    public OpenObverseHttpLogRecordExporterBuilder setTrustedCertificates(byte[] trustedCertificatesPem) {
        delegate.setTrustManagerFromCerts(trustedCertificatesPem);
        return this;
    }

    /**
     * Sets ths client key and the certificate chain to use for verifying client when TLS is enabled.
     * The key must be PKCS8, and both must be in PEM format.
     */
    public OpenObverseHttpLogRecordExporterBuilder setClientTls(
            byte[] privateKeyPem, byte[] certificatePem) {
        delegate.setKeyManagerFromCerts(privateKeyPem, certificatePem);
        return this;
    }

    /**
     * Sets the "bring-your-own" SSLContext for use with TLS. Users should call this _or_ set raw
     * certificate bytes, but not both.
     */
    public OpenObverseHttpLogRecordExporterBuilder setSslContext(
            SSLContext sslContext, X509TrustManager trustManager) {
        delegate.setSslContext(sslContext, trustManager);
        return this;
    }

    /**
     * Ses the retry policy. Retry is disabled by default.
     *
     * @since 1.28.0
     */
    public OpenObverseHttpLogRecordExporterBuilder setRetryPolicy(RetryPolicy retryPolicy) {
        requireNonNull(retryPolicy, "retryPolicy");
        delegate.setRetryPolicy(retryPolicy);
        return this;
    }

    /**
     * Sets the {@link MeterProvider} to use to collect metrics related to export. If not set, uses
     * {@link GlobalOpenTelemetry#getMeterProvider()}.
     */
    public OpenObverseHttpLogRecordExporterBuilder setMeterProvider(MeterProvider meterProvider) {
        requireNonNull(meterProvider, "meterProvider");
        setMeterProvider(() -> meterProvider);
        return this;
    }

    /**
     * Sets the {@link MeterProvider} supplier used to collect metrics related to export. If not set,
     * uses {@link GlobalOpenTelemetry#getMeterProvider()}.
     *
     * @since 1.32.0
     */
    public OpenObverseHttpLogRecordExporterBuilder setMeterProvider(
            Supplier<MeterProvider> meterProviderSupplier) {
        requireNonNull(meterProviderSupplier, "meterProviderSupplier");
        delegate.setMeterProvider(meterProviderSupplier);
        return this;
    }

    public OpenObverseHttpLogRecordExporterBuilder writeJson() {
        delegate.exportAsJson();
        return this;
    }

    /**
     * Constructs a new instance of the exporter based on the builder's values.
     *
     * @return a new exporter's instance
     */
    public OpenObserveHttpLogRecordExporter build() {
        return new OpenObserveHttpLogRecordExporter(delegate, delegate.build());
    }
}
