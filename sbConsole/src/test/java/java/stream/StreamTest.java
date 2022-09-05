package java.stream;

import com.hxx.sbConsole.SbConsoleApplication;
import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbcommon.common.basic.datetime.LocalDateTimeUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.math.BigDecimal;
import java.models.Employee;
import java.models.Person;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SbConsoleApplication.class)
public class StreamTest {

    @Test
    public void Run() {
        Map<String, Integer> map = new HashMap<>();
        map.put(null, null);


        System.out.println("==============stream.StreamTest.Run==============");
        {
            Stream<String> stream = Stream.of("java2s.com");
            stream.forEach(System.out::println);
        }
        {
            Stream<String> stream = Stream.of("java2s.com", "222");
            stream.forEach(System.out::println);
        }
        {
            String[] names = {"XML", "Java", "SQL", "CSS"};
            Stream<String> stream = Stream.of(names);
            stream.forEach(System.out::println);
        }

        {
            Stream<String> stream = Stream.<String>builder()
                    .add("XML").add("Java").add("CSS").add("SQL")
                    .build();
            stream.forEach(System.out::println);
        }
        {
            IntStream oneToFive = IntStream.range(1, 6);
            //IntStream oneToFive  = IntStream.rangeClosed(1, 5);
            oneToFive.forEach(System.out::println);
        }
        {
            Stream<String> stream = Stream.empty();
            stream.forEach(System.out::println);
        }
        {
            Stream.generate(StreamTest::next)
                    .limit(5)
                    .forEach(System.out::println);
        }
        {
            IntStream numbers = Arrays.stream(new int[]{1, 2, 3});
            Stream<String> names = Arrays.stream(new String[]{"XML", "Java"});
        }
        {
            Set<String> names = new HashSet<>();
            names.add("XML");
            names.add("Java");

            Stream<String> sequentialStream = names.stream();
            sequentialStream.forEach(System.out::println);

            Stream<String> parallelStream = names.parallelStream();
            parallelStream.forEach(System.out::println);
        }
    }

    // 集合转数组
    @Test
    public void toArray() {
        List<String> listStrings = new ArrayList<>();
        listStrings.add("1");
        listStrings.add("2");

        String[] ss = listStrings.stream().toArray(String[]::new);
        Arrays.stream(ss).forEach(System.out::println);
    }

    @Test
    public void Sorted() {
        List<Person> people = listPerson();
        {
            // 按成绩倒序+id正序
            System.out.println("按成绩倒序+id正序");
            Comparator<Person> comp1 = Comparator.comparing(Person::getScore)
                    .reversed()
                    .thenComparing(Person::getId);
            List<Person> data = people.stream().sorted(comp1).collect(Collectors.toList());
            data.forEach(d->{
                System.out.println(d.getName()+"");
            });
        }
        {
            // 按成绩倒序+id正序 ls.sort()
            System.out.println("按成绩倒序+id正序 ls.sort()");
            Comparator<Person> comp1 = Comparator.comparing(Person::getScore)
                    .reversed()
                    .thenComparing(Person::getId);
            people.sort(comp1);
            people.forEach(d->{
                System.out.println(d.getName()+"");
            });
        }

        List<Employee> persons = listPersons();

        Comparator<Employee> comparator = (o1, o2) -> {
            int flag = o1.getBirthday().compareTo(o2.getBirthday());
            return flag;
        };

        // 正序
        persons.stream().sorted(comparator).forEach(d -> System.out.println(d.getBirthday()));

        System.out.println("===============================");

        // 逆序
        persons.stream().sorted(comparator.reversed()).forEach(d -> System.out.println(d.getBirthday()));

        // 先按xx排序，后按xx排序
        persons.stream().sorted(Comparator.comparing(Employee::getSalary).thenComparing(Employee::getAge))
                .map(Employee::getName)
                .collect(Collectors.toList());

        // 先按工资再按年龄自定义排序（降序）
        persons.stream().sorted((p1, p2) -> {
            if (p1.getSalary() == p2.getSalary()) {
                return p2.getAge() - p1.getAge();
            } else {
                return p2.getSalary() - p1.getSalary();
            }
        }).map(Employee::getName).collect(Collectors.toList());

        System.out.println("ok");
    }

    @Test
    public void limit() {
        List<Employee> persons = listPersons();
        persons.stream().forEach(d -> System.out.println(d.getName()));
        System.out.println("ls.size=" + persons.size());
        System.out.println();

        persons = persons.stream().limit(2).collect(Collectors.toList());
        System.out.println("ls.size limit 2=" + persons.size());
        persons.stream().forEach(d -> System.out.println(d.getName()));
        System.out.println();

        persons = persons.stream().limit(20).collect(Collectors.toList());
        System.out.println("ls.size limit 20=" + persons.size());
        persons.stream().forEach(d -> System.out.println(d.getName()));
        System.out.println();
    }

