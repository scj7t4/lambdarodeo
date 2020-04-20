package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.ValueHolder;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConstantExpr<T> implements ExpressionAst {
  ValueHolder<T> valueHolder;
  Type type;
}
