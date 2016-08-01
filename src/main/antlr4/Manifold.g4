grammar Manifold;

// TODO type identifier vs value identifier

WHITESPACE: [ \t\r\n]+ -> skip;

COMMENT: '/*' .*? '*/' -> skip;

LINE_COMMENT: '//' ~[\r\n]* -> skip;

////////////////////////////////////////////////////////
//                                                    //
//                        Values                      //
//                                                    //
////////////////////////////////////////////////////////

INTEGER_VALUE: [0-9]+;
BOOLEAN_VALUE: 'false' | 'true';
TYPE_KEYWORD: 'Type';
STRING_VALUE: '"' ( '\"' | ~["] )*? '"';

VISIBILITY_PUBLIC: 'public';

tupleTypeValueEntry: (IDENTIFIER ':')? typevalue ('=' expression)?;
tupleTypeValue: '(' tupleTypeValueEntry (',' tupleTypeValueEntry)* ')';

tupleValueEntry: (IDENTIFIER '=')? expression;
tupleValue:
  '(' tupleValueEntry (',' tupleValueEntry)* ')' |
  '(' ')';

listValueEntry: (IDENTIFIER '=')? expression;
listValue:
  '[' listValueEntry (',' listValueEntry)* ']' |
  '[' ']';

functionTypeValue: tupleTypeValue '->' tupleTypeValue;
functionValue: functionTypeValue '{' (expression EXPRESSION_TERMINATOR)* '}';

////////////////////////////////////////////////////////
//                                                    //
//                     Identifiers                    //
//                                                    //
////////////////////////////////////////////////////////

IDENTIFIER: [a-zA-Z] [0-9a-zA-Z_]*;
namespacedIdentifier: (IDENTIFIER '::')* IDENTIFIER;

////////////////////////////////////////////////////////
//                                                    //
//                     Expressions                    //
//                                                    //
////////////////////////////////////////////////////////

type:
    namespacedIdentifier # Typename
  | tupleTypeValue # TupleType
  | type '[' ']' # ListType
  ;

typevalue:
    type # SimpleType
  | functionTypeValue # FunctionType
  ;

undefinedDeclaration: type namespacedIdentifier;
typeDeclaration: TYPE_KEYWORD namespacedIdentifier '=' type;

declaration:
    typeDeclaration
  | undefinedDeclaration
  ;

reference:
    tupleValue # Tuple
  | reference '.' IDENTIFIER # StaticAttributeAccessExpression
  | reference '[' INTEGER_VALUE ']' # StaticAttributeAccessExpression
  | namespacedIdentifier # VariableReferenceExpression
  ;

// TODO implement arglist for multiple parameter passing to functions
// arglist:
//     rvalue arglist | ;

rvalue:
    BOOLEAN_VALUE # Boolean
  | INTEGER_VALUE # Integer
  | functionValue # Function
  | VISIBILITY_PUBLIC? lvalue '=' rvalue # AssignmentExpression
  | reference rvalue # FunctionInvocationExpression // TODO: function invocation needs to be 'reference arglist'
  | reference # ReferenceExpression
  | 'primitive' 'port' typevalue (':' tupleTypeValue)? # PrimitivePortDefinitionExpression
  | 'primitive' 'node' functionTypeValue # PrimitiveNodeDefinitionExpression
  | 'import' STRING_VALUE #ImportExpr
  ;

lvalue:
    undefinedDeclaration # AssignmentDeclaration
  | reference # LValueExpression
  ;

expression:
    declaration
  | rvalue
  ;

EXPRESSION_TERMINATOR: ';';


////////////////////////////////////////////////////////
//                                                    //
//                      Schematic                     //
//                                                    //
////////////////////////////////////////////////////////

schematic: (expression EXPRESSION_TERMINATOR)*;
