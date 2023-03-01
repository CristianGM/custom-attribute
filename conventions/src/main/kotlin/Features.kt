import org.gradle.api.attributes.*

interface Features {
    companion object {
        val FEATURES_ATTRIBUTE = Attribute.of("features", String::class.java)
    }

    abstract class IncludeFeaturesRule : AttributeCompatibilityRule<String> {
        override fun execute(details: CompatibilityCheckDetails<String>) = details.run {
            val pluginFeatures = producerValue?.split(",")
            val includedFeatures = consumerValue?.split(",")?.filter { it.isNotEmpty() }

            println("""
                IncludeFeaturesRule
                    producer features:$pluginFeatures
                    included features:$includedFeatures
                """.trimIndent()
            )
            if (pluginFeatures?.all { includedFeatures?.contains(it) == true } == true) {
                compatible()
            } else {
                incompatible()
            }
        }
    }
}
