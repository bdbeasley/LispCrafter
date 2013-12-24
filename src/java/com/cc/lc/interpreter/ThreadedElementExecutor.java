package com.cc.lc.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//well I have to say it's not as impressive as I'd hoped, but 
//basicly what this does is manages as many threads as you want
//when you call execute(com.cc.lc.interpreter.Element), it'll put the com.cc.lc.interpreter.Element on queuqe to
//be evaluated using the com.cc.lc.interpreter.Interpreter.
public class ThreadedElementExecutor extends ThreadPoolExecutor {
	// a map of the executed elements, keyed by their order at which execution is called.
	// this takes into account the possibility that the first element that I call execute() 
	// on could be the last element to finish executing.
	// I chose the concurrent skip list implementation for good amortized performance and thread-safety, which is optimal for large input.
	// Oh yeah, and one other reason I chose it is it's ordered. You may ask "why didn't this fool use a List"... well, I discovered when you add elements
	// out of order, the list was never big enough, and frequently increasing the list size would have been troublesome so I chose to just use a Map.
	Map<Integer,Element> executed = new ConcurrentSkipListMap<Integer,Element>();
	
	// simply the index (or key) in the "executed" Map of the next com.cc.lc.interpreter.Element if the
	int maxIndex = 0;
	
	public ThreadedElementExecutor() {
		super(10, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}


	//does the infamous "threaded execute," by creating a Runnable (aka thread), within which we call com.cc.lc.interpreter.Interpreter.execute on the com.cc.lc.interpreter.Element.
	public void execute(final Element element) {
		final int index = maxIndex++;
		this.execute(new Runnable() {
			public void run() {
				//oh yeah, one caveat: you have to synchronized "executed".. otherwise you'll be race-conditioned to death
				//(i.e. two threads will put Elements into "executed" at the same time.
				synchronized(executed) {
					//and here you have it: we call Interpeter.execute on the element and add it to the map of executed elements
					executed.put(new Integer(index), Interpreter.execute(element));
				}
			}
		});
	}
	
	

	/* BELOW: JUST SOME INCOMPLETE THREAD CONTROL NONSENSE I WAS GOING TO USE FOR VERSION 2 OF PART 9 BUT I RAN OUT OF TIME */
	private boolean isPaused;
	private ReentrantLock pauseLock = new ReentrantLock();
	private Condition unpaused = pauseLock.newCondition();
	
	public List<Element> getExecuted() {
		return new ArrayList(executed.values());
	}

	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
     	pauseLock.lock();
     	try {
    	 while (isPaused) unpaused.await();
     	} catch (InterruptedException ie) {
    	 t.interrupt();
     	} finally {
    	 pauseLock.unlock();
     	}
	}

	public void pause() {
		pauseLock.lock();
     	try {
    	 isPaused = true;
     	} finally {
     		pauseLock.unlock();
     	}
	}

	public void resume() {
		pauseLock.lock();
     	try {
    	 isPaused = false;
       	unpaused.signalAll();
     	} finally {
     		pauseLock.unlock();
     	}
	}
}
