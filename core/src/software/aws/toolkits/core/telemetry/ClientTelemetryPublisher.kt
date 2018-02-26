// Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: Apache-2.0

package software.aws.toolkits.core.telemetry

import software.amazon.awssdk.services.toolkittelemetry.ToolkitTelemetryClient
import software.amazon.awssdk.services.toolkittelemetry.model.MetadataEntry
import software.aws.toolkits.core.utils.getLogger

class ClientTelemetryPublisher(
    private val productName: String,
    private val productVersion: String,
    private val clientId: String,
    private val client: ToolkitTelemetryClient) : MetricsPublisher {
    override fun publishMetrics(metrics: Collection<Metric>): Boolean = try {
        client.postMetrics {
            it.awsProduct(productName)
            it.awsProductVersion(productVersion)
            it.clientID(clientId)
//            it.metricData(metrics.toMetricData())
        }
        true
    } catch (e: Exception) {
        LOG.warn("Failed to publish metrics", e)
        false
    }

    override fun shutdown() { }

    private fun MetadataEntry.build(key: String, value: String) = MetadataEntry.builder()
        .key(key)
        .value(value)
        .build()

//    private fun Collection<Metric>.toMetricData(): Any {
//        this.stream()
//            .map { MetricDatum.builder()
//                .metricName(it.)}
//    }

    private companion object {
        private val LOG = getLogger<ClientTelemetryPublisher>()
    }
}
