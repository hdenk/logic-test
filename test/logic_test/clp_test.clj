(ns logic-test.clp-test
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :as l]
            [clojure.core.logic.fd :as fd]
            [clojure.test :as t]))

(t/deftest test-fd-in-1 []
  (t/is
    ""
    (=
      '([1 1 2] [1 2 3] [2 1 3] [1 3 4] [1 4 5] [2 2 4] [3 1 4] [2 3 5] [4 1 5] [3 2 5])
      (l/run* 
        [q]
        (l/fresh 
          [x y z]
          (fd/in x y z (fd/interval 1 5))
          (fd/+ x y z)
          (l/== q [x y z]))))))

(t/deftest test-ckanren-1
  (t/is (= (into #{}
           (l/run* [q]
             (l/fresh [x]
               (fd/in x (fd/interval 1 3))
               (l/== q x))))
         (into #{} '(1 2 3)))))
