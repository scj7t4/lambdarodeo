package lambda.rodeo.lang.typed.functions;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ARETURN;

import java.util.List;
import lambda.rodeo.lang.ast.functions.FunctionAst;
import lambda.rodeo.lang.ast.functions.FunctionSigAst;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.ast.statements.TypeScope;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
public class TypedFunctionAst {
  private final FunctionAst functionAst;
  private final TypedFunctionBodyAst functionBodyAst;
  private final FunctionSigAst functionSigAst;

  public String generateFunctionDescriptor() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (TypedVarAst var : functionSigAst.getArguments()) {
      String descriptor = Type.getDescriptor(var.getType().javaType());
      sb.append(descriptor);
    }
    sb.append(")").append(Type.getDescriptor(functionBodyAst.getReturnType().javaType()));
    return sb.toString();
  }

  public void compile(ClassWriter cw, CompileContext compileContext) {
    MethodVisitor methodVisitor = cw
        .visitMethod(
            ACC_PUBLIC | ACC_STATIC,
            functionSigAst.getName(),
            generateFunctionDescriptor(),
            null,
            null);

    methodVisitor.visitCode();
    Label startFunc = new Label();
    methodVisitor.visitLabel(startFunc);

    // The function body should emit a typescope which is all the variables it uses.
    functionBodyAst.compile(methodVisitor, compileContext);

    // After compiling all the statements, the final statement should be on the stack:
    // An assigment in the last statement should be illegal, so we don't allow it:
    methodVisitor.visitInsn(ARETURN);
    Label endFunc = new Label();
    methodVisitor.visitLabel(endFunc);

    TypeScope finalTypeScope = functionBodyAst.getFinalTypeScope();
    finalTypeScope.compile(methodVisitor, startFunc, endFunc);
    methodVisitor.visitMaxs(0, 0);
    methodVisitor.visitEnd();
  }

  public String getName() {
    return getFunctionSigAst().getName();
  }

  public List<TypedVarAst> getArguments() {
    return getFunctionSigAst().getArguments();
  }
}
