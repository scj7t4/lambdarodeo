package lambda.rodeo.gradle.plugin;

import groovy.lang.Closure;
import org.gradle.api.file.SourceDirectorySet;

public interface LambdaRodeoSourceSet {
    String getName();

    String getDisplayName();

    SourceDirectorySet getLambdaRodeo();

    LambdaRodeoSourceSet lambdaRodeo(Closure<?> clsr);
}
