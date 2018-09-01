parser grammar ExpresionesParser;

options {
    tokenVocab=ExpresionesLexer;
}

root : expression EOF;

expression
    :
    '(' expression ')'                                  # ParenExpression
    | left=expression '[].' right=expression            # AnyMemberExpression
    | left=expression '.' right=expression              # MemberExpression
    | left=expression ' in ' right=expression           # InExpression
    | left=expression '[' right=expression ']'          # IndexExpression
    | left=expression '<' right=expression              # LessThanExpression
    | left=expression '>' right=expression              # GreaterThanExpression
    | left=expression '<=' right=expression             # LessThanOrEqualsExpression
    | left=expression '>=' right=expression             # GreaterThanOrEqualsExpression
    | left=expression '==' right=expression             # EqualsExpression
    | left=expression '!=' right=expression             # NotEqualsExpression
    | left=expression '&&' right=expression             # BooleanAndExpression
    | left=expression '||' right=expression             # BooleanOrExpression
    | '!' expression                                    # LogicalNotExpression
    | 'true'                                            # BooleanLiteralExpression
    | 'false'                                           # BooleanLiteralExpression
    | Identifier                                        # IdentifierExpression
    | StringLiteral                                     # StringLiteralExpression
    | StringLiteralQuoted                               # StringLiteralQuotedExpression
    | IntegerLiteral                                    # IntegerLiteralExpression
    | DecimalLiteral                                    # DecimalLiteralExpression
    ;