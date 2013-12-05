package com.othelle.samples;

import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * author: Vasily Vlasov date: 12/5/13
 */
public class ReflectionTest {

    /**
     * There is no way to get the exact type which was used to parametrize class. But we can do this for Fields
     * and Methods, see other tests
     */
    @Test
    public void testGetGenericType() {
        TypeVariable[] typeParameters = ParametrizedWithString.class.getTypeParameters();
        //in this case you already know your parameters, not need to keep this information :)
        assertThat(typeParameters.length, Matchers.equalTo(0));

        //the class is typed so we are expecting 1
        typeParameters = ParametrizedA.class.getTypeParameters();
        assertThat(typeParameters.length, Matchers.equalTo(1));
    }

    @Test
    public void testGetGenericTypeForMethodAndField() throws NoSuchFieldException, NoSuchMethodException {
        Class<ClassToInspectTypes> aClass = ClassToInspectTypes.class;

        Field field = null;
        ParameterizedType genericType = null;

        field = aClass.getDeclaredField("listOfCollections");
        genericType = ((ParameterizedType) field.getGenericType());
        assertThat(genericType.getActualTypeArguments()[0], Matchers.instanceOf(WildcardType.class));
        assertThat(((Class) ((WildcardType) genericType.getActualTypeArguments()[0]).getUpperBounds()[0]), Matchers.equalTo(Collection.class));

        field = aClass.getDeclaredField("listOfParametrized");
        genericType = ((ParameterizedType) field.getGenericType());
        assertThat(genericType.getActualTypeArguments()[0], Matchers.instanceOf(WildcardType.class));
        assertThat(((Class) ((WildcardType) genericType.getActualTypeArguments()[0]).getLowerBounds()[0]), Matchers.equalTo(ParametrizedWithString.class));


        field = aClass.getDeclaredField("listOfStrings");
        genericType = ((ParameterizedType) field.getGenericType());
        assertThat(genericType.getActualTypeArguments()[0], Matchers.instanceOf(Class.class));
        assertThat(((Class) genericType.getActualTypeArguments()[0]), Matchers.equalTo(String.class));

        //Same logic applies to method return parameters
        Method method;

        method = aClass.getDeclaredMethod("getListOfNumbers");
        genericType = (ParameterizedType) method.getGenericReturnType();
        assertThat(genericType.getActualTypeArguments()[0], Matchers.instanceOf(WildcardType.class));
        assertThat(((Class) ((WildcardType) genericType.getActualTypeArguments()[0]).getUpperBounds()[0]), Matchers.equalTo(Number.class));


        //When you write <?> - this is equal to <? extends Object>
        method = aClass.getDeclaredMethod("getListOfSomething");
        genericType = (ParameterizedType) method.getGenericReturnType();
        assertThat(genericType.getActualTypeArguments()[0], Matchers.instanceOf(WildcardType.class));
        assertThat((((WildcardType) genericType.getActualTypeArguments()[0]).getUpperBounds().length), Matchers.equalTo(1));
        assertThat(((Class) ((WildcardType) genericType.getActualTypeArguments()[0]).getUpperBounds()[0]), Matchers.equalTo(Object.class));
        assertThat((((WildcardType) genericType.getActualTypeArguments()[0]).getLowerBounds().length), Matchers.equalTo(0));

        //interesting enough to work with <T> T method with parameters
        method = aClass.getDeclaredMethod("getSomethingTyped", Object.class);
        assertThat(method.getGenericReturnType(), Matchers.instanceOf(TypeVariable.class));
        assertThat(method.getGenericParameterTypes()[0], Matchers.instanceOf(TypeVariable.class));
    }

    static class ClassToInspectTypes {
        List<? extends Collection> listOfCollections;
        List<? super ParametrizedWithString> listOfParametrized;
        List<String> listOfStrings;

        public List<? extends Number> getListOfNumbers(){
            return null;
        }

        public List<?> getListOfSomething(){
            return null;
        }

        public <T> T getSomethingTyped(T parameter){
            return null;
        }
    }

    /**
     * author: Vasily Vlasov
     * date: 12/5/13
     */
    public static class ParametrizedA<T> {
    }

    /**
     * author: Vasily Vlasov
     * date: 12/5/13
     */
    public static class ParametrizedWithString extends ParametrizedA<String> {
    }
}
