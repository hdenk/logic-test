(ns logic-test.facts-and-rels-test
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :as l]
            [clojure.test :as t]))

;;
;; facts and rels
;;

(l/defrel man p)

(l/fact man 'Bob)
(l/fact man 'John)
(l/fact man 'Ricky)

(l/defrel woman p)
(l/fact woman 'Mary)
(l/fact woman 'Martha)
(l/fact woman 'Lucy)

(l/defrel likes p1 p2)
(l/fact likes 'Bob 'Mary)
(l/fact likes 'John 'Martha)
(l/fact likes 'Ricky 'Lucy)

(l/defrel fun p)
(l/fact fun 'Lucy)

(t/deftest test-rel-1
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh
          [x y]
          (likes x y)
          (fun y)
          (l/== q [x y])))
      '([Ricky Lucy]))))

; For long-running or interactive programs, 
; it is useful to allow facts to become "no longer" 
; true at a point in time.
(l/retraction likes 'Bob 'Mary)

(t/deftest test-rel-retract
  (t/is 
    (= 
      (into #{}
            (l/run*
              [q]
              (l/fresh 
                [x y]
                (likes x y)
                (l/== q [x y]))))
      (into #{} '([John Martha] [Ricky Lucy])))))

; It's important to index relationships so that 
; the time to run queries doesn't grow linearly 
; with the number of facts. You can create indexes 
; for any element of the fact tuple. Note that this 
; has implications for memory usage.
(l/defrel rel1 ^:index a)
(l/fact rel1 [1 2]) ; a ist hier ein vector

(t/deftest test-rel-logic-29
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [a] ; nicht das a der Relation
          (rel1 [q a])
          (l/== a 2)))
      '(1))))

(l/defrel rel2 ^:index e ^:index a ^:index v)
(l/facts rel2 [[:e1 :a1 :v1]
               [:e1 :a2 :v2]])

(t/deftest test-rel2
  (t/is 
    (= 
      (l/run* 
        [out]
        (l/fresh 
          [e a v]
          (rel2 e a v)
          (rel2 e :a2 v)
          (l/== [e a v] out)))
      '([:e1 :a2 :v2]))))

(l/defrel rel3 ^:index e ^:index a ^:index v)
(l/facts rel3 [[:e1 :a1 :v1] ; facts ist Plural !
               [:e1 :a2 :v2]])
(l/retractions rel3 [[:e1 :a1 :v1] ; retractions ist Plural !
                     [:e1 :a1 :v1]
                     [:e1 :a2 :v2]])

(t/deftest rel3-dup-retractions
  (t/is 
    (= 
      (l/run*
        [out]
        (l/fresh 
          [e a v]
          (rel3 e :a1 :v1)
          (rel3 e a v)
          (l/== [e a v] out)))
      '())))

