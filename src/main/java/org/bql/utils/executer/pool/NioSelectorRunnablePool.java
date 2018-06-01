package org.bql.utils.executer.pool;

import org.bql.utils.executer.NioServerWorker;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class NioSelectorRunnablePool {

	/**
	 * boss线程数组
	 */
	private final AtomicInteger bossIndex = new AtomicInteger();

	/**
	 * worker线程数组
	 */
	private final AtomicInteger workerIndex = new AtomicInteger();
	private Worker[] workeres;

	
	public NioSelectorRunnablePool(Executor worker,int threadCount) {
		int cout = threadCount == 0 ? Runtime.getRuntime().availableProcessors() * 2 : threadCount;
		initWorker(worker, cout);
	}


	/**
	 * 初始化worker线程
	 * @param worker
	 * @param count
	 */
	private void initWorker(Executor worker, int count) {
		this.workeres = new NioServerWorker[count];
		for (int i = 0; i < workeres.length; i++) {
			workeres[i] = new NioServerWorker(worker, "worker thread " + (i+1), this);
		}
	}

	/**
	 * 获取一个worker
	 * @return
	 */
	public Worker nextWorker() {
		 return workeres[Math.abs(workerIndex.getAndIncrement() % workeres.length)];
	}


}