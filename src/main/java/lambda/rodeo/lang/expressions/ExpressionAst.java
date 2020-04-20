package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.ValueHolder;

public interface ExpressionAst {
  Type getType();
  ValueHolder<?> getValueHolder();
}
