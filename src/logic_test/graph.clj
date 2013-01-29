(ns logic-test.graph
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic))

;; Directed Acyclic Graphs

(defrel edge a b)

;; a
;; |
;; b
;; / | \
;; c d e
;; \ /
;; f

(fact edge :a :b)
(fact edge :b :c)
(fact edge :b :d)
(fact edge :b :e)
(fact edge :c :f)
(fact edge :e :f)

;; ------------------------------------------------

(defne ancestorso
  "y is all ancestors of x"
  [x y]
  ([x y]
   (edge y x))
  ([x y]
   (fresh [z]
          (edge z x)
          (ancestorso z y))))

(run* [q] (ancestorso :d q))
;; (:b :a)

;; ------------------------------------------------

(defne descendantso
  "y is all descendants of x"
  [x y]
  ([x y]
   (edge x y))
  ([x y]
   (fresh [z]
          (edge x z)
          (descendantso z y))))

(run* [q] (descendantso :a q))
;; (:b :c :d :e :f :f)
;; Note that :f is counted twice since there are two valid paths to :f

;; ------------------------------------------------

(defn siblingso
  "y is all siblings (common parent) of x"
  [x y]
  (fresh [z]
         (edge z x)
         (edge z y)
         (!= x y)))

(run* [q] (siblingso :c q))
;; (:d :e)

;; ------------------------------------------------

(defn common-ancestoro
  "r is all common acenstors of x and y"
  [x y r]
  (ancestorso x r)
  (ancestorso y r))

(run* [q] (common-ancestoro :f :d q))
;; (:b :a)

;; ------------------------------------------------

(defne travel [a b visted path]
  ([a b visited [b . visited]]
   (edge a b))
  ([a b visited path]
   (fresh [c new-vis]
          (edge a c)
          (!= c b)
          (conso c visited new-vis)
          (travel c b new-vis path))))

(defne reverseo
  "w is reverse of l"
  [l z w]
  ([() x x])
  ([[x . y] z w]
   (fresh [nz]
          (conso x z nz)
          (reverseo y nz w))))

(defn path
  "p is all paths between a and b"
  [a b p]
  (fresh [z]
         (travel a b [a] z)
         (reverseo z [] p)))

(comment
  (run* [q] (path :a :f q))
  ;; ((:a :b :c :f) (:a :b :e :f))
  )
