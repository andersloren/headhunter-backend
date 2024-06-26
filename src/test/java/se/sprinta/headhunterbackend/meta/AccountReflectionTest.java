package se.sprinta.headhunterbackend.meta;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import se.sprinta.headhunterbackend.account.AccountService;
import se.sprinta.headhunterbackend.account.AccountServiceH2Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class AccountReflectionTest {

    @Test
    void testReflection() {
        Class<?> cls = AccountService.class;

        Method[] methodsAccountService = cls.getDeclaredMethods();

        System.out.println("Methods of " + cls.getName() + ":");
        for (Method method : methodsAccountService) {
            if (method.getName().contains("lambda")) {
                System.out.println(method.getName().substring(7, method.getName().length() - 2));
            } else {
                System.out.println(method.getName());
            }
        }


        Class<?> testCls = AccountServiceH2Test.class;

        Method[] methodsAccountServiceH2Test = testCls.getDeclaredMethods();

        List<String> methodsMapped = Arrays.stream(methodsAccountServiceH2Test)
                .filter(method -> method.getName().contains("test"))
                .map(method -> method.getName().contains("lambda") ?
                        method.getName().substring(12, method.getName().length() - 2) :
                        method.getName().substring(5))
                .map(method -> Character.toLowerCase(method.charAt(0)) + method.substring(1))
                .toList();

        for (String s : methodsMapped) {
            System.out.println(s);
        }
    }
}
