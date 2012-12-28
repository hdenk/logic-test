(ns logic-test.tabled-test
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :as logic]
    [clojure.test :as test]))

;;
;; tabled
;;

; der Graph enthält einen Zyklus (:a ... :a)
; ohne Tabling wäre patho indeterminant 
(logic/defne arco [x y]
  ([:a :b])
  ([:b :a]) 
  ([:b :d]))

; Welche y sind von x aus erreichbar ?
; Zyklen sollen nicht zu indeterminantem
; Verhalten führen.
(def patho
  (logic/tabled [x y]
    (logic/conde
      [(arco x y)]
      [(logic/fresh [z]
         (arco x z)
         (patho z y))])))

(test/deftest test-tabled-1
  (test/is (= (into #{} (logic/run* [q] (patho :a q)))
         (into #{} '(:b :a :d)))))

; Dieser Graph enthält keine Zyklen
(logic/defne arco-2 [x y]
  ([1 2])
  ([1 4])
  ([1 3])
  ([2 3])
  ([2 5])
  ([3 4])
  ([3 5])
  ([4 5]))

; Die Referenzierung der Relation arco-2 schliesst die
; Wiederverwendung von patho (siehe oben) aus 
(def patho-2
  (logic/tabled [x y]
    (logic/conde
      [(arco-2 x y)] ; hier
      [(logic/fresh [z]
         (arco-2 x z) ; und hier
         (patho-2 z y))])))

(test/deftest test-tabled-2
  (let [r (set (logic/run* [q] (patho-2 1 q)))]
    (test/is (and (= (count r) 4)
             (= r #{2 3 4 5})))))
