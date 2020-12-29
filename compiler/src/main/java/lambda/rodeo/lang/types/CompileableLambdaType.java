package lambda.rodeo.lang.types;

import static org.objectweb.asm.Opcodes.AASTORE;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lambda.rodeo.lang.util.DescriptorUtils;
import lambda.rodeo.runtime.exceptions.RuntimeCriticalLanguageException;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.lambda.Lambda1;
import lambda.rodeo.runtime.types.LRLambda;
import lambda.rodeo.runtime.types.LRType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
@EqualsAndHashCode
public class CompileableLambdaType implements CompileableType {

  @NonNull
  private final List<CompileableType> args;

  @NonNull
  private final CompileableType returnType;

  private final LambdaRodeoType from;

  @Override
  public LambdaRodeoType getType() {
    return from;
  }

  @Override
  public boolean isLambda() {
    return true;
  }

  @Override
  public String getDescriptor() {
    return Type.getDescriptor(functionalRep());
  }

  @Override
  public String getInternalName() {
    return Type.getInternalName(functionalRep());
  }

  @Override
  public String getSignature() {
    String[] params = Stream.concat(args.stream(), Stream.of(returnType))
        .map(CompileableType::getSignature)
        .toArray(String[]::new);
    return DescriptorUtils.genericType(Lambda0.class,
        DescriptorUtils.genericType(functionalRep(), params));
  }

  @Override
  public void provideRuntimeType(MethodVisitor methodVisitor) {
    // Push a number onto the stack
    methodVisitor.visitLdcInsn(args.size());
    methodVisitor.visitTypeInsn(ANEWARRAY, Type.getInternalName(LRType.class));
    // Load all the arg type in...
    for (int i = 0; i < args.size(); i++) {
      methodVisitor.visitInsn(DUP);
      methodVisitor.visitLdcInsn(i);
      args.get(i).provideRuntimeType(methodVisitor);
      methodVisitor.visitInsn(AASTORE);
    }
    // Then put them into a list
    methodVisitor.visitMethodInsn(INVOKESTATIC,
        Type.getInternalName(Arrays.class),
        "asList",
        "([Ljava/lang/Object;)Ljava/util/List;",
        false);
    // Then invoke the args on the builder:
    methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
        Type.getInternalName(LRLambda.LRLambdaBuilder.class),
        "arguments",
        "(Ljava/util/List;)Llambda/rodeo/runtime/type/LRLambda$LRLambdaBuilder;",
        false);
    // Then load the return type:
    returnType.provideRuntimeType(methodVisitor);
    // Then invoke the return type thinger
    methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
        Type.getInternalName(LRLambda.LRLambdaBuilder.class),
        "returnType",
        "(Llambda/rodeo/runtime/type/LRType;)Llambda/rodeo/runtime/type/LRLambda$LRLambdaBuilder;",
        false);
    // Then invoke the build method
    methodVisitor.visitMethodInsn(INVOKEVIRTUAL,
        Type.getInternalName(LRLambda.LRLambdaBuilder.class),
        "build",
        "()Llambda/rodeo/runtime/type/LRLambda;",
        false);
  }

  public String getFunctionDescriptor() {
    StringBuilder sb = new StringBuilder("(");
    for (CompileableType arg : args) {
      sb.append(arg.getLambdaDescriptor());
    }
    sb.append(")").append(Type.getDescriptor(Lambda0.class));
    return sb.toString();
  }

  public String getGenericFunctionDescriptor() {
    StringBuilder sb = new StringBuilder("(");
    for (CompileableType arg : args) {
      // Object because type erasure erases args.
      sb.append(Type.getDescriptor(Object.class));
    }
    sb.append(")").append(Type.getDescriptor(Object.class));
    return sb.toString();
  }

  public Class<?> functionalRep() {
    switch (args.size()) {
      case 0:
        return Lambda0.class;
      case 1:
        return Lambda1.class;
    }
    throw new RuntimeCriticalLanguageException(
        "Lambda function had too many arguments (" + args.size() + ")");
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("<*lambda>(");
    for (int i = 0; i < args.size(); i++) {
      CompileableType arg = args.get(i);
      sb.append(arg);
      if (i < args.size() - 1) {
        sb.append(",");
      }
    }
    sb.append(")=>").append(returnType);
    return sb.toString();
  }

  @Override
  public boolean assignableFrom(CompileableType other) {
    if (this.equals(other)) {
      return true;
    } else if (other instanceof CompileableLambdaType && this.assignableFrom(other)) {
      return true;
    }
    return false;
  }
}
