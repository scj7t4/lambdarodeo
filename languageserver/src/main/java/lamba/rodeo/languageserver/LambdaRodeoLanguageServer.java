package lamba.rodeo.languageserver;

import java.util.concurrent.CompletableFuture;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

public class LambdaRodeoLanguageServer implements LanguageServer {

  @Override
  public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
    return null;
  }

  @Override
  public CompletableFuture<Object> shutdown() {
    return null;
  }

  @Override
  public void exit() {

  }

  @Override
  public TextDocumentService getTextDocumentService() {
    return null;
  }

  @Override
  public WorkspaceService getWorkspaceService() {
    return null;
  }
}
