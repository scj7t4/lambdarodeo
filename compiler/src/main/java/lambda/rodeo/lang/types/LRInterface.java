package lambda.rodeo.lang.types;

import static org.objectweb.asm.Opcodes.ACC_ABSTRACT;
import static org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.V11;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.type.InterfaceAst;
import lambda.rodeo.lang.s2typed.type.S2TypedVar;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.LRObject;
import lambda.rodeo.runtime.types.LRPackaged;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
public class LRInterface implements LambdaRodeoType, CompileableType, CompilesToInnerClass {

  /**
   * Can be null if the source of the interface is anonymous (like the result of an exprssion)
   */
  private final InterfaceAst from;

  @NonNull
  private final List<S2TypedVar> members;

  @Override
  public void forwardDeclare(String name, ClassWriter classWriter) {
    classWriter.visitNestMember(name);
  }

  @Override
  public Map<String, byte[]> declareAndCompile(String internalName,
      String parentInternalName,
      String name,
      ClassWriter classWriter) {
    classWriter.visitInnerClass(
        internalName,
        parentInternalName,
        name,
        ACC_PUBLIC | ACC_STATIC | ACC_ABSTRACT | ACC_INTERFACE);

    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
    cw.visit(
        V11,
        ACC_PUBLIC | ACC_ABSTRACT | ACC_INTERFACE,
        internalName,
        null,
        "java/lang/Object",
        new String[]{Type.getInternalName(LRPackaged.class)});

    MethodVisitor methodVisitor;

    for (S2TypedVar member : members) {
      String memberName =
          "get" + member.getName().substring(0, 1).toUpperCase() + member.getName().substring(1);
      if (member.getType() instanceof CompileableLambdaType) {
        memberName = member.getName();
      }
      // Figure out the name...
      methodVisitor = classWriter
          .visitMethod(ACC_PUBLIC | ACC_ABSTRACT, "someMethod", "()V", null, null);
      methodVisitor.visitEnd();
    }

    return null;
  }

  @Override
  public LambdaRodeoType getType() {
    return this;
  }

  @Override
  public String getDescriptor() {
    return Type.getDescriptor(LRObject.class);
  }

  @Override
  public String getInternalName() {
    return Type.getInternalName(LRObject.class);
  }

  @Override
  public void provideRuntimeType(MethodVisitor methodVisitor) {
    // Start the builder
    methodVisitor.visitMethodInsn(INVOKESTATIC, "lambda/rodeo/runtime/types/LRInterface", "builder",
        "()Llambda/rodeo/runtime/type/LRInterface$LRInterfaceBuilder;", false);

    // Start the map
    methodVisitor.visitTypeInsn(NEW, "java/util/HashMap");
    methodVisitor.visitInsn(DUP);
    methodVisitor.visitMethodInsn(INVOKESPECIAL,
        Type.getInternalName(HashMap.class),
        "<init>",
        "()V",
        false);

    // For each member push into the map
    for (S2TypedVar member : members) {
      methodVisitor.visitInsn(DUP);
      methodVisitor.visitLdcInsn(member.getName());
      member.getType().provideRuntimeType(methodVisitor);
      methodVisitor.visitMethodInsn(INVOKEINTERFACE,
          Type.getInternalName(Map.class),
          "put",
          "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;",
          true);
      methodVisitor.visitInsn(POP); // Pop because map put returns an item
    }

    // Invoke setting the type map
    methodVisitor
        .visitMethodInsn(INVOKEVIRTUAL, "lambda/rodeo/runtime/types/LRInterface$LRInterfaceBuilder",
            "typeMap",
            "(Ljava/util/Map;)Llambda/rodeo/runtime/type/LRInterface$LRInterfaceBuilder;", false);

    // Invoke build();
    methodVisitor
        .visitMethodInsn(INVOKEVIRTUAL, "lambda/rodeo/runtime/types/LRInterface$LRInterfaceBuilder",
            "build", "()Llambda/rodeo/runtime/type/LRInterface;", false);
  }

  @Override
  public CompileableType toCompileableType(
      TypedModuleScope typedModuleScope,
      CollectsErrors compileContext) {
    return this;
  }

  @Override
  public boolean assignableFrom(CompileableType other) {
    // TODO refactor to provide better reasoning for failures.
    if (!(other instanceof LRInterface)) {
      return false;
    }
    List<S2TypedVar> otherEntries = ((LRInterface) other).getMembers();
    for (S2TypedVar entry : this.members) {
      boolean present = otherEntries.stream()
          .filter(otherEntry -> Objects.equals(otherEntry.getName(), entry.getName()))
          .anyMatch(otherEntry -> entry.getType().assignableFrom(otherEntry.getType()));
      if (!present) {
        return false;
      }
    }
    return true;
  }
}
