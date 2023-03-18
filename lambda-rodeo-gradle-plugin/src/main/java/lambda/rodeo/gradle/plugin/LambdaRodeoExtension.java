package lambda.rodeo.gradle.plugin;

import lombok.Getter;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;

@Getter
public class LambdaRodeoExtension {
    private final NamedDomainObjectContainer<LambdaRodeoSourceSet> sourceSetsContainer;

    public LambdaRodeoExtension(Project project, ObjectFactory objectFactory) {
        sourceSetsContainer = project.container(LambdaRodeoSourceSet.class,
                new LambdaRodeoSourceSetFactory(objectFactory));
    }

    public void srcDir(String file) {
        sourceSetsContainer.getByName("main").getLambdaRodeo().srcDir(file);
    }
}
