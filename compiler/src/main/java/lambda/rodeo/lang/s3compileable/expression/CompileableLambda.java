package lambda.rodeo.lang.s3compileable.expression;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s2typed.expressions.TypedLambda;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunction;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.types.Lambda;
import lambda.rodeo.runtime.types.LambdaRodeoType;
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
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    String objectDescriptor = Type.getDescriptor(Object.class);
    StringBuilder bootstrapArgs = new StringBuilder("(");
    for (LambdaRodeoType type : typedExpression.getType().getArgs()) {
      bootstrapArgs.append(objectDescriptor);
    }
    bootstrapArgs.append(")").append(objectDescriptor);

    methodVisitor.visitInvokeDynamicInsn(
        "apply",
        "()"+typedExpression.getType().getDescriptor(),
        new Handle(Opcodes.H_INVOKESTATIC,
            "java/lang/invoke/LambdaMetafactory",
            "metafactory",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
            false),
        Type.getType(bootstrapArgs.toString()),
        new Handle(Opcodes.H_INVOKESTATIC,
            getModuleAst().getInternalJavaName(),
            lambdaFunction.getName(),
            typedExpression.getType().getFunctionDescriptor(),
            false),
        Type.getType(typedExpression.getType().getFunctionDescriptor()));

  }

  @Override
  public void lambdaLift(ClassWriter cw, CompileContext compileContext, String internalModuleName) {
    lambdaFunction.compile(cw, compileContext, internalModuleName);
    lambdaFunction.lambdaLift(cw, compileContext, internalModuleName);
  }
}