    @Test
    public void Collector() {
        {
            Map<Long, String> idToNameMap = listPersons()
                    .stream()
                    .collect(Collectors.toMap(Employee::getId, Employee::getName));
            System.out.println(idToNameMap);
        }
        {
            Map<Employee.Gender, String> genderToNamesMap = listPersons()
                    .stream()
                    .collect(Collectors.toMap(Employee::getGender,
                            Employee::getName,
                            (oldValue, newValue) -> String.join(", ", oldValue, newValue)));
            System.out.println(genderToNamesMap);
        }
        {
            Map<Employee.Gender, Long> countByGender = listPersons()
                    .stream()
                    .collect(Collectors.toMap(Employee::getGender, p -> 1L, (oldCount, newCount) -> newCount + oldCount));

            System.out.println(countByGender);
        }
        {
            Map<Employee.Gender, Employee> highestEarnerByGender = listPersons()
                    .stream()
                    .collect(Collectors.toMap(Employee::getGender, Function.identity(),
                            (oldPerson, newPerson) -> newPerson.getIncome() > oldPerson.getIncome() ? newPerson : oldPerson));
            System.out.println(highestEarnerByGender);
        }

        {
            Stream<String> s = Stream.of("a", "b", "c");
            List<String> names = s.collect(Collectors.toList());
            System.out.println(names);
        }
        {
            Stream<String> s = Stream.of("a", "b", "c");
            Set<String> names = s.collect(Collectors.toSet());
            System.out.println(names);
        }
    }

    @Test
    public void Find() {
        List<Employee> persons = listPersons();
        // Find any male
        Optional<Employee> anyMale = persons.stream().filter(Employee::isMale).findAny();
        if (anyMale.isPresent()) {
            System.out.println("Any male:   " + anyMale.get());
        } else {
            System.out.println("No male  found.");
        }
        // Find the first male
        Optional<Employee> firstMale = persons.stream().filter(Employee::isMale).findFirst();
        if (firstMale.isPresent()) {
            System.out.println("First male:   " + anyMale.get());
        } else {
            System.out.println("No male  found.");
        }

    }

    @Test
    public void Filter() {
        {
            listPersons()
                    .stream()
                    .filter(Employee::isFemale)
                    .map(Employee::getName)
                    .forEach(System.out::println);
        }
        {
            listPersons()
                    .stream()
                    .filter(Employee::isMale)
                    .filter(p -> p.getIncome() > 5000.0)
                    .map(Employee::getName)
                    .forEach(System.out::println);
        }
        {
            listPersons()
                    .stream()
                    .filter(p -> p.isMale() && p.getIncome() > 5000.0)
                    .map(Employee::getName)
                    .forEach(System.out::println);
        }
    }

    @Test
    public void Map() {
        {
            IntStream.rangeClosed(1, 5)
                    .map(n -> n * n)
                    .forEach(System.out::println);
        }
        {
            listPersons()
                    .stream()
                    .map(Employee::getName)
                    .forEach(System.out::println);
        }
        {
            Stream.of(1, 2, 3)
                    .flatMap(n -> Stream.of(n, n + 1))
                    .forEach(System.out::println);
        }
        {
            Stream.of("XML", "Java", "CSS")
                    .map(name -> name.chars())
                    .flatMap(intStream -> intStream.mapToObj(n -> (char) n))
                    .forEach(System.out::println);
        }
        {
            Stream.of("XML", "Java", "CSS")
                    .flatMap(name -> IntStream.range(0, name.length())
                            .mapToObj(name::charAt))
                    .forEach(System.out::println);
        }
    }

    @Test
    public void Count() {
        {
            long personCount = listPersons().stream().count();
            System.out.println("Person count: " + personCount);
        }
        {
            long personCount = listPersons()
                    .stream()
                    .mapToLong(p -> 1L)
                    .sum();
            System.out.println(personCount);
        }
        {
            long personCount = listPersons()
                    .stream()
                    .map(p -> 1L)
                    .reduce(0L, Long::sum);
            System.out.println(personCount);
        }
        {
            long personCount = listPersons()
                    .stream()
                    .reduce(0L, (partialCount, person) -> partialCount + 1L, Long::sum);
            System.out.println(personCount);
        }
    }

