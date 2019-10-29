(ns identity-test
  (:require [clojure.test :refer :all]))

(defn int->b36
  [int]
  (Integer/toString int 36))


(defn b36->int
  [b36]
  (BigInteger. b36 36))

[7 101 1999 59999]
[35 1295 46655 1679615]

(defn int->scramble
  [int]
  (int->b36 (mod (* int 5) 36)))

(defn scramble->int
  ([scramble] (scramble->int scramble 5))
  ([scramble prime]
   (let [int (b36->int scramble)
         round (mod (- prime (mod int prime)) prime)]
     (quot (+ (* round 36) int) prime))))


(defn id->scrambled
  [int]
  (int->b36 (mod (* int 59999) 1679616)))

(def prime 5)

(deftest conversion
  (is (= "z" (int->b36 35)))
  (is (= 35 (b36->int "z")))
  (is (= "5" (int->scramble 1)))
  (is (= "a" (int->scramble 2)))
  (is (= "f" (int->scramble 3)))
  (is (= 1 (scramble->int "5")))
  (is (= 2 (scramble->int "a")))
  (is (= 3 (scramble->int "f")))
  (is (= 35 (scramble->int "v"))))
