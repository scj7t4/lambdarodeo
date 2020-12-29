package lambda.rodeo.lang.s3compileable.expression;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s2typed.expressions.TypedLambda;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunction;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lambda.rodeo.lang.types.CompileableLambdaType;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.util.DescriptorUtils;
import lambda.rodeo.lang.util.FunctionDescriptorBuilder;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.lambda.Value;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

@Builder
@Getter
public class CompileableLambda implements CompileableExpr, LambdaLiftable {

  private final TypedLambda typedExpression;
  private final CompileableFunction lambdaFunction;

  public @NonNull ModuleAst getModuleAst() {
    return typedExpression.getTypedModuleScope().getThisScope().getThisModule();
  }

  @Override
  public void compile(MethodVisitor methodVisitor, S1CompileContext compileContext) {
    String objectDescriptor = Type.getDescriptor(Object.class);
    StringBuilder bootstrapArgs = new StringBuilder("(");
    for (CompileableType type : typedExpression.getType().getArgs()) {
      // This needs to be Object because of the type erasure onto the lambda interfaces.
      bootstrapArgs.append(objectDescriptor);
    }
    bootstrapArgs.append(")").append(objectDescriptor);

    // aload all the variables carried into the closure:
    for (Entry entry : typedExpression.getScopeArgs()) {
      methodVisitor.visitVarInsn(ALOAD, entry.getIndex());
    }

    StringBuilder invokeDynamicDescriptor = new StringBuilder("(");
    for (Entry entry : typedExpression.getScopeArgs()) {
      invokeDynamicDescriptor.append(entry.getType().getLambdaDescriptor());
    }
    invokeDynamicDescriptor.append(")");
    invokeDynamicDescriptor.append(typedExpression.getType().getDescriptor());

    methodVisitor.visitInvokeDynamicInsn(
        "apply",
        invokeDynamicDescriptor.toString(),
        new Handle(Opcodes.H_INVOKESTATIC,
            Type.getInternalName(LambdaMetafactory.class),
            "metafactory",
            FunctionDescriptorBuilder.args(
                MethodHandles.Lookup.class,
                String.class,
                MethodType.class,
                MethodType.class,
                MethodHandle.class,
                MethodType.class).returns(CallSite.class),
            false),
        // This seems to be just the general number of args and return of the lambda:
        Type.getType(bootstrapArgs.toString()),
        new Handle(Opcodes.H_INVOKESTATIC,
            getModuleAst().getInternalJavaName(),
            lambdaFunction.getName(),
            // This should be the description of the full function call (with the closure variables):
            lambdaFunction.generateFunctionDescriptor(),
            false),
        // And this should be the descriptor of how the lambda appears:
        Type.getType(typedExpression.getType().getFunctionDescriptor()));
  }

  @Override
  public void lambdaLift(ClassWriter cw, S2CompileContext compileContext, String internalModuleName) {
    lambdaFunction.compile(cw, compileContext, internalModuleName);
    lambdaFunction.lambdaLift(cw, compileContext, internalModuleName);
  }
}
