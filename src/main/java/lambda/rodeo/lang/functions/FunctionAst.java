package lambda.rodeo.lang.functions;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.GETSTATIC;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.ModuleAst;
import lambda.rodeo.lang.expressions.ExpressionAst;
import lambda.rodeo.lang.types.Atom;
import lombok.Builder;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Data
@Builder
public class FunctionAst {

  private String name;
  private List<TypedVarAst> arguments;

  @Builder.Default()
  private final List<ExpressionAst> expressions = new ArrayList<>();

  public String generateFunctionDescriptor() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (TypedVarAst var : arguments) {
      String descriptor = Type.getDescriptor(var.getType().javaType());
      sb.append(descriptor);
    }
    sb.append(")").append(Type.getDescriptor(Result.class));
    return sb.toString();
  }

  public void compile(ModuleAst module, ClassWriter cw) {
    MethodVisitor methodVisitor = cw
        .visitMethod(
            ACC_PUBLIC | ACC_STATIC,
            name,
            generateFunctionDescriptor(),
            null,
            null);

    methodVisitor.visitCode();
    Label label0 = new Label();
    methodVisitor.visitLabel(label0);
    methodVisitor.visitFieldInsn(
        GETSTATIC,
        Type.getInternalName(Atom.class),
        "NULL",
        Type.getDescriptor(Atom.class));
    methodVisitor.visitInsn(ARETURN);
    Label label1 = new Label();
    methodVisitor.visitLabel(label1);
    methodVisitor.visitLocalVariable(
        "this",
        module.getModuleJVMDescriptor(),
        null,
        label0,
        label1,
        0);
    methodVisitor.visitMaxs(0, 0);
    methodVisitor.visitEnd();
  }
}
