package lambda.rodeo.lang.compileable.expression;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.typed.expressions.TypedExpression;
import org.objectweb.asm.MethodVisitor;

public interface CompileableExpr extends Compileable {

  TypedExpression getTypedExpression();
}
