package lambda.rodeo.lang.types;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import lambda.rodeo.lang.CompileUnit;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.CompileUtils.CompiledClass;
import lambda.rodeo.lang.utils.TestUtils;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.LRInteger;
import lambda.rodeo.runtime.types.LRObject;
import lambda.rodeo.runtime.types.LRString;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class GenericTests {

  @Test
  @SneakyThrows
  public void testMaybe() {
    String importResource = "/test_cases/generics/GenericMaybe.rdo";
    Supplier<InputStream> interfaceSource = TestUtils.supplyResource(importResource);

    CompileUnit interfaceUnit = CompileUnit.builder()
        .contents(interfaceSource)
        .sourcePath("lambda.rodeo.test.GenericMaybe")
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(interfaceUnit);

    Map<String, CompiledClass> classes = CompileUtils.createClasses(toCompile);
    CompiledClass compiledClass = classes.get(interfaceUnit.getSourcePath());
    CompileUtils.asmifyByteCode(compiledClass.getByteCode());
    Class<?> aClass = compiledClass.getLoaded();

    Method test = aClass.getMethod("useGeneric", Object.class);
    Object invoke = test.invoke(null, "hello");
    assertThat(invoke, Matchers.equalTo("hello"));

    Object invoke2 = test.invoke(null, new Atom("empty"));
    assertThat(invoke2, Matchers.equalTo(""));
  }

  @Test
  @SneakyThrows
  public void testInterface() {
    String importResource = "/test_cases/generics/GenericInterface.rdo";
    Supplier<InputStream> interfaceSource = TestUtils.supplyResource(importResource);

    CompileUnit interfaceUnit = CompileUnit.builder()
        .contents(interfaceSource)
        .sourcePath("lambda.rodeo.test.GenericInterface")
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(interfaceUnit);

    Map<String, CompiledClass> classes = CompileUtils.createClasses(toCompile);
    CompiledClass compiledClass = classes.get(interfaceUnit.getSourcePath());
    CompileUtils.asmifyByteCode(compiledClass.getByteCode());
    Class<?> aClass = compiledClass.getLoaded();

    Method test = aClass.getMethod("useGeneric", LRObject.class);
    Object invoke = test.invoke(
        null,
        LRObject.create()
            .set("value", "hello", LRString.INSTANCE)
            .done());
    assertThat(invoke, Matchers.equalTo("hello"));

    Object invoke2 = test.invoke(null,
        LRObject.create()
            .set("value", BigInteger.valueOf(1337), LRInteger.INSTANCE)
            .done());
    assertThat(invoke2, Matchers.equalTo(BigInteger.valueOf(1337)));
  }
}
