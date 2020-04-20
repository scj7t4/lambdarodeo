package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.ValueHolder;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ConstantExpr<T> implements ExpressionAst {
  ValueHolder<T> valueHolder;
  Type type;
}
