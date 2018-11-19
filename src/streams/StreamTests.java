package streams;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Bely Oleg on 18.11.2018.
 */
/**
 * found shot tasks for learning streams
 */
class StreamTests {
    //Вернуть количество вхождений объекта "a1"
    @Test
    void getCountOfOccurrence(){
        Collection<String> data = Arrays.asList("a1", "ab", "a2", "a1");
        Stream<String> stream  = data.stream();
        long result = stream.filter((item) -> item.equals("a1")).count();

        assertEquals(result,2);
    }

    //Вернуть первый элемент коллекции или 0, если коллекция пуста
    @Test
    void findFirst(){
        Stream<String> stream1  = Stream.of("1", "2");
        Optional<String> result1 = stream1.findFirst();

        assertEquals(true, result1.isPresent());
        assertEquals("1", result1.get());


        Stream<Object> stream2  = Stream.empty();
        Optional<Object> result2 = stream2.findFirst();

        assertEquals(false, result2.isPresent());
        assertThrows(NoSuchElementException.class, result2::get);


        Stream<Integer> stream3 = Stream.of(1, 2);
        Integer result3 = stream3.findFirst().orElse(null);

        assertEquals(new Integer(1), result3);


        Stream<Integer> stream4 = Stream.empty();
        Integer result4 = stream4.findFirst().orElse(null);

        assertEquals(null, result4);
    }

    //Вернуть последний элемент коллекции или "empty", если коллекция пуста
    @Test
    void findLast(){
        String[] data = new String[]{"a1", "a2", "aLast"};
        String result = Arrays.stream(data).reduce((value1, value2) -> value2).orElse("empty");

        assertEquals(result, "aLast");


        String result2 = Arrays.stream(data).skip(data.length -1).findAny().orElse("empty");

        assertEquals(result2, "aLast");


        Stream<String> stream3 = Stream.empty();
        String result3 = stream3.reduce((value1, value2) -> value2).orElse("empty");

        assertEquals(result3, "empty");
    }

    //Найти элемент в коллекции равный "a3" или кинуть ошибку
    @Test
    void findElementOfThrowException() {
        List<String> data = Arrays.asList("a1", "a2", "a3", "a4");

        Optional<String> result = data.stream()
                .filter((item)-> item.equals("a3"))
                .findFirst();

        assertEquals(result.get(), "a3");

        List<String> data2 = Arrays.asList("a1", "a2", "a2", "a4");

        Optional<String> result2 = data2.stream()
                .filter((item)-> item.equals("a3"))
                .findAny();

        assertThrows(NoSuchElementException.class, result2::get);

    }

    //Вернуть третий элемент коллекции по порядку
    @Test
    void find3Element() {
        List<String> data = Arrays.asList("a1", "a2", "a3", "a4");

        String result = data.stream().skip(2).findFirst().orElse(null);

        assertEquals(result, "a3");

    }

    //Вернуть два элемента начиная со второго
    @Test
    void find2ElementsFrom2Position() {
        List<String> data = Arrays.asList("a1", "a2", "a3", "a4");

        String[] result = data.stream().skip(1).limit(2).toArray(String[]::new);

        assertEquals(result.length, 2);
        assertEquals(result[0], "a2");
        assertEquals(result[1], "a3");

    }

    private List<People> peopleData = Arrays.asList(
            new People("Вася", 16, People.Sex.MAN),
            new People("Петя", 23, People.Sex.MAN),
            new People("Елена", 42, People.Sex.WOMEN),
            new People("Иван Иванович", 69, People.Sex.MAN));

    //выбрать мужчин-военнообязанных (от 18 до 27 лет)
    @Test
    void filterByCriteria() {
        List<People> filteredPeople = peopleData.stream().filter(item-> {
            return
                item.getAge() >= 18 &&
                item.getAge() < 27 &&
                item.getSex().equals(People.Sex.MAN);
        }).collect(Collectors.toList());

        assertEquals(filteredPeople.size(), 1);
        assertEquals(filteredPeople.get(0).getName(), "Петя");
    }

    //Найти средний возраст среди мужчин
    @Test
    void findAverageAge() {
        double averageAge = peopleData.stream()
                .filter(item -> item.getSex() == People.Sex.MAN)
                .mapToInt(People::getAge)
                .average().orElse(0d);

        assertEquals(averageAge, 36);
    }

    //Найти кол-во потенциально работоспособных людей в выборке
    // (т.е. от 18 лет и учитывая что женщины выходят в 55 лет, а мужчина в 60)
    @Test
    void findCountOfWorkingPeople() {
        long count = peopleData.stream()
            .filter(item -> {
                int age = item.getAge();
                return age > 18 && (item.getSex() == People.Sex.MAN ? age < 60 : age < 55);
            }).count();

        assertEquals(count, 2);
    }

    // Получение коллекции без дубликатов из неупорядоченного стрима
    @Test
    void removeDuplicates() {
        List<String> data = Arrays.asList("a1", "a2", "a2", "a3", "a1", "a2", "a2");

        List<String> uniqueData = data.stream().distinct().collect(Collectors.toList());

        assertEquals(uniqueData, Arrays.asList("a1", "a2","a3"));
    }

    //Найти существуют ли хоть один «a1» элемент в коллекции
    @Test
    void containElement() {
        List<String> data = Arrays.asList("a1", "a2", "a2", "a3", "a1", "a2", "a2");

        boolean result = data.stream().anyMatch(item->item.equals("a1"));

        assertEquals(result, true);

        boolean result2 = Arrays.asList("a11", "a2", "a2", "a3", "a31", "a2", "a2")
                .stream().anyMatch(item->item.equals("a1"));

        assertEquals(result2, false);
    }

