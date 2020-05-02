package lambda.rodeo.lang.typed.statements;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compileable.statement.CompileableAssignment;
import org.objectweb.asm.MethodVisitor;

public interface TypedAssignment {
  CompileableAssignment toCompileableAssignment();
}
