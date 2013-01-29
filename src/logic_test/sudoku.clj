(ns logic-test.sudoku
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :as l]
            [clojure.core.logic.fd :as fd]))

(defn get-square [rows x y]
  (for [x (range x (+ x 3))
        y (range y (+ y 3))]
    (get-in rows [x y])))

(defn init [vars hints]
  (if (seq vars)
    (let [hint (first hints)]
      (l/all
        (if-not (zero? hint)
          (l/== (first vars) hint)
          l/succeed)
        (init (next vars) (next hints))))
    l/succeed))

(defn sudokufd [hints]
  (let [vars (repeatedly 81 l/lvar)
        rows (->> vars (partition 9) (map vec) (into []))
        cols (apply map vector rows)
        sqs (for [x (range 0 9 3)
                   y (range 0 9 3)]
               (get-square rows x y))]
    (l/run 1 [q]
      (l/== q vars)
      (l/everyg #(fd/in % (fd/domain 1 2 3 4 5 6 7 8 9)) vars)
      (init vars hints)
      (l/everyg fd/distinct rows)
      (l/everyg fd/distinct cols)
      (l/everyg fd/distinct sqs))))

;; ====

;(comment
  (sudokufd
    [0 0 3 0 2 0 6 0 0
     9 0 0 3 0 5 0 0 1
     0 0 1 8 0 6 4 0 0

     0 0 8 1 0 2 9 0 0
     7 0 0 0 0 0 0 0 8
     0 0 6 7 0 8 2 0 0

     0 0 2 6 0 9 5 0 0
     8 0 0 2 0 3 0 0 9
     0 0 5 0 1 0 3 0 0])
;)
