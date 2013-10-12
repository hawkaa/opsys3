package oving3;

/**
 * This class contains a lot of public variables that can be updated
 * by other classes during a simulation, to collect information about
 * the run.
 */
public class Statistics
{
	/** The number of processes that have exited the system */
	public long nofCompletedProcesses = 0;
	/** The number of processes that have entered the system */
	public long nofCreatedProcesses = 0;
	/** The total time that all completed processes have spent waiting for memory */
	public long totalTimeSpentWaitingForMemory = 0;
	/** The time-weighted length of the memory queue, divide this number by the total time to get average queue length */
	public long memoryQueueLengthTime = 0;
	/** The largest memory queue length that has occured */
	public long memoryQueueLargestLength = 0;
	
	public long forcedSwitched = 0;
	public long ioOperations = 0;
	public long totalCpuProcessingTime = 0;
	public long totalCpuIdleTime = 0;
	public long cpuQueueLargestLength = 0;
	public long cpuQueueLengthTime = 0;
	public long ioQueueLargestLength = 0;
	public long ioQueueLengthTime = 0;

	public long processPlacedInCPUQueue=0;
	public long processPlacedInIOQueue=0;

	public long totalTimeWaitingForCPU=0;
	public long totalTimeInCPU=0;
	public long totalTimeWaitingForIO=0;
	public long totalTimeInIO=0;
	public long totalTimeInSystem = 0;
	
	
    
	/**
	 * Prints out a report summarizing all collected data about the simulation.
	 * @param simulationLength	The number of milliseconds that the simulation covered.
	 */
	public void printReport(long simulationLength) {
		
		double avgThroughput = 1000.0*nofCompletedProcesses/simulationLength;
		double cpuProcessingFraction = 100.0*totalCpuProcessingTime/simulationLength;
		double cpuIdleFraction = 100.0*totalCpuIdleTime/simulationLength;
		
		System.out.println();
		System.out.println("Simulation statistics:");
		System.out.println();
		System.out.println("Number of completed processes:                                "+nofCompletedProcesses);
		System.out.println("Number of created processes:                                  "+nofCreatedProcesses);
		System.out.println("Number of (forced) process switches:                          "+forcedSwitched);
		System.out.println("Number of processed I/O operations:                           "+ioOperations);
		System.out.println("Average throughput (processes per second):                    "+avgThroughput);
		System.out.println();
		System.out.println("Total CPU time spent processing:                              "+totalCpuProcessingTime);
		System.out.println("Fraction of CPU time spent processing:                        "+cpuProcessingFraction + "%");
		System.out.println("Total CPU time spent waiting:                                 "+totalCpuIdleTime);
		System.out.println("Fraction of CPU time spent waiting:                           "+cpuIdleFraction + "%");
		System.out.println();
		System.out.println("Largest occuring memory queue length:                         "+memoryQueueLargestLength);
		System.out.println("Average memory queue length:                                  "+(float)memoryQueueLengthTime/simulationLength);
		System.out.println("Largest occuring cpu queue length:                            "+cpuQueueLargestLength);
		System.out.println("Average cpu queue length:                                     "+(float)cpuQueueLengthTime/simulationLength);
		System.out.println("Largest occuring I/O queue length:                            "+ioQueueLargestLength);
		System.out.println("Average I/O queue length:                                     "+(float)ioQueueLengthTime/simulationLength);
		
		
		if(nofCompletedProcesses > 0) {
			System.out.println("Average # of times a process has been placed in memory queue: "+1);
			System.out.println("Average # of times a process has been placed in cpu queue:    "+(double)this.processPlacedInCPUQueue/this.nofCompletedProcesses);
			System.out.println("Average # of times a process has been placed in I/O queue:    "+(double)this.processPlacedInIOQueue/this.nofCompletedProcesses);
			System.out.println(); 
			System.out.println("Average time spent in system per process:                     " + this.totalTimeInSystem/this.nofCompletedProcesses+" ms");
			System.out.println("Average time spent waiting for memory per process:            "+
				totalTimeSpentWaitingForMemory/nofCompletedProcesses+" ms");
			System.out.println("Average time spent waiting for cpu per process:               " + this.totalTimeWaitingForCPU/this.nofCompletedProcesses+" ms");
			System.out.println("Average time processing per process:                          " + this.totalTimeInCPU/this.nofCompletedProcesses+" ms");
			System.out.println("Average time spent waiting for I/O per process:               " + this.totalTimeWaitingForIO/this.nofCompletedProcesses+" ms");
			System.out.println("Average time spent in I/O per process:                        " + this.totalTimeInIO/this.nofCompletedProcesses+" ms");
		}
	}
}
