grammar Delphi;

// --- 1. Top-level structure ---
program
    : PROGRAM identifier SEMI
      (typeSection)?
      (varSection)?
      (implementationSection)?
      compoundStatement
      DOT
    ;

// --- 2. Type and class declarations ---
typeSection : TYPE typeDefinition+ ;
typeDefinition : identifier EQUAL (type_ | classType | interfaceType) SEMI ;

// ========== Class definition (supports inheritance) ==========
classType
    : CLASS (LPAREN identifier RPAREN)?  // Supports inheritance: class(TBaseClass)
      (interfaceList)?                    // Supports interface implementation
      memberList?
      END
    ;

// ========== Interface definition ==========
interfaceType
    : INTERFACE
      interfaceMemberList?
      END
    ;

interfaceList : LPAREN identifier (COMMA identifier)* RPAREN ;

interfaceMemberList : interfaceMethodDeclaration+ ;

interfaceMethodDeclaration
    : PROCEDURE identifier (formalParameters)? SEMI
    | FUNCTION identifier (formalParameters)? COLON type_ SEMI
    ;

memberList : (visibilitySpecifier? memberContent)+ ;

memberContent
    : identifierList COLON type_ SEMI  // Variable/field declaration
    | methodDeclaration                // Method declaration
    ;

visibilitySpecifier : PUBLIC | PRIVATE | PROTECTED | PUBLISHED ;

methodDeclaration
    : CONSTRUCTOR identifier (formalParameters)? SEMI
    | DESTRUCTOR identifier SEMI
    | PROCEDURE identifier (formalParameters)? SEMI
    | FUNCTION identifier (formalParameters)? COLON type_ SEMI
    ;

// --- 3. Global variable declarations ---
varSection : VAR variableDeclaration+ ;
variableDeclaration : identifierList COLON type_ SEMI ;
identifierList : identifier (COMMA identifier)* ;

// --- 4. Method implementations ---
implementationSection : methodImplementation+ ;
methodImplementation
    : CONSTRUCTOR qualifiedIdentifier (formalParameters)? SEMI
      (varSection)?
      compoundStatement
      SEMI
    | DESTRUCTOR qualifiedIdentifier SEMI
      (varSection)?
      compoundStatement
      SEMI
    | PROCEDURE qualifiedIdentifier (formalParameters)? SEMI
      (varSection)?
      compoundStatement
      SEMI
    | FUNCTION qualifiedIdentifier (formalParameters)? COLON type_ SEMI
      (varSection)?
      compoundStatement
      SEMI
    ;

// --- 5. Statement logic ---
compoundStatement : BEGIN statementList END ;
statementList : (nonEmptyStatement (SEMI nonEmptyStatement)*)? (SEMI)? ;

nonEmptyStatement
    : assignment
    | methodCall
    | ifStatement
    | compoundStatement
    ;

ifStatement : IF expression THEN nonEmptyStatement (ELSE nonEmptyStatement)? ;

assignment : qualifiedIdentifier ASSIGN expression ;
methodCall : qualifiedIdentifier (LPAREN expressionList? RPAREN)? ;

// --- 6. Expressions (layered to support comparisons) ---
expressionList : expression (COMMA expression)* ;

expression : relExpression ( (AND | OR) relExpression )* ;

relExpression : additiveExpression ( (EQUAL | LT | GT | LE | GE | NOT_EQUAL) additiveExpression )? ;

additiveExpression : term ( (PLUS | MINUS) term )* ;

term : factor ( (MUL | DIV) factor )* ;

factor
    : INT_LITERAL
    | STRING_LITERAL
    | methodCall
    | qualifiedIdentifier
    | LPAREN expression RPAREN
    ;

// --- 7. Basic rules ---
type_ : INTEGER_TYPE | STRING_TYPE | identifier ;
identifier : IDENTIFIER ;
qualifiedIdentifier : identifier (DOT identifier)* ;
formalParameters : LPAREN (parameterGroup (SEMI parameterGroup)*)? RPAREN ;
parameterGroup : identifierList COLON type_ ;

// --- 8. Lexer rules ---
// Keywords
PROGRAM: [Pp][Rr][Oo][Gg][Rr][Aa][Mm];
VAR: [Vv][Aa][Rr];
TYPE: [Tt][Yy][Pp][Ee];
BEGIN: [Bb][Ee][Gg][Ii][Nn];
END: [Ee][Nn][Dd];
CLASS: [Cc][Ll][Aa][Ss][Ss];
INTERFACE: [Ii][Nn][Tt][Ee][Rr][Ff][Aa][Cc][Ee];  // Added
CONSTRUCTOR: [Cc][Oo][Nn][Ss][Tt][Rr][Uu][Cc][Tt][Oo][Rr];
DESTRUCTOR: [Dd][Ee][Ss][Tt][Rr][Uu][Cc][Tt][Oo][Rr];
PROCEDURE: [Pp][Rr][Oo][Cc][Ee][Dd][Uu][Rr][Ee];
FUNCTION: [Ff][Uu][Nn][Cc][Tt][Ii][Oo][Nn];
IF: [Ii][Ff];
THEN: [Tt][Hh][Ee][Nn];
ELSE: [Ee][Ll][Ss][Ee];

// Visibility keywords
PUBLIC: [Pp][Uu][Bb][Ll][Ii][Cc];
PRIVATE: [Pp][Rr][Ii][Vv][Aa][Tt][Ee];
PROTECTED: [Pp][Rr][Oo][Tt][Ee][Cc][Tt][Ee][Dd];
PUBLISHED: [Pp][Uu][Bb][Ll][Ii][Ss][Hh][Ee][Dd];

INTEGER_TYPE: [Ii][Nn][Tt][Ee][Gg][Ee][Rr];
STRING_TYPE: [Ss][Tt][Rr][Ii][Nn][Gg];

// Operators
ASSIGN: ':=';
PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
COLON: ':';
SEMI: ';';
COMMA: ',';
DOT: '.';
EQUAL: '=';
NOT_EQUAL: '<>';
LT: '<';
GT: '>';
LE: '<=';
GE: '>=';
AND: [Aa][Nn][Dd];
OR: [Oo][Rr];
LPAREN: '(';
RPAREN: ')';

// Identifiers and literals
IDENTIFIER: [a-zA-Z_] [a-zA-Z0-9_]*;
INT_LITERAL: [0-9]+;
STRING_LITERAL: '\'' ( ~['\r\n] | '\'\'' )* '\'';

// Ignored content
LINE_COMMENT : '//' ~[\r\n]* -> skip;
BLOCK_COMMENT : '{' .*? '}' -> skip;
WS : [ \t\r\n]+ -> skip;