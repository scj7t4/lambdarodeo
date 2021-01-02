package lambda.rodeo.lang.scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lambda.rodeo.lang.types.CompileableType;

public interface TypeScope {

  TypeScope EMPTY = new TypeScopeImpl();

  TypeScope declare(String varName, CompileableType type);

  default Stream<Entry> get(String varName) {
    if (varName.indexOf('.') == -1) {
      return getSimple(varName)
          .map(entry -> entry);
    } else {
      String[] tokens = varName.split("\\.");
      List<Entry> consider = getSimple(tokens[0]).collect(Collectors.toList());
      for(int i = 1; i < tokens.length; i++) {
        String token = tokens[i];
        List<Entry> next = new ArrayList<>();
        for(int j = 0; j < consider.size(); j++) {
          Entry current = consider.get(j);
          CompileableType currentType = current.getType();
          Optional<Entry> memberEntry = currentType.getMemberEntry(current, token);
          memberEntry.ifPresent(next::add);
        }
        consider = next;
      }
      return consider.stream();
    }
  }

  Stream<SimpleEntry> getSimple(String varName);

  Stream<SimpleEntry> getAllSimple();

  CompileableTypeScope toCompileableTypeScope();

  int maxSlot();

  TypeScope parent();

  Stream<SimpleEntry> getByIndex(int index);

}
