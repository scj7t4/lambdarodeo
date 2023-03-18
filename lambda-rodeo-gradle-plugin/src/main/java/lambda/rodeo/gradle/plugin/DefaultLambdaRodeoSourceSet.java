package lambda.rodeo.gradle.plugin;

import groovy.lang.Closure;
import lombok.Getter;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.model.ObjectFactory;
import org.gradle.util.internal.ConfigureUtil;


@Getter
public class DefaultLambdaRodeoSourceSet implements LambdaRodeoSourceSet {
    final String name;
    final String displayName;
    final SourceDirectorySet lambdaRodeo;

    public DefaultLambdaRodeoSourceSet(String name, String displayName, ObjectFactory objectFactory) {
        this.name = name;
        this.displayName = displayName;
        this.lambdaRodeo = objectFactory.sourceDirectorySet(name, displayName);
    }

    @Override
    public LambdaRodeoSourceSet lambdaRodeo(Closure<?> clsr) {
        ConfigureUtil.configure(clsr, lambdaRodeo);
        return this;
    }
}
