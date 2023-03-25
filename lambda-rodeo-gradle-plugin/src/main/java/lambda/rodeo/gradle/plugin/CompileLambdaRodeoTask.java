package lambda.rodeo.gradle.plugin;

import lambda.rodeo.lang.CompileUnit;
import lambda.rodeo.lang.CompilerChain;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;
import org.gradle.internal.impldep.org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CompileLambdaRodeoTask extends DefaultTask {

    List<CompileUnit> determineCompileUnits(SourceDirectorySet sourceDirectorySet) throws LambdaRodeoCompilationFailedException {
        Collection<Path> srcDirs = sourceDirectorySet.getSrcDirs()
                .stream()
                .map(File::toPath)
                .map(Path::toAbsolutePath)
                .collect(Collectors.toList());
        List<CompileUnit> compileUnits = new ArrayList<>();
        for (File f : sourceDirectorySet) {
            var ext = FilenameUtils.getExtension(f.getName());
            if (!Objects.equals(ext.toLowerCase(), "rdo")) {
                continue;
            }
            Path filePath = f.toPath().toAbsolutePath();
            Path bestRoot = srcDirs.stream()
                    .filter(filePath::startsWith)
                    .findFirst()
                    .orElseThrow(() ->
                            new LambdaRodeoCompilationFailedException("Source file " + filePath +
                                    " is not in any of the source directories. Checked " + srcDirs));
            String relativized = bestRoot.relativize(filePath).toString();
            if (SystemUtils.IS_OS_WINDOWS) {
                relativized = FilenameUtils.separatorsToUnix(relativized);
            }
            compileUnits.add(CompileUnit.builder()
                    .sourcePath(relativized)
                    .contents(() -> new FileInputStream(f))
                    .build());
        }
        return compileUnits;
    }

    @TaskAction
    public void compileLambdaRodeo() throws LambdaRodeoCompilationFailedException {
        log.info("Yeehaw! Let's Lambda Rodeo!");
        SourceSetContainer container = (SourceSetContainer) getProject().getProperties().get("sourceSets");
        SourceSet main = container.getByName("main");
        LambdaRodeoSourceSet lrss = getProject()
                .getExtensions()
                .getByType(LambdaRodeoExtension.class)
                .getSourceSetsContainer()
                .maybeCreate(main.getName());
        log.error("directories under {}: {}", main.getName(), lrss.getLambdaRodeo().getSrcDirs());
        List<CompileUnit> compileUnits = determineCompileUnits(lrss.getLambdaRodeo());
        CompilerChain compiler = CompilerChain.builder()
                .compileUnits(compileUnits)
                .build();
        CompilerChain.CompileResult result;
        try {
            result = compiler.compile();
        } catch (IOException ex) {
            throw new LambdaRodeoCompilationFailedException("Compilation failed", ex);
        }
        for (var compiledUnit : result.getCompiledUnits().orElse(Collections.emptyList())) {

            Path targetFile = getProject().getBuildDir().toPath()
                    .resolve("classes/lambdarodeo/main")
                    .resolve(compiledUnit.getModuleName() + ".class");
            log.error("Writing compiled unit: {}, module {}", targetFile, compiledUnit.getModuleName());
            try {
                Files.createDirectories(targetFile.getParent());
            } catch (IOException ex) {
                throw new LambdaRodeoCompilationFailedException("Failed creating class file output directory" + targetFile.getParent(), ex);
            }
            try (FileOutputStream fos = new FileOutputStream(targetFile.toFile())) {

                fos.write(compiledUnit.getByteCode());
            } catch (IOException ex) {
                throw new LambdaRodeoCompilationFailedException("Failed writing class file: " + targetFile, ex);
            }
        }
        if (!result.isSuccess()) {
            CompileErrorCollector errors = result.getErrorCollector();
            List<CompileError> compileErrors = errors.getCompileErrors();
            compileErrors.forEach(error -> log.error("{}", error));
            throw new LambdaRodeoCompilationFailedException("Compilation failed");
        }
    }
}
