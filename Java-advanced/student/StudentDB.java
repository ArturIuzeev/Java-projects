package info.kgeorgiy.ja.iuzeev.student;

import info.kgeorgiy.java.advanced.student.*;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDB implements AdvancedQuery {
    private final Function<Student, String> getFirstName = Student::getFirstName;
    private final Function<Student, String> getLastName = Student::getLastName;
    private final Function<Student, Integer> getId = Student::getId;
    private final Function<Student, String> getFullName = x -> String.format("%s %s", getFirstName.apply(x), getLastName.apply(x));
    private final Function<Student, GroupName> getGroup = Student::getGroup;

    private final Function<Set<String>, Integer> getSetSize = Set::size;

    private final Function<Set<GroupName>, Integer> getSetGroupSize = Set::size;
    private final Comparator<Student> studentComparatorByName = Comparator.comparing(getLastName, Comparator.reverseOrder())
            .thenComparing(getFirstName, Comparator.reverseOrder())
            .thenComparing(getId);

    private final Comparator<Student> studentComparatorById = Student::compareTo;
    private final Comparator<Student> studentComparatorByIdReverse = Comparator.comparing(getId, Comparator.reverseOrder());

    private Map<GroupName, List<Student>> createGroups(Collection<Student> students) {
        return students.stream().collect(Collectors.groupingBy(getGroup, Collectors.toList()));
    }

    private List<Group> getGroupsBy(Collection<Student> students, Function<Collection<Student>, List<Student>> function) {
        return createGroups(students).entrySet().stream()
                .map(x -> new Group(x.getKey(), function.apply(x.getValue())))
                .sorted(Comparator.comparing(Group::getName))
                .collect(Collectors.toList());
    }


    @Override
    public List<Group> getGroupsByName(Collection<Student> students) {
        return getGroupsBy(students, this::sortStudentsByName);
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> students) {
        return getGroupsBy(students, this::sortStudentsById);
    }


    private Map<GroupName, Long> countStudentsInGroups(Collection<Student> students) {
        return students.stream().collect(Collectors.groupingBy(getGroup, Collectors.counting()));
    }


    private Long maxValueLong(Map<GroupName, Long> map) {
        return map.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getValue).orElse(null);
    }

    @Override
    public GroupName getLargestGroup(Collection<Student> students) {
        return countStudentsInGroups(students).entrySet().stream()
                .filter(x -> Objects.equals(x.getValue(),
                        maxValueLong(countStudentsInGroups(students))
                ))
                .max(Map.Entry.comparingByKey()).map(Map.Entry::getKey).orElse(null);
    }

    private Map<GroupName, Set<String>> createGroupsSet(Collection<Student> students) {
        return students.stream().collect(Collectors.groupingBy(getGroup,
                Collectors.mapping(getFirstName, Collectors.toSet())));
    }

    private int maxValueSet(Map<GroupName, Set<String>> map) {
        return map.entrySet().stream().max(Comparator.comparing(x -> getSetSize.apply(x.getValue())))
                .map(Map.Entry::getValue).map(getSetSize).orElse(0);
    }

    @Override
    public GroupName getLargestGroupFirstName(Collection<Student> students) {
        return createGroupsSet(students).entrySet().stream()
                .filter(x -> Objects.equals(x.getValue().size(),
                        maxValueSet(createGroupsSet(students))
                )).min(Map.Entry.comparingByKey()).map(Map.Entry::getKey).orElse(null);

    }

    private <T> Stream<T> getNamesOrGroups(List<Student> students, Function<Student, T> function) {
        return students.stream().map(function);
    }

    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getNamesOrGroups(students, getFirstName).collect(Collectors.toList());
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getNamesOrGroups(students, getLastName).collect(Collectors.toList());
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getNamesOrGroups(students, getFullName).collect(Collectors.toList());
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return getNamesOrGroups(students, getGroup).collect(Collectors.toList());
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return getNamesOrGroups(students, getFirstName).collect(Collectors.toSet());
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream().sorted(studentComparatorByIdReverse).map(getFirstName).limit(1).collect(Collectors.joining());
    }

    private List<Student> sortStudentsBy(Collection<Student> students, Comparator<Student> comparator) {
        return students.stream().sorted(comparator).collect(Collectors.toList());
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sortStudentsBy(students, studentComparatorById);
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sortStudentsBy(students, studentComparatorByName);
    }

    private <T> List<Student> findStudentsBy(Collection<Student> students, T name, Function<Student, T> function) {
        return students.stream().filter(x -> function.apply(x).equals(name)).sorted(studentComparatorByName).collect(Collectors.toList());
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return findStudentsBy(students, name, getFirstName);
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return findStudentsBy(students, name, getLastName);
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return findStudentsBy(students, group, getGroup);
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return findStudentsByGroup(students, group).stream()
                .collect(Collectors.toMap(getLastName, getFirstName,
                        BinaryOperator.minBy(String::compareTo)
                ));
    }

    private Map<String, Set<GroupName>> countNames(Collection<Student> students) {
        return students.stream().collect(Collectors.groupingBy(getFirstName, Collectors.mapping(getGroup, Collectors.toSet())));
    }

    private int maxValueNames(Map<String, Set<GroupName>> map) {
        return map.entrySet().stream().max(Comparator.comparing(x -> getSetGroupSize.apply(x.getValue())))
                .map(Map.Entry::getValue).map(getSetGroupSize).orElse(0);
    }

    @Override
    public String getMostPopularName(Collection<Student> students) {
        return countNames(students).entrySet().stream()
                .filter(x -> Objects.equals(x.getValue().size(), maxValueNames(countNames(students))))
                .min(Map.Entry.comparingByKey()).map(Map.Entry::getKey).orElse("");
    }

    private <T> List<T> searchById(int id, Collection<Student> students, Function<Student, T> func) {
        return students.stream().filter(x -> getId.apply(x) == id).map(func).limit(1).collect(Collectors.toList());
    }

    private <T> List<T> getNamesOrGroupsByIds(Collection<Student> students, int[] ids, Function<Student, T> func) {
        return Arrays.stream(ids).mapToObj(x -> searchById(x, students, func).get(0)).collect(Collectors.toList());
    }

    @Override
    public List<String> getFirstNames(Collection<Student> students, int[] ids) {
        return getNamesOrGroupsByIds(students, ids, getFirstName);
    }

    @Override
    public List<String> getLastNames(Collection<Student> students, int[] ids) {
        return getNamesOrGroupsByIds(students, ids, getLastName);
    }

    @Override
    public List<GroupName> getGroups(Collection<Student> students, int[] ids) {
        return getNamesOrGroupsByIds(students, ids, getGroup);
    }

    @Override
    public List<String> getFullNames(Collection<Student> students, int[] ids) {
        return getNamesOrGroupsByIds(students, ids, getFullName);
    }
}
