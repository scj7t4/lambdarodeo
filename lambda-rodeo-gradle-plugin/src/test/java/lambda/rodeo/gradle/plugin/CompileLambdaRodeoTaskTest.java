package lambda.rodeo.gradle.plugin;

import lambda.rodeo.lang.CompileUnit;
import org.gradle.api.Project;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.testfixtures.ProjectBuilder;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompileLambdaRodeoTaskTest {
    @Test
    public void testDetermineCompileUnits() throws LambdaRodeoCompilationFailedException {
        SourceDirectorySet sourceDirectorySet = Mockito.mock(SourceDirectorySet.class);
        File root = new File("C:\\my\\cool\\project\\src\\main\\lambdarodeo");
        Mockito.when(sourceDirectorySet.getSrcDirs()).thenReturn(Set.of(
                root
        ));
        Mockito.when(sourceDirectorySet.iterator()).thenReturn(List.of(
                root.toPath().resolve("example/project/examplefile.rdo").toFile(),
                root.toPath().resolve("example/README.md").toFile()
        ).iterator());
        Project project = ProjectBuilder.builder().build();
        project.getTasks().register("compileLambdaRodeo", CompileLambdaRodeoTask.class);
        CompileLambdaRodeoTask instance = (CompileLambdaRodeoTask) project.getTasks().getByName("compileLambdaRodeo");
        List<CompileUnit> result = instance.determineCompileUnits(sourceDirectorySet);
        assertThat(result, IsCollectionWithSize.hasSize(1));
        assertThat(result.get(0).getSourcePath(), Matchers.equalTo("example/project/examplefile.rdo"));
        assertThrows(IOException.class, () -> {
            result.get(0).getContents().get();
        });
    }
}
