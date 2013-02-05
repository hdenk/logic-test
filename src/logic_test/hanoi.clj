(ns logic-test.hanoi
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :as l]))

(l/defne moveo [n x y z]
  ([1 _ _ _]
     (l/trace-lvars "Move top disk from " x)
     (l/trace-lvars " to " y))
  ([_ _ _ _]
     (l/pred n #(> % 1))
     (l/fresh [m _] (l/is m n dec)
       (moveo m x z y) (moveo 1 x y _) (moveo m z y x))))

(comment
  (l/run* [q]
    (moveo 3 :left :right :center))
)

