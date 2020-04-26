package lambda.rodeo.lang.functions;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ARETURN;

import java.util.List;
import lambda.rodeo.lang.ModuleAst;
import lambda.rodeo.lang.statements.TypeScope;
import lombok.Builder;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/*
 * Function is composed of multiple statements A -> B -> C -> D
 *
 * We would like to determine last function call <- | -> everything
 * to the right is grouped into a result call..
 */
@Data
@Builder
public class FunctionAst {

  private final FunctionSigAst functionSignature;
  private final FunctionBodyAst functionBodyAst;

  public void compile(ModuleAst module, ClassWriter cw) {
    MethodVisitor methodVisitor = cw
        .visitMethod(
            ACC_PUBLIC | ACC_STATIC,
            functionSignature.getName(),
            functionSignature.generateFunctionDescriptor(),
            null,
            null);

    methodVisitor.visitCode();
    Label startFunc = new Label();
    methodVisitor.visitLabel(startFunc);

    // The function body should emit a typescope which is all the variables it uses.
    TypeScope typeScope = functionBodyAst.compile(methodVisitor,
        functionSignature.getInitialTypeScope());

    // After compiling all the statements, the final statement should be on the stack:
    // An assigment in the last statement should be illegal, so we don't allow it:
    methodVisitor.visitInsn(ARETURN);
    Label endFunc = new Label();
    methodVisitor.visitLabel(endFunc);

    typeScope.compile(methodVisitor, startFunc, endFunc);
    methodVisitor.visitMaxs(0, 0);
    methodVisitor.visitEnd();
  }

  public String getName() {
    return getFunctionSignature().getName();
  }

  public List<TypedVarAst> getArguments() {
    return getFunctionSignature().getArguments();
  }
}
