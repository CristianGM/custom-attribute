import org.gradle.kotlin.dsl.*

dependencies {
    attributesSchema {
        attribute(Features.FEATURES_ATTRIBUTE) {
            compatibilityRules.add(Features.IncludeFeaturesRule::class.java)
        }
    }
}

val extension = extensions.create<ConsumerExtension>("consumer")

