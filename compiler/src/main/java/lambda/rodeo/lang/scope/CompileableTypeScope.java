package lambda.rodeo.lang.scope;

import java.util.List;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

@Getter
@Builder
@EqualsAndHashCode
public class CompileableTypeScope {

  @NonNull
  private final List<CompileableEntry> scope;

  @Builder
  @Getter
  public static class CompileableEntry {

    private final String name;
    private final CompileableType type;
    private final int index;
  }

  public void compile(MethodVisitor methodVisitor, Label start, Label end) {
    for (CompileableEntry scopeEntry : scope) {
      if (scopeEntry.getIndex() == -1) {
        continue;
      }

      methodVisitor.visitLocalVariable(
          scopeEntry.getName(),
          scopeEntry.getType().getDescriptor(),
          null,
          start,
          end,
          scopeEntry.getIndex());
    }
  }
}
