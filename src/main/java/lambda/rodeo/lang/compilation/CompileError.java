package lambda.rodeo.lang.compilation;

import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;

@Builder
@Getter
public class CompileError {

  public static final String UNDEFINED_VAR = "UNDEFINED_VARIABLE";
  public static final String POINTLESS_ASSIGNMENT = "POINTLESS_ASSIGNMENT";
  public static final String ILLEGAL_MATH_OPERATION = "ILLEGAL_MATH_OPERATION";

  private final int startLine;
  private final int endLine;
  private final int characterStart;
  private final String errorType;
  private final String errorMsg;

  public static CompileError undefinedVariableError(String variableName,
     AstNode astNode) {
    return CompileError.builder()
        .errorType(UNDEFINED_VAR)
        .errorMsg("Variable '" + variableName + "' is not defined in the current scope")
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
}
