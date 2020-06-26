package service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPool {

	ExecutorService executor = Executors.newCachedThreadPool();
	
	ThreadPoolExecutor pool = (ThreadPoolExecutor) executor;
}
