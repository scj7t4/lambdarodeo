package lambda.rodeo.lang.s1ast.type;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CollectsErrors;
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
public class TypeDef {

  private final String identifier;
  private final LambdaRodeoType type;
  @Builder.Default
  private final List<TypedVar> generics = Collections.emptyList();

  public TypedTypeDef toTypedTypeDef(
      TypeResolver typeResolver,
      S2CompileContext compileContext) {

    List<CompileableType> defaultBindings = generics.stream()
        .map(TypedVar::getType)
        .map(type -> type.toCompileableType(typeResolver, compileContext))
        .collect(Collectors.toList());

    TypeResolver boundGenerics = bindGenerics(typeResolver, defaultBindings, compileContext);

    return TypedTypeDef.builder()
        .from(this)
        .type(type.toCompileableType(boundGenerics, compileContext))
        .build();
  }

  public TypeResolver bindGenerics(
      TypeResolver typeResolver,
      List<CompileableType> bindings,
      CollectsErrors compileContext) {
    if (bindings.size() != generics.size()) {
      // TODO
      throw new CriticalLanguageException("TODO: make this a compiler error");
    }
    Map<String, TypeDef> subs = new HashMap<>();
    for (int i = 0; i < bindings.size(); i++) {
      TypedVar param = generics.get(i);
      CompileableType sub = bindings.get(i);
      CompileableType minType = param.getType()
          .toCompileableType(typeResolver, compileContext);
      if (!minType.assignableFrom(sub)) {
        // TODO
        throw new CriticalLanguageException("TODO: make this a compiler error");
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
