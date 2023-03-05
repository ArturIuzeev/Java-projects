prime(2) :- !.

prime(NUM) :-
    NUM > 1,
    (\+ prime_helper(NUM, 2)).

prime_helper(NUM, SNUM) :-
    0 is mod(NUM, SNUM), !.

prime_helper(NUM, SNUM) :-
    (SNUM * SNUM < NUM;
    SNUM * SNUM = NUM),
    SNUM1 = SNUM + 1,
 prime_helper(NUM, SNUM1).

composite(NUM) :-
    (\+ prime(NUM)).

prime_divisors(1, []) :- !.

prime_divisors(NUM, [NUM]) :-
    prime(NUM), !.

prime_divisors(NUM, [H | T]) :-
    min_D(NUM, H, 2),
    N2 is NUM / H,
    prime_divisors(N2, T).

min_D(NUM, H, D):-
    prime(D),
    0 is mod(NUM, D),
    H is D, !.

min_D(NUM, H, D):-
    D =< NUM,
    D1 is D + 1, !,
    min_D(NUM, H, D1).

min_D(NUM, H, D):-
    D is H, !.

unique_prime_divisors(N, R) :-
    prime_divisors(N, Divisors), filter(Divisors, R).

filter([], []).
filter([H], [H]).

filter([H, H | T], R) :-
    append([H], T, L),
    filter(L, R), !.

filter([H, H1 | T], R) :-
    append([H1], T, L),
    filter(L, R1),
    append([H], R1, R), !.




