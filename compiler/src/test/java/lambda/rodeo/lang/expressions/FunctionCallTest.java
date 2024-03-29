package lambda.rodeo.lang.expressions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lambda.rodeo.lang.CompileUnit;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.ModuleContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.compilation.S1CompileContextImpl;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s1ast.ModuleAstFactory;
import lambda.rodeo.lang.s1ast.expressions.AtomAst;
import lambda.rodeo.lang.s1ast.expressions.FunctionCallAst;
import lambda.rodeo.lang.s1ast.expressions.IntConstantAst;
import lambda.rodeo.lang.s1ast.expressions.VariableAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionBodyAst;
import lambda.rodeo.lang.s1ast.functions.FunctionSigAst;
import lambda.rodeo.lang.s1ast.type.TypedVar;
import lambda.rodeo.lang.s2typed.expressions.TypedFunctionCall;
import lambda.rodeo.lang.s3compileable.expression.CompileableFunctionCall;
import lambda.rodeo.lang.scope.ModuleScope;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.CompileableAtom;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.util.IoSupplier;
import lambda.rodeo.lang.utils.CompileContextUtils;
import lambda.rodeo.lang.utils.CompileUtils;
import lambda.rodeo.lang.utils.CompileUtils.CompiledClass;
import lambda.rodeo.lang.utils.ExpectedLocation;
import lambda.rodeo.lang.utils.TestUtils;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FunctionCallTest {

  @Test
  public void testGetCallDescriptorNoArgs() {
    String functionToCall = "someFunc";

    ModuleAst moduleAst = ModuleAst.builder()
        .name("testModule")
        .functionAsts(Collections.singletonList(
            FunctionAst.builder()
                .functionSignature(FunctionSigAst.builder()
                    .name(functionToCall)
                    .declaredReturnType(IntType.INSTANCE)
                    .build())
                .functionBodyAst(Mockito.mock(FunctionBodyAst.class))
                .build()
        ))
        .build();

    TypedModuleScope typedModuleScope = ModuleScope.builder()
        .thisModule(moduleAst)
        .functions(moduleAst.getFunctionAsts())
        .types(moduleAst.getTypes())
        .build()
        .toTypedModuleScope(Collections.emptyList());

    TypedFunctionCall tfc = TypedFunctionCall.builder()
        .returnType(IntType.INSTANCE)
        .functionCallAst(FunctionCallAst.builder()
            .callTarget(VariableAst.builder()
                .name(functionToCall)
                .build())
            .build())
        .typeScope(TypeScope.EMPTY)
        .typedModuleScope(typedModuleScope)
        .args(Collections.emptyList())
        .callTarget(functionToCall)
        .build();

    CompileableFunctionCall cfc = CompileableFunctionCall.builder()
        .args(Collections.emptyList())
        .typedExpression(tfc)
        .build();

    String callDescriptor = cfc.getCallDescriptor();
    assertThat(callDescriptor, equalTo("()Ljava/math/BigInteger;"));
  }

  @Test
  public void testGetCallDescriptorArgs() {
    String functionToCall = "someFunc";
    List<TypedVar> arguments = new ArrayList<>();
    arguments.add(TypedVar.builder()
        .name("v1")
        .type(CompileableAtom.NULL)
        .build()
    );
    arguments.add(TypedVar.builder()
        .name("v2")
        .type(IntType.INSTANCE)
        .build()
    );

    ModuleAst moduleAst = ModuleAst.builder()
        .name("testModule")
        .functionAsts(Collections.singletonList(
            FunctionAst.builder()
                .functionSignature(FunctionSigAst.builder()
                    .name(functionToCall)
                    .arguments(arguments)
                    .declaredReturnType(IntType.INSTANCE)
                    .build())
                .functionBodyAst(Mockito.mock(FunctionBodyAst.class))
                .build()
        ))
        .build();

    TypedModuleScope typedModuleScope = ModuleScope.builder()
        .thisModule(moduleAst)
        .functions(moduleAst.getFunctionAsts())
        .types(moduleAst.getTypes())
        .build()
        .toTypedModuleScope(Collections.emptyList());

    TypedFunctionCall tfc = TypedFunctionCall.builder()
        .returnType(IntType.INSTANCE)
        .functionCallAst(FunctionCallAst.builder()
            .callTarget(VariableAst.builder()
                .name(functionToCall)
                .build())
            .build())
        .typeScope(TypeScope.EMPTY)
        .args(List.of(
            AtomAst
                .builder()
                .atom(CompileableAtom.NULL)
                .build()
                .toTypedExpression(),
            IntConstantAst.builder()
                .literal("666")
                .build()
                .toTypedExpression()
        ))
        .typedModuleScope(typedModuleScope)
        .callTarget(functionToCall)
        .build();

    S1CompileContext cc = S1CompileContextImpl.builder()
        .compileErrorCollector(new CompileErrorCollector())
        .build();
    CompileableFunctionCall cfc = (CompileableFunctionCall) tfc.toCompileableExpr(cc);

    String callDescriptor = cfc.getCallDescriptor();
    assertThat(callDescriptor,
        equalTo("(Llambda/rodeo/runtime/types/Atom;Ljava/math/BigInteger;)Ljava/math/BigInteger;")
    );
  }

  @Test
  @SneakyThrows
  public void testFunctionCallCompilation() {
    String resource = "/test_cases/modules/BasicFunctionCall.rdo";
    ModuleContext module = TestUtils.parseModule(resource);

    ModuleAstFactory factory = new ModuleAstFactory(module,
        S1CompileContextImpl.builder()
            .compileErrorCollector(new CompileErrorCollector())
            .sourcePath("testcase/BasicFunctionCall.rdo")
            .build()
    );

    ModuleAst moduleAst = factory.toAst();
    assertThat(moduleAst.getName(), CoreMatchers.equalTo("testcase.BasicFunctionCall"));

    Class<?> compiledModule = CompileUtils.createClass(moduleAst);
    assertThat(compiledModule.getCanonicalName(),
        CoreMatchers.equalTo("testcase.BasicFunctionCall"));

    Method twoptwo = compiledModule.getMethod("twoptwo");
    assertThat(twoptwo.invoke(null), equalTo(BigInteger.valueOf(4)));

    Method callAndAdd = compiledModule.getMethod("callAndAdd");
    assertThat(callAndAdd.invoke(null), equalTo(BigInteger.valueOf(7)));
  }

  @Test
  @SneakyThrows
  public void testFunctionCallCompilation2() {
    String resource = "/test_cases/modules/BasicFunctionCall.rdo";
    IoSupplier<InputStream> inputStreamSupplier = TestUtils.supplyResource(resource);
    CompileUnit unit = CompileUnit.builder()
        .contents(inputStreamSupplier)
        .sourcePath("testcase/BasicFunctionCall.rdo")
        .build();

    Map<String, CompiledClass> classes = CompileUtils.createClasses(Collections.singletonList(unit));
    Class<?> compiledModule = classes.get("testcase.BasicFunctionCall").getLoaded();

    assertThat(compiledModule.getCanonicalName(),
        CoreMatchers.equalTo("testcase.BasicFunctionCall"));

    Method twoptwo = compiledModule.getMethod("twoptwo");
    assertThat(twoptwo.invoke(null), equalTo(BigInteger.valueOf(4)));

    Method callAndAdd = compiledModule.getMethod("callAndAdd");
    assertThat(callAndAdd.invoke(null), equalTo(BigInteger.valueOf(7)));
  }

  @Test
  @SneakyThrows
  public void testFunctionCallCompilation3() {
    String basicResource = "/test_cases/modules/BasicFunctionCall.rdo";
    IoSupplier<InputStream> basicSource = TestUtils.supplyResource(basicResource);
    String importResource = "/test_cases/modules/ImportModuleFunctionCall.rdo";
    IoSupplier<InputStream> importSource = TestUtils.supplyResource(importResource);

    CompileUnit basicUnit = CompileUnit.builder()
        .contents(basicSource)
        .sourcePath("testcase/BasicFunctionCall.rdo")
        .build();

    CompileUnit importUnit = CompileUnit.builder()
        .contents(importSource)
        .sourcePath("testcase/ImportModuleFunctionCall.rdo")
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(basicUnit);
    toCompile.add(importUnit);

    Map<String, CompiledClass> classes = CompileUtils.createClasses(toCompile);
    Class<?> compiledModule = classes.get("testcase.ImportModuleFunctionCall").getLoaded();

    assertThat(compiledModule.getCanonicalName(),
        CoreMatchers.equalTo("testcase.ImportModuleFunctionCall"));

    Method callAndAdd = compiledModule.getMethod("callAndAdd");
    assertThat(callAndAdd.invoke(null), equalTo(BigInteger.valueOf(7)));
  }

  @Test
  @SneakyThrows
  public void testFunctionCallCompilation4() {
    String basicResource = "/test_cases/modules/BasicFunctionCall.rdo";
    IoSupplier<InputStream> basicSource = TestUtils.supplyResource(basicResource);
    String importResource = "/test_cases/modules/AliasModuleFunctionCall.rdo";
    IoSupplier<InputStream> importSource = TestUtils.supplyResource(importResource);

    CompileUnit basicUnit = CompileUnit.builder()
        .contents(basicSource)
        .sourcePath("testcase/BasicFunctionCall.rdo")
        .build();

    CompileUnit importUnit = CompileUnit.builder()
        .contents(importSource)
        .sourcePath("testcase/AliasModuleFunctionCall.rdo")
        .build();

    List<CompileUnit> toCompile = new ArrayList<>();
    toCompile.add(basicUnit);
    toCompile.add(importUnit);

    Map<String, CompiledClass> classes = CompileUtils.createClasses(toCompile);
    Class<?> compiledModule = classes.get("testcase.AliasModuleFunctionCall").getLoaded();

    assertThat(compiledModule.getCanonicalName(),
        CoreMatchers.equalTo("testcase.AliasModuleFunctionCall"));

    Method callAndAdd = compiledModule.getMethod("callAndAdd");
    assertThat(callAndAdd.invoke(null), equalTo(BigInteger.valueOf(7)));
  }

  @Test
  @SneakyThrows
  public void testFunctionNotInScope() {
    String resource = "/test_cases/functions/FunctionNotInScope.rdo";
    ModuleContext moduleDef = TestUtils.parseModule(resource);
    ModuleAstFactory factory = new ModuleAstFactory(moduleDef,
        CompileContextUtils.testS1CompileContext());
    ModuleAst testCase = factory.toAst();

    CompileErrorCollector compileErrorCollector = CompileUtils.expectCompileErrors(testCase);

    assertThat(compileErrorCollector.getCompileErrors(), contains(
        CompileError.undefinedIdentifier(
            ExpectedLocation.builder()
                .startLine(2)
                .endLine(2)
                .characterStart(2)
                .build(), "fibonacci")
    ));
  }
}