package lambda.rodeo.lang.functions;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

import java.util.List;
import lambda.rodeo.lang.ModuleAst;
import lombok.Builder;
import lombok.Data;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

@Data
@Builder
public class FunctionAst {

  private String name;
  private List<TypedVarAst> arguments;
  Runnable runnable;

  public void compile(ModuleAst module, ClassWriter cw) {
    //TODO define better signature? ASM says I can leave null if I don't have generics info
    MethodVisitor methodVisitor = cw
        .visitMethod(ACC_PUBLIC, name, "()Ljava/util/function/Supplier;",
            null, null);

    methodVisitor.visitInvokeDynamicInsn("get", "()Ljava/util/function/Supplier;",
        new Handle(Opcodes.H_INVOKESTATIC,
            "java/lang/invoke/LambdaMetafactory",
            "metafactory",
            "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;",
            false),
        Type.getType("()Ljava/lang/Object;"),
        new Handle(Opcodes.H_INVOKESTATIC,
            module.getJavaName(),
            "lambda$" + name + "$0",
            "()Ljava/lang/Object;",
            false),
        Type.getType("()Ljava/lang/Object;")
    );

    cw.visitEnd();
  }
}
