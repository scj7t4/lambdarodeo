package lambda.rodeo.gradle.plugin;

public class LambdaRodeoCompilationFailedException extends Exception {

    public LambdaRodeoCompilationFailedException(String message) {
        super(message);
    }

    public LambdaRodeoCompilationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
