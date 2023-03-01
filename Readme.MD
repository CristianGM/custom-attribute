A consumer requires producer to have some "features"

running
`./gradlew :consumer:copyLibs --dry-run`

if `:producer` doesn't include the `features` attribute
should fail because it `:producer` doesn't match AttributeCompatibilityRule for the required attributes, but...
the Rule is not called and the `:producer` is included as dependency

just comment the line
`attributeProvider(Features.FEATURES_ATTRIBUTE, extension.features.map { it.joinToString(",") })`
in `conventions/src/main/kotlin/producer.gradle.kts` to make it reproduce the issue

There is a lof in the `AttributeCompatibilityRule` to show it's not even called


The example is probably overcomplicated, I used real code from a complex feature and simplified it.