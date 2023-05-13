package info.kgeorgiy.ja.iuzeev.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;


/**
 * This class implements the classes.
 * Implement interfaces Impler and JarImpler
 *
 * @author arturuzeev
 * @version 17
 */
public class Implementor implements Impler, JarImpler {
    /**
     * Const like className
     */
    private String ClassName;
    /**
     * This stringbuilder must be for create methods in strings
     */
    private StringBuilder stringBuilder;
    /**
     * This HashSet catches methods that we can implement, also has the type {@link MyString}
     */
    private Set<MyString> hashSetMethodsAbstract;
    /**
     * This HashSet catches so-called bad methods,
     * which have modifiers final,private, also are not abstract,
     * made to avoid such situations, when we go to the ancestors and one method is declared final and the other abstract
     */
    private Set<MyString> hashSetBadMethods;

    /**
     * The main method for implementing the class.
     * This method determines whether we can implement a class or an interface and will call the {@link Implementor#parser(Class)}, otherwise it will throw an error.
     * After all calls it will print the result in the directory path.
     *
     * @param token A token is a class that we don't know in advance
     * @param root  is path to directory
     * @throws ImplerException incorrect subject class
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        root = root.resolve(token.getPackageName().replace('.', File.separatorChar)).resolve(token.getSimpleName() + "Impl.java");
        try {
            if (root.getParent() != null) {
                Files.createDirectories(root.getParent());
            }
        } catch (IOException e) {
            throw new ImplerException("Problems with create directory", e.getCause());
        }

        stringBuilder = new StringBuilder();

        if (token.isPrimitive() || token == Enum.class || token.isArray() || Modifier.isFinal(token.getModifiers()) || Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("Incorrect class token", null);
        }

        if (token.isInterface()) {
            stringBuilder.append(getPackage(token));
            stringBuilder.append("class ").append(token.getSimpleName()).append("Impl").append(" implements ").append(token.getCanonicalName()).append(" {");
        } else {
            ClassName = token.getSimpleName() + "Impl";
            stringBuilder.append(getPackage(token));
            stringBuilder.append(accessModifier(token.getModifiers())).append("class ").append(ClassName);
            stringBuilder.append(" extends ").append(token.getCanonicalName()).append(" {");
        }

        parser(token);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(root)) {
            bufferedWriter.write(stringBuilder.toString() + "}" + System.lineSeparator());
        } catch (IOException e) {
            throw new ImplerException("Couldn't write to the file", e.getCause());
        }
    }


    /**
     * This method is for creat {@link Package}
     *
     * @param token Class
     * @return package in string or nothing
     */
    private String getPackage(Class<?> token) {
        return Objects.equals(token.getPackage(), null) ? System.lineSeparator() : String.format("%s; %s", token.getPackage(), System.lineSeparator());
    }

    /**
     * This method parses the class that needs to be implemented.
     * This method defines whether an interface or a class is passed,
     * if it's a class then we loop through the ancestors and thereby collect all the ones we need to implement,
     * also calls the {@link Implementor#constructor(Constructor[])} method and {@link Implementor#deep(Method[])},
     * is needed to loop through the methods to get the ones we need.
     * We also throw an error when it occurs in the constructor method
     *
     * @param token Class
     * @throws ImplerException incorrect subject class
     */
    private void parser(Class<?> token) throws ImplerException {
        hashSetMethodsAbstract = new HashSet<>();
        hashSetBadMethods = new HashSet<>();
        if (!token.isInterface()) {
            constructor(token.getDeclaredConstructors());

            Class<?> parents = token;
            while (parents != null) {
                deep(parents.getMethods());
                deep(parents.getDeclaredMethods());
                parents = parents.getSuperclass();
            }

            List<Method> list = new ArrayList<>();
            hashSetMethodsAbstract.forEach(x -> list.add(x.getMethod()));

            methods(list.toArray(new Method[0]));
        } else {
            methods(token.getMethods());
        }
    }

