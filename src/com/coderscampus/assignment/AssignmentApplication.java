package com.coderscampus.assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class AssignmentApplication {

	public static void main(String[] args) {
		Assignment8 txt = new Assignment8();
		
		List<Integer> nums = Collections.synchronizedList(new ArrayList<>(1000));
		
		List<CompletableFuture<Void>> tasks = new ArrayList<>();
		ExecutorService cachedExecutor = Executors.newCachedThreadPool();
		
		for (int i = 0; i < 1000; i++) {
			CompletableFuture<Void> task = CompletableFuture.supplyAsync(() -> txt.getNumbers(), cachedExecutor)
									.thenAccept(number -> nums.addAll(number));
			tasks.add(task);
		}
		while (tasks.stream().filter(CompletableFuture::isDone).count() < 1000) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				System.out.println("Encountered an Interrupted Exception");
				e.printStackTrace();
			}
		}
		System.out.println("Records successfully fetched asynchronously: " + nums.size());
		
		Map<Object, Long> counts = 
				nums.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		
		System.out.println("Number occurences from the output.txt file: ");
		System.out.println(counts);
		
		cachedExecutor.shutdown();
	}

}
