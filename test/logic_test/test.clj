(ns logic-test.test
  (:use clojure.test))

; Tests can be written in separate functions.
(deftest add-test
  ; The "is" macro takes a predicate, arguments to it,
  ; and an optional message.
  (is (= 4 (+ 2 2)))
  (is (= 2 (+ 2 0)) "adding zero doesn't change value"))

(deftest reverse-test
  (is (= [3 2 1] (reverse [1 2 3]))))

; Tests can verify that a specific exception is thrown.
(deftest division-test
  (is (thrown? ArithmeticException (/ 3.0 0))))

; The with-test macro can be used to add tests
; to the functions they test as metadata.
(with-test
  (defn my-add [n1 n2] (+ n1 n2))
  (is (= 4 (my-add 2 2)))
  (is (= 2 (my-add 2 0)) "adding zero doesn't change value"))

; The "are" macro takes a predicate template and
; multiple sets of arguments to it, but no message.
; Each set of arguments are substituted one at a time
; into the predicate template and evaluated.
(deftest multiplication
  (are [n1 n2 result]
    (= (* n1 n2) result) ; a template
    1 1 1,
    1 2 2,
    2 3 6))

; Run all the tests in the current namespace.
; This includes tests that were added as function metadata using with-test.
; Other namespaces can be specified as quoted arguments.
;(run-tests)
