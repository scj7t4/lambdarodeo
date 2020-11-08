package lambda.rodeo.lang.interfaces;

import static lambda.rodeo.lang.utils.TestUtils.parseResource;
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
import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeDefContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lambda.rodeo.lang.s1ast.type.InterfaceAst;
import lambda.rodeo.lang.s1ast.type.TypeDef;
import lambda.rodeo.lang.s1ast.type.TypeDefAstFactory;
import lambda.rodeo.lang.s1ast.type.TypedVar;
import lambda.rodeo.lang.types.CompileableAtom;
import lambda.rodeo.lang.types.DefinedType;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.TestUtils;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.LRObject;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsArrayWithSize;
import org.junit.jupiter.api.Test;

public class InterfaceTest {

  @Test
  @SneakyThrows
  public void testInterfaceAstParsing() {
    String interfaceResource = "/test_cases/interfaces/Interface1.rdo";
    LambdaRodeoParser parser = parseResource(interfaceResource);
    TypeDefContext interfaceDefContext = parser.typeDef();

    TypeDefAstFactory typeDefAstFactory = new TypeDefAstFactory(interfaceDefContext);
    TypeDef ast = typeDefAstFactory.toAst();

    assertThat(ast.getIdentifier(), Matchers.equalTo("MyCoolInterface"));
    InterfaceAst interfaceDef = (InterfaceAst) ast.getType();
    assertThat(interfaceDef.getMembers(), contains(
        TypedVar.builder()
            .name("member1")
            .type(IntType.INSTANCE)
            .startLine(2)
            .characterStart(2)
            .endLine(2)
            .build(),
        TypedVar.builder()
            .name("member2")
            .type(CompileableAtom.NULL)
            .startLine(3)
            .endLine(3)
            .characterStart(2)
            .build()
    ));
  }

  @Test
  @SneakyThrows
  public void testInterfaceAstParsing2() {
    String interfaceResource = "/test_cases/interfaces/Interface2.rdo";
    LambdaRodeoParser parser = parseResource(interfaceResource);
    TypeDefContext interfaceDefContext = parser.typeDef();

    TypeDefAstFactory typeDefAstFactory = new TypeDefAstFactory(interfaceDefContext);
    TypeDef ast = typeDefAstFactory.toAst();

    assertThat(ast.getIdentifier(), Matchers.equalTo("Tree"));
    InterfaceAst interfaceDef = (InterfaceAst) ast.getType();
    assertThat(interfaceDef.getMembers(), contains(
        TypedVar.builder()
            .name("left")
            .type(DefinedType.builder().declaration("Tree").build())
            .startLine(2)
            .characterStart(2)
            .endLine(2)
            .build(),
        TypedVar.builder()
            .name("right")
            .type(DefinedType.builder().declaration("Tree").build())
            .startLine(3)
            .endLine(3)
            .characterStart(2)
            .build()
    ));
  }

  @Test
  @SneakyThrows
  public void testInterfaceReturn() {
    String importResource = "/test_cases/interfaces/InterfaceReturn.rdo";
    Supplier<InputStream> interfaceSource = TestUtils.supplyResource(importResource);

    CompileUnit interfaceUnit = CompileUnit.builder()
        .contents(interfaceSource)
        .sourcePath("lambda.rodeo.test.InterfaceReturn")
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(interfaceUnit);

    Map<String, Class<?>> classes = CompileUtils.createClasses(toCompile);
    Class<?> aClass = classes.get(interfaceUnit.getSourcePath());

    Method test = aClass.getMethod("test");
    Object invoke = test.invoke(null);
    LRObject asLRObject = (LRObject) invoke;
    assertThat(asLRObject.getEntries(), IsArrayWithSize.emptyArray());
  }

  @Test
  @SneakyThrows
  public void testInterfaceReturn2() {
    String importResource = "/test_cases/interfaces/InterfaceReturn.rdo";
    Supplier<InputStream> interfaceSource = TestUtils.supplyResource(importResource);

    CompileUnit interfaceUnit = CompileUnit.builder()
        .contents(interfaceSource)
        .sourcePath("lambda.rodeo.test.InterfaceReturn")
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(interfaceUnit);

    Map<String, Class<?>> classes = CompileUtils.createClasses(toCompile);
    Class<?> aClass = classes.get(interfaceUnit.getSourcePath());

    Method test = aClass.getMethod("test2");
    Object invoke = test.invoke(null);
    LRObject asLRObject = (LRObject) invoke;
    assertThat(asLRObject.getEntries(), IsArrayWithSize.arrayWithSize(2));
    assertThat(asLRObject.get("member1"), Matchers.equalTo(BigInteger.valueOf(3L)));
    assertThat(asLRObject.get("member2"), Matchers.equalTo(Atom.NULL));
  }

  @Test
  @SneakyThrows
  public void testInterfaceReturn3() {
    String importResource = "/test_cases/interfaces/InterfaceReturn.rdo";
    Supplier<InputStream> interfaceSource = TestUtils.supplyResource(importResource);

    CompileUnit interfaceUnit = CompileUnit.builder()
        .contents(interfaceSource)
        .sourcePath("lambda.rodeo.test.InterfaceReturn")
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(interfaceUnit);

    Map<String, Class<?>> classes = CompileUtils.createClasses(toCompile);
    Class<?> aClass = classes.get(interfaceUnit.getSourcePath());

    Method test = aClass.getMethod("test3");
    Object invoke = test.invoke(null);
    LRObject asLRObject = (LRObject) invoke;
    assertThat(asLRObject.getEntries(), IsArrayWithSize.arrayWithSize(2));
    assertThat(asLRObject.get("member1"), Matchers.equalTo(BigInteger.valueOf(3L)));
    assertThat(asLRObject.get("member2"), Matchers.equalTo(Atom.NULL));
  }

  @Test
  @SneakyThrows
  public void testInterfaceReturn4() {
    String importResource = "/test_cases/interfaces/InterfaceReturn2.rdo";
    Supplier<InputStream> interfaceSource = TestUtils.supplyResource(importResource);

    CompileUnit interfaceUnit = CompileUnit.builder()
        .contents(interfaceSource)
        .sourcePath("lambda.rodeo.test.InterfaceReturn")
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(interfaceUnit);
    CompileErrorCollector compileErrorCollector = CompileUtils.expectCompileErrors(toCompile);

    assertThat(compileErrorCollector.getCompileErrors(), Matchers.contains(
        CompileError.builder()
            .characterStart(31)
            .startLine(6)
            .endLine(8)
            .errorType(CompileError.RETURN_TYPE_MISMATCH)
            .errorMsg(
                "Pattern case returns 'LRInterface<{}>'; it cannot be assigned to 'LRInterface<{member1: int; member2: :null}>'")
            .build()
    ));
  }
}
