(defn v+ [a b] (mapv + a b))
(defn v- [a b] (mapv - a b))
(defn v* [a b] (mapv * a b))
(defn vd [a b] (mapv / a b))
; :NOTE: из v+,v-,v*,vd можно вынести общий код
(defn scalar [& args] (apply + (apply v* args)))

(defn vect [a b]
  [
   (- (* (a 1) (b 2)) (* (a 2) (b 1)))
   (- (* (a 2) (b 0)) (* (a 0) (b 2)))
   (- (* (a 0) (b 1)) (* (a 1) (b 0)))])

(defn v*s [s v] (mapv #(* % v) s))

(defn m [f] (fn [a b] (mapv f a b)))

(def m+ (m v+))
(def m- (m v-))
(def m* (m v*))
(def md (m vd))

(defn m*s[s m] (mapv #(v*s % m) s))

(defn m*v [a b] (mapv #(apply + (v* % b)) a))

(defn transpose [m] (apply mapv vector m))

(defn m*m [a b] (mapv #(mapv (partial scalar %) (transpose b)) a))

(def c+ (m m+))
(def c- (m m-))
(def c* (m m*))
(def cd (m md))
