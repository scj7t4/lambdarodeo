package lambda.rodeo.lang;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.V1_8;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.objectweb.asm.ClassWriter;

@Data
@Builder
public class ModuleAst {

  @NonNull
  private final String name;

  private String javaName() {
    return name.replace(".", "/");
  }

  public byte[] compile() {
    ClassWriter cw = new ClassWriter(0);
    cw.visit(V1_8, ACC_PUBLIC, javaName(), null, "java/lang/Object", null);

    cw.visitEnd();
    return cw.toByteArray();
  }
}
