find([(K, V) | _], Mid, K, V, Mid).
find([_ | T], Mid, K, V, Res) :-
    Res1 is Res + 1,
    find(T, Mid, K, V, Res1).

list_length([], 0).
list_length([_ | T], R) :-
    list_length(T, R1),
    R is R1 + 1.

count_mid_and_find(A, B, C, Mid, List, Key, Value) :-
    Mid is div(A + B, C),
    find(List, Mid, Key, Value, 0).

map_build_helper(_, null, L, L) :- !.
map_build_helper(List, tree(Key, Value, null, null), L1, R1) :-
                                        R1 is L1 + 1, !,
										find(List, L1, Key, Value, 0).
map_build_helper(List, tree(Key, Value, L, R), L1, R1) :-
                                       count_mid_and_find(L1, R1, 2, Mid, List, Key, Value),
                                       Mid1 is Mid + 1,
                                       map_build_helper(List, L, L1, Mid),
                                       map_build_helper(List, R, Mid1, R1), !.
map_build([], null) :- !.
map_build(List, TreeMap) :-
    list_length(List, R),
    map_build_helper(List, TreeMap, 0, R).

map_get(tree(Key, Value, _, _), Key, Value) :- !.
map_get(tree(Key, _, L, R), Key1, Value1) :-
    ((Key1 > Key, map_get(R, Key1, Value1));
    (Key1 < Key, map_get(L, Key1, Value1))), !.

map_minKey(Map, Key) :-
    min_search(Map, Key).

map_maxKey(Map, Key) :-
    max_search(Map, Key).

min_search(tree(Key, _, null, _), Key) :- !.
max_search(tree(Key, _, _, null), Key) :- !.

min_search(tree(_, _, L, _), Key1) :-
    min_search(L, Key1), !.

max_search(tree(_, _, _, R), Key1) :-
    max_search(R, Key1), !.