    @Test
    public void Collects() {
        {
            List<String> names = listPersons()
                    .stream()
                    .map(Employee::getName)
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            System.out.println(names);
        }
        {
            List<String> names = listPersons()
                    .stream()
                    .map(Employee::getName)
                    .collect(Collectors.toList());
            System.out.println(names);
        }
        {
            Set<String> uniqueNames = listPersons()
                    .stream()
                    .map(Employee::getName)
                    .collect(Collectors.toSet());
            System.out.println(uniqueNames);
        }
        {
            SortedSet<String> uniqueSortedNames = listPersons()
                    .stream()
                    .map(Employee::getName)
                    .collect(Collectors.toCollection(TreeSet::new));
            System.out.println(uniqueSortedNames);
            System.out.println(uniqueSortedNames);
        }
        {
            List<String> names = listPersons()
                    .stream()//from w w w  .j a v  a2s  .c o m
                    .map(Employee::getName)
                    .collect(Collectors.collectingAndThen(Collectors.toList(),
                            result -> Collections.unmodifiableList(result)));
            System.out.println(names);
        }
    }

    @Test
    public void CollectsMap() {
        {
            Map<Long, String> idToNameMap = listPersons()
                    .stream()
                    .collect(Collectors.toMap(Employee::getId, Employee::getName));
            System.out.println(idToNameMap);
        }
        {
            Map<Employee.Gender, String> genderToNamesMap =
                    listPersons()
                            .stream()
                            .collect(Collectors.toMap(Employee::getGender,
                                    Employee::getName,
                                    (oldValue, newValue) -> String.join(", ", oldValue, newValue)));
            System.out.println(genderToNamesMap);
        }
        {
            Map<Employee.Gender, Long> countByGender = listPersons()
                    .stream()
                    .collect(Collectors.toMap(Employee::getGender, p -> 1L, (oldCount, newCount) -> newCount + oldCount));

            System.out.println(countByGender);
        }
        {
            Map<Employee.Gender, Employee> highestEarnerByGender = listPersons()
                    .stream()
                    .collect(Collectors.toMap(Employee::getGender, Function.identity(),
                            (oldPerson, newPerson) -> newPerson.getIncome() > oldPerson.getIncome() ? newPerson : oldPerson));
            System.out.println(highestEarnerByGender);
        }
    }

    @Test
    public void Group() {
        {
            Map<Employee.Gender, Long> countByGender = listPersons()
                    .stream()
                    .collect(Collectors.groupingBy(Employee::getGender, Collectors.counting()));
            System.out.println(countByGender);
        }
        {
            Map<Employee.Gender, String> namesByGender = listPersons()
                    .stream()
                    .collect(Collectors.groupingBy(Employee::getGender,
                            Collectors.mapping(Employee::getName, Collectors.joining(", "))));
            System.out.println(namesByGender);
        }
        {
            Map<Employee.Gender, List<String>> namesByGender =
                    listPersons()
                            .stream()
                            .collect(Collectors.groupingBy(Employee::getGender,
                                    Collectors.mapping(Employee::getName, Collectors.toList())));

            System.out.println(namesByGender);
        }
        {
            Map personsByGenderAndDobMonth
                    = listPersons()
                    .stream()
                    .collect(Collectors.groupingBy(Employee::getGender,
                            Collectors.groupingBy(p -> p.getCreateTime().getMonth(),
                                    Collectors.mapping(Employee::getName, Collectors.joining(", ")))));

            System.out.println(personsByGenderAndDobMonth);
        }
        {
            Map<Employee.Gender, DoubleSummaryStatistics> incomeStatsByGender = listPersons()
                    .stream()//from  w  w  w . ja  v a 2  s  .c om
                    .collect(Collectors.groupingBy(Employee::getGender, Collectors.summarizingDouble(Employee::getIncome)));

            System.out.println(incomeStatsByGender);
        }
    }

    @Test
    public void Join() {
        {
            List<Employee> persons = listPersons();
            String names = persons.stream()
                    .map(Employee::getName)
                    .collect(Collectors.joining());

            String delimitedNames = persons.stream()
                    .map(Employee::getName)
                    .collect(Collectors.joining(", "));

            String prefixedNames = persons.stream()
                    .map(Employee::getName)
                    .collect(Collectors.joining(", ", "Hello ", ".  Goodbye."));

            System.out.println("Joined names:  " + names);
            System.out.println("Joined,  delimited names:  " + delimitedNames);
            System.out.println(prefixedNames);
        }
    }

