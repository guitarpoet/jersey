package info.thinkingcloud.jersey.main.service;

import info.thinkingcloud.jersey.core.meta.Function;
import info.thinkingcloud.jersey.core.meta.Module;
import info.thinkingcloud.jersey.core.meta.Parameter;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;


@Service
@Module(doc = "The background job manager.")
public class JobManager {
	private ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

	/**
	 * @param task
	 * @return
	 * @see java.util.concurrent.AbstractExecutorService#submit(java.lang.Runnable)
	 */
	public Future<?> submit(Runnable task) {
		return pool.submit(task);
	}

	/**
	 * @param task
	 * @param result
	 * @return
	 * @see java.util.concurrent.AbstractExecutorService#submit(java.lang.Runnable,
	 *      java.lang.Object)
	 */
	public <T> Future<T> submit(Runnable task, T result) {
		return pool.submit(task, result);
	}

	/**
	 * @param task
	 * @return
	 * @see java.util.concurrent.AbstractExecutorService#submit(java.util.concurrent.Callable)
	 */
	public <T> Future<T> submit(Callable<T> task) {
		return pool.submit(task);
	}

	/**
	 * @param tasks
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @see java.util.concurrent.AbstractExecutorService#invokeAny(java.util.Collection)
	 */
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return pool.invokeAny(tasks);
	}

	/**
	 * @param tasks
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * @see java.util.concurrent.AbstractExecutorService#invokeAny(java.util.Collection,
	 *      long, java.util.concurrent.TimeUnit)
	 */
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
	        throws InterruptedException, ExecutionException, TimeoutException {
		return pool.invokeAny(tasks, timeout, unit);
	}

	/**
	 * @param tasks
	 * @return
	 * @throws InterruptedException
	 * @see java.util.concurrent.AbstractExecutorService#invokeAll(java.util.Collection)
	 */
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return pool.invokeAll(tasks);
	}

	/**
	 * @param tasks
	 * @param timeout
	 * @param unit
	 * @return
	 * @throws InterruptedException
	 * @see java.util.concurrent.AbstractExecutorService#invokeAll(java.util.Collection,
	 *      long, java.util.concurrent.TimeUnit)
	 */
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
	        throws InterruptedException {
		return pool.invokeAll(tasks, timeout, unit);
	}

	/**
	 * @param command
	 * @see java.util.concurrent.ThreadPoolExecutor#execute(java.lang.Runnable)
	 */
	public void execute(Runnable command) {
		pool.execute(command);
	}

	@Function(doc = "Wait until task is done.", parameters = @Parameter(name = "timeout", type = "int", doc = "The timeout time in seconds for wait, default is 10 seconds.", optional = true))
	public void waitUntilDone(int timeout) throws InterruptedException {
		pool.awaitTermination(timeout, TimeUnit.SECONDS);
	}

	public void waitUntilDone() throws InterruptedException {
		waitUntilDone(10);
	}

	/**
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdown()
	 */
	@Function(doc = "Shutdown the jobmanager.")
	public void shutdown() {
		pool.shutdown();
	}

	@PreDestroy
	public void cleanup() throws InterruptedException {
		if (!isShutdown()) {
			shutdown();
			pool.awaitTermination(10, TimeUnit.SECONDS);
		}
	}

	/**
	 * @return
	 * @see java.util.concurrent.ThreadPoolExecutor#shutdownNow()
	 */
	public List<Runnable> shutdownNow() {
		return pool.shutdownNow();
	}

	/**
	 * @return
	 * @see java.util.concurrent.ThreadPoolExecutor#isShutdown()
	 */
	public boolean isShutdown() {
		return pool.isShutdown();
	}

	/**
	 * @return
	 * @see java.util.concurrent.ThreadPoolExecutor#isTerminating()
	 */
	public boolean isTerminating() {
		return pool.isTerminating();
	}

	/**
	 * @return
	 * @see java.util.concurrent.ThreadPoolExecutor#isTerminated()
	 */
	public boolean isTerminated() {
		return pool.isTerminated();
	}

	/**
	 * @param task
	 * @return
	 * @see java.util.concurrent.ThreadPoolExecutor#remove(java.lang.Runnable)
	 */
	public boolean remove(Runnable task) {
		return pool.remove(task);
	}

	/**
	 * 
	 * @see java.util.concurrent.ThreadPoolExecutor#purge()
	 */
	public void purge() {
		pool.purge();
	}

	/**
	 * @return
	 * @see java.util.concurrent.ThreadPoolExecutor#getPoolSize()
	 */
	public int getPoolSize() {
		return pool.getPoolSize();
	}

	/**
	 * @return
	 * @see java.util.concurrent.ThreadPoolExecutor#getTaskCount()
	 */
	public long getTaskCount() {
		return pool.getTaskCount();
	}

	/**
	 * @return
	 * @see java.util.concurrent.ThreadPoolExecutor#getCompletedTaskCount()
	 */
	public long getCompletedTaskCount() {
		return pool.getCompletedTaskCount();
	}
}
