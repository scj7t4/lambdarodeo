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
typeExpression: intType | atom;
intType: 'int';
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
  | ('-') expr #unaryMinus
  | expr multiDivOp expr #multiDiv
  | expr addSubOp expr #addSub
  | literal #literalExpr
  | functionCall #functionCallExpr
  | identifier #identifierExpr
  | lambda #lambdaFn;
literal: atom
       | intLiteral;
intLiteral: '-'?INT_LITERAL;
identifier: IDENTIFIER;
addSubOp: ('+'|'-');
multiDivOp: ('*'|'/');
lambda: '(' (lambdaTypedVar (',' lambdaTypedVar)*)? ')' '=>' ('{' lambdaStatement+ '}' | lambdaExpr);
lambdaTypedVar: varName ':' varType;
lambdaStatement: assignment? expr ';';
lambdaExpr: expr;

assignment: 'let' IDENTIFIER '=';
functionCall: callTarget '(' (expr (',' expr)*)? ')';
callTarget: (IDENTIFIER | SCOPED_IDENTIFIER);

interfaceDef: 'interface' '{' memberDecl* '}';
memberDecl: typedVar ';';

INT_LITERAL: [0-9]+;
STATEMENT: [^;]* ';';
IDENTIFIER: [a-zA-Z][a-zA-Z0-9_]*;
SCOPED_IDENTIFIER: IDENTIFIER(('.'IDENTIFIER)*);
WS: [ \t\r\n]+ -> skip; // skip spaces, tabs, newlines