    @Test
    public void Match() {
        {
            List<Employee> persons = listPersons();
            {
                // Check if all persons are males
                boolean allMales = persons.stream()
                        .allMatch(Employee::isMale);
                System.out.println("All  males: " + allMales);
            }
            {
                // Check if any person was born in 1971
                boolean anyoneBornIn1971 = persons.stream()
                        .anyMatch(p -> p.getCreateTime().getYear() == 1971);
                System.out.println("Anyone born  in 1971:  " + anyoneBornIn1971);
            }
            {
                // Check if none person was born in 1971
                boolean noneBornIn1971 = persons.stream()
                        .noneMatch(p -> p.getCreateTime().getYear() == 1971);
                System.out.println("none born  in 1971:  " + noneBornIn1971);
            }
        }
    }

    @Test
    public void Partitioning() {
        {
            Map<Boolean, List<Employee>> partionedByMaleGender =
                    listPersons()//  w  w  w. ja v  a 2 s  . c  o m
                            .stream()
                            .collect(Collectors.partitioningBy(Employee::isMale));
            System.out.println(partionedByMaleGender);
        }
        {
            Map<Boolean, String> partionedByMaleGender = listPersons()
                    .stream()/*from   w  w  w.  jav  a2 s  .  c  o m*/
                    .collect(Collectors.partitioningBy(Employee::isMale,
                            Collectors.mapping(Employee::getName, Collectors.joining(", "))));
            System.out.println(partionedByMaleGender);
        }
    }

    @Test
    public void Statistics() {
        {
            DoubleSummaryStatistics stats = new DoubleSummaryStatistics();
            stats.accept(100.0);
            stats.accept(300.0);
            stats.accept(230.0);
            stats.accept(532.0);
            stats.accept(422.0);

            long count = stats.getCount();
            double sum = stats.getSum();
            double min = stats.getMin();
            double avg = stats.getAverage();
            double max = stats.getMax();

            System.out.printf(
                    "count=%d, sum=%.2f,  min=%.2f,  average=%.2f, max=%.2f%n", count, sum,
                    min, max, avg);
        }
        {
            DoubleSummaryStatistics incomeStats = listPersons()
                    .stream()
                    .map(Employee::getIncome)
                    .collect(DoubleSummaryStatistics::new,
                            DoubleSummaryStatistics::accept,
                            DoubleSummaryStatistics::combine);
            System.out.println(incomeStats);
        }
        {
            DoubleSummaryStatistics incomeStats = listPersons()
                    .stream()
                    .collect(Collectors.summarizingDouble(Employee::getIncome));
            System.out.println(incomeStats);
        }
    }

    @Test
    public void Aggregation() {
        {
            double totalIncome = listPersons()
                    .stream()
                    .mapToDouble(Employee::getIncome)
                    .sum();
            System.out.println("Total Income:  " + totalIncome);
        }
        {
            Optional<Employee> person = listPersons().stream()
                    .max((o1, o2) -> {
                        //借助BigDecimal函数来比较，也可以把Float转成int来进行比较，方法很多种。
                        BigDecimal first = new BigDecimal(String.valueOf(o1.getIncome()));
                        BigDecimal second = new BigDecimal(String.valueOf(o2.getIncome()));
                        return first.compareTo(second);
                    });
//                    .max(Comparator.comparingDouble(Employee::getIncome));

            if (person.isPresent()) {
                System.out.println("Highest earner: " + person.get());
            } else {
                System.out.println("Could not  get   the   highest earner.");
            }
        }
        {
            OptionalDouble income =
                    listPersons()
                            .stream()
                            .mapToDouble(Employee::getIncome).max();

            if (income.isPresent()) {
                System.out.println("Highest income:   " + income.getAsDouble());
            } else {
                System.out.println("Could not  get   the   highest income.");
            }
        }
    }

