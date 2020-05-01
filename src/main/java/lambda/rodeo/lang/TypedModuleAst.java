package lambda.rodeo.lang;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.V1_8;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.functions.TypedFunctionAst;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

@Builder
@Getter
public class TypedModuleAst {

  private final ModuleAst moduleAst;
  private final List<TypedFunctionAst> functionAsts;



  public byte[] compile(CompileContext compileContext) {
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

    for (TypedFunctionAst func : functionAsts) {
      func.compile(cw, compileContext);
    }

    cw.visitEnd();
    return cw.toByteArray();
  }

  public Optional<TypedFunctionAst> getFunction(String funcName) {
    return functionAsts.stream()
        .filter(x -> Objects.equals(funcName, x.getName()))
        .findAny();
  }

  public String getInternalJavaName() {
    return getModuleAst().getInternalJavaName();
  }

  public String getModuleJVMDescriptor() {
    return getModuleAst().getModuleJVMDescriptor();
  }
}
