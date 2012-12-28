(ns logic-test.family
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :as logic]))

;; define the relations and their arity
(logic/defrel parent p c)
(logic/defrel male p)
(logic/defrel female p)

;; C is the offspring of P if P is the parent of C
(defn offspring [c p]
  (parent p c))

;; X is the grandparent of Z if, for some Y, X is the parent
;; of Y and Y is the parent of Z
(defn grandparent [x z]
  (logic/fresh [y]
    (parent x y)
    (parent y z)))

;; X is the grandmother of Y if X is the grandparent of Y and
;; X is a female
(defn grandmother [x y]
  (logic/fresh [] ; wozu das ?
    (grandparent x y)
    (female x)))

;; X is the sister of Y if, for some Z, Z is the parent of X
;; and that same Z is the parent of Y and X is female and
;; Y and X are not the same person.
(defn sister [x y]
  (logic/fresh [z]
    (parent z x)
    (parent z y)
    (female x)
    (logic/!= x y)))

;; X is the brother of Y if, for some Z, Z is the parent of X
;; and that same Z is the parent of Y and X is male and
;; Y and X are not the same person.
(defn brother [x y]
  (logic/fresh [z]
    (parent z x)
    (parent z y)
    (male x)
    (logic/!= x y)))

;; X is a predecessor of Z if X is the parent of Z or if
;; X is the parent of some Y and that Y is a predecessor
;; of Z
(defn ancestor [x z]
  (logic/conde
    [(parent x z)]
    [(logic/fresh [y]
       (parent x y)
       (ancestor y z))]))