    @Test
    public void Reduce() {
        {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            int sum = numbers.stream()
                    .reduce(0, Integer::sum);
            System.out.println(sum);
        }
        {
            double sum = listPersons()
                    .stream()
                    .map(Employee::getIncome)
                    .reduce(0.0, Double::sum);
            System.out.println(sum);
        }
        {
            double sum = listPersons()
                    .stream()
                    .reduce(0.0, (partialSum, person) -> partialSum + person.getIncome(), Double::sum);
            System.out.println(sum);
        }
        {
            double sum = listPersons()
                    .stream()
                    .reduce(
                            0.0,
                            (Double partialSum, Employee p) -> {
                                double accumulated = partialSum + p.getIncome();
                                System.out.println(Thread.currentThread().getName()
                                        + "  - Accumulator: partialSum  = " + partialSum
                                        + ",  person = " + p + ", accumulated = " + accumulated);
                                return accumulated;
                            },
                            (a, b) -> {
                                double combined = a + b;
                                System.out.println(Thread.currentThread().getName()
                                        + "  - Combiner:  a  = " + a + ", b  = " + b
                                        + ", combined  = " + combined);
                                return combined;
                            });
            System.out.println("--------------------------------------");
            System.out.println(sum);

            sum = listPersons()
                    .parallelStream()
                    .reduce(
                            0.0,
                            (Double partialSum, Employee p) -> {
                                double accumulated = partialSum + p.getIncome();
                                System.out.println(Thread.currentThread().getName()
                                        + "  - Accumulator: partialSum  = " + partialSum
                                        + ",  person = " + p + ", accumulated = " + accumulated);
                                return accumulated;
                            },
                            (a, b) -> {
                                double combined = a + b;
                                System.out.println(Thread.currentThread().getName()
                                        + "  - Combiner:  a  = " + a + ", b  = " + b
                                        + ", combined  = " + combined);
                                return combined;
                            });
            System.out.println(sum);
        }
        {
            Optional<Integer> max = Stream.of(1, 2, 3, 4, 5).reduce(Integer::max);

            if (max.isPresent()) {
                System.out.println("max = " + max.get());
            } else {
                System.out.println("max is not  defined.");
            }

            max = Stream.<Integer>empty().reduce(Integer::max);
            if (max.isPresent()) {
                System.out.println("max = " + max.get());
            } else {
                System.out.println("max is not  defined.");
            }
        }
        {
            Optional<Employee> person = listPersons()
                    .stream()
                    .reduce((p1, p2) -> p1.getIncome() > p2.getIncome() ? p1 : p2);
            if (person.isPresent()) {
                System.out.println("Highest earner: " + person.get());
            } else {
                System.out.println("Could not  get   the   highest earner.");
            }
        }
    }

    @Test
    public void Parallel() {
        {
            String names = listPersons()
                    .stream()
                    .filter(Employee::isMale)
                    .map(Employee::getName)
                    .collect(Collectors.joining(", "));
            System.out.println(names);

            names = listPersons()
                    .parallelStream()
                    .filter(Employee::isMale)
                    .map(Employee::getName)
                    .collect(Collectors.joining(", "));
            System.out.println(names);
        }
        {
            String names = listPersons()                // The data source
                    .stream()                  // Produces a  sequential  stream
                    .filter(Employee::isMale)   // Processed in serial
                    .parallel()               // Produces a  parallel  stream
                    .map(Employee::getName)       // Processed in parallel
                    .collect(Collectors.joining(", "));  // Processed in parallel
            System.out.println(names);
        }
        {

        }
    }

    static int i = 0;

    private static int next() {
        i++;
        return i;
    }

    public static List<Employee> listPersons() {
        Employee p1 = new Employee(1, "Jake", Employee.Gender.MALE,
                LocalDate.of(2021, Month.JANUARY, 1), 2343.0, true,
                LocalDateTimeUtil.toDate(LocalDateTime.of(1971, 1, 12, 3, 4)));
        Employee p2 = new Employee(2, "Jack", Employee.Gender.MALE,
                LocalDate.of(1972, Month.JULY, 21), 7100.0, true,
                LocalDateTimeUtil.toDate(LocalDateTime.of(1971, 1, 2, 3, 4)));
        Employee p3 = new Employee(3, "Jane", Employee.Gender.FEMALE,
                LocalDate.of(1973, Month.MAY, 29), 5455.0, true,
                LocalDateTimeUtil.toDate(LocalDateTime.of(1971, 1, 3, 3, 4)));
        Employee p4 = new Employee(4, "Jode", Employee.Gender.MALE,
                LocalDate.of(1974, Month.OCTOBER, 16), 1800.0, true,
                LocalDateTimeUtil.toDate(LocalDateTime.of(1971, 1, 1, 3, 4)));
        Employee p5 = new Employee(5, "Jeny", Employee.Gender.FEMALE,
                LocalDate.of(1975, Month.DECEMBER, 13), 1234.0, true,
                LocalDateTimeUtil.toDate(LocalDateTime.of(1971, 1, 6, 3, 4)));
        Employee p6 = new Employee(6, "Jason", Employee.Gender.MALE,
                LocalDate.of(1976, Month.JUNE, 9), 3211.0, true,
                LocalDateTimeUtil.toDate(LocalDateTime.of(1971, 1, 2, 3, 4)));

        List<Employee> persons = Arrays.asList(p1, p2, p3, p4, p5, p6);
        //String json= JsonUtil.toJSON(persons);

        return persons;
    }

    public static List<Person> listPerson() {
        //获取文件的URL
        try {
            File file = ResourceUtils.getFile("classpath:json/person.json");
            //转成string输入文本
            String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            return JsonUtil.parseArray(content, Person.class);
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }

        return null;
    }
}
