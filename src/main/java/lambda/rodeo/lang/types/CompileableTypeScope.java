package lambda.rodeo.lang.types;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.types.TypeScope.Entry;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

@Getter
@Builder
public class CompileableTypeScope {

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
          org.objectweb.asm.Type.getDescriptor(scopeEntry.getType().getType().javaType()),
          null,
          start,
          end,
          scopeEntry.getIndex());
    }
  }
}
