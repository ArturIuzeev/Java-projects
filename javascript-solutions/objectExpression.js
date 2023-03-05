class BinaryOperations {

    constructor(x, y, z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    evaluate(number, number2, number3) {
        if (this.z === "-") {
            return this.x.evaluate(number, number2, number3) - this.y.evaluate(number, number2, number3);
        }
        if (this.z === "+") {
            return this.x.evaluate(number, number2, number3) + this.y.evaluate(number, number2, number3);
        }
        if (this.z === "*") {
            return this.x.evaluate(number, number2, number3) * this.y.evaluate(number, number2, number3);
        } else {
            return this.x.evaluate(number, number2, number3) / this.y.evaluate(number, number2, number3);
        }
    }

    toString() {
        return this.x + " " + this.y + " " + this.z;
    }

    prefix() {
        return "(" + this.z + " " + this.x.prefix() + " " + this.y.prefix() + ")";
    }
}

class UnaryOperations {

    constructor(x, operation) {
        this.x = x;
        this.operation = operation;
    }

    evaluate(number, number2, number3) {
        if (this.operation === "negate") {
            return -this.x.evaluate(number, number2, number3);
        }
        if (this.operation === "sinh") {
            return Math.sinh(this.x.evaluate(number, number2, number3));
        } else {
            return Math.cosh(this.x.evaluate(number, number2, number3));
        }
    }

    toString() {
        return this.x + " " + this.operation;
    }

    prefix() {
        return "(" + this.operation + " " + this.x.prefix() + ")";
    }
}

class Sinh extends UnaryOperations {

    constructor(x) {
        super();
        this.x = x;
        this.operation = "sinh";
    }
}

class Cosh extends UnaryOperations {

    constructor(x) {
        super();
        this.x = x;
        this.operation = "cosh";
    }
}

class Negate extends UnaryOperations {

    constructor(x) {
        super();
        this.x = x;
        this.operation = "negate";
    }
}

class Subtract extends BinaryOperations {

    constructor(x, y) {
        super();
        this.x = x;
        this.y = y;
        this.z = "-";
    }
}


class Add extends BinaryOperations {

    constructor(x, y) {
        super();
        this.x = x;
        this.y = y;
        this.z = "+";
    }
}

class Multiply extends BinaryOperations {

    constructor(x, y) {
        super();
        this.x = x;
        this.y = y;
        this.z = "*";
    }
}

class Divide extends BinaryOperations {

    constructor(x, y) {
        super();
        this.x = x;
        this.y = y;
        this.z = "/";
    }
}

class ConstVariable {

    constructor(x) {
        this.x = x;
    }

    toString() {
        return this.x.toString();
    }

    prefix() {
        return this.x + "";
    }
}

class Const extends ConstVariable {

    constructor(x) {
        super();
        this.x = x;
    }

    evaluate() {
        return +this.x;
    }
}

class Variable extends ConstVariable {

    constructor(x) {
        super();
        this.x = x;
    }

    evaluate(first, second, third) {
        if (this.x === "x") {
            return first;
        }
        if (this.x === "y") {
            return second;
        } else {
            return third;
        }
    }
}

function getString(a) {
    let result = "";
    let count = 1;
    for (let i = 0; i < a.length; i++) {
        if (a[i] !== " ") {
            result += a[i];
            count = 0;
        } else {
            if (count === 0) {
                result += " ";
                count++;
            }
        }
    }
    return result;
}

function parse(a) {
    getString(a);
    let elements = a.split(" ");
    elements = elements.filter((n) => n !== "");
    let numbers = [];
    for (let i = 0; i < elements.length; i++) {
        if (!isNaN(elements[i])) {
            numbers.push(new Const(elements[i]));
        } else {
            if (elements[i] === "+") {
                let first = numbers.pop();
                numbers.push(new Add(numbers.pop(), first));
            } else if (elements[i] === "/") {
                let first = numbers.pop();
                numbers.push(new Divide(numbers.pop(), first));
            } else if (elements[i] === "*") {
                let first = numbers.pop();
                numbers.push(new Multiply(numbers.pop(), first));
            } else if (elements[i] === "-") {
                let first = numbers.pop();
                numbers.push(new Subtract(numbers.pop(), first));
            } else if (elements[i] === "negate") {
                numbers.push(new Negate(numbers.pop()));
            } else if ((elements[i] === "x") || (elements[i] === "y") || (elements[i] === "z")) {
                numbers.push(new Variable(elements[i]));
            } else if (elements[i] === "sinh") {
                numbers.push(new Sinh(numbers.pop()));
            } else if (elements[i] === "cosh") {
                numbers.push(new Cosh(numbers.pop()));
            }
        }
    }
    return numbers[0];
}

function parsePrefix(a) {
    getString(a);
    a = a.replaceAll("(", " ( ");
    a = a.replaceAll(")", " ) ");
    let elements = a.split(" ");
    elements = elements.filter((n) => n !== "");
    let numbers = [];
    elements.reverse();
    let count_for_double_operations = 0;
    let count_for_elements = 0;
    let count_for_psp = 0;
    let count_for_open = 0;
    let count_for_operations = 0;
    for (let i = 0; i < elements.length; i++) {
        if (!isNaN(elements[i])) {
            numbers.push(new Const(elements[i]));
            count_for_elements++;
        } else {
            if (elements[i] === "+") {
                numbers.push(new Add(numbers.pop(), numbers.pop()));
                count_for_operations++;
                count_for_double_operations++;
            } else if (elements[i] === "/") {
                numbers.push(new Divide(numbers.pop(), numbers.pop()));
                count_for_operations++;
                count_for_double_operations++;
            } else if (elements[i] === "*") {
                numbers.push(new Multiply(numbers.pop(), numbers.pop()));
                count_for_operations++;
                count_for_double_operations++;
            } else if (elements[i] === "-") {
                numbers.push(new Subtract(numbers.pop(), numbers.pop()));
                count_for_operations++;
                count_for_double_operations++;
            } else if (elements[i] === "negate") {
                numbers.push(new Negate(numbers.pop()));
                count_for_operations++;
            } else if ((elements[i] === "x") || (elements[i] === "y") || (elements[i] === "z")) {
                numbers.push(new Variable(elements[i]));
                count_for_elements++;
            } else if (elements[i] === "sinh") {
                numbers.push(new Sinh(numbers.pop()));
                count_for_operations++;
            } else if (elements[i] === "cosh") {
                numbers.push(new Cosh(numbers.pop()));
                count_for_operations++;
            } else if (elements[i] === "(") {
                count_for_psp++;
                count_for_open++;
            } else if (elements[i] === ")") {
                count_for_psp--;
            } else {
                throw new SyntaxError();
            }
        }
    }
    if (count_for_elements === 0) {
        throw new SyntaxError();
    }
    if (count_for_psp !== 0) {
        throw new SyntaxError();
    }
    if (count_for_open < count_for_operations) {
        throw new SyntaxError();
    }
    if (numbers.length !== 1) {
        throw new SyntaxError();
    }
    if (count_for_double_operations !== 0) {
        if (count_for_double_operations >= count_for_elements) {
            throw new SyntaxError();
        }
    }
    if (count_for_operations === 0 && count_for_elements !== 0 && count_for_open !== 0) {
        throw new SyntaxError();
    }
    return numbers[0];
}

