(ns logic-test.unifier-test
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :as l]
            [clojure.test :as t]))

;;
;; unifier
;;

(t/deftest test-unifier-1
  (t/is (= (l/unifier '(?x ?y) '(1 2))
         '(1 2))))

(t/deftest test-unifier-2
  (t/is (= (l/unifier '(?x ?y 3) '(1 2 ?z))
         '(1 2 3))))

(t/deftest test-unifier-3
  (t/is (= (l/unifier '[(?x . ?y) 3] [[1 2] 3])
         '[(1 2) 3])))

(t/deftest test-unifier-4
  (t/is (= (l/unifier '(?x . ?y) '(1 . ?z))
         (l/lcons 1 '_0))))

(t/deftest test-unifier-5
  (t/is (= (l/unifier '(?x 2 . ?y) '(1 2 3 4 5))
         '(1 2 3 4 5))))

(t/deftest test-unifier-6
  (t/is (= (l/unifier '(?x 2 . ?y) '(1 9 3 4 5))
         nil)))

(t/deftest test-unifier-7
  (t/is (= (l/unifier '(?x 2 . ?y) '(1 9 3 4 5))
         nil)))

(t/deftest test-unifier-8 ;;nested maps
  (t/is (= (l/unifier '{:a {:b ?b}} {:a {:b 1}})
         {:a {:b 1}})))

(t/deftest test-unifier-9 ;;nested vectors
  (t/is (= (l/unifier '[?a [?b ?c] :d] [:a [:b :c] :d])
         [:a [:b :c] :d])))

(t/deftest test-unifier-10 ;;nested seqs
  (t/is (= (l/unifier '(?a (?b ?c) :d) '(:a (:b :c) :d))
         '(:a (:b :c) :d))))

(t/deftest test-unifier-11 ;;all together now
  (t/is (= (l/unifier '{:a [?b (?c [?d {:e ?e}])]} {:a [:b '(:c [:d {:e :e}])]})
         {:a [:b '(:c [:d {:e :e}])]})))


(t/deftest test-binding-map-1
  (t/is (= (l/binding-map '(?x ?y) '(1 2))
         '{?x 1 ?y 2})))

(t/deftest test-binding-map-2
  (t/is (= (l/binding-map '(?x ?y 3) '(1 2 ?z))
         '{?x 1 ?y 2 ?z 3})))

(t/deftest test-binding-map-3
  (t/is (= (l/binding-map '[(?x . ?y) 3] [[1 2] 3])
         '{?x 1 ?y (2)})))

(t/deftest test-binding-map-4
  (t/is (= (l/binding-map '(?x . ?y) '(1 . ?z))
         '{?z _0, ?x 1, ?y _0})))

(t/deftest test-binding-map-5
  (t/is (= (l/binding-map '(?x 2 . ?y) '(1 2 3 4 5))
         '{?x 1 ?y (3 4 5)})))

(t/deftest test-binding-map-6
  (t/is (= (l/binding-map '(?x 2 . ?y) '(1 9 3 4 5))
         nil)))
