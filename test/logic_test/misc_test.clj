(ns logic-test.misc-test
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :as l]
            [clojure.test :as t]))

;; 
;; conso
;;

(t/deftest test-conso
  (t/is (= (l/run* [q]
           (l/fresh [a d]
             (l/conso a d '())))
         ())))

(t/deftest test-conso-1
  (let [a (l/lvar 'a)
        d (l/lvar 'd)]
    (t/is (= (l/run* [q]
             (l/conso a d q))
           [(l/lcons a d)]))))

(t/deftest test-conso-2
  (t/is (= (l/run* [q]
           (l/== [q] nil))
         [])))

(t/deftest test-conso-3
  (t/is (=
       (l/run* [q]
         (l/conso 'a nil q))
       '[(a)])))

(t/deftest test-conso-4
  (t/is (= (l/run* [q]
           (l/conso 'a '(d) q))
         '[(a d)])))

(t/deftest test-conso-empty-list
  (t/is (= (l/run* [q]
           (l/conso 'a q '(a)))
         '[()])))

(t/deftest test-conso-5
  (t/is (= (l/run* [q]
           (l/conso q '(b c) '(a b c)))
         '[a])))

;;
;; firsto
;;

(t/deftest test-firsto
  (t/is (= (l/run* [q]
           (l/firsto q '(1 2)))
         (list (l/lcons '(1 2) (l/lvar 'x))))))

;;
;; resto
;;

(t/deftest test-resto
  (t/is (= (l/run* [q]
           (l/resto q '(1 2)))
         '[(_0 1 2)])))

(t/deftest test-resto-2
  (t/is (= (l/run* [q]
           (l/resto q [1 2]))
         '[(_0 1 2)])))

(t/deftest test-resto-3
  (t/is (= (l/run* [q]
           (l/resto [1 2] q))
         '[(2)])))

(t/deftest test-resto-4
  (t/is (= (l/run* [q]
           (l/resto [1 2 3 4 5 6 7 8] q))
         '[(2 3 4 5 6 7 8)])))

;;
;; membero
;;

(t/deftest membero-1
  (t/is (= (l/run* [q]
           (l/all
            (l/== q [(l/lvar)])
            (l/membero ['foo (l/lvar)] q)
            (l/membero [(l/lvar) 'bar] q)))
         '([[foo bar]]))))

(t/deftest membero-2
  (t/is (= (into #{}
           (l/run* [q]
             (l/all
              (l/== q [(l/lvar) (l/lvar)])
              (l/membero ['foo (l/lvar)] q)
              (l/membero [(l/lvar) 'bar] q))))
         (into #{}
           '([[foo bar] _0] [[foo _0] [_1 bar]]
               [[_0 bar] [foo _1]] [_0 [foo bar]])))))

;;
;; rembero
;;

(t/deftest rembero-1
  (t/is (= (l/run 1 [q]
           (l/rembero 'b '(a b c b d) q))
         '((a c b d)))))

;;
;; flatteno (The Reasoned Schemer)
;;

(defn pairo [p]
  (l/fresh [a d]
    (l/== (l/lcons a d) p)))

(defn twino [p]
  (l/fresh [x]
    (l/conso x x p)))

(defn listo [l]
  (l/conde
    [(l/emptyo l) l/s#]
    [(pairo l)
     (l/fresh [d]
       (l/resto l d)
       (listo d))]))

(defn flatteno [s out]
  (l/conde
    [(l/emptyo s) (l/== '() out)]
    [(pairo s)
     (l/fresh [a d res-a res-d]
       (l/conso a d s)
       (flatteno a res-a)
       (flatteno d res-d)
       (l/appendo res-a res-d out))]
    [(l/conso s '() out)]))

(t/deftest test-flatteno
  (t/is (= (into #{}
           (l/run* [x]
             (flatteno '[[a b] c] x)))
         (into #{}
           '(([[a b] c]) ([a b] (c)) ([a b] c) ([a b] c ())
             (a (b) (c)) (a (b) c) (a (b) c ()) (a b (c))
             (a b () (c)) (a b c) (a b c ()) (a b () c)
             (a b () c ()))))))

;;
;; anyo
;;

(defn anyo [q] ; vorsicht: terminiert nicht
  (l/conde
    [q l/s#]
    [(anyo q)]))

(t/deftest test-anyo-1
  (t/is (= (l/run 1 [q]
           (anyo l/s#)
           (l/== true q))
         (list true))))

(t/deftest test-anyo-2
  (t/is (= (l/run 5 [q]
           (anyo l/s#)
           (l/== true q))
         (list true true true true true))))

(t/deftest test-anyo-3
  (t/is (= (l/run 3 [q]
           (anyo l/s#))
         '(_0 _0 _0))))
