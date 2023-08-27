package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class Pa3ReactiveJjFmApplication {

	public static <T> Mono<List<T>> appendToList(List<T> list, T value) {
		return Mono.fromSupplier(() -> {
			List<T> newList = new ArrayList<>(list);
			newList.add(value);
			return newList;
		});
	}

	public static <T> Mono<List<T>> prependToList(List<T> list, T value) {
		return Mono.fromSupplier(() -> {
			List<T> newList = new ArrayList<>();
			newList.add(value);
			newList.addAll(list);
			return newList;
		});
	}

	public static <T> Mono<List<T>> reverseList(List<T> list) {
		return Mono.fromSupplier(() -> {
			List<T> reversedList = new ArrayList<>(list);
			Collections.reverse(reversedList);
			return reversedList;
		});
	}

	public static <T> Mono<List<T>> dropFromList(List<T> list, int count) {
		return Mono.fromSupplier(() -> {
			List<T> droppedList = new ArrayList<>(list);
			droppedList.subList(0, Math.min(count, droppedList.size())).clear();
			return droppedList;
		});
	}

	public static <T> Mono<List<T>> dropWhileFromList(List<T> list, Predicate<T> predicate) {
		return Mono.fromSupplier(() -> {
			List<T> droppedList = new ArrayList<>(list);
			while (!droppedList.isEmpty() && predicate.test(droppedList.get(0))) {
				droppedList.remove(0);
			}
			return droppedList;
		});
	}

	public static <T> Mono<List<T>> takeFromList(List<T> list, int count) {
		return Mono.fromSupplier(() -> {
			List<T> takenList = new ArrayList<>(list);
			if (takenList.size() > count) {
				takenList.subList(count, takenList.size()).clear();
			}
			return takenList;
		});
	}

	public static <T> Mono<List<T>> takeWhileFromList(List<T> list, Predicate<T> predicate) {
		return Mono.fromSupplier(() -> {
			List<T> takenList = new ArrayList<>();
			for (T item : list) {
				if (!predicate.test(item)) {
					break;
				}
				takenList.add(item);
			}
			return takenList;
		});
	}

	public static <T> Mono<List<T>> removeElement(List<T> list, T elementToRemove) {
		return Mono.just(list)
				.map(lst -> lst.stream().filter(value -> !value.equals(elementToRemove)).collect(Collectors.toList()));
	}

	public static <T> Mono<List<T>> concatenateLists(List<T> list1, List<T> list2) {
		return Mono.fromSupplier(() -> {
			List<T> concatenatedList = new ArrayList<>(list1);
			concatenatedList.addAll(list2);
			return concatenatedList;
		});
	}
	
	
	public static <T> Mono<List<T>> replaceValue(List<T> list, T oldValue, T newValue) {
        return Mono.fromSupplier(() -> {
            List<T> replacedList = new ArrayList<>(list);
            Collections.replaceAll(replacedList, oldValue, newValue);
            return replacedList;
        });
    }
	
	public static <T> Mono<Integer> measureListSize(List<T> list) {
        return Mono.fromSupplier(list::size);
    }

	public static <T, R> Mono<List<R>> mapList(List<T> list, Function<T, R> mapper) {
        return Mono.just(list)
                .map(lst -> lst.stream()
                               .map(mapper)
                               .collect(Collectors.toList()));
    }


	public static <T, R> Mono<R> foldList(List<T> list, R initialValue, java.util.function.BiFunction<R, T, R> folder) {
        return Mono.fromSupplier(() -> list.stream().reduce(initialValue, folder, (a, b) -> a));
    }
	
	
	public static <T> Flux<T> invertFlux(Flux<T> flux) {
	        return flux.collectList()
	            .map(list -> {
	                Collections.reverse(list);
	                return list;
	            })
	            .flatMapMany(Flux::fromIterable);
	    }
	 
	 public static <T> Flux<T> remove(Flux<T> flux, T elementToRemove) {
	        return flux.filter(element -> !element.equals(elementToRemove));
	    }

	  public static <T> Flux<T> replace(Flux<T> flux, T oldValue, T newValue) {
	        return flux.map(element -> element.equals(oldValue) ? newValue : element);
	    }
	  
	  public static <T> void size(Flux<T> flux) {
	        flux.count()
	            .subscribe(size -> System.out.println(size));
	    }
	  
	  public static <T> void prin(Flux<T> flux) {
		    flux
		        .collectList()
		        .map(list -> {
		            StringBuilder result = new StringBuilder();
		            for (int i = 0; i < list.size(); i++) {
		                if (i > 0) {
		                    result.append(", ");
		                }
		                result.append(list.get(i));
		            }
		            return result.toString();
		        })
		        .subscribe(System.out::println);
		}
	  
	 

	public static void main(String[] args) {

		List<Integer> originalList = List.of(1, 2, 3, 4, 5);

		Mono<List<Integer>> appendedMono = appendToList(originalList, 4);

		appendedMono.subscribe(result -> System.out.println("Lista después de append: " + result));

		Mono<List<Integer>> prependedMono = prependToList(originalList, 1);

		prependedMono.subscribe(result -> System.out.println("Lista después de prepend: " + result));

		Mono<List<Integer>> reversedMono = reverseList(originalList);

		reversedMono.subscribe(result -> System.out.println("Lista invertida: " + result));

		int countToDrop = 2;

		Mono<List<Integer>> droppedMono = dropFromList(originalList, countToDrop);

		droppedMono.subscribe(result -> System.out.println("Lista después de drop: " + result));

		Predicate<Integer> dropPredicate = value -> value < 3;

		Mono<List<Integer>> droppedMonoWhile = dropWhileFromList(originalList, dropPredicate);

		droppedMonoWhile.subscribe(result -> System.out.println("Lista después de dropWhile: " + result));

		int countToTake = 3;

		Mono<List<Integer>> takenMono = takeFromList(originalList, countToTake);

		takenMono.subscribe(result -> System.out.println("Lista después de take: " + result));

		Predicate<Integer> takePredicate = value -> value <= 3;

		Mono<List<Integer>> takenMonoWhile = takeWhileFromList(originalList, takePredicate);

		takenMono.subscribe(result -> System.out.println("Lista después de takeWhile: " + result));

		Mono<List<Integer>> removedMono = removeElement(originalList, 3);

		removedMono.subscribe(result -> System.out.println("Lista después de remover elemento: " + result));

		List<Integer> list1 = List.of(1, 2, 3);
		List<Integer> list2 = List.of(4, 5, 6);
		Mono<List<Integer>> concatenatedMono = concatenateLists(list1, list2);

		concatenatedMono.subscribe(result -> System.out.println("Listas concatenadas: " + result));

		
		Mono<List<Integer>> replacedMono = replaceValue(originalList, 3, 22);

		replacedMono.subscribe(result -> System.out.println("Lista después de reemplazar valor: " + result));

		List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        Mono<Integer> sizeMono = measureListSize(numbers);

        sizeMono.subscribe(size -> System.out.println("Tamaño de la lista: " + size));
        

        Mono<List<String>> mappedMono = mapList(numbers, number -> "Mapped: " + number);

        mappedMono.subscribe(result -> System.out.println(result));
        
        Mono<Integer> foldingMono = foldList(numbers, 0, (accumulator, number) -> accumulator + number);

        foldingMono.subscribe(result -> System.out.println("Resultado del folding: " + result));
        
        Mono<Integer> mono1 = Mono.just(5);
        Mono<Integer> mono2 = Mono.just(10);


        System.out.println();
        Flux<Integer> flux1 = Flux.just(1, 2, 3);
        Flux<Integer> flux2 = Flux.just(4, 5, 6);
        Flux<Integer> flux3 = Flux.just(1,2,3,4,5,6);

        System.out.println();
        System.out.println("Flux-Append");
        
        Flux<Integer> fluxa = Flux.just(7, 8, 9);
        Flux<Integer> fluxb = Flux.just(4, 3, 2);
        
        Flux<Integer> appendedFlux = fluxa.concatWith(fluxb);
        prin(appendedFlux);

        System.out.println();
        System.out.println("Flux-Preppend");
        Flux<Integer> prependedFlux = flux1.concatWith(flux2);
        prin(prependedFlux);

        System.out.println();
        System.out.println("Flux-oncatenar");
        List<Integer> list3 = List.of(1, 2, 3);
        List<Integer> list4 = List.of(4, 5, 6);
        Flux<Integer> concatenatedListFlux = Flux.concat(Flux.fromIterable(list3), Flux.fromIterable(list4));
        prin(concatenatedListFlux);
        
        
        System.out.println();
        System.out.println("Flux-Drop");
        
        Flux<Integer> droppedFlux = flux3.skip(2);
        prin(droppedFlux);

        System.out.println();
        System.out.println("Flux-DropWhile");
        Flux<Integer> filteredFlux = flux3.filter(number -> number % 2 == 0);
        prin(filteredFlux);
        
        
        System.out.println();
        System.out.println("Flux-Take");
        Flux<Integer> takenFlux = flux3.take(3);
        prin(takenFlux);
        
        System.out.println();
        System.out.println("Flux-TakeWhile");
        Flux<Integer> takenWhileFlux = flux3.filter(number -> number %2!=0);
        prin(takenWhileFlux);
        
        System.out.println();
        System.out.println("Flux-Invert");
        Flux<Integer> ta = invertFlux(flux3);
        prin(ta);
        

        System.out.println();
        System.out.println("Flux-Remove");
        prin(remove(flux3, 3));
        
        System.out.println();
        System.out.println("Flux-Reeplace");
        prin(replace(flux3, 3, 10));
        
        System.out.println();
        System.out.println("Flux-Size");
        size(flux3);
        
       
        System.out.println();
        System.out.println("Flux-Map");
        Flux<Integer> mappedFlux = flux1.map(x -> x * 2);
        prin(mappedFlux);
        
        System.out.println();
        System.out.println("Flux-Reduction");
        Mono<Integer> sumMono = flux1.reduce(0, (acc, val) -> acc + val);
        sumMono.subscribe(System.out::println);
        
        System.out.println();
        System.out.println("Flux-Folding");
        Flux<Integer> foldedFlux = flux1.scan(1, (accumulated, value) -> accumulated * value);
        prin(foldedFlux);

	}

}
