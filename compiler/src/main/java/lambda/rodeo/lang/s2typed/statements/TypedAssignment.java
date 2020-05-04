package lambda.rodeo.lang.s2typed.statements;

import lambda.rodeo.lang.s3compileable.statement.CompileableAssignment;

public interface TypedAssignment {
  CompileableAssignment toCompileableAssignment();
}
