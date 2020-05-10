package lambda.rodeo.lang.compilation;

import lambda.rodeo.lang.AstNode;
import lambda.rodeo.runtime.types.Type;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CompileError {

  public static final String UNDEFINED_VAR = "UNDEFINED_VARIABLE";
  public static final String POINTLESS_ASSIGNMENT = "POINTLESS_ASSIGNMENT";
  public static final String ILLEGAL_MATH_OPERATION = "ILLEGAL_MATH_OPERATION";
  public static final String NOT_A_FUNCTION = "NOT_A_FUNCTION";
  public static final String IDENTIFIER_ALREADY_IN_SCOPE = "ALREADY_IN_SCOPE";

  private final int startLine;
  private final int endLine;
  private final int characterStart;
  private final String errorType;
  private final String errorMsg;

  @Override
  public String toString() {
    return errorType + ":L" + startLine + "->" + endLine + ":" + characterStart + ":: " + errorMsg;
  }

  public static CompileError undefinedIdentifier(String identifier,
     AstNode astNode) {
    return CompileError.builder()
        .errorType(UNDEFINED_VAR)
        .errorMsg("Identifier '" + identifier + "' is not defined in the current scope")
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }

  public static CompileError triedToCallNonFunction(String identifier, AstNode astNode) {
    return CompileError.builder()
        .errorType(NOT_A_FUNCTION)
        .errorMsg("Identifier '"+ identifier +"' is not a function")
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }

  public static CompileError lastStatementCannotBeAssignment(AstNode astNode) {
    return CompileError.builder()
        .errorType(POINTLESS_ASSIGNMENT)
        .errorMsg("Assigning to a variable as the last statement of a function body is pointless")
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }

  public static CompileError mathOperationWithNonNumeric(AstNode astNode,
      String operation, Type lhsType, Type rhsType) {
    return CompileError.builder()
        .errorType(ILLEGAL_MATH_OPERATION)
        .errorMsg("Cannot do " + operation + " with '" + lhsType + "' and '" + rhsType + "'")
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }

  public static CompileError identifierAlreadyDeclaredInScope(AstNode astNode, String identifier) {
    return CompileError.builder()
        .errorType(IDENTIFIER_ALREADY_IN_SCOPE)
        .errorMsg("Cannot declare '" + identifier + "' it is already defined in this scope")
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }
}
