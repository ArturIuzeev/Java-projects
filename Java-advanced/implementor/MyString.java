package info.kgeorgiy.ja.iuzeev.implementor;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * This class is needed to get rid of collisions in methods {@link MyString#equals(Object)}, {@link MyString#hashCode()}.
 *
 * @author arturuzeev
 * @version 17
 */
public class MyString {
    /**
     * global field of method
     */
    private Method method;

    /**
     * Constructor
     *
     * @param method is setting to global field
     */
    public MyString(Method method) {
        this.method = method;
    }

    /**
     * This method checks if both methods have the same names and types of parameters and returns true,
     * provided we have the same return values,
     * if they are different we check which return type is narrower and assign it to both and return true
     *
     * @param o is class instance MyString
     * @return a boolean variable that denotes whether the objects are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof MyString myString) {
            if (myString.getMethod().getName().equals(this.method.getName()) && Arrays.equals(((MyString) o).getMethod().getParameterTypes(), this.method.getParameterTypes())) {
                if (!((MyString) o).getMethod().getReturnType().equals(this.method.getReturnType())) {
                    if (this.method.getReturnType().isAssignableFrom(((MyString) o).getMethod().getReturnType())) {
                        setMethod(((MyString) o).getMethod());
                    } else {
                        ((MyString) o).setMethod(this.method);
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * This method calc the hash code with respect to parameter, names and method annotations
     *
     * @return an integer value used to identify the object
     */
    @Override
    public int hashCode() {
        return method.getName().hashCode() * 31
                + Arrays.hashCode(method.getParameterTypes()) * 31
                + Arrays.hashCode(method.getAnnotations()) * 31;
    }

    /**
     * @return arg type of method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Method is setting global field
     *
     * @param method is method
     */
    private void setMethod(Method method) {
        this.method = method;
    }
}
