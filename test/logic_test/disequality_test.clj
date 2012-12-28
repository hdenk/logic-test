(ns logic-test.disequality-test
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :as l]
            [clojure.test :as t]))

;;
;; disequality
;;

(t/deftest test-disequality-1
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x]
          (l/!= x 1)
          (l/== q x)))
      '((_0 :- (!= _0 1))))))

(t/deftest test-disequality-2
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x]
          (l/== q x)
          (l/!= x 1)))
      '((_0 :- (!= _0 1))))))

(t/deftest test-disequality-3
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x]
          (l/!= x 1)
          (l/== x 1)
          (l/== q x)))
      ())))

(t/deftest test-disequality-4
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x]
          (l/== x 1)
          (l/!= x 1)
          (l/== q x)))
      ())))

(t/deftest test-disequality-5
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x y]
          (l/!= x y)
          (l/== x 1)
          (l/== y 1)
          (l/== q x)))
      ())))

(t/deftest test-disequality-6
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x y]
          (l/== x 1)
          (l/== y 1)
          (l/!= x y)
          (l/== q x)))
      ())))

(t/deftest test-disequality-7
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x y]
          (l/== x 1)
          (l/!= x y)
          (l/== y 2)
          (l/== q x)))
      '(1))))

(t/deftest test-disequality-8
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x y]
          (l/!= [x 2] [y 1])
          (l/== x 1)
          (l/== y 3)
          (l/== q [x y])))
      '([1 3]))))

(t/deftest test-disequality-9
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x y]
          (l/== x 1)
          (l/== y 3)
          (l/!= [x 2] [y 1])
          (l/== q [x y])))
      '([1 3]))))

(t/deftest test-disequality-10
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x y]
          (l/!= [x 2] [1 y])
          (l/== x 1)
          (l/== y 2)
          (l/== q [x y])))
      ())))

(t/deftest test-disequality-11
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x y]
          (l/== x 1)
          (l/== y 2)
          (l/!= [x 2] [1 y])
          (l/== q [x y])))
      ())))

(t/deftest test-disequality-12
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x y z]
          (l/!= x y)
          (l/== y z)
          (l/== x z)
          (l/== q x)))
      ())))

(t/deftest test-disequality-13
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x y z]
          (l/== y z)
          (l/== x z)
          (l/!= x y)
          (l/== q x)))
      ())))

(t/deftest test-disequality-14
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x y z]
          (l/== z y)
          (l/== x z)
          (l/!= x y)
          (l/== q x)))
      ())))

(t/deftest test-disequality-15
  (t/is 
    (= 
      (l/run* 
        [q]
        (l/fresh 
          [x y]
          (l/== q [x y])
          (l/!= x 1)
          (l/!= y 2)))
      '(([_0 _1] :- (!= _1 2) (!= _0 1))))))
