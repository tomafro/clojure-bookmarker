(ns util)

(defn to-base [radix n]
  (Integer/toString n radix))

(def base36 (partial to-base 36))

; (defn random-bytes
;   ([] (random-bytes 64))
;   ([n] (let [result (byte-array n)]
;          (.nextBytes (java.security.SecureRandom.) result)
;          result)))

; (defn random-bytes-seq
;   (lazy-seq (cons )))

(defn random-bytes []
  (let [result (byte-array 64)]
    (flatten (repeatedly #(do (.nextBytes (java.security.SecureRandom.) result)
                             (into [] result))))))
