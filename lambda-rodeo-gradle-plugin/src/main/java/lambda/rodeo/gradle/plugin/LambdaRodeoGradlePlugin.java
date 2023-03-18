package lambda.rodeo.gradle.plugin;


import org.codehaus.groovy.runtime.InvokerHelper;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.plugins.Convention;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

import java.io.File;

class LambdaRodeoGradlePlugin implements Plugin<Project> {
  @Override
  public void apply(Project project) {
    project.getPluginManager().apply(JavaPlugin.class);
    LambdaRodeoExtension ext = project.getExtensions().create(
            "lambdaRodeo",
            LambdaRodeoExtension.class,
            project,
            project.getObjects()
    );
    SourceSetContainer ssContainer = (SourceSetContainer) project.getProperties().get("sourceSets");
    ssContainer.all((SourceSet ss) -> {
      String name = ss.getName();
      File sources = project.file("src/" + name + "/lambdarodeo");
      LambdaRodeoSourceSet lrss = ext.getSourceSetsContainer().maybeCreate(name);
      SourceDirectorySet sds = lrss.getLambdaRodeo();
      sds.srcDir(sources);
      Convention sourceSetConvention = (Convention) InvokerHelper.getProperty(ss, "convention");
      sourceSetConvention.getPlugins().put("lambdarodeo", lrss);
    });
    project.getTasks().register("compileLambdaRodeo", CompileLambdaRodeoTask.class);
  }
}
