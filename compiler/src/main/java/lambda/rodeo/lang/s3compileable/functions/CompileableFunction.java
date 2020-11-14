package lambda.rodeo.lang.s3compileable.functions;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.s1ast.expressions.ObjectAst;
import lambda.rodeo.lang.s2typed.functions.TypedFunction;
import lambda.rodeo.lang.s2typed.functions.TypedFunctionSignature;
import lambda.rodeo.lang.s2typed.type.S2TypedVar;
import lambda.rodeo.lang.scope.CompileableTypeScope;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.util.DescriptorUtils;
import lambda.rodeo.lang.util.FunctionDescriptorBuilder;
import lambda.rodeo.runtime.execution.Trampoline;
import lambda.rodeo.runtime.lambda.Lambda0;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

@Builder
@Getter
@EqualsAndHashCode
public class CompileableFunction {

  private final TypedFunction typedFunction;
  private final CompileableFunctionBody functionBody;
  private final TypedFunctionSignature functionSignature;

  public String generateTrampolineFunctionDescriptor() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (S2TypedVar var : functionSignature.getArguments()) {
      String descriptor = var.getType().getDescriptor();
      sb.append(descriptor);
    }
    sb.append(")").append(Type.getDescriptor(Trampoline.class));
    return sb.toString();
  }

  public String generateTrampolineLambdaFunctionDescriptor() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (S2TypedVar var : functionSignature.getArguments()) {
      String descriptor = var.getType().getDescriptor();
      sb.append(descriptor);
    }
    sb.append(")").append(Type.getDescriptor(Lambda0.class));
    return sb.toString();
  }

  public String generateFunctionDescriptor() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (S2TypedVar var : functionSignature.getArguments()) {
      String descriptor = var.getType().getDescriptor();
      sb.append(descriptor);
    }
    sb.append(")").append(functionSignature.getDeclaredReturnType().getDescriptor());
    return sb.toString();
  }

  public String generateTrampolineFunctionSignature() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (S2TypedVar var : functionSignature.getArguments()) {
      String descriptor = var.getType().getSignature();
      sb.append(descriptor);
    }
    sb.append(")")
        .append(DescriptorUtils.genericType(
            Trampoline.class,
            functionSignature.getDeclaredReturnType().getSignature())
        );
    return sb.toString();
  }

  public String generateFunctionSignature() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (S2TypedVar var : functionSignature.getArguments()) {
      String descriptor = var.getType().getSignature();
      sb.append(descriptor);
    }
    sb.append(")")
        .append(functionSignature.getDeclaredReturnType().getSignature());
    return sb.toString();
  }

  public void compile(ClassWriter cw, S2CompileContext compileContext, String internalModuleName) {
    compileEntryPoint(cw, compileContext, internalModuleName);
    compileImplementation(cw, compileContext, internalModuleName);
  }

  private void compileEntryPoint(ClassWriter cw, S2CompileContext compileContext,
      String internalModuleName) {
    int access = ACC_PUBLIC | ACC_STATIC;
    MethodVisitor methodVisitor = cw
        .visitMethod(
            access,
            functionSignature.getName(),
            generateTrampolineFunctionDescriptor(),
            generateTrampolineFunctionSignature(),
            null);

    String objectDescriptor = Type.getDescriptor(Object.class);
    StringBuilder bootstrapArgs = new StringBuilder("(");
    for (S2TypedVar arg : functionSignature.getArguments()) {
      // This needs to be Object because of the type erasure onto the lambda interfaces.
      bootstrapArgs.append(objectDescriptor);
    }
    bootstrapArgs.append(")").append(objectDescriptor);

    methodVisitor.visitCode();
    methodVisitor.visitInvokeDynamicInsn(
        "apply",
        generateTrampolineLambdaFunctionDescriptor(),
        new Handle(Opcodes.H_INVOKESTATIC,
            Type.getInternalName(LambdaMetafactory.class),
            "metafactory",
            FunctionDescriptorBuilder.args(
                MethodHandles.Lookup.class,
                String.class,
                MethodType.class,
                MethodType.class,
                MethodHandle.class,
                MethodType.class).returns(CallSite.class),
            false),
        // This seems to be just the general number of args and return of the lambda:
        Type.getType(bootstrapArgs.toString()),
        new Handle(Opcodes.H_INVOKESTATIC,
            internalModuleName,
            functionSignature.getName(),
            // This should be the description of the full function call (with the closure variables):
            generateFunctionDescriptor(),
            false),
        // And this should be the descriptor of how the lambda appears:
        Type.getType(FunctionDescriptorBuilder.args().returns(Object.class)));

    methodVisitor.visitMethodInsn(
        INVOKESTATIC,
        Type.getInternalName(Trampoline.class),
        "make",
        FunctionDescriptorBuilder.args(Lambda0.class)
            .returns(Trampoline.class),
        false);
    methodVisitor.visitInsn(ARETURN);
    methodVisitor.visitMaxs(1, 0);
    methodVisitor.visitEnd();
  }

  public void compileImplementation(ClassWriter cw, S2CompileContext compileContext,
      String internalModuleName) {
    int access = ACC_PUBLIC | ACC_STATIC;
    if (isLambda()) {
      access |= ACC_SYNTHETIC;
    }

    MethodVisitor methodVisitor = cw
        .visitMethod(
            access,
            "trampoline$" + functionSignature.getName(),
            generateFunctionDescriptor(),
            generateFunctionSignature(),
            null);

    methodVisitor.visitCode();
    Label startFunc = new Label();
    methodVisitor.visitLabel(startFunc);

    // The function body should emit a typescope which is all the variables it uses.
    functionBody.compile(methodVisitor, compileContext, internalModuleName);
    Label endFunc = new Label();
    methodVisitor.visitLabel(endFunc);

    CompileableTypeScope finalTypeScope = functionBody.getFinalTypeScope();
    finalTypeScope.compile(methodVisitor, startFunc, endFunc);
    methodVisitor.visitMaxs(0, 0);
    methodVisitor.visitEnd();
  }

  public String getName() {
    return getFunctionSignature().getName();
  }

  public void lambdaLift(ClassWriter cw, S2CompileContext compileContext, String internalJavaName) {
    functionBody.lambdaLift(cw, compileContext, internalJavaName);
  }

  public boolean isLambda() {
    return getTypedFunction().isLambda();
  }
}
