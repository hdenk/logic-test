(ns logic-test.scratch
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic))


(def vars (take 81 (iterate inc 0)))
(def rows (->> vars (partition 9) (map vec) (into [])))
(def cols (apply map vector rows))

(print cols)
    


