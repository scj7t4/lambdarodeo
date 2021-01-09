package lamba.rodeo.languageserver;

import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.services.TextDocumentService;

public class LambdaRodeoTextDocumentService implements TextDocumentService {

  @Override
  public void didOpen(DidOpenTextDocumentParams params) {

  }

  @Override
  public void didChange(DidChangeTextDocumentParams params) {

  }

  @Override
  public void didClose(DidCloseTextDocumentParams params) {

  }

  @Override
  public void didSave(DidSaveTextDocumentParams params) {

  }
}
