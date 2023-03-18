package lambda.rodeo.gradle.plugin;

import lombok.extern.slf4j.Slf4j;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskAction;

@Slf4j
public class CompileLambdaRodeoTask extends DefaultTask {

    @TaskAction
    public void compileLambdaRodeo() {
        log.info("Yeehaw!");
        SourceSetContainer container = (SourceSetContainer) getProject().getProperties().get("sourceSets");
        SourceSet main = container.getByName("main");
        LambdaRodeoSourceSet lrss = getProject()
                .getExtensions()
                .getByType(LambdaRodeoExtension.class)
                .getSourceSetsContainer()
                .maybeCreate(main.getName());
        log.info("directories under {}: {}", main.getName(), lrss.getLambdaRodeo().getSrcDirs());
        lrss.getLambdaRodeo().forEach(f -> {}); // Convert to compileUnits
    }
}
