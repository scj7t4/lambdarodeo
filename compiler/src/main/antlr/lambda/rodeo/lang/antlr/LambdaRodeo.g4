// Define a grammar called Hello
grammar LambdaRodeo;
@header {
    package lambda.rodeo.lang.antlr;
}
//TODO: Choose what's next, duck types? Lambdas?
module: 'module' moduleIdentifier moduleBody;
moduleIdentifier: IDENTIFIER | SCOPED_IDENTIFIER;
moduleBody: '{' moduleEntry*'}';
moduleEntry: functionDef | interfaceDef;

functionDef: 'def' functionSig functionBody;
functionSig: functionName functionArgs '=>' returnType;
functionBody: '{' patternCase+ '}' | '{' statement+ '}';
functionName: IDENTIFIER;
functionArgs:
  | '(' ')'
  | '(' (typedVar (',' typedVar)*) ')';
typedVar: varName ':' varType;
varName: IDENTIFIER;
varType: typeExpression;

returnType: typeExpression;
typeExpression: intType
  | atom
  | lambdaTypeExpression;
intType: 'int';
lambdaTypeExpression: '(' (typeExpression (',' typeExpression)*)? ')' '=>' typeExpression;

patternCase: 'case' '(' caseArg (',' caseArg)* ')' '{' statement+ '}';
caseArg: caseLiteral
  | caseVarName
  | caseWildCard;
caseWildCard: '*';
caseLiteral: literal;
caseVarName: varName;

statement: assignment? expr ';';
atom: ':'IDENTIFIER;
expr
  : '(' expr ')' #parenthetical
  // Not sure about putting the function call priority here... not sure of the implications
  // For order of operations
  | expr '(' (expr (',' expr)*)? ')' #functionCall
  | ('-') expr #unaryMinus
  | expr multiDivOp expr #multiDiv
  | expr addSubOp expr #addSub
  | literal #literalExpr
  | identifier #identifierExpr
  | lambda #lambdaFn;
literal: atom
       | intLiteral;
intLiteral: '-'?INT_LITERAL;
identifier: IDENTIFIER | SCOPED_IDENTIFIER;
addSubOp: ('+'|'-');
multiDivOp: ('*'|'/');
lambda: '(' (lambdaTypedVar (',' lambdaTypedVar)*)? ')' '=>' ('{' lambdaStatement+ '}' | lambdaExpr);
lambdaTypedVar: varName ':' varType;
lambdaStatement: assignment? expr ';';
lambdaExpr: expr;

assignment: 'let' IDENTIFIER '=';

interfaceDef: 'interface' '{' memberDecl* '}';
memberDecl: typedVar ';';

INT_LITERAL: [0-9]+;
STATEMENT: [^;]* ';';
IDENTIFIER: [a-zA-Z][a-zA-Z0-9_]*;
SCOPED_IDENTIFIER: IDENTIFIER(('.'IDENTIFIER)*);
WS: [ \t\r\n]+ -> skip; // skip spaces, tabs, newlines