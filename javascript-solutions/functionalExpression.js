let cnst = a => (x,y,z) => {
    return a;
}

let variable = str => (x,y,z) => {
    if (str === "x") {
        return x;
    }
    if (str === "y") {
        return y;
    }
    else {
        return z;
    }
}

let multiply = (a, b) => (x,y,z) => {
    return a(x,y,z) * b(x,y,z);
}

let subtract = (a, b) => (x,y,z) => {
    return a(x,y,z) - b(x,y,z);
}

let add = (a, b) => (x,y,z) => {
    return a(x,y,z) + b(x,y,z);
}

let divide = (a, b) => (x,y,z) => {
    return a(x,y,z)/b(x,y,z);
}

let negate = (a) => (x,y,z) => {
    return -a(x,y,z);
}

let pi = function () {
    return Math.PI;
}

let e = function () {
    return Math.E;
}

// let expr = add(
//     subtract(
//         multiply(
//             variable("x"),
//             variable("x")
//         ),
//         multiply(
//             cnst(2),
//             variable("x")
//         )
//     ),
//     cnst(1)
// );
//
// println(expr(5, 0, 0));


