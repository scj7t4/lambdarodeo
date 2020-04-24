package lambda.rodeo.lang.functions;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.GETSTATIC;

import java.util.List;
import lambda.rodeo.lang.ModuleAst;
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
  Runnable runnable;

  public void compile(ModuleAst module, ClassWriter cw) {
    //TODO define better signature? ASM says I can leave null if I don't have generics info
    MethodVisitor methodVisitor = cw
        .visitMethod(
            ACC_PUBLIC | ACC_STATIC,
            name,
            "()" + Type.getDescriptor(Result.class),
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