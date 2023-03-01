import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.file.*
import org.gradle.api.provider.*
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.*
import javax.inject.*

abstract class CopyPlugins @Inject constructor(
    private val fsOperations: FileSystemOperations,
    layout: ProjectLayout,
) : DefaultTask() {
    fun pluginConfigurations(pluginConfigurations: List<Configuration>) {
        pluginClasspaths.set(pluginConfigurations.map {
            PluginClasspath(
                it.name,
                it.incoming.artifactView { lenient(false) }.files
            )
        })
    }

    @get:Nested
    internal abstract val pluginClasspaths: ListProperty<PluginClasspath>

    class PluginClasspath(
        @get:Input val name: String,

        @get:InputFiles val classpath: FileCollection
    )

    @get:OutputDirectory
    val pluginsDir: Provider<Directory> = layout.buildDirectory.dir("plugins")

    @TaskAction
    fun action() {
        fsOperations.sync {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            into(pluginsDir)
            pluginClasspaths.get().forEach {
                into(it.name) {
                    from(it.classpath)
                }
            }
        }
    }
}
