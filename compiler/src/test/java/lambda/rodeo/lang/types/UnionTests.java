package lambda.rodeo.lang.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

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
import lambda.rodeo.runtime.types.LRObject;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsArrayWithSize;
import org.junit.jupiter.api.Test;

public class UnionTests {
  @Test
  @SneakyThrows
  public void testBasicUnionFunction() {
    String importResource = "/test_cases/advanced_types/BasicUnion.rdo";
    Supplier<InputStream> interfaceSource = TestUtils.supplyResource(importResource);

    CompileUnit interfaceUnit = CompileUnit.builder()
        .contents(interfaceSource)
        .sourcePath("lambda.rodeo.test.BasicUnion")
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(interfaceUnit);

    Map<String, CompiledClass> classes = CompileUtils.createClasses(toCompile);
    Class<?> aClass = classes.get(interfaceUnit.getSourcePath()).getLoaded();

    Method test = aClass.getMethod("stringOrInt", Object.class);
    Object invoke = test.invoke(null, BigInteger.ZERO);
    assertThat(invoke, Matchers.equalTo(new Atom("zero")));
  }

  @Test
  @SneakyThrows
  public void testBasicUnionFunction2() {
    String importResource = "/test_cases/advanced_types/BasicUnion2.rdo";
    Supplier<InputStream> interfaceSource = TestUtils.supplyResource(importResource);

    CompileUnit interfaceUnit = CompileUnit.builder()
        .contents(interfaceSource)
        .sourcePath("lambda.rodeo.test.BasicUnion2")
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(interfaceUnit);

    Map<String, CompiledClass> classes = CompileUtils.createClasses(toCompile);
    CompiledClass compiledClass = classes.get(interfaceUnit.getSourcePath());
    CompileUtils.asmifyByteCode(compiledClass.getByteCode());
    Class<?> aClass = compiledClass.getLoaded();

    Method test = aClass.getMethod("stringOrInt", Object.class);
    Object invoke = test.invoke(null, BigInteger.ZERO);
    assertThat(invoke, Matchers.equalTo(new Atom("number")));

    Object invoke2 = test.invoke(null, "hello world");
    assertThat(invoke2, Matchers.equalTo(new Atom("string")));

    Object invoke3 = test.invoke(null, LRObject.create());
    assertThat(invoke3, Matchers.equalTo(new Atom("err")));
  }
}
