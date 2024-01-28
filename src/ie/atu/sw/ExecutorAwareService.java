package ie.atu.sw;

import java.util.concurrent.ExecutorService;

/**
 * The abstract class ExecutorAwareService provides basic functionality
 * for services related to an executor (ExecutorService).
 */

abstract public class ExecutorAwareService {

    ExecutorService executor;
    /**
     * Method performing an operation with an estimated time complexity of O(f(n)),
     * where f(n) represents the time complexity function.
     * This method exemplifies an operation performed by an executor-aware service.
     *
     * @param executor The input parameter for the operation.
     */
   
    public ExecutorAwareService(ExecutorService executor) {
        this.executor = executor;
    }
}
