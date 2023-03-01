import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.attributes.*
import org.gradle.api.attributes.java.*
import org.gradle.api.model.*
import org.gradle.api.provider.*
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.*
import org.gradle.kotlin.dsl.named
import javax.inject.*


abstract class BundleExtension @Inject constructor(objects: ObjectFactory) {
    val includedFeatures: ListProperty<String> = objects.listProperty<String>().convention(emptyList<String>())
}

abstract class ConsumerExtension @Inject constructor(private val project: Project, private val objects: ObjectFactory) {
    private val plugins = mutableListOf<String>()

    fun bundle(configure: BundleExtension.() -> Unit) {
        val bundle = objects.newInstance(BundleExtension::class)
        bundle.configure()

        val pluginsConfigurations: List<Configuration> = plugins.map {
            val libName = it.split(":").last()
            val configuration = createConfiguration(
                libName,
                bundle.includedFeatures.map { it.joinToString(",") })
            project.dependencies {
                configuration(project(it, configuration = "myConfiguration"))
            }
            configuration
        }

        project.tasks.register<CopyPlugins>("copyLibs") {
            pluginConfigurations(pluginsConfigurations)
        }
    }

    private fun createConfiguration(name: String, includedFeatures: Provider<out String>) =
        project.configurations.create(name) {
            isCanBeConsumed = false
            isCanBeResolved = true
            isVisible = false
            attributes {
                attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
                attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
                attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
                attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, JavaVersion.current().majorVersion.toInt())
                attribute(Features.FEATURES_ATTRIBUTE, includedFeatures.get())
            }
        }

    fun plugin(path: String) {
        plugins.add(path)
    }
}
