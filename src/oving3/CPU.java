package oving3;

import oving3.gui.Gui;
import oving3.gui.Queue;

public class CPU implements Constants {
	
	// Queue
	private Queue cpuQueue;
	
	// Active
	private Process running;
	
	// reference to the GUI
	private Gui gui;
	
	// max running time
	private long maxRunTime;
	
	// Reference to statistics
	private Statistics stats;
	
	private long lastEvent;
	
	public CPU(Queue cpuQueue, Gui gui, long maxRunTime, Statistics s, long clock) {
		this.cpuQueue = cpuQueue;
		this.gui = gui;
		this.maxRunTime = maxRunTime;
		this.stats = s;
		this.lastEvent = clock;
	}
	
	/**
	 * Adds process to queue
	 * @param p Process to enter to queue
	 * @param clock Current time
	 */
	public void addProcess(Process p, long clock) {
		p.enterCPUQueue(clock);
		this.cpuQueue.insert(p);
	}
	
	/**
	 * Removes the current process and inserts the next one in the queue. Updates GUI
	 * @param clock Current time
	 */
	public void pop(long clock) {
		
		if(this.running == null) {
			// Process has been idle
			this.stats.totalCpuIdleTime += clock - lastEvent;
		} else {
			// Process has been running
			this.stats.totalCpuProcessingTime += clock - lastEvent;
		}
		lastEvent = clock;
		
		if(cpuQueue.isEmpty()) {
			this.running = null;
		} else {
			this.running = (Process)cpuQueue.removeNext();
		}
		
		gui.setCpuActive(this.running);
		
		
	}
	/**
	 * 
	 * @return The current running process
	 */
	public Process getRunning() {
		return this.running;
	}
	
	/**
	 * 
	 * @return if there is currently no process running
	 */
	public boolean isIdle() {
		return this.running == null;
	}
	
	/**
	 * @return Returns the maximum running time per process
	 */
	public long getMaxRunTime() {
		return this.maxRunTime;
	}
	
	/**
	 * Statistics method to keep queue data up to date
	 * @param timePassed Current time
	 */
	public void timePassed(long timePassed) {
		stats.cpuQueueLengthTime += cpuQueue.getQueueLength()*timePassed;
		if (cpuQueue.getQueueLength() > stats.cpuQueueLargestLength) {
			stats.cpuQueueLargestLength = cpuQueue.getQueueLength(); 
		}
    }
	
	
}
