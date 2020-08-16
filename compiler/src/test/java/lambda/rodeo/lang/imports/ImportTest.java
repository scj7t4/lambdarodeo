package lambda.rodeo.lang.imports;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import lambda.rodeo.lang.CompileUnit;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.ExpectedLocation;
import lambda.rodeo.lang.utils.TestUtils;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class ImportTest {

  @Test
  @SneakyThrows
  public void testMisingModuleImport() {
    String importResource = "/test_cases/modules/AliasModuleFunctionCall.rdo";
    Supplier<InputStream> importSource = TestUtils.supplyResource(importResource);

    CompileUnit importUnit = CompileUnit.builder()
        .contents(importSource)
        .sourcePath(importResource)
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(importUnit);

    CompileErrorCollector compileErrorCollector = CompileUtils.expectCompileErrors(toCompile);

    assertThat(compileErrorCollector.getCompileErrors(), Matchers.contains(
        CompileError.badImport(
            ExpectedLocation.builder()
                .characterStart(0)
                .startLine(1)
                .endLine(1)
                .build(),
            "testcase.BasicFunctionCall"
        ),
        CompileError.undefinedIdentifier(
            ExpectedLocation.builder()
                .characterStart(6)
                .startLine(4)
                .endLine(4)
                .build(),
            "BFC.twoptwo"
        )
    ));
  }
}
