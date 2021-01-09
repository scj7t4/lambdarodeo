package lambda.rodeo.lang.s1ast.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.s2typed.type.TypedTypeDef;
import lambda.rodeo.lang.scope.BoundGenericsTypeResolver;
import lambda.rodeo.lang.scope.TypeResolver;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.types.LambdaRodeoType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypeDef implements AstNode {

  private final String identifier;
  private final LambdaRodeoType type;
  @Builder.Default
  private final List<TypedVar> generics = Collections.emptyList();
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  public TypedTypeDef toTypedTypeDef(
      TypeResolver typeResolver,
      S2CompileContext compileContext) {

    List<CompileableType> defaultBindings = generics.stream()
        .map(TypedVar::getType)
        .map(type -> type.toCompileableType(typeResolver, compileContext))
        .collect(Collectors.toList());

    TypeResolver boundGenerics = bindGenerics(this, typeResolver, defaultBindings, compileContext);

    return TypedTypeDef.builder()
        .from(this)
        .type(type.toCompileableType(boundGenerics, compileContext))
        .build();
  }

  public TypeResolver bindGenerics(
      AstNode bindPoint,
      TypeResolver typeResolver,
      List<CompileableType> bindings,
      CollectsErrors compileContext) {
    if (bindings.size() != generics.size()) {
      compileContext.getCompileErrorCollector()
          .collect(CompileError.incorrectNumberOfTypeParams(bindPoint,
              generics.size(), bindings.size()));
    }
    // If bindings is too short, we will plug in the minimum typings in order to continue...
    bindings = new ArrayList<>(bindings);
    for (int i = bindings.size(); i < generics.size(); i++) {
      bindings.add(generics.get(i).getType().toCompileableType(typeResolver, compileContext));
    }
    Map<String, TypeDef> subs = new HashMap<>();
    for (int i = 0; i < generics.size(); i++) {
      TypedVar param = generics.get(i);
      CompileableType sub = bindings.get(i);
      CompileableType minType = param.getType()
          .toCompileableType(typeResolver, compileContext);
      if (!minType.assignableFrom(sub)) {
        compileContext.getCompileErrorCollector()
            .collect(CompileError.incompatibleTypeSubstitution(bindPoint,
                sub, minType));
      }

      TypeDef typeDef = TypeDef.builder()
          .generics(Collections.emptyList())
          .type(sub.getType())
          .identifier(param.getName())
          .build();
      subs.put(param.getName(), typeDef);
    }
    return BoundGenericsTypeResolver.builder()
        .overrides(subs)
        .parent(typeResolver)
        .build();
  }
}
