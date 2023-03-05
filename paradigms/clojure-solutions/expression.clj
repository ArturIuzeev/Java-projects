(def constant constantly)
(defn variable [name] (fn [v] (v name)))

(defn for_operations [f]
  (fn [a b]
    (fn [x] (f (force (a x)) (force (b x))))))

(def add (for_operations +))
(def subtract (for_operations -))
(def multiply (for_operations *))

(defn negate [a] (fn [x] (- (force (a x)))))
(defn divide [a b] (fn [x] (/ (double (a x)) (double (b x)))))

(defn pow [a b] (fn [x] (Math/pow (force (a x)) (force (b x)))))
(defn log [a b] (fn [x] (/ (Math/log (Math/abs (force (b x)))) (Math/log (Math/abs (force (a x)))))))

(def operations {'+      add,
                 '-      subtract,
                 '*      multiply,
                 '/      divide,
                 'negate negate,
                 'pow    pow,
                 'log    log})

(defn parse [expression]
  (cond
    (list? expression) (apply (operations (first expression)) (map parse (rest expression)))
    (number? expression) (constant expression)
    (symbol? expression) (variable (str expression))))

(defn parseFunction [expression]
  (parse (read-string expression)))

;HW11
(defn proto-get
  ([obj key] (proto-get obj key nil))
  ([obj key default]
   (cond
     (contains? obj key) (obj key)
     (contains? obj :prototype) (proto-get (obj :prototype) key default)
     :else default)))

(defn proto-call
  [this key & args]
  (apply (proto-get this key) this args))

(defn field [key]
  (fn
    ([this] (proto-get this key))
    ([this, def] (proto-get this key def))))

(defn method [key]
  (fn [this & args] (apply proto-call this key args)))

(defn constructor
  ([ctor, prototype, func, symb]
   (fn [& args] (apply ctor {:prototype prototype
                             :function func
                             :symbol symb
                             } args)))
  ([ctor, prototype, symb]
   (fn [& args] (apply ctor {:prototype prototype
                             :symbol symb
                             } args)))
  ([ctor prototype]
   (fn [& args] (apply ctor {:prototype prototype} args))))

(def _function (field :function))
(def _symbol (field :symbol))
(def _ln (field :ln))
(def _arg1 (field :arg1))
(def _arg2 (field :arg2))
(def _const (field :const))
(def _negate (field :negate))
(def _variable (field :variable))
(def evaluate (method :evaluate))
(def toString (method :toString))
(def diff (method :diff))


(defn Operation [this a b]
  (assoc this :arg1 a :arg2 b))

(defn OperationConst [this a]
  (assoc this :const a))

(defn OperationVariab [this a]
  (assoc this :variable a))

(defn OperationNegate [this a]
  (assoc this :negate a))

(defn OperationLn [this a]
  (assoc this :ln a))

(declare Add)
(declare Subtract)
(declare Multiply)
(declare Divide)
(declare Negate)
(declare Pow)
(declare Log)
(declare Ln)

(def OperationProto
  {
   :evaluate (fn [this args] (if (= "/" (_symbol this))
                               (/
                                 (double(evaluate (_arg1 this) args))
                                 (double(evaluate (_arg2 this) args)))
                               ((_function this)
                                (evaluate (_arg1 this) args)
                                (evaluate (_arg2 this) args))))

   :toString (fn [this] (str "(" (_symbol this) " " (toString (_arg1 this)) " " (toString (_arg2 this)) ")"))
   :diff (fn [this args] (cond
                           (= "+" (_symbol this)) (Add (diff (_arg1 this) args) (diff (_arg2 this) args))
                           (= "*" (_symbol this)) (Add (Multiply (diff (_arg1 this) args) (_arg2 this)) (Multiply (diff (_arg2 this) args) (_arg1 this)))
                           (= "-" (_symbol this)) (Subtract (diff (_arg1 this) args) (diff (_arg2 this) args))
                           (= "/" (_symbol this)) (Divide (Subtract (Multiply (diff (_arg1 this) args) (_arg2 this)) (Multiply (diff (_arg2 this) args) (_arg1 this))) (Multiply (_arg2 this) (_arg2 this)))
                           (= "-" (_symbol this)) (Subtract (diff (_arg1 this) args) (diff (_arg2 this) args))
                           ))
   })

(declare Constant)

(def OperationProtoConst
  {
   :evaluate (fn [this, args] (_const this))
   :toString (fn [this] (str (_const this)))
   :diff (fn [this args] (Constant 0))
   })

(defn CreateOpConst []
  (constructor OperationConst OperationProtoConst))

(def Constant (CreateOpConst))

(def OperationProtoVariable
  {
   :evaluate (fn [this args] (get args (_variable this)))
   :toString (fn [this] (_variable this))
   :diff (fn [this args] (if (= args (_variable this)) (Constant 1) (Constant 0)))
   })

(def OperationProtoNegate
  {
   :evaluate (fn [this, args] (- (evaluate (_negate this) args)))
   :toString (fn [this] (str "(negate " (toString (_negate this)) ")"))
   :diff (fn [this args] (= "negate" (_symbol this)) (Negate (diff (_negate this) args)))
   })

(def OperationProtoLn
  {
   :evaluate (fn [this, args] (Math/log (Math/abs (evaluate (_ln this) args))))
   :toString (fn [this] (str "(ln "(toString (_ln this)) ")"))
   :diff (fn [this args] (Divide (diff (_ln this) args) (_ln this)))
   })

(def OperationProtoLog
  {
   :evaluate (fn [this, args] (/ (Math/log (Math/abs (evaluate (_arg2 this) args))) (Math/log (Math/abs (evaluate (_arg1 this) args)))))
   :toString (fn [this] (str "(log " (toString (_arg1 this)) " " (toString (_arg2 this)) ")"))
   :diff (fn [this args] (= "log" (_symbol this)) (diff (Divide (Ln (_arg2 this)) (Ln (_arg1 this))) args))
   })


(def OperationProtoPow
  {
   :evaluate (fn [this, args] (Math/pow (evaluate (_arg1 this) args) (evaluate (_arg2 this) args)))
   :toString (fn [this] (str "(pow " (toString (_arg1 this)) " " (toString (_arg2 this)) ")"))
   :diff (fn [this args] (Multiply (Pow (_arg1 this) (_arg2 this)) (diff (Multiply (_arg2 this) (Ln (_arg1 this))) args)))
   })


(defn CreateOp [function, symbol]
  (constructor Operation OperationProto function symbol))

(defn CreateOpVar []
  (constructor OperationVariab OperationProtoVariable))

(defn CreateOpNegate [symb]
  (constructor OperationNegate OperationProtoNegate symb))

(defn CreateOpLog [symbol]
  (constructor Operation OperationProtoLog symbol))

(defn CreateOpPow [symbol]
  (constructor Operation OperationProtoPow symbol))

(defn CreateOpLn [symbol]
  (constructor OperationLn OperationProtoLn symbol))


(def Add (CreateOp + "+"))
(def Subtract (CreateOp - "-" ))
(def Multiply (CreateOp * "*" ))
(def Divide (CreateOp / "/" ))
(def Log (CreateOpLog "log"))
(def Pow (CreateOpPow "pow"))
(def Negate (CreateOpNegate "negate"))
(def Ln (CreateOpLn "ln"))
(def Constant (CreateOpConst))
(def Variable (CreateOpVar))

(def operationsObj {'+        Add,
                    '-        Subtract,
                    '*        Multiply,
                    '/        Divide,
                    'negate   Negate
                    'log Log,
                    'pow Pow
                    })

(defn parseObj [expression]
  (cond
    (list? expression) (cond
                         (= (count expression) 3) ((get operationsObj (first expression)) (parseObj (second expression)) (parseObj (last expression)))
                         :else ((get operationsObj (first expression)) (parseObj (second expression))))
    (number? expression) (Constant expression)
    (symbol? expression) (Variable (str expression))))

(defn parseObject [expression]
  (parseObj (read-string expression)))

