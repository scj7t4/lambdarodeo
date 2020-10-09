package lambda.rodeo.lang.types;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lambda.rodeo.lang.s1ast.type.InterfaceAst;
import lambda.rodeo.lang.s2typed.types.S2TypedVar;
import lambda.rodeo.runtime.types.LRObject;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
public class LRInterface implements LambdaRodeoType, CompileableType {

  /**
   * Can be null if the source of the interface is anonymous (like the result of an exprssion)
   */
  private final InterfaceAst from;

  @NonNull
  private final List<S2TypedVar> members;

  public void compile() {

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
    methodVisitor.visitMethodInsn(INVOKESTATIC, "lambda/rodeo/runtime/types/LRInterface", "builder", "()Llambda/rodeo/runtime/types/LRInterface$LRInterfaceBuilder;", false);

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
    methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "lambda/rodeo/runtime/types/LRInterface$LRInterfaceBuilder", "typeMap", "(Ljava/util/Map;)Llambda/rodeo/runtime/types/LRInterface$LRInterfaceBuilder;", false);

    // Invoke build();
    methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "lambda/rodeo/runtime/types/LRInterface$LRInterfaceBuilder", "build", "()Llambda/rodeo/runtime/types/LRInterface;", false);
  }

  @Override
  public CompileableType toCompileableType() {
    return this;
  }

  @Override
  public boolean assignableFrom(LambdaRodeoType other) {
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
