package lambda.rodeo.lang.types;

import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETSTATIC;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.LRTypeUnion;
import lombok.Builder;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
public class TypeUnion implements LambdaRodeoType {

  private final List<LambdaRodeoType> unions;

  @Override
  public CompileableType toCompileableType(TypedModuleScope typedModuleScope,
      CollectsErrors compileContext) {
    return CompileableTypeUnion.builder()
        .type(this)
        .unions(unions.stream()
            .map(union -> union.toCompileableType(typedModuleScope, compileContext))
            .collect(Collectors.toList()))
        .build();
  }

}
