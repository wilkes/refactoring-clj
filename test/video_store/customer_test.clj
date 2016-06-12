(ns video-store.customer-test
  (:require [clojure.test :refer :all]
            [video-store.customer :as c]
            [video-store.movie :as m]
            [video-store.rental :as r]))

(def customer (c/make-customer "Martin" []))
(def regular-movie (m/make-movie "Mad Max" m/REGULAR))
(def new-movie (m/make-movie "The Hobbit" m/NEW-RELEASE))
(def childrens-movie (m/make-movie "Bambi" m/CHILDRENS))

(deftest statement-test
  (testing "empty rentals"
    (is (= (c/statement customer)
           "Rental Record for Martin\nAmount owed is 0.0\nYou earned 0 frequent renter points"))
    (testing "with one regular rental"
      (testing "for 1 day"
        (let [c (c/add-rental customer (r/make-rental regular-movie 1))]
          (is (= (c/statement c)
                 "Rental Record for Martin\n\tMad Max\t2.0\nAmount owed is 2.0\nYou earned 1 frequent renter points"))))
      (testing "for 2 day"
        (let [c (c/add-rental customer (r/make-rental regular-movie 2))]
          (is (= (c/statement c)
                 "Rental Record for Martin\n\tMad Max\t2.0\nAmount owed is 2.0\nYou earned 1 frequent renter points"))))
      (testing "for 3 day"
        (let [c (c/add-rental customer (r/make-rental regular-movie 3))]
          (is (= (c/statement c)
                 "Rental Record for Martin\n\tMad Max\t3.5\nAmount owed is 3.5\nYou earned 1 frequent renter points"))))
      (testing "with one new release rental"
        (testing "for 1 day"
          (let [c (c/add-rental customer (r/make-rental new-movie 1))]
            (is (= (c/statement c)
                   "Rental Record for Martin\n\tThe Hobbit\t3.0\nAmount owed is 3.0\nYou earned 1 frequent renter points"))))
        (testing "for 2 day"
          (let [c (c/add-rental customer (r/make-rental new-movie 2))]
            (is (= (c/statement c)
                   "Rental Record for Martin\n\tThe Hobbit\t6.0\nAmount owed is 6.0\nYou earned 2 frequent renter points"))))
        (testing "for 3 day"
          (let [c (c/add-rental customer (r/make-rental new-movie 3))]
            (is (= (c/statement c)
                   "Rental Record for Martin\n\tThe Hobbit\t9.0\nAmount owed is 9.0\nYou earned 2 frequent renter points")))))
      (testing "with one childrens rental"
        (testing "for 1 day"
          (let [c (c/add-rental customer (r/make-rental childrens-movie 1))]
            (is (= (c/statement c)
                   "Rental Record for Martin\n\tBambi\t1.5\nAmount owed is 1.5\nYou earned 1 frequent renter points"))))
        (testing "for 3 day"
          (let [c (c/add-rental customer (r/make-rental childrens-movie 3))]
            (is (= (c/statement c)
                   "Rental Record for Martin\n\tBambi\t1.5\nAmount owed is 1.5\nYou earned 1 frequent renter points"))))
        (testing "for 4 day"
          (let [c (c/add-rental customer (r/make-rental childrens-movie 4))]
            (is (= (c/statement c)
                   "Rental Record for Martin\n\tBambi\t3.0\nAmount owed is 3.0\nYou earned 1 frequent renter points"))))))))

