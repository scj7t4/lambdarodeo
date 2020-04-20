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
atom: ':'IDENTIFIER;
expr
  : '(' expr ')' #parenthetical
  | <assoc=right> expr '*' expr #multiply
  | <assoc=right> expr '/' expr #divide
  | <assoc=right> expr '+' expr #add
  | <assoc=right> expr '-' expr #subtract
  | literal #literalExpr
  | identifier #identifierExpr;
literal: atom
       | intLiteral;
intLiteral: INT_LITERAL;
identifier: IDENTIFIER;

interfaceDef: 'interface' '{' memberDecl* '}';
memberDecl: typedVar ';';

INT_LITERAL: '-'?[0-9]+;
STATEMENT: [^;]* ';';
IDENTIFIER: [a-zA-Z][a-zA-Z0-9_]*;
SCOPED_IDENTIFIER: IDENTIFIER(('.'IDENTIFIER)*);
WS: [ \t\r\n]+ -> skip; // skip spaces, tabs, newlines