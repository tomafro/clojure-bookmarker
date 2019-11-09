(ns identity-test
  (:require [clojure.test :refer :all]))

(defn int->b36
  [int]
  (Integer/toString int 36))

(defn b36->int
  [b36]
  (BigInteger. b36 36))

; [7 101 1999 59999]
; [35 1295 46655 1679615]

; (defn id->scrambled
;   [int]
;   (cond
;     (< int 36  ) (int->b36 (mod (* int 5) 36))
;     (< int 1296) (int->b36 (mod (* int 101) 1296))))

; (defn scrambled->id
;   ([scramble] (scrambled->id scramble 101))
;   ([scramble prime]
;    (let [int (b36->int scramble)
;          round (mod (- prime (mod int prime)) prime)]
;      (quot (+ (* round 1296) int) prime))))

; (deftest scrambled-ids
;   (let [one-char  (map id->scrambled (range 0 35))
;         two-chars (map id->scrambled (range 36 1295))]
;     (testing "unscrambled"
;       (is (= (range 0 35) (map scrambled->id one-char)))
;       (is (= (take 20 (range 36 1295)) (take 20 (map scrambled->id two-chars)))))))

(defn show [numbers]
  (prn numbers)
  (prn (map #(* 101 %) numbers))
  (prn (map #(mod (* 101 %) 1296) numbers))
  (prn (map #(quot (* 101 %) 1296) numbers)))

(show (range 36 56))
