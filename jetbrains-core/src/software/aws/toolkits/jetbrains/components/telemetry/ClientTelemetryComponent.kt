package software.aws.toolkits.jetbrains.components.telemetry

import com.intellij.openapi.components.ApplicationComponent
import software.amazon.awssdk.http.apache.ApacheHttpClient
import software.amazon.awssdk.services.toolkittelemetry.ToolkitTelemetryClient
import software.aws.toolkits.core.telemetry.BatchingMetricsPublisher
import software.aws.toolkits.core.telemetry.ClientTelemetryPublisher
import software.aws.toolkits.core.telemetry.MetricUnit
import java.time.Duration
import java.time.ZonedDateTime

interface ClientTelemetryComponent : ApplicationComponent

class DefaultClientTelemetryComponent() : ClientTelemetryComponent {
    private lateinit var publisher: BatchingMetricsPublisher
    private lateinit var startupTime: ZonedDateTime


    override fun initComponent() {
        super.initComponent()

        publisher = BatchingMetricsPublisher(ClientTelemetryPublisher(
            "productName",
            "productVersion",
            "clientId",
            ToolkitTelemetryClient
                    .builder()
                    // TODO: Determine why this client is not picked up by default.
                    .httpClient(ApacheHttpClient.builder().build())
                    .build()

        ))

        publisher.newMetric("ToolkitStart").use {
            startupTime = it.createTime
            publisher.publishMetric(it)
        }
    }

    override fun disposeComponent() {
        try {
            publisher.newMetric("ToolkitEnd").use {
                val duration = Duration.between(startupTime, it.createTime)
                it.addMetricEntry("duration", duration.toMillis().toDouble(), MetricUnit.MILLISECONDS)
                publisher.publishMetric(it)
            }

        } finally {
            publisher.shutdown()
            super.disposeComponent()
        }
    }

    override fun getComponentName(): String = javaClass.simpleName
}
