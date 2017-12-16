package lambda;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Bely Oleg on 16.12.2017.
 */
public class LambdaTest {
    @Test
    public void testLambda(){
        Comparator<Integer> cmp = (x, y) -> (x < y) ? -1 : ((x > y) ? 1 : 0);

        Assert.assertEquals(0, cmp.compare(0, 0));
        Assert.assertEquals(-1, cmp.compare(-100, 100));
        Assert.assertEquals(1,  cmp.compare(100, -100));
    }

    @Test
    public void testFunctionalInterface(){
        Function<Boolean, Integer> booleanConverter = (x)-> x ? 1 : 0;

        Assert.assertEquals(Integer.valueOf(1), booleanConverter.apply(Boolean.TRUE));
        Assert.assertEquals(Integer.valueOf(0), booleanConverter.apply(Boolean.FALSE));
    }

    @Test
    public void testFunctionalInterfaceChain(){
        Function<Boolean, Integer> booleanToIntegerConverter = b-> b ? 1 : 0;
        Function<Integer, String> integerToStringConverter = i -> i.toString();

        Assert.assertEquals(Integer.valueOf(1), booleanToIntegerConverter.apply(Boolean.TRUE));
        Assert.assertEquals("1", integerToStringConverter.apply(1));

        Assert.assertEquals("1",
                integerToStringConverter.compose(booleanToIntegerConverter).apply(Boolean.TRUE));

        Assert.assertEquals("0",
                booleanToIntegerConverter.andThen(integerToStringConverter).apply(Boolean.FALSE));
    }

    @Test
    public void testMethodReference(){
        Consumer<String> methodReference = System.out::println;
        Arrays.asList("A", "D", "Z").forEach(methodReference);
    }

    @Test
    public void testCapturing(){
        int external = 1;

        Function<Integer, Integer> function = x -> x + external;

        Assert.assertEquals(Integer.valueOf(2), function.apply(1));
        Assert.assertEquals(Integer.valueOf(3), function.apply(2));
    }
}