(deftest statement-data-test
  (testing "empty rentals"
    (is (= (c/statement-data customer) {::c/frequent-renter-points 0
                                        ::c/rentals []
                                        ::c/name "Martin"
                                        ::c/total-amount 0.0}))
    (testing "with one regular rental"
      (testing "for 1 day"
        (let [c (c/add-rental customer (r/make-rental regular-movie 1))]
          (is (= (c/statement-data c) {::c/frequent-renter-points 1
                                       ::c/rentals [{::r/title "Mad Max"
                                                     ::r/amount 2.0
                                                     ::r/frequent-renter-points 1
                                                     ::r/days-rented 1}]
                                       ::c/name "Martin"
                                       ::c/total-amount 2.0}))))
      (testing "for 2 day"
        (let [c (c/add-rental customer (r/make-rental regular-movie 2))]
          (is (= (c/statement-data c) {::c/name "Martin"
                                       ::c/frequent-renter-points 1
                                       ::c/rentals [{::r/title "Mad Max"
                                                     ::r/amount 2.0
                                                     ::r/frequent-renter-points 1
                                                     ::r/days-rented 2}]
                                       ::c/total-amount 2.0}))))
      (testing "for 3 day"
        (let [c (c/add-rental customer (r/make-rental regular-movie 3))]
          (is (= (c/statement-data c) {::c/name "Martin"
                                       ::c/frequent-renter-points 1
                                       ::c/rentals [{::r/title "Mad Max"
                                                     ::r/amount 3.5
                                                     ::r/frequent-renter-points 1
                                                     ::r/days-rented 3}]
                                       ::c/total-amount 3.5}))))
      (testing "with one new release rental"
        (testing "for 1 day"
          (let [c (c/add-rental customer (r/make-rental new-movie 1))]
            (is (= (c/statement-data c) {::c/name "Martin"
                                         ::c/frequent-renter-points 1
                                         ::c/rentals [{::r/title "The Hobbit"
                                                       ::r/amount 3.0
                                                       ::r/frequent-renter-points 1
                                                       ::r/days-rented 1}]
                                         ::c/total-amount 3.0}))))
        (testing "for 2 day"
          (let [c (c/add-rental customer (r/make-rental new-movie 2))]
            (is (= (c/statement-data c) {::c/name "Martin"
                                         ::c/frequent-renter-points 2
                                         ::c/rentals [{::r/title "The Hobbit"
                                                       ::r/amount 6.0
                                                       ::r/frequent-renter-points 2
                                                       ::r/days-rented 2}]
                                         ::c/total-amount 6.0}))))
        (testing "for 3 day"
          (let [c (c/add-rental customer (r/make-rental new-movie 3))]
            (is (= (c/statement-data c) {::c/name "Martin"
                                         ::c/frequent-renter-points 2
                                         ::c/rentals [{::r/title "The Hobbit"
                                                       ::r/amount 9.0
                                                       ::r/frequent-renter-points 2
                                                       ::r/days-rented 3}]
                                         ::c/total-amount 9.0})))))
      (testing "with one childrens rental"
        (testing "for 1 day"
          (let [c (c/add-rental customer (r/make-rental childrens-movie 1))]
            (is (= (c/statement-data c) {::c/name "Martin"
                                         ::c/frequent-renter-points 1
                                         ::c/rentals [{::r/title "Bambi"
                                                       ::r/amount 1.5
                                                       ::r/frequent-renter-points 1
                                                       ::r/days-rented 1}]
                                         ::c/total-amount 1.5}))))
        (testing "for 3 day"
          (let [c (c/add-rental customer (r/make-rental childrens-movie 3))]
            (is (= (c/statement-data c) {::c/name "Martin"
                                         ::c/frequent-renter-points 1
                                         ::c/rentals [{::r/title "Bambi"
                                                       ::r/amount 1.5
                                                       ::r/frequent-renter-points 1
                                                       ::r/days-rented 3}]
                                         ::c/total-amount 1.5}))))
        (testing "for 4 day"
          (let [c (c/add-rental customer (r/make-rental childrens-movie 4))]
            (is (= (c/statement-data c) {::c/name "Martin"
                                         ::c/frequent-renter-points 1
                                         ::c/rentals [{::r/title "Bambi"
                                                       ::r/amount 3.0
                                                       ::r/frequent-renter-points 1
                                                       ::r/days-rented 4}]
                                         ::c/total-amount 3.0}))))))))