    //Найти есть ли символ «1» у всех элементов коллекции
    @Test
    void containAllElements() {
        List<String> data = Arrays.asList("a1", "a2", "a2", "a3", "a1", "a2", "a2");

        boolean result = data.stream().allMatch(item->item.equals("a1"));

        assertEquals(result, false);

        boolean result2 = Arrays.asList("a1", "a1", "a1")
                .stream().anyMatch(item->item.equals("a1"));

        assertEquals(result2, true);
    }

    //Добавить "_1" к каждому элементу коллекции
    @Test
    void mapElements() {
        String[] result = Stream.of("a1", "a2", "a3").map(item-> item+= "_1").toArray(String[]::new);

        assertEquals(result[1], "a2_1");
        assertEquals(result[2], "a3_1");
    }

    //убрать первый символ и вернуть массив чисел (int[])
    @Test
    void mapElements2() {
        int[] result = Stream.of("a1", "a2", "a3")
                .map(item-> item = item.substring(1))
                .mapToInt(Integer::valueOf)
                .toArray();

        assertEquals(result[1], 2);
        assertEquals(result[2], 3);
    }

    //получить все числа и вернуть массив чисел (int[])
    @Test
    void mapElements3() {
        int[] result = Stream.of("1,2,0", "4,5")
                .flatMap(item-> Stream.of(item.split(",")))
                .mapToInt(Integer::valueOf)
                .toArray();

        assertEquals(result[1], "a2_1");
    }

    //получить сумму всех чител
    @Test
    void mapElements4() {
        int result = Stream.of("1,2,0", "4,5")
                .flatMap(item-> Stream.of(item.split(",")))
                .mapToInt(Integer::valueOf)
                .reduce((item1, item2)-> item1 + item2).getAsInt();

        assertEquals(result, 12);
    }

    //Отсортировать коллекцию строк по алфавиту
    @Test
    void sort(){
        String[] result = Stream.of("a", "c", "d", "b", "a")
                //.sorted((item1, item2)->item1.compareTo(item2))
                .sorted(Comparator.naturalOrder())
                .toArray(String[]::new);

        assertArrayEquals(result, new String[]{"a", "a", "b", "c", "d"});
    }

    //Отсортировать коллекцию строк по алфавиту в обратном порядке
    @Test
    void sortDesc(){
        String[] result = Stream.of("a", "c", "d", "b", "a")
                //.sorted((item1, item2)->item2.compareTo(item1))
                .sorted(Comparator.reverseOrder())
                .toArray(String[]::new);

        assertArrayEquals(result, new String[]{"d", "c", "b", "a", "a"});
    }

    //Отсортировать коллекцию строк по алфавиту и убрать дубликаты
    @Test
    void sortWithoutDuplicates(){
        String[] result = Stream.of("a", "c", "d", "b", "a", "c")
                .sorted(Comparator.naturalOrder())
                .distinct()
                .toArray(String[]::new);

        assertArrayEquals(result, new String[]{"a", "b", "c", "d"});
    }

    //Отсортировать коллекцию людей по имени в обратном алфавитном порядке
    @Test
    void sort2(){
        String[] names = peopleData.stream()
                .sorted((item1, item2)-> item2.getName().compareTo(item1.getName()))
                .map(People::getName)
                .toArray(String[]::new);

        assertArrayEquals(names, new String[]{"Петя", "Иван Иванович", "Елена", "Вася"});
    }

    //Отсортировать коллекцию людей сначала по полу, а потом по возрасту в обратном порядке
    @Test
    void sort3(){
        List<People> sorted = peopleData.stream()
                .sorted(Comparator.comparing((People::getSex))
                        .thenComparing((People::getAge), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        assertEquals(sorted, Arrays.asList(
                new People("Иван Иванович", 69, People.Sex.MAN),
                new People("Петя", 23, People.Sex.MAN),
                new People("Вася", 16, People.Sex.MAN),
                new People("Елена", 42, People.Sex.WOMEN)
        ));
    }

    //Найти минимальное значение среди коллекции цифр
    @Test
    void findMin(){
        int result = Stream.of(3,2, -1, 0)
                //.min((item1, item2)->item1 - item2)
                .min(Comparator.comparingInt(item -> item))
                .get();

        assertEquals(result, -1);
    }

    //Найдем человека с максимальным возрастом
    @Test
    void findWithMaxAge(){
        People result = peopleData.stream()
                .max(Comparator.comparing((People::getAge)))
                .orElse(null);

        assertNotNull(result);
        assertEquals(result.getAge(), new Integer(69));
    }

    //Получить сумму чисел или вернуть 0
    @Test
    void getSum(){
        int result = Stream.of(1, 2, 3, 4, 2)
                .reduce((accumulator, item) -> item + accumulator)
                .orElse(0);

        assertEquals(result, 12);
    }

    //Вернуть максимум или -1
    @Test
    void getMax(){
        int result = Stream.of(1, 2, 3, 4, 2)
                .reduce((accumulator, item) -> accumulator > item ? accumulator : item)
                .orElse(-1);

        assertEquals(result, 4);
    }

    //Вернуть сумму нечетных чисел или 0
    @Test
    void getOddNumbersSum(){
        int result = Stream.of(1, 2, 3, 4, 2)
                .filter(item->item % 2 != 0)
                .reduce((accumulator, item)->accumulator + item)
                .orElse(0);

        assertEquals(result, 4);
    }

    @Test
    void groupElements(){
        Map<String, List<Integer>> result = Stream.of(1, 2, 3, 4, 2)
                .collect(Collectors.groupingBy(value-> value % 2 == 0 ? "even" : "odd"));

        assertEquals(result.get("odd"), Arrays.asList(1, 3));
    }
}
