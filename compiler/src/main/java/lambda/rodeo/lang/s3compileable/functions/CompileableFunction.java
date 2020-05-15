package lambda.rodeo.lang.s3compileable.functions;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;

import lambda.rodeo.lang.s1ast.functions.FunctionSigAst;
import lambda.rodeo.lang.s1ast.functions.TypedVar;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s2typed.functions.TypedFunction;
import lambda.rodeo.lang.scope.CompileableTypeScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
@EqualsAndHashCode
public class CompileableFunction {
  private final TypedFunction typedFunction;
  private final CompileableFunctionBody functionBody;
  private final FunctionSigAst functionSigAst;

  public String generateFunctionDescriptor() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (TypedVar var : functionSigAst.getArguments()) {
      String descriptor = Type.getDescriptor(var.getType().javaType());
      sb.append(descriptor);
    }
    sb.append(")").append(Type.getDescriptor(functionSigAst.getDeclaredReturnType().javaType()));
    return sb.toString();
  }

  public void compile(ClassWriter cw, CompileContext compileContext, String internalModuleName) {
    int access = ACC_PUBLIC | ACC_STATIC;
    if(isLambda()) {
      access |= ACC_SYNTHETIC;
    }

    MethodVisitor methodVisitor = cw
        .visitMethod(
            access,
            functionSigAst.getName(),
            generateFunctionDescriptor(),
            null,
            null);

    methodVisitor.visitCode();
    Label startFunc = new Label();
    methodVisitor.visitLabel(startFunc);

    // The function body should emit a typescope which is all the variables it uses.
    functionBody.compile(methodVisitor, compileContext, internalModuleName);
    Label endFunc = new Label();
    methodVisitor.visitLabel(endFunc);

    CompileableTypeScope finalTypeScope = functionBody.getFinalTypeScope();
    finalTypeScope.compile(methodVisitor, startFunc, endFunc);
    methodVisitor.visitMaxs(0, 0);
    methodVisitor.visitEnd();
  }

  public String getName() {
    return getFunctionSigAst().getName();
  }

  public void lambdaLift(ClassWriter cw, CompileContext compileContext, String internalJavaName) {
    functionBody.lambdaLift(cw, compileContext, internalJavaName);
  }

  public boolean isLambda() {
    return getTypedFunction().isLambda();
  }
}
