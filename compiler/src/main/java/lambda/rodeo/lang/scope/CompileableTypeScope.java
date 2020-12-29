package lambda.rodeo.lang.scope;

import java.util.List;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.runtime.lambda.Lambda0;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Getter
@Builder
@EqualsAndHashCode
public class CompileableTypeScope {

  @NonNull
  private final List<Entry> scope;

  @Builder
  @Getter
  public static class Entry {

    private final String name;
    private final CompileableType type;
    private final int index;
  }

  public void compile(MethodVisitor methodVisitor, Label start, Label end) {
    for (Entry scopeEntry : scope) {
      if (scopeEntry.getIndex() == -1) {
        continue;
      }

      methodVisitor.visitLocalVariable(
          scopeEntry.getName(),
          scopeEntry.getType().getLambdaDescriptor(),
          null,
          start,
          end,
          scopeEntry.getIndex());
    }
  }
}
