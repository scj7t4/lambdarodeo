package lambda.rodeo.gradle.plugin


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet

class LambdaRodeoGradlePlugin implements Plugin<Project> {
  @Override
  void apply(Project project) {
    project.getPluginManager().apply(JavaPlugin.class);
    def sourceSet = project.getExtensions().findByType(SourceSet.class);
    sourceSet.extensions.create('lambdarodeo', LambdaRodeoSourceSetExtension.class)
    System.out.println()
  }
}
