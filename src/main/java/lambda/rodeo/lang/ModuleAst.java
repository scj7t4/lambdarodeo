package lambda.rodeo.lang;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.V1_8;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.functions.FunctionAst;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

@Data
@Builder
public class ModuleAst {

  @NonNull
  private final String name;

  @Builder.Default
  private final List<FunctionAst> functionAsts = new ArrayList<>();

  public String getInternalJavaName() {
    return name.replace(".", "/");
  }

  public byte[] compile() {
    // Tell ASM we want it to compute max stack and frames.
    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    cw.visit(
        V1_8,
        ACC_PUBLIC | ACC_SUPER,
        getInternalJavaName(),
        null,
        "java/lang/Object",
        null);
    // TODO: Mark source
    cw.visitInnerClass("java/lang/invoke/MethodHandles$Lookup", "java/lang/invoke/MethodHandles",
        "Lookup", ACC_PUBLIC | ACC_FINAL | ACC_STATIC);

    {
      // Generate an empty private constructor:
      MethodVisitor constructor = cw.visitMethod(
          Opcodes.ACC_PRIVATE,
          "<init>",
          "()V",
          null,
          null);

      constructor.visitCode();
      // Call super()
      Label label0 = new Label();
      constructor.visitLabel(label0);
      constructor.visitVarInsn(Opcodes.ALOAD, 0);
      constructor.visitMethodInsn(Opcodes.INVOKESPECIAL,
          Type.getInternalName(Object.class), "<init>", "()V", false);
      constructor.visitInsn(Opcodes.RETURN);
      Label label1 = new Label();
      constructor.visitLabel(label1);
      constructor.visitLocalVariable(
          "this",
          getModuleJVMDescriptor(),
          null,
          label0,
          label1,
          0);
      constructor.visitMaxs(0, 0);
      constructor.visitEnd();
    }

    for (FunctionAst func : functionAsts) {
      func.compile(this, cw);
    }

    cw.visitEnd();
    return cw.toByteArray();
  }

  public String getModuleJVMDescriptor() {
    return "L" + getInternalJavaName() + ";";
  }
}
