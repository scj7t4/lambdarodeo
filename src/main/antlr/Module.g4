// Define a grammar called Hello
grammar Module;
@header {
    package lambda.rodeo.lang.antlr.module;
}

module: 'module' IDENTIFIER moduleBody;
moduleBody: '{' functionDef* '}';
functionDef: 'def' functionSig functionBody;
functionSig: functionName functionArgs;
functionBody: '{' STATEMENT '}';
functionName: IDENTIFIER;
functionArgs: '(' (|typedIdentifier (',' typedIdentifier)*) ')';
STATEMENT: [^;]* ';';

IDENTIFIER: [a-zA-Z][a-zA-Z_0-9]*;
typedIdentifier: IDENTIFIER ':' IDENTIFIER;
WS: [ \t\r\n]+ -> skip; // skip spaces, tabs, newlines