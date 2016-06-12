(ns video-store.rental
  (:require [video-store.movie :as m]))

(defrecord Rental [movie days-rented])


(defn rental-amount [rental]
  (condp = (-> rental :movie :price-code)
    m/REGULAR (+ 2.0 (if (> (-> rental :days-rented) 2)
                       (* 1.5 (- (-> rental :days-rented) 2))
                       0))
    m/NEW-RELEASE (* 3.0 (-> rental :days-rented))
    m/CHILDRENS (+ 1.5 (if (> (-> rental :days-rented) 3)
                         (* 1.5 (- (-> rental :days-rented) 3))
                         0))))

(defn frequent-renter-points [rental]
  (if (and (= m/NEW-RELEASE (-> rental :movie :price-code))
           (> (-> rental :days-rented) 1))
    2
    1))