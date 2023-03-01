abstract class ProducerExtension @Inject constructor(objects: ObjectFactory) {
    val features: ListProperty<String> = objects.listProperty<String>().convention(emptyList())
}

val extension = extensions.create<ProducerExtension>("producer")

dependencies {
    attributesSchema {
        attribute(Features.FEATURES_ATTRIBUTE)
    }
}

internal val myConfiguration: Configuration by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
    isVisible = false
    attributes {
        attributeProvider(Features.FEATURES_ATTRIBUTE, extension.features.map { it.joinToString(",") })
    }
    extendsFrom(configurations["default"])
}
