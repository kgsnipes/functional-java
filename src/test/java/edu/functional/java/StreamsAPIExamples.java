package edu.functional.java;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class StreamsAPIExamples {

    List<String> list= Arrays.asList(StringUtils.split("one two three four five six seven eight nine ten eleven twelve thirteen fourteen fifteen",' '));
    List<Integer> integerList=Arrays.asList(1,2,3,4,5,6,7,8,9,10);
    List<Double> doubleList=Arrays.asList(100.50,200.99,300.84,400.30);

    // Streams API introduces the functional constructs to the JAVA language when combined with lambda expression.
    // Streams API helps in better readability for bulk operations and provides map-reduce equivalents as found in other functional languages.
    // Streams API in Java represents a stream of objects defined by a stream source.
    // Streams prevents modification of the source object.
    // Streams helps creating multiple instances from a single source.
    // Streams provide a way to iterate/operate over a stream of objects without defining how to iterate over these objects and helps focus on what needs to be done with the objects.




    @Test
    public void createStream()
    {
        Stream.of("a","b","c","d");

        Stream.of(10,20,30,40);

        Stream.generate(new Random()::nextInt).limit(3);

        Stream.builder().add("11").add("12").add("13").add("14").build();

        Stream<Integer> numberStream = Stream.iterate(1, n -> n + 2).limit(5);
        numberStream.forEach(System.out::println);

    }


    @Test
    public void iterateList()
    {
        Stream.of("a","b","c","d").forEach(obj->{
            System.out.println(obj);
        });

        Stream.of(10,20,30,40).forEach(System.out::println);

        Stream.generate(new Random()::nextInt).limit(3).forEach(System.out::println);

        Stream.builder().add("11").add("12").add("13").add("14").build().forEach(System.out::println);

        Stream<Integer> numberStream = Stream.iterate(1, n -> n + 2).limit(5);
        numberStream.forEach(System.out::println);

        list.stream().forEach(System.out::println);

        //stream from List
        List<Integer> list=Arrays.asList(1,2,3,4,5);
        Stream intStream=list.stream();
    }

    @Test
    public void iterateWithRange()
    {
        IntStream.range(0,list.size()).forEach(index->{
            System.out.println(list.get(index));
        });
    }

    @Test
    public void filterStrings()
    {
        list.stream().filter(str->str.contains("h")).forEach(System.out::println);

        Stream.of("a","b","c","d","e")
                .filter(s->s.contains("c"))
                .forEach(System.out::println);

        Stream.of("a","b","c","d","e").filter(s->s.contains("c")).map(s->s+"at").forEach(System.out::println);
    }

    @Test
    public void collectWithFilteredStrings()
    {
        List<String> filteredResults=list.stream().filter(str->str.contains("h")).collect(Collectors.toList());
        filteredResults.stream().forEach(System.out::println);
    }

    @Test
    public void filterStringsFindFirst()
    {
        Optional<String> firsOccurrence=list.stream().filter(str->str.contains("h")).findFirst();
        if(firsOccurrence.isPresent())
        {
            System.out.println(firsOccurrence.get());
        }
        else
        {
            System.out.print("Not found");
        }
    }

    @Test
    public void filterStringsFindFirstAndExecuteIf()
    {
        list.stream().filter(str->str.contains("h")).findFirst().ifPresent(str->{
            System.out.println("Found result : "+str);
        });
    }

    @Test
    public void filterStringsFindFirstAndExecuteIfElse()
    {
        list.stream().filter(str->str.contains("h")).findFirst().ifPresentOrElse(s -> {
            System.out.println("Found result : "+s);
        }, new Runnable() {
            @Override
            public void run() {
                System.out.println("Did not find the result");
            }
        });
    }

    @Test
    public void filterStringsFindFirstAndExecuteElse()
    {
        String result=list.stream().filter(str->str.contains("z")).findFirst().orElse("eight");
        System.out.println(result);
    }

    @Test
    public void filterStringsFindFirstAndExecuteElseThrow()
    {
        String result=list.stream().filter(str->str.contains("z")).findFirst().orElseThrow();

    }

    @Test
    public void filterStringsFindAny()
    {
        list.stream().filter(str->str.contains("h")).parallel().findAny().ifPresentOrElse(s -> {
            System.out.println("Found result : "+s);
        }, new Runnable() {
            @Override
            public void run() {
                System.out.println("Did not find the result");
            }
        });

    }

    @Test
    public void iterateListWithTransformation()
    {
        list.stream().map(str->StringUtils.join("Hello"," ",str)).forEach(System.out::println);
    }

    @Test
    public void iterateWithRangeOverEvenNumbers()
    {

        IntStream.range(0,list.size()).filter(index->index%2!=0).forEach(index->{
            System.out.println(list.get(index));
        });
    }


    @Test
    public void iterateWithRangeOverOddNumbers()
    {
        IntStream.range(0,list.size()).filter(index->index%2==0).forEach(index->{
            System.out.println(list.get(index));
        });
    }

    @Test
    public void sumDoubleNumbers()
    {
       Double result=doubleList.stream().reduce((value,sum)->sum=sum+value).get();
       System.out.println(result);
    }

    @Test
    public void sumDoubleNumbersWithFilterAndReduce()
    {
        integerList.stream().filter(integer -> integer>5).reduce((value,sum)->sum=sum+value).ifPresent(System.out::println);
    }

    @Test
    public void collectExample()
    {
        List<String> result=list.stream().filter(s->s.contains("h")).collect(Collectors.toList());
        result.stream().forEach(System.out::println);
    }

    @Test
    public void collectWithNumbersExample()
    {
        List<Integer> result=integerList.stream().filter(integer -> integer>5).collect(Collectors.toList());
        result.stream().forEach(System.out::println);
    }

    @Test
    public void collectWithJoin()
    {
        String result=list.stream().filter(integer -> integer.contains("h")).collect(Collectors.joining(","));
        System.out.println(result);
    }

    @Test
    public void collectWithCount()
    {
        Long result=list.stream().filter(integer -> integer.contains("h")).collect(Collectors.counting());
        System.out.println(result);
    }


}
