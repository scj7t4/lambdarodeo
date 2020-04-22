package lambda.rodeo.lang.values;

import lambda.rodeo.lang.statements.Scope;

public interface Computable<T> {

  T compute(Scope scope);
}
