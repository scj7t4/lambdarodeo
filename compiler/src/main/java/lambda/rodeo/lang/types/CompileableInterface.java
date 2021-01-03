package lambda.rodeo.lang.types;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.POP;


import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.type.InterfaceAst;
import lambda.rodeo.lang.s2typed.type.S2TypedVar;
import lambda.rodeo.lang.scope.Entry;
import lambda.rodeo.lang.scope.TypeResolver;
import lambda.rodeo.lang.util.FunctionDescriptorBuilder;
import lambda.rodeo.runtime.types.LRObject;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
@EqualsAndHashCode
public class CompileableInterface implements LambdaRodeoType, CompileableType {

  /**
   * Can be null if the source of the interface is anonymous (like the result of an exprssion)
   */
  @EqualsAndHashCode.Exclude
  private final InterfaceAst from;

  @NonNull
  private final LinkedHashSet<S2TypedVar> members;

  @Override
  public CompileableType toCompileableType(
      TypeResolver typeResolver,
      CollectsErrors compileContext) {
    return this;
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
  public boolean assignableFrom(CompileableType other) {
    // TODO refactor to provide better reasoning for failures.
    if (!(other instanceof CompileableInterface)) {
      return false;
    }
    Collection<S2TypedVar> otherEntries = ((CompileableInterface) other).getMembers();
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

  @Override
  public void provideRuntimeType(MethodVisitor methodVisitor) {
    // Set up that we want to make an LR interface
    String runtimeType = Type.getInternalName(lambda.rodeo.runtime.types.LRInterface.class);
    methodVisitor.visitTypeInsn(NEW,
        runtimeType);
    methodVisitor.visitInsn(DUP);

    // The map is the one arg for for the CTOR so make that
    methodVisitor.visitTypeInsn(NEW, "java/util/HashMap");
    methodVisitor.visitInsn(DUP);
    methodVisitor.visitMethodInsn(INVOKESPECIAL,
        Type.getInternalName(HashMap.class),
        "<init>",
        "()V",
        false);
    for (S2TypedVar member : members) {
      // DUP to keep the map on the top of the stack after we put into it
      methodVisitor.visitInsn(DUP);
      methodVisitor.visitLdcInsn(member.getName());
      member.getType().provideRuntimeType(methodVisitor);
      methodVisitor.visitMethodInsn(INVOKEINTERFACE,
          Type.getInternalName(Map.class),
          "put",
          FunctionDescriptorBuilder.args(Object.class, Object.class).returns(Object.class),
          true);
      methodVisitor.visitInsn(POP);
    }
    methodVisitor.visitMethodInsn(INVOKESPECIAL,
        runtimeType,
        "<init>",
        "(Ljava/util/Map;)V", false);
  }

  @Override
  public Optional<Entry> getMemberEntry(Entry parent, String name) {
    return members.stream()
        .filter(member -> Objects.equals(member.getName(), name))
        .map(member -> LRInterfaceEntry.builder()
            .type(member.getType())
            .name(name)
            .parent(parent)
            .build())
        .findFirst()
        .map(entry -> entry);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("LRInterface<{");
    for (S2TypedVar member : members) {
      sb.append(member.getName()).append(": ").append(member.getType()).append("; ");
    }
    if (!members.isEmpty()) {
      sb.setLength(sb.length() - 2);
    }
    return sb.append("}>").toString();
  }
}
