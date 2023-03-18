package lambda.rodeo.gradle.plugin;

import lombok.AllArgsConstructor;
import org.gradle.api.NamedDomainObjectFactory;
import org.gradle.api.model.ObjectFactory;

@AllArgsConstructor
public class LambdaRodeoSourceSetFactory implements NamedDomainObjectFactory<LambdaRodeoSourceSet> {

    private final ObjectFactory objectFactory;
    @Override
    public LambdaRodeoSourceSet create(String name) {
        return new DefaultLambdaRodeoSourceSet(name, name, objectFactory);
    }
}
