import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.zip.ZipFile


private const val ICON_NAME = "murglar_icon.xml"

abstract class CopyPluginIconTask : DefaultTask() {

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun run() {
        val outputFile = File(outputDir.get().asFile.resolve("drawable").apply { mkdirs() }, ICON_NAME)

        copyFromSubprojects(outputFile)
            ?: copyFromDependencies(outputFile)
            ?: throw GradleException("Plugin icon not found in any subproject or dependency!")
    }

    private fun copyFromSubprojects(outputFile: File) = project.rootProject.subprojects
        .map { it.layout.projectDirectory.file("src/main/resources/$ICON_NAME").asFile }
        .firstOrNull { it.exists() }
        ?.also {
            it.copyTo(outputFile, overwrite = true)
            println("Copied $ICON_NAME from ${it.absolutePath}")
        }

    private fun copyFromDependencies(outputFile: File) = project.configurations
        .filter { it.name.endsWith("releaseCompileClasspath") }
        .flatMap { it.files }
        .filter { it.name.endsWith(".jar") }
        .firstNotNullOfOrNull {
            ZipFile(it).use { jar ->
                jar.getEntry(ICON_NAME)?.let { entry ->
                    jar.getInputStream(entry).use { input ->
                        outputFile.outputStream().use(input::copyTo)
                        println("Copied $ICON_NAME from ${it.absolutePath}")
                    }
                    it
                }
            }
        }
}