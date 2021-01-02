package lambda.rodeo.lang.types;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.type.InterfaceAst;
import lambda.rodeo.lang.s2typed.type.S2TypedVar;
import lambda.rodeo.lang.scope.Entry;
import lambda.rodeo.lang.scope.TypedModuleScope;
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

  @Override
  public CompileableType toCompileableType(
      TypedModuleScope typedModuleScope,
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

  @Override
  public void provideRuntimeType(MethodVisitor methodVisitor) {

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
