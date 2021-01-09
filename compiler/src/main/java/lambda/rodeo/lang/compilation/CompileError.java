package lambda.rodeo.lang.compilation;

import java.util.List;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.types.LambdaRodeoType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;

@Builder
@Getter
@EqualsAndHashCode
public class CompileError {

  public static final String UNDEFINED_VAR = "UNDEFINED_VARIABLE";
  public static final String POINTLESS_ASSIGNMENT = "POINTLESS_ASSIGNMENT";
  public static final String ILLEGAL_MATH_OPERATION = "ILLEGAL_MATH_OPERATION";
  public static final String NOT_A_FUNCTION = "NOT_A_FUNCTION";
  public static final String IDENTIFIER_ALREADY_IN_SCOPE = "ALREADY_IN_SCOPE";
  public static final String RETURN_TYPE_MISMATCH = "RETURN_TYPE_MISMATCH";
  public static final String CALLED_WITH_WRONG_ARGS = "CALLED_WITH_WRONG_ARGS";
  public static final String BAD_MODULE_IMPORT = "BAD_MODULE_IMPORT";
  public static final String INCORRECT_NUM_TYPE_PARAMS = "NOT_ENOUGH_TYPE_PARAMS";
  public static final String INCOMPATIBLE_TYPE_SUBSTITUTION = "INCOMPATIBLE_TYPE_SUBSTITUTION";

  private final int startLine;
  private final int endLine;
  private final int characterStart;
  private final String errorType;
  private final String errorMsg;

  @Override
  public String toString() {
    return errorType + ":L" + startLine + "->" + endLine + ":" + characterStart + ":: " + errorMsg;
  }

  public static CompileError undefinedIdentifier(AstNode astNode, String identifier) {
    return CompileError.builder()
        .errorType(UNDEFINED_VAR)
        .errorMsg("Identifier '" + identifier + "' is not defined in the current scope")
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }

  public static CompileError triedToCallNonFunction(AstNode astNode, String identifier) {
    return CompileError.builder()
        .errorType(NOT_A_FUNCTION)
        .errorMsg("Identifier '" + identifier + "' is not a function")
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }

  public static CompileError calledFunctionWithWrongArgs(AstNode astNode,
      List<CompileableType> expected, List<CompileableType> actual) {
    StringBuilder sb = new StringBuilder("Function called with wrong number/type of args.")
        .append(" Expected: <");
    for(int i = 0; i < expected.size(); i++) {
      sb.append(expected.get(i));
      if(i != expected.size() -1) {
        sb.append(",");
      }
    }
    sb.append(">; Actual: <");
    for(int i = 0; i < actual.size(); i++) {
      sb.append(actual.get(i));
      if(i != actual.size() -1) {
        sb.append(",");
      }
    }
    sb.append(">");

    return CompileError.builder()
        .errorType(CALLED_WITH_WRONG_ARGS)
        .errorMsg(sb.toString())
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }

  public static CompileError triedToCallNonFunction(ParserRuleContext ctx, String identifier) {
    return CompileError.builder()
        .errorType(NOT_A_FUNCTION)
        .errorMsg("Identifier '" + identifier + "' is not a function")
        .startLine(ctx.getStart().getLine())
        .endLine(ctx.getStop().getLine())
        .characterStart(ctx.getStart().getCharPositionInLine())
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
      String operation, CompileableType lhsType, CompileableType rhsType) {
    return CompileError.builder()
        .errorType(ILLEGAL_MATH_OPERATION)
        .errorMsg("Cannot do " + operation + " with '" + lhsType + "' and '" + rhsType + "'")
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }

  public static CompileError mathOperationWithNonNumeric(AstNode astNode,
      String operation, CompileableType type) {
    return CompileError.builder()
        .errorType(ILLEGAL_MATH_OPERATION)
        .errorMsg("Cannot do " + operation + " with '" + type)
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

  public static CompileError returnTypeMismatch(AstNode astNode, LambdaRodeoType declared, LambdaRodeoType actual) {
    return CompileError.builder()
        .errorType(RETURN_TYPE_MISMATCH)
        .errorMsg(
            "Pattern case returns '" + actual + "'; it cannot be assigned to '" + declared + "'")
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }

  public static CompileError returnTypeMismatch(AstNode astNode, CompileableType declared, CompileableType actual) {
    return CompileError.builder()
        .errorType(RETURN_TYPE_MISMATCH)
        .errorMsg(
            "Pattern case returns '" + actual + "'; it cannot be assigned to '" + declared + "'")
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }

  public static CompileError badImport(AstNode astNode, String badTarget) {
    return CompileError.builder()
        .errorType(BAD_MODULE_IMPORT)
        .errorMsg("Cannot find module '" + badTarget + "'")
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }

  public static CompileError incorrectNumberOfTypeParams(AstNode astNode, int desiredNum, int actualNum) {
    return CompileError.builder()
        .errorType(INCORRECT_NUM_TYPE_PARAMS)
        .errorMsg("Generic type expected " + desiredNum + " parameters, definition only set "
          + actualNum)
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }

  public static CompileError incompatibleTypeSubstitution(AstNode astNode, CompileableType target,
      CompileableType typeThatCannotBeAssigned) {
    return CompileError.builder()
        .errorType(INCOMPATIBLE_TYPE_SUBSTITUTION)
        .errorMsg("The type '" + target + "' cannot be assigned to '" + typeThatCannotBeAssigned
            + "'")
        .startLine(astNode.getStartLine())
        .endLine(astNode.getEndLine())
        .characterStart(astNode.getCharacterStart())
        .build();
  }
}
