package lambda.rodeo.lang.s3compileable.expression;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Collections;
import java.util.List;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s1ast.expressions.FunctionCallAst;
import lambda.rodeo.lang.s1ast.expressions.IntConstantAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.functions.FunctionSigAst;
import lambda.rodeo.lang.s2typed.expressions.TypedFunctionCall;
import lambda.rodeo.lang.scope.CompileableModuleScope;
import lambda.rodeo.lang.scope.ModuleScope;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.IntType;
import org.junit.jupiter.api.Test;

class CompileableFunctionCallTest {

  @Test
  public void testGetCallDescriptorNoArgs() {
    String functionToCall = "someFunc";

    TypedFunctionCall tfc = TypedFunctionCall.builder()
        .returnType(IntType.INSTANCE)
        .functionCallAst(FunctionCallAst.builder()
            .callTarget(functionToCall)
            .build())
        .build();

    ModuleAst moduleAst = ModuleAst.builder()
        .name("testModule")
        .functionAsts(Collections.singletonList(
            FunctionAst.builder()
                .functionSignature(FunctionSigAst.builder()
                    .name(functionToCall)
                    .declaredReturnType(IntType.INSTANCE)
                    .build())
                .build()
        ))
        .build();

    CompileableModuleScope compileableModuleScope = CompileableModuleScope.builder()
        .thisScope(ModuleScope.builder()
            .thisModule(moduleAst)
            .build())
        .build();

    CompileableFunctionCall cfc = CompileableFunctionCall.builder()
        .args(Collections.emptyList())
        .typedExpression(tfc)
        .compileableModuleScope(compileableModuleScope)
        .build();

    String callDescriptor = cfc.getCallDescriptor();
    assertThat(callDescriptor, equalTo("()Ljava/math/BigInteger;"));
  }

  @Test
  public void testGetCallDescriptorArgs() {
    String functionToCall = "someFunc";

    TypedFunctionCall tfc = TypedFunctionCall.builder()
        .returnType(IntType.INSTANCE)
        .functionCallAst(FunctionCallAst.builder()
            .callTarget(functionToCall)
            .build())
        .build();

    ModuleAst moduleAst = ModuleAst.builder()
        .name("testModule")
        .functionAsts(Collections.singletonList(
            FunctionAst.builder()
                .functionSignature(FunctionSigAst.builder()
                    .name(functionToCall)
                    .declaredReturnType(IntType.INSTANCE)
                    .build())
                .build()
        ))
        .build();

    CompileableModuleScope compileableModuleScope = CompileableModuleScope.builder()
        .thisScope(ModuleScope.builder()
            .thisModule(moduleAst)
            .build())
        .build();

    CompileableFunctionCall cfc = CompileableFunctionCall.builder()
        .args(List.of(
            Atom.NULL
                .toTypedExpression()
                .toCompileableExpr(compileableModuleScope),
            IntConstantAst.builder()
                .literal("666")
                .build()
                .toTypedExpression()
                .toCompileableExpr(compileableModuleScope)
        ))
        .typedExpression(tfc)
        .compileableModuleScope(compileableModuleScope)
        .build();

    String callDescriptor = cfc.getCallDescriptor();
    assertThat(callDescriptor,
        equalTo("(Llambda/rodeo/lang/types/Atom;Ljava/math/BigInteger;)Ljava/math/BigInteger;")
    );
  }
}