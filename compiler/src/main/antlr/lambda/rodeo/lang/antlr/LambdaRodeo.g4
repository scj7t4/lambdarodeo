// Define a grammar called Hello
grammar LambdaRodeo;
@header {
    package lambda.rodeo.lang.antlr;
}
module: moduleBody;
moduleBody: (lrImport)* moduleEntry*;
moduleEntry: functionDef | interfaceDef;
lrImport: moduleImport;
alias: 'as' IDENTIFIER;
moduleImport: 'import' SCOPED_IDENTIFIER alias? ';';


interfaceDef: 'interface' IDENTIFIER '{' memberDecl* '}';
memberDecl: typedVar ';';

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
  | stringType
  | atom
  | lambdaTypeExpression
  | definedType;
intType: 'int';
stringType: 'string';
lambdaTypeExpression: '(' (typeExpression (',' typeExpression)*)? ')' '=>' typeExpression;
definedType: identifier;

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
  | lambda #lambdaFn
  | object #objectCreate;
literal: atom
       | intLiteral
       | stringLiteral;
       // TODO: support elixir sigils?

intLiteral: '-'?INT_LITERAL;
stringLiteral: DOUBLEQSTRING;

identifier: IDENTIFIER | SCOPED_IDENTIFIER;
addSubOp: ('+'|'-');
multiDivOp: ('*'|'/');
lambda: '(' (lambdaTypedVar (',' lambdaTypedVar)*)? ')' '=>' ('{' lambdaStatement+ '}' | lambdaExpr);
lambdaTypedVar: varName ':' varType;
lambdaStatement: assignment? expr ';';
lambdaExpr: expr;

object: '{' objectExpr* '}';
objectExpr
  : objectMember;
  // TODO: spread operator
objectMember: IDENTIFIER ':' expr;

assignment: 'let' IDENTIFIER '=';

INT_LITERAL: [0-9]+;
STATEMENT: [^;]* ';';
IDENTIFIER: [a-zA-Z][a-zA-Z0-9_]*;
SINGLE_LETTER: [a-zA-Z];
SCOPED_IDENTIFIER: IDENTIFIER(('.'IDENTIFIER)*);
WS: [ \t\r\n]+ -> skip; // skip spaces, tabs, newlines
// Opening quote : (anything that isn't a quote or (literal slash
DOUBLEQSTRING: '"' (~["] | '\\"' )* '"';
TRIPLEQSTRING: '"""' (~["] | ('"' ~["]) | '""' ~["])* '"""';

BLOCK_COMMENT
	: '/*' .*? '*/' -> channel(HIDDEN)
	;
LINE_COMMENT
	: '//' ~[\r\n]* -> channel(HIDDEN)
	;