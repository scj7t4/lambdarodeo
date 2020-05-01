package lambda.rodeo.lang.expressions;


import static lambda.rodeo.lang.types.Atom.UNDEFINED_VAR;
import static org.objectweb.asm.Opcodes.ALOAD;

import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.statements.TypeScope.Entry;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class VariableAst implements ExpressionAst {

  private final String name;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public TypedExpressionAst toTypedExpressionAst(
      TypeScope typeScope,
      CompileContext compileContext) {
    Optional<Entry> entry = typeScope.get(name);
    Type type = entry
        .map(Entry::getType)
        .orElse(UNDEFINED_VAR);
    //TODO: Fix context...
    if (Objects.equals(UNDEFINED_VAR, type)) {
      compileContext.getCompileErrorCollector()
          .collect(CompileError.undefinedVariableError(name, null));
      return AtomAst.builder()
          .atom(UNDEFINED_VAR)
          .build()
          .toTypedExpressionAst(typeScope, compileContext);
    } else {
      return TypedVariableAst.builder()
          .scopeEntry(entry.get())
          .variableAst(this)
          .build();
    }
  }
}
