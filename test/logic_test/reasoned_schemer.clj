(ns logic-test.reasoned-schemer
  (refer-clojure :exclude [==])
  (:require [clojure.core.logic :as logic])
  (:require [clojure.test :as test]))

(test/deftest unify
  (test/testing 
    "successful unify"
    (test/is 
      (= 
        '(_0) 
        (logic/run* 
          [q] 
          (logic/== :a :a))))
    (test/is 
      (= 
        '(:a) 
        (logic/run* 
          [q] 
          (logic/== q :a))))
    (test/is 
      (= 
        '(:a) 
        (logic/run* 
          [q] 
          (logic/fresh 
            [x]
            (logic/== x :a)
            (logic/== q x))))))
    (test/is 
      (= 
        '()
        (logic/run* 
          [q] 
          (logic/== :a :b)
          (logic/== q :b))))
    (test/is 
      (= 
        '()
        (logic/run* 
          [q] 
          (logic/== q :a)
          (logic/== q :b))))
    (test/is 
      (= 
        '()
        (logic/run* 
          [q] 
          (logic/== :x :a)
          (logic/== q :b)))))

(test/deftest conde
  (test/testing 
    "conde"
    (test/is 
      (= 
        '(:a :b)
        (logic/run* 
          [q] 
          (logic/conde
            [(logic/== q :a)]
            [(logic/== q :b)]))))) 
    (test/is 
      (= 
        '(:b)
        (logic/run* 
          [q] 
          (logic/conde
            [(logic/== :x :a)]
            [(logic/== q :b)]))))) 

(test/deftest conso
  (test/testing 
    "conso"
    (test/is 
      (= 
        '((:b))
        (logic/run* 
          [q] 
          (logic/conso
            :a
            q
            '(:a :b))))))
  (test/is 
    (= 
      [(logic/lcons :a '_0)]
      (logic/run* 
        [q] 
        (logic/fresh 
          [x]
          (logic/conso
            :a
            x
            q))))))

#_(test/deftest listo
  (test/testing 
    "listo"
    (test/is 
      (= 
        '()
        (logic/run* 
          [x]
          (logic/listo (list :a :b x :c))))))) 


(test/deftest membero
  (test/testing 
    "membero"
    (test/is ; 3.58
      (= 
        '(:hummus)
        (logic/run 1 
          [x]
          (logic/membero x '(:hummus :with :pita))))) 
    (test/is ; 3.62
      (= 
        '(:hummus :with :pita)
        (logic/run* 
          [x]
          (logic/membero x '(:hummus :with :pita))))) 
    (test/is ; 3.66
      (= 
        '(:e)
        (logic/run* 
          [x]
          (logic/membero :e (list :pasta x :fagioli))))) 
    (test/is ; 3.69
      (= 
        '(_0)
        (logic/run 1
          [x]
          (logic/membero :e (list :pasta :e x :fagioli)))))
    (test/is ; 3.70
      (= 
        '(:e)
        (logic/run 1 
          [x]
          (logic/membero :e (list :pasta x :e :fagioli))))) 
    (test/is 
      (= 
        '(:e _0)
        (logic/run*
          [x]
          (logic/membero :e (list :pasta x :e :fagioli))))) 
   (test/is ; 3.71 
      (= 
        '((:e _0) (_0 :e))
        (logic/run*
          [r]
          (logic/fresh 
            [x y]
            (logic/membero :e (list :pasta x :fagioli y))
            (logic/== (list x y) r))))))) 
