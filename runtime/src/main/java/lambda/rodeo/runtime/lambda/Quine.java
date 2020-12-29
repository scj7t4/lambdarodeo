package lambda.rodeo.runtime.lambda;

/**
 * A Quine is a Lambda0 that returns itself.
 *
 * <p>For the purposes of exhausting a "Trampoline" a Quine is a mark that the trampoline is in
 * an absorbing state and further bounces would just return the same value.</p>
 *
 * @param <T>
 */
public interface Quine<T> extends Lambda0<T> {

}