    /**
     * This method goes around the parents.
     * This method goes around the parents and also skip final, private and not abstract methods.
     * Final, private and not abstract methods add to {@link Implementor#hashSetBadMethods}, to exclude them later with other modifiers
     *
     * @param methods methods, which we catch from {@link Implementor#parser(Class)}
     */
    private void deep(Method[] methods) {
        for (Method method : methods) {
            if (unnecessaryMethods(method.getModifiers())) {
                hashSetBadMethods.add(new MyString(method));
                continue;
            }

            if (hashSetBadMethods.contains(new MyString(method))) {
                continue;
            }

            hashSetMethodsAbstract.add(new MyString(method));
        }
    }

    /**
     * This method implements constructors.
     * this method defines the modifier, also in case of an error it rolls it,
     * also calls methods to implement errors {@link Implementor#exception(Class[])} and parameters {@link Implementor#parameters(Class[])}
     *
     * @param constructors array of constructors
     * @throws ImplerException incorrect subject class
     */
    @SuppressWarnings("rawtypes")
    private void constructor(Constructor<?>[] constructors) throws ImplerException {
        boolean flagPublicConstructors = false;
        for (Constructor constructor : constructors) {
            String mode = accessModifier(constructor.getModifiers());

            if (mode.equals("private ")) {
                continue;
            } else {
                flagPublicConstructors = true;
            }

            stringBuilder.append(mode).append(ClassName).append(" ").append("(");

            parameters(constructor.getParameterTypes());
            exception(constructor.getExceptionTypes());

            Class<?>[] parameters = constructor.getParameterTypes();
            if (parameters.length > 0) {
                stringBuilder.append("super(");
                for (int i = 0; i < parameters.length; i++) {
                    stringBuilder.append("arg").append(i).append(",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1).append(");");
            }
            stringBuilder.append("}");
        }
        if (!flagPublicConstructors) {
            throw new ImplerException("We cant extends from this class due to it has not any public constructor", null);
        }
    }

    /**
     * Defines an access modifier
     *
     * @param modifiers modifiers of method
     * @return String modifier
     */
    private String accessModifier(int modifiers) {
        if (Modifier.isPublic(modifiers)) {
            return "public ";
        } else if (Modifier.isPrivate(modifiers)) {
            return "private ";
        } else if (Modifier.isProtected(modifiers)) {
            return "protected ";
        } else if (Modifier.isStatic(modifiers)) {
            return "static";
        }
        return "";
    }

    /**
     * Returns the string final if the method final
     *
     * @param mode modifiers of method
     * @return String modifier
     */
    private String isF(int mode) {
        if (Modifier.isFinal(mode)) {
            return "final ";
        }
        return "";
    }

    /**
     * Whether this method is unnecessary
     *
     * @param mode modifiers of method
     * @return true needed or false not needed
     */
    private boolean unnecessaryMethods(int mode) {
        return Modifier.isFinal(mode) || !Modifier.isAbstract(mode) || Modifier.isPrivate(mode);
    }

    /**
     * This method implements methods.
     * This method goes through all the methods of the class ,
     * sets the modifier of access to the methods,
     * also calls the annotation function {@link Implementor#annotation(Annotation[])}
     * and calls the parameters function {@link Implementor#parameters(Class[])}
     *
     * @param methods array of methods
     */
    private void methods(Method[] methods) {
        for (Method method : methods) {
            if (method == null) {
                continue;
            }

            if (unnecessaryMethods(method.getModifiers())) {
                continue;
            }

            annotation(method.getAnnotations());

            stringBuilder.append(accessModifier(method.getModifiers())).append(" ").append(method.getReturnType().getCanonicalName()).append(" ").append(method.getName()).append("(");
            parameters(method.getParameterTypes());

            stringBuilder.append("{");
            Class<?> type = method.getReturnType();
            if (type.equals(boolean.class)) {
                stringBuilder.append("return false;} ");
            } else if (type.equals(Void.TYPE)) {
                stringBuilder.append("} ");
            } else if (type.isPrimitive()) {
                stringBuilder.append("return 0;} ");
            } else {
                stringBuilder.append("return null;} ");
            }
        }
    }

    /**
     * This method implements the creation of annotations and adds to the {@link Implementor#stringBuilder}
     *
     * @param annotations array of annotations
     */
    private void annotation(Annotation[] annotations) {
        Arrays.stream(annotations).forEach(x -> stringBuilder.append("@").append(x.annotationType().getCanonicalName()).append("()"));
    }


    /**
     * This method adds method or constructor parameters to the {@link Implementor#stringBuilder}
     *
     * @param parameters array of parameters of method or constructor
     */
    private void parameters(Class<?>[] parameters) {
        if (parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                stringBuilder.append(isF(parameters[i].getModifiers())).append(parameters[i].getCanonicalName()).append(" ").append("arg").append(i).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append(")");
    }

    /**
     * This method determines which errors the constructor is throwing
     *
     * @param exceptions array of exceptions
     */
    private void exception(Class<?>[] exceptions) {
        if (exceptions.length > 0) {
            stringBuilder.append("throws ");
            Arrays.stream(exceptions).forEach(x -> stringBuilder.append(x.getCanonicalName()).append(","));
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("{");
    }

    /**
     * This method finds the path where the compiled token class lies
     *
     * @param token implementable class
     * @return String path of package compiled class
     */
    private static String getClassPath(Class<?> token) {
        try {
            return Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
        } catch (final URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * The method creates the name of the resulting file
     *
     * @param token implementable class
     * @return package of file + Impl name
     */
    private static String getImplName(final Class<?> token) {
        return token.getPackageName() + "." + token.getSimpleName() + "Impl";
    }

    /**
     * Path normalization with method {@link Path#resolve(Path)}
     *
     * @param root  path
     * @param clazz implementable class
     * @return path + ImplName of class
     */
    public static Path getFile(final Path root, final Class<?> clazz) {
        return root.resolve(getImplName(clazz).replace(".", File.separator) + ".java").toAbsolutePath();
    }

    /**
     * This method compiles the code.
     *
     * @param root  the path in which we implement
     * @param file  package name + class name + Impl
     * @param token implementable class
     * @throws ImplerException incorrect subject class
     */
    private static void compile(final Path root, final String file, Class<?> token) throws ImplerException {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new ImplerException("Could not find java compiler, include tools.jar to classpath", null);
        }
        final String classpath = root + File.pathSeparator + getClassPath(token);
        final String[] args = {"-encoding", "UTF-8", file, "-cp", classpath};
        final int exitCode = compiler.run(null, null, null, args);
        if (exitCode != 0) {
            throw new ImplerException("Compiler exit code", null);
        }
    }

    /**
     * Creates jar file.
     * This method invokes the method {@link Implementor#implement(Class, Path)}
     * and compiles it using the {@link Implementor#compile(Path, String, Class)} method and then writes it to the file
     *
     * @param token implementable class
     * @param path  the path in which we implement
     * @throws ImplerException incorrect subject class
     */
    @Override
    public void implementJar(Class<?> token, Path path) throws ImplerException {
        try {
            String root = getFile(path.getParent(), token).toString();

            final Path dir = path.getParent();

            implement(token, dir);
            compile(path, root, token);

            final JarOutputStream stream = new JarOutputStream(Files.newOutputStream(path));
            final ZipEntry zipEntry = new ZipEntry(token.getPackageName().replace('.', '/') + "/" + token.getSimpleName() + "Impl.class");

            stream.putNextEntry(zipEntry);
            Files.copy(Path.of(root).resolveSibling(token.getSimpleName() + "Impl.class"), stream);
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * this method handles a request for an implementation of the class.
     * The main method creates an instance of the {@link Implementor} class and calls method {@link Implementor#implementJar(Class, Path)}.
     * Also, method check parameters on null.
     *
     * @param args request agents
     * @throws ImplerException        incorrect subject class
     * @throws ClassNotFoundException incorrect path
     */
    public static void main(String[] args) throws ClassNotFoundException, ImplerException {
        if (args == null || args.length != 2) {
            System.err.println("Expected two arguments");
            return;
        }

        for (String arg : args) {
            if (arg == null) {
                System.err.println("Expected not null arguments");
                return;
            }
        }

        Class<?> token = Class.forName(args[0]);
        Path path = Path.of(args[1]);
        JarImpler implementor = new Implementor();
        implementor.implementJar(token, path);
    }
}