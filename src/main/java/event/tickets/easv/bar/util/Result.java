package event.tickets.easv.bar.util;

import java.util.function.Consumer;

/**
 * A union type representing the outcome of an operation, which can either be successful,
 * holding ar result of type T, or a failure, holding details about the error.
 *
 * @param <T> the type of the successful operation's result.
 */
public sealed interface Result<T> {
    /**
     * Executes the given action if the result is a Success.
     *
     * @param action a consumer that accepts the result of a successful operation.
     */
    default void ifSuccess(Consumer<T> action) {
        if (this instanceof Success<T> success) {
            action.accept(success.result);
        }
    }

    /**
     * Retrieves the result if this is a Success, otherwise throws an exception.
     *
     * @return The result contained by this Success.
     * @throws IllegalStateException if this is a Failure.
     */
    default T get() {
        if (this instanceof Success<T> success) {
            return success.result;
        } else if (this instanceof Failure<T> failure) {
            throw new IllegalStateException("Attempted to get result from a Failure: " + failure.message());
        } else {
            throw new IllegalStateException("Unknown Result state");
        }
    }

    /**
     * Represents a successful outcome of an operation, encapsulating the resulting value.
     *
     * @param <T> the type of the result.
     */
    record Success<T>(T result) implements Result<T> {
        public static <T> Success<T> of(T result) {
            return new Success<>(result);
        }
    }

    /**
     * Represents a failed outcome of an operation, encapsulating details about the failure.
     *
     * @param type the type of failure
     * @param message description of the failure
     * @param cause reason for why the failure happened
     * @param <T> carried over from the enclosing Result interface.
     *           Does not directly relate to any of the fields of Failure.
     *           It's used to keep the Failure record consistent with the Result type's generic signature.
     */
    record Failure<T>(FailureType type, String message, Throwable cause) implements Result<T> {
        public static <T> Failure<T> of(FailureType type, String message, Throwable cause) {
            return new Failure<>(type, message, cause);
        }

        public static <T> Failure<T> of(FailureType type, String message) {
            return new Failure<>(type, message, null);
        }
    }
}
