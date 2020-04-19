package lambda.rodeo.lang;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModuleAst {

  private final String name;
}
