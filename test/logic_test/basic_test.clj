(ns logic-test.basic-test
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :as l]
            [clojure.test :as t]))

;;
;; run
;;

(t/deftest test-basic-unify
  (t/is (= (l/run* [q]
           (l/== true q))
         '(true))))

(t/deftest test-basic-unify-2
  (t/is (= (l/run* [q]
           (l/fresh [x y]
             (l/== [x y] [1 5])
             (l/== [x y] q)))
         [[1 5]])))

(t/deftest test-basic-unify-3
  (t/is (=  (l/run* [q]
            (l/fresh [x y]
              (l/== [x y] q)))
          '[[_0 _1]])))

(t/deftest test-all
  (t/is (= (l/run* [q]
           (l/all
            (l/== 1 1)
            (l/== q true)))
         '(true))))


;;
;; fail
;;

(t/deftest test-basic-failure
  (t/is (= (l/run* [q]
           l/fail
           (l/== true q))
         [])))

(t/deftest test-unify-fail-1
  (t/is (= (l/run* [p] (l/fresh [a b] (l/== b ()) (l/== '(0 1) (l/lcons a b)) (l/== p [a b])))
         ())))

(t/deftest test-unify-fail-2
  (t/is (= (l/run* [p] (l/fresh [a b] (l/== b '(1)) (l/== '(0) (l/lcons a b)) (l/== p [a b])))
         ())))

(t/deftest test-unify-fail-3
  (t/is (= (l/run* [p] (l/fresh [a b c d] (l/== () b) (l/== '(1) d) (l/== (l/lcons a b) (l/lcons c d)) (l/== p [a b c d])))
         ())))

;;
;; Occurs Check
;;

(t/deftest test-occurs-check-1
  (t/is (= (l/run* [q]
           (l/== q [q]))
         ())))

