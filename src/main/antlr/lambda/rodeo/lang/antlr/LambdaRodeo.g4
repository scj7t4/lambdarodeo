// Define a grammar called Hello
grammar LambdaRodeo;
@header {
    package lambda.rodeo.lang.antlr;
}

module: 'module' moduleIdentifier moduleBody;
moduleIdentifier: IDENTIFIER | SCOPED_IDENTIFIER;
moduleBody: '{' moduleEntry*'}';
moduleEntry: functionDef | interfaceDef;

functionDef: 'def' functionSig functionBody;
functionSig: functionName functionArgs;
functionBody: '{' statement+ '}';
functionName: IDENTIFIER;
functionArgs:
  | '(' ')'
  | '(' (typedVar (',' typedVar)*) ')';
typedVar: varName ':' varType;
varName: IDENTIFIER;
varType: typeExpression;
typeExpression: intType | atom;
intType: 'int';

statement: expr ';';
expr
  : '(' expr ')'
  | atom
  | mathExpression
  | intLiteral
  | identifier
  ;
atom: ':'IDENTIFIER;
mathExpression
  : addition
  | subtraction
  | division
  | multiplication;
addition: (INT_LITERAL | IDENTIFIER) '+' expr;
subtraction: (INT_LITERAL | IDENTIFIER) '-' expr;
division: (INT_LITERAL | IDENTIFIER) '/' expr;
multiplication: (INT_LITERAL | IDENTIFIER) '*' expr;
intLiteral: INT_LITERAL;
identifier: IDENTIFIER;

interfaceDef: 'interface' '{' memberDecl* '}';
memberDecl: typedVar ';';

INT_LITERAL: '-'?[0-9]+;
STATEMENT: [^;]* ';';
IDENTIFIER: [a-zA-Z][a-zA-Z0-9_]*;
SCOPED_IDENTIFIER: IDENTIFIER(('.'IDENTIFIER)*);
WS: [ \t\r\n]+ -> skip; // skip spaces, tabs